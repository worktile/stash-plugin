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
import com.pragbits.stash.LesschatGlobalSettingsService;
import com.pragbits.stash.LesschatSettings;
import com.pragbits.stash.LesschatSettingsService;
import com.pragbits.stash.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RepositoryPushActivityListener {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2lesschat.globalsettings.hookurl";
    static final String KEY_GLOBAL_lesschat_CHANNEL_NAME = "stash2lesschat.globalsettings.channelname";
    private static final Logger log = LoggerFactory.getLogger(RepositoryPushActivityListener.class);

    private final LesschatGlobalSettingsService lesschatGlobalSettingsService;
    private final LesschatSettingsService lesschatSettingsService;
    private final CommitService commitService;
    private final NavBuilder navBuilder;
    private final LesschatNotifier lesschatNotifier;
    private final Gson gson = new Gson();

    public RepositoryPushActivityListener(LesschatGlobalSettingsService lesschatGlobalSettingsService,
                                          LesschatSettingsService lesschatSettingsService,
                                          CommitService commitService,
                                          NavBuilder navBuilder,
                                          LesschatNotifier lesschatNotifier) {
        this.lesschatGlobalSettingsService = lesschatGlobalSettingsService;
        this.lesschatSettingsService = lesschatSettingsService;
        this.commitService = commitService;
        this.navBuilder = navBuilder;
        this.lesschatNotifier = lesschatNotifier;
    }

    @EventListener
    public void NotifylesschatChannel(RepositoryPushEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getRepository();
        LesschatSettings lesschatSettings = lesschatSettingsService.getlesschatSettings(repository);
        String globalHookUrl = lesschatGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);

        SettingsSelector settingsSelector = new SettingsSelector(lesschatSettingsService,  lesschatGlobalSettingsService, repository);
        LesschatSettings resolvedlesschatSettings = settingsSelector.getResolvedlesschatSettings();


        if (resolvedlesschatSettings.islesschatNotificationsEnabledForPush()) {
            String localHookUrl = lesschatSettings.getlesschatWebHookUrl();
            WebHookSelector hookSelector = new WebHookSelector(globalHookUrl, localHookUrl);
            ChannelSelector channelSelector = new ChannelSelector(lesschatGlobalSettingsService.getChannelName(KEY_GLOBAL_lesschat_CHANNEL_NAME), lesschatSettings.getlesschatChannelName());

            if (!hookSelector.isHookValid()) {
                log.error("There is no valid configured Web hook url! Reason: " + hookSelector.getProblem());
                return;
            }

            String repoName = repository.getSlug();
            String projectName = repository.getProject().getKey();

            for (RefChange refChange : event.getRefChanges()) {
                LesschatPayload payload = new LesschatPayload();

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

                switch (resolvedlesschatSettings.getNotificationLevel()) {
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

                // lesschatSettings.getlesschatChannelName might be:
                // - empty
                // - comma separated list of channel names, eg: #mych1, #mych2, #mych3

//                if (channelSelector.getSelectedChannel().isEmpty()) {
                    lesschatNotifier.SendlesschatNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//                } else {
//                    // send message to multiple channels
//                    List<String> channels = Arrays.asList(channelSelector.getSelectedChannel().split("\\s*,\\s*"));
//                    for (String channel: channels) {
//                        payload.setChannel(channel.trim());
//                        lesschatNotifier.SendlesschatNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//                    }
//                }
            }
        }
    }

    private void compactCommitLog(RepositoryPushEvent event, RefChange refChange, LesschatPayload payload, String url, List<Changeset> myChanges) {
        LesschatAttachment commits = new LesschatAttachment();
        commits.setColor(ColorCode.GRAY.getCode());
        commits.setTitle(String.format("[%s:%s]", event.getRepository().getName(), refChange.getRefId()));
        StringBuilder attachmentFallback = new StringBuilder();
        for (Changeset ch : myChanges) {
            String commitUrl = url.concat(String.format("/%s", ch.getId()));
            String firstCommitMessageLine = ch.getMessage().split("\n")[0];

            LesschatAttachmentField commit = new LesschatAttachmentField();
            commit.setValue(String.format("<%s|%s>: %s - %s",
                    commitUrl, ch.getDisplayId(), firstCommitMessageLine, ch.getAuthor().getName()));
            commit.setShort(false);

            commits.addField(commit);
            attachmentFallback.append(String.format("%s: %s\n", ch.getDisplayId(), firstCommitMessageLine));
        }
        commits.setFallback(attachmentFallback.toString());

        payload.addAttachment(commits);
    }

    private void verboseCommitLog(RepositoryPushEvent event, RefChange refChange, LesschatPayload payload, String url, String text, List<Changeset> myChanges) {
        for (Changeset ch : myChanges) {
            LesschatAttachment attachment = new LesschatAttachment();
            attachment.setFallback(text);
            attachment.setColor(ColorCode.GRAY.getCode());
            LesschatAttachmentField field = new LesschatAttachmentField();

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
