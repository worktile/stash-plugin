package com.pragbits.stash.tools;

import com.atlassian.stash.repository.Repository;
import com.pragbits.stash.ImmutableSlackSettings;
import com.pragbits.stash.SlackGlobalSettingsService;
import com.pragbits.stash.SlackSettings;
import com.pragbits.stash.SlackSettingsService;

public class SettingsSelector {

    private SlackSettingsService slackSettingsService;
    private SlackGlobalSettingsService slackGlobalSettingsService;
    private SlackSettings slackSettings;
    private Repository repository;
    private SlackSettings resolvedSlackSettings;


    private static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2slack.globalsettings.hookurl";
    private static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2slack.globalsettings.channelname";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2slack.globalsettings.slacknotificationsenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2slack.globalsettings.slacknotificationsreopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2slack.globalsettings.slacknotificationsupdatedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2slack.globalsettings.slacknotificationsunapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2slack.globalsettings.slacknotificationsdeclinedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2slack.globalsettings.slacknotificationsmergedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2slack.globalsettings.slacknotificationscommentedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2slack.globalsettings.slacknotificationslevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2slack.globalsettings.slacknotificationsprlevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2slack.globalsettings.slacknotificationspushenabled";


    public SettingsSelector(SlackSettingsService slackSettingsService, SlackGlobalSettingsService slackGlobalSettingsService, Repository repository) {
        this.slackSettingsService = slackSettingsService;
        this.slackGlobalSettingsService = slackGlobalSettingsService;
        this.repository = repository;
        this.slackSettings = slackSettingsService.getSlackSettings(repository);
        this.setResolvedSlackSettings();
    }

    public SlackSettings getResolvedSlackSettings() {
        return this.resolvedSlackSettings;
    }

    private void setResolvedSlackSettings() {
        resolvedSlackSettings = new ImmutableSlackSettings(
                slackSettings.isSlackNotificationsOverrideEnabled() ? true : false,
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsEnabled() : slackGlobalSettingsService.getSlackNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsOpenedEnabled() : slackGlobalSettingsService.getSlackNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsReopenedEnabled() : slackGlobalSettingsService.getSlackNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsUpdatedEnabled() : slackGlobalSettingsService.getSlackNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsApprovedEnabled() : slackGlobalSettingsService.getSlackNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsUnapprovedEnabled() : slackGlobalSettingsService.getSlackNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsDeclinedEnabled() : slackGlobalSettingsService.getSlackNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsMergedEnabled() : slackGlobalSettingsService.getSlackNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsCommentedEnabled() : slackGlobalSettingsService.getSlackNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.isSlackNotificationsEnabledForPush() : slackGlobalSettingsService.getSlackNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getNotificationLevel() : slackGlobalSettingsService.getNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getNotificationPrLevel() : slackGlobalSettingsService.getNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackChannelName() : slackGlobalSettingsService.getChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME),
                slackSettings.isSlackNotificationsOverrideEnabled() ? slackSettings.getSlackWebHookUrl() : slackGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL)
        );
    }

}
