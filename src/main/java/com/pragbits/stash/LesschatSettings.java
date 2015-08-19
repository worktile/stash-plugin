package com.pragbits.stash;

public interface LesschatSettings {

    boolean islesschatNotificationsOverrideEnabled();
    boolean islesschatNotificationsEnabled();
    boolean islesschatNotificationsOpenedEnabled();
    boolean islesschatNotificationsReopenedEnabled();
    boolean islesschatNotificationsUpdatedEnabled();
    boolean islesschatNotificationsApprovedEnabled();
    boolean islesschatNotificationsUnapprovedEnabled();
    boolean islesschatNotificationsDeclinedEnabled();
    boolean islesschatNotificationsMergedEnabled();
    boolean islesschatNotificationsCommentedEnabled();
    boolean islesschatNotificationsEnabledForPush();
    NotificationLevel getNotificationLevel();
    NotificationLevel getNotificationPrLevel();
    String getlesschatChannelName();
    String getlesschatWebHookUrl();

}
