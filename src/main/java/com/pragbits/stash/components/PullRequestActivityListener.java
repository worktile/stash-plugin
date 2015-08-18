package com.pragbits.stash.components;

import com.atlassian.event.api.EventListener;
import com.atlassian.stash.event.pull.PullRequestActivityEvent;
import com.atlassian.stash.event.pull.PullRequestCommentActivityEvent;
import com.atlassian.stash.event.pull.PullRequestRescopeActivityEvent;
import com.atlassian.stash.nav.NavBuilder;
import com.atlassian.stash.pull.PullRequestParticipant;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.avatar.AvatarService;
import com.atlassian.stash.avatar.AvatarRequest;
import com.google.gson.Gson;
import com.pragbits.stash.*;
import com.pragbits.stash.tools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PullRequestActivityListener {
    static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    static final String KEY_GLOBAL_SLACK_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    private static final Logger log = LoggerFactory.getLogger(PullRequestActivityListener.class);

    private final SlackGlobalSettingsService slackGlobalSettingsService;
    private final SlackSettingsService slackSettingsService;
    private final NavBuilder navBuilder;
    private final SlackNotifier slackNotifier;
    private final AvatarService avatarService;
    private final AvatarRequest avatarRequest = new AvatarRequest(true, 16, true);
    private final Gson gson = new Gson();

    public PullRequestActivityListener(SlackGlobalSettingsService slackGlobalSettingsService,
                                             SlackSettingsService slackSettingsService,
                                             NavBuilder navBuilder,
                                             SlackNotifier slackNotifier,
                                             AvatarService avatarService) {
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.slackSettingsService = slackSettingsService;
        this.navBuilder = navBuilder;
        this.slackNotifier = slackNotifier;
        this.avatarService = avatarService;
    }

    @EventListener
    public void NotifySlackChannel(PullRequestActivityEvent event) {
        // find out if notification is enabled for this repo
        Repository repository = event.getPullRequest().getToRef().getRepository();
        SlackSettings slackSettings = slackSettingsService.getSlackSettings(repository);
        String globalHookUrl = slackGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL);


        SettingsSelector settingsSelector = new SettingsSelector(slackSettingsService,  slackGlobalSettingsService, repository);
        SlackSettings resolvedSlackSettings = settingsSelector.getResolvedSlackSettings();

        if (resolvedSlackSettings.isSlackNotificationsEnabled()) {

            String localHookUrl = resolvedSlackSettings.getSlackWebHookUrl();
            WebHookSelector hookSelector = new WebHookSelector(globalHookUrl, localHookUrl);
            ChannelSelector channelSelector = new ChannelSelector(slackGlobalSettingsService.getChannelName(KEY_GLOBAL_SLACK_CHANNEL_NAME), slackSettings.getSlackChannelName());

            if (!hookSelector.isHookValid()) {
                log.error("There is no valid configured Web hook url! Reason: " + hookSelector.getProblem());
                return;
            }

            String repoName = repository.getSlug();
            String projectName = repository.getProject().getKey();
            long pullRequestId = event.getPullRequest().getId();
            String userName = event.getUser() != null ? event.getUser().getDisplayName() : "unknown user";
            String activity = event.getActivity().getAction().name();
            String avatar = event.getUser() != null ? avatarService.getUrlForPerson(event.getUser(), avatarRequest) : "";

            NotificationLevel resolvedLevel = resolvedSlackSettings.getNotificationPrLevel();

            // Ignore RESCOPED PR events
            if (activity.equalsIgnoreCase("RESCOPED") && event instanceof PullRequestRescopeActivityEvent) {
                return;
            }

            if (activity.equalsIgnoreCase("OPENED") && !resolvedSlackSettings.isSlackNotificationsOpenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("REOPENED") && !resolvedSlackSettings.isSlackNotificationsReopenedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UPDATED") && !resolvedSlackSettings.isSlackNotificationsUpdatedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("APPROVED") && !resolvedSlackSettings.isSlackNotificationsApprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("UNAPPROVED") && !resolvedSlackSettings.isSlackNotificationsUnapprovedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("DECLINED") && !resolvedSlackSettings.isSlackNotificationsDeclinedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("MERGED") && !resolvedSlackSettings.isSlackNotificationsMergedEnabled()) {
                return;
            }

            if (activity.equalsIgnoreCase("COMMENTED") && !resolvedSlackSettings.isSlackNotificationsCommentedEnabled()) {
                return;
            }
            
            String url = navBuilder
                    .project(projectName)
                    .repo(repoName)
                    .pullRequest(pullRequestId)
                    .overview()
                    .buildAbsolute();

            SlackPayload payload = new SlackPayload();
            payload.setMrkdwn(true);
            payload.setLinkNames(true);

            SlackAttachment attachment = new SlackAttachment();
            attachment.setAuthorName(userName);
            attachment.setAuthorIcon(avatar);

            String color = "";
            String fallback = "";
            String text = "";

            switch (event.getActivity().getAction()) {
                case OPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s opened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("opened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));


                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }

                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case REOPENED:
                    attachment.setColor(ColorCode.BLUE.getCode());
                    attachment.setFallback(String.format("%s reopened pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("reopened pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case UPDATED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s updated pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("updated pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));

                    if (resolvedLevel == NotificationLevel.COMPACT) {
                        this.addField(attachment, "Description", event.getPullRequest().getDescription());
                    }
                    if (resolvedLevel == NotificationLevel.VERBOSE) {
                        this.addReviewers(attachment, event.getPullRequest().getReviewers());
                    }
                    break;

                case APPROVED:
                    attachment.setColor(ColorCode.GREEN.getCode());
                    attachment.setFallback(String.format("%s approved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("approved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case UNAPPROVED:
                    attachment.setColor(ColorCode.RED.getCode());
                    attachment.setFallback(String.format("%s unapproved pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("unapproved pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case DECLINED:
                    attachment.setColor(ColorCode.RED.getCode());
                    attachment.setFallback(String.format("%s declined pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("declined pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case MERGED:
                    attachment.setColor(ColorCode.GREEN.getCode());
                    attachment.setFallback(String.format("%s merged pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("merged pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case RESCOPED:
                    attachment.setColor(ColorCode.PURPLE.getCode());
                    attachment.setFallback(String.format("%s rescoped on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    attachment.setText(String.format("rescoped on pull request <%s|#%d: %s>",
                                                            url,
                                                            event.getPullRequest().getId(),
                                                            event.getPullRequest().getTitle()));
                    break;

                case COMMENTED:
                    attachment.setColor(ColorCode.PALE_BLUE.getCode());
                    attachment.setFallback(String.format("%s commented on pull request \"%s\". <%s|(open)>",
                                                            userName,
                                                            event.getPullRequest().getTitle(),
                                                            url));
                    if (resolvedLevel == NotificationLevel.MINIMAL) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>",
                                url,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle()));
                    }
                    if (resolvedLevel == NotificationLevel.COMPACT || resolvedLevel == NotificationLevel.VERBOSE) {
                        attachment.setText(String.format("commented on pull request <%s|#%d: %s>\n%s",
                                url,
                                event.getPullRequest().getId(),
                                event.getPullRequest().getTitle(),
                                ((PullRequestCommentActivityEvent) event).getActivity().getComment().getText()));
                    }
                    break;
            }

            if (resolvedLevel == NotificationLevel.VERBOSE) {
                SlackAttachmentField projectField = new SlackAttachmentField();
                projectField.setTitle("Source");
                projectField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getFromRef().getRepository().getProject().getName(),
                        event.getPullRequest().getFromRef().getRepository().getName(),
                        event.getPullRequest().getFromRef().getDisplayId()));
                projectField.setShort(true);
                attachment.addField(projectField);

                SlackAttachmentField repoField = new SlackAttachmentField();
                repoField.setTitle("Destination");
                repoField.setValue(String.format("_%s — %s_\n`%s`",
                        event.getPullRequest().getFromRef().getRepository().getProject().getName(),
                        event.getPullRequest().getToRef().getRepository().getName(),
                        event.getPullRequest().getToRef().getDisplayId()));
                repoField.setShort(true);
                attachment.addField(repoField);
            }

            payload.addAttachment(attachment);

            // slackSettings.getSlackChannelName might be:
            // - empty
            // - comma separated list of channel names, eg: #mych1, #mych2, #mych3

//            if (channelSelector.getSelectedChannel().isEmpty()) {
                slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//            } else {
//                // send message to multiple channels
//                List<String> channels = Arrays.asList(channelSelector.getSelectedChannel().split("\\s*,\\s*"));
//                for (String channel: channels) {
//                    payload.setChannel(channel.trim());
//                    slackNotifier.SendSlackNotification(hookSelector.getSelectedHook(), gson.toJson(payload));
//                }
//            }
        }

    }

    private void addField(SlackAttachment attachment, String title, String message) {
        SlackAttachmentField field = new SlackAttachmentField();
        field.setTitle(title);
        field.setValue(message);
        field.setShort(false);
        attachment.addField(field);
    }

    private void addReviewers(SlackAttachment attachment, Set<PullRequestParticipant> reviewers) {
        if (reviewers.isEmpty()) {
            return;
        }
        String names = "";
        for(PullRequestParticipant p : reviewers) {
            names += String.format("@%s ", p.getUser().getSlug());
        }
        this.addField(attachment, "Reviewers", names);
    }
}
