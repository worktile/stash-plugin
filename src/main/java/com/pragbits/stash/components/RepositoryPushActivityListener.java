package com.pragbits.stash.components;

import com.atlassian.event.api.EventListener;
import com.atlassian.stash.commit.CommitService;
import com.atlassian.stash.content.Changeset;
import com.atlassian.stash.content.ChangesetsBetweenRequest;
import com.atlassian.stash.event.RepositoryPushEvent;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.repository.RefChange;
import com.atlassian.stash.repository.RefChangeType;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.util.Page;
import com.atlassian.stash.util.PageRequest;
import com.atlassian.stash.util.PageUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.pragbits.stash.ColorCode;
import com.pragbits.stash.SlackGlobalSettingsService;
import com.pragbits.stash.SlackSettings;
import com.pragbits.stash.SlackSettingsService;
import com.pragbits.stash.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RepositoryPushActivityListener {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    static final String KEY_GLOBAL_SLACK_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    private static final Logger log = LoggerFactory.getLogger(RepositoryPushActivityListener.class);

    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SlackSettingsService slackSettingsService;
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    private final SlackNotifier slackNotifier;
    private final Gson gson = new Gson();

    public RepositoryPushActivityListener(SlackGlobalSettingsService slackGlobalSettingsService,
                                          SlackSettingsService slackSettingsService,
                                          CommitService commitService,
                                          NavBuilder navBuilder,
                                          SlackNotifier slackNotifier) {
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.slackSettingsService = slackSettingsService;
        this.commitService = commitService;
        this.navBuilder = navBuilder;
        this.slackNotifier = slackNotifier;
    }

    @EventListener
    public void NotifySlackChannel(RepositoryPushEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getRepository();
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        String globalHookUrl = slackGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);

        SettingsSelector settingsSelector = new SettingsSelector(slackSettingsService,  slackGlobalSettingsService, repository);
        SlackSettings resolvedSlackSettings = settingsSelector.getResolvedSlackSettings();


        if (resolvedSlackSettings.isSlackNotificationsEnabledForPush()) {
            String localHookUrl = slackSettings.getSlackWebHookUrl();
            WebHookSelector hookSelector = new WebHookSelector(globalHookUrl, localHookUrl);
            ChannelSelector channelSelector = new ChannelSelector(slackGlobalSettingsService.getChannelName(KEY_GLOBAL_SLACK_CHANNEL_NAME), slackSettings.getSlackChannelName());

            if (!hookSelector.isHookValid()) {
                log.error("There is no valid configured Web hook url! Reason: " + hookSelector.getProblem());
                return;
            }

            String repoName = repository.getSlug();
            String projectName = repository.getProject().getKey();

            for (RefChange refChange : event.getRefChanges()) {
                SlackPayload payload = new SlackPayload();

                String url = navBuilder
                        .project(projectName)
                        .repo(repoName)
                        .commits()
                        .buildAbsolute();

                String text = String.format("Push on `%s` by `%s <%s>`. See [%s|commit list].",
                        event.getRepository().getName(),
                        event.getUser() != null ? event.getUser().getDisplayName() : "unknown user",
                        event.getUser() != null ? event.getUser().getEmailAddress() : "unknown email",
                        url);

                if (refChange.getToHash().equalsIgnoreCase("0000000000000000000000000000000000000000") && refChange.getType() == RefChangeType.DELETE) {
                    // issue#4: if type is "DELETE" and toHash is all zero then this is a branch delete
                    text = String.format("Branch `%s` deleted from repository `%s` by `%s <%s>`.",
                            refChange.getRefId(),
                            event.getRepository().getName(),
                            event.getUser() != null ? event.getUser().getDisplayName() : "unknown user",
                            event.getUser() != null ? event.getUser().getEmailAddress() : "unknown email");
                }

                payload.setText(text);
                payload.setMrkdwn(true);

                List<Changeset> myChanges = new LinkedList<Changeset>();
                if (refChange.getFromHash().equalsIgnoreCase("0000000000000000000000000000000000000000")) {
                    // issue#3 if fromHash is all zero (meaning the beginning of everything, probably), then this push is probably
                    // a new branch, and we want only to display the latest commit, not the entire history
                    Changeset latestChangeSet = commitService.getChangeset(repository, refChange.getToHash());
                    myChanges.add(latestChangeSet);
                } else {
                    ChangesetsBetweenRequest request = new ChangesetsBetweenRequest.Builder(repository)
                            .exclude(refChange.getFromHash())
                            .include(refChange.getToHash())
                            .build();

                    Page<Changeset> changeSets = commitService.getChangesetsBetween(
                            request, PageUtils.newRequest(0, PageRequest.MAX_PAGE_LIMIT));

                    myChanges.addAll(Lists.newArrayList(changeSets.getValues()));
                }

                switch (resolvedSlackSettings.getNotificationLevel()) {
                    case COMPACT:
                        compactCommitLog(event, refChange, payload, url, myChanges);
                        break;
                    case VERBOSE:
                        verboseCommitLog(event, refChange, payload, url, text, myChanges);
                        break;
                    case MINIMAL:
                    default:
                        break;
                }

                // slackSettings.getSlackChannelName might be:
                // - empty
                // - comma separated list of channel names, eg: #mych1, #mych2, #mych3

//                if (channelSelector.getSelectedChannel().isEmpty()) {
                    slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//                } else {
//                    // send message to multiple channels
//                    List<String> channels = Arrays.asList(channelSelector.getSelectedChannel().split("\\s*,\\s*"));
//                    for (String channel: channels) {
//                        payload.setChannel(channel.trim());
//                        slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//                    }
//                }
            }
        }
    }

    private void compactCommitLog(RepositoryPushEvent event, RefChange refChange, SlackPayload payload, String url, List<Changeset> myChanges) {
        SlackAttachment commits = new SlackAttachment();
        commits.setColor(ColorCode.GRAY.getCode());
        commits.setTitle(String.format("[%s:%s]", event.getRepository().getName(), refChange.getRefId()));
        StringBuilder attachmentFallback = new StringBuilder();
        for (Changeset ch : myChanges) {
            String commitUrl = url.concat(String.format("/%s", ch.getId()));
            String firstCommitMessageLine = ch.getMessage().split("\n")[0];

            SlackAttachmentField commit = new SlackAttachmentField();
            commit.setValue(String.format("<%s|%s>: %s - %s",
                    commitUrl, ch.getDisplayId(), firstCommitMessageLine, ch.getAuthor().getName()));
            commit.setShort(false);

            commits.addField(commit);
            attachmentFallback.append(String.format("%s: %s\n", ch.getDisplayId(), firstCommitMessageLine));
        }
        commits.setFallback(attachmentFallback.toString());

        payload.addAttachment(commits);
    }

    private void verboseCommitLog(RepositoryPushEvent event, RefChange refChange, SlackPayload payload, String url, String text, List<Changeset> myChanges) {
        for (Changeset ch : myChanges) {
            SlackAttachment attachment = new SlackAttachment();
            attachment.setFallback(text);
            attachment.setColor(ColorCode.GRAY.getCode());
            SlackAttachmentField field = new SlackAttachmentField();

            attachment.setTitle(String.format("[%s:%s] - %s", event.getRepository().getName(), refChange.getRefId(), ch.getId()));
            attachment.setTitle_link(url.concat(String.format("/%s", ch.getId())));

            field.setTitle("comment");
            field.setValue(ch.getMessage());
            field.setShort(false);
            attachment.addField(field);
            payload.addAttachment(attachment);
        }
    }

}
