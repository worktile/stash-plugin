package com.pragbits.stash.tools;

import com.atlassian.stash.repository.Repository;
import com.pragbits.stash.ImmutableLesschatSettings;
import com.pragbits.stash.LesschatGlobalSettingsService;
import com.pragbits.stash.LesschatSettings;
import com.pragbits.stash.LesschatSettingsService;

public class SettingsSelector {

    private LesschatSettingsService lesschatSettingsService;
    private LesschatGlobalSettingsService lesschatGlobalSettingsService;
    private LesschatSettings lesschatSettings;
    private Repository repository;
    private LesschatSettings resolvedlesschatSettings;


    private static final String KEY_GLOBAL_SETTING_HOOK_URL = "stash2lesschat.globalsettings.hookurl";
    private static final String KEY_GLOBAL_SETTING_CHANNEL_NAME = "stash2lesschat.globalsettings.channelname";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsreopenedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsupdatedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsunapprovedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsdeclinedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationsmergedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationscommentedenabled";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL = "stash2lesschat.globalsettings.lesschatnotificationslevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL = "stash2lesschat.globalsettings.lesschatnotificationsprlevel";
    private static final String KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED = "stash2lesschat.globalsettings.lesschatnotificationspushenabled";


    public SettingsSelector(LesschatSettingsService lesschatSettingsService, LesschatGlobalSettingsService lesschatGlobalSettingsService, Repository repository) {
        this.lesschatSettingsService = lesschatSettingsService;
        this.lesschatGlobalSettingsService = lesschatGlobalSettingsService;
        this.repository = repository;
        this.lesschatSettings = lesschatSettingsService.getlesschatSettings(repository);
        this.setResolvedlesschatSettings();
    }

    public LesschatSettings getResolvedlesschatSettings() {
        return this.resolvedlesschatSettings;
    }

    private void setResolvedlesschatSettings() {
        resolvedlesschatSettings = new ImmutableLesschatSettings(
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? true : false,
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsOpenedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsOpenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_OPENED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsReopenedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsReopenedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_REOPENED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsUpdatedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsUpdatedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UPDATED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsApprovedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsApprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_APPROVED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsUnapprovedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsUnapprovedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_UNAPPROVED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsDeclinedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsDeclinedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_DECLINED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsMergedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsMergedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_MERGED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsCommentedEnabled() : lesschatGlobalSettingsService.getlesschatNotificationsCommentedEnabled(KEY_GLOBAL_SETTING_NOTIFICATIONS_COMMENTED_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.islesschatNotificationsEnabledForPush() : lesschatGlobalSettingsService.getlesschatNotificationsEnabledForPush(KEY_GLOBAL_SETTING_NOTIFICATIONS_PUSH_ENABLED),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.getNotificationLevel() : lesschatGlobalSettingsService.getNotificationLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_LEVEL),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.getNotificationPrLevel() : lesschatGlobalSettingsService.getNotificationPrLevel(KEY_GLOBAL_SETTING_NOTIFICATIONS_PR_LEVEL),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.getlesschatChannelName() : lesschatGlobalSettingsService.getChannelName(KEY_GLOBAL_SETTING_CHANNEL_NAME),
                lesschatSettings.islesschatNotificationsOverrideEnabled() ? lesschatSettings.getlesschatWebHookUrl() : lesschatGlobalSettingsService.getWebHookUrl(KEY_GLOBAL_SETTING_HOOK_URL)
        );
    }

}
