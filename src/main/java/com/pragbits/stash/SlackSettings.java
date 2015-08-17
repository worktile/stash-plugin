package com.pragbits.stash;

public interface SlackSettings {

    boolean isSlackNotificationsOverrideEnabled();
    boolean isSlackNotificationsEnabled();
    boolean isSlackNotificationsOpenedEnabled();
    boolean isSlackNotificationsReopenedEnabled();
    boolean isSlackNotificationsUpdatedEnabled();
    boolean isSlackNotificationsApprovedEnabled();
    boolean isSlackNotificationsUnapprovedEnabled();
    boolean isSlackNotificationsDeclinedEnabled();
    boolean isSlackNotificationsMergedEnabled();
    boolean isSlackNotificationsCommentedEnabled();
    boolean isSlackNotificationsEnabledForPush();
    NotificationLevel getNotificationLevel();
    NotificationLevel getNotificationPrLevel();
    String getSlackChannelName();
    String getSlackWebHookUrl();

}
