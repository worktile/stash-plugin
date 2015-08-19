package com.pragbits.stash;

public interface LesschatGlobalSettingsService {

    // hook and channel name
    String getWebHookUrl(String key);
    void setWebHookUrl(String key, String value);

    String getChannelName(String key);
    void setChannelName(String key, String value);

    // pull requests are enabled and pr events
    boolean getlesschatNotificationsEnabled(String key);
    void setlesschatNotificationsEnabled(String key, String value);

    boolean getlesschatNotificationsOpenedEnabled(String key);
    void setlesschatNotificationsOpenedEnabled(String key, String value);

    boolean getlesschatNotificationsReopenedEnabled(String key);
    void setlesschatNotificationsReopenedEnabled(String key, String value);

    boolean getlesschatNotificationsUpdatedEnabled(String key);
    void setlesschatNotificationsUpdatedEnabled(String key, String value);

    boolean getlesschatNotificationsApprovedEnabled(String key);
    void setlesschatNotificationsApprovedEnabled(String key, String value);

    boolean getlesschatNotificationsUnapprovedEnabled(String key);
    void setlesschatNotificationsUnapprovedEnabled(String key, String value);

    boolean getlesschatNotificationsDeclinedEnabled(String key);
    void setlesschatNotificationsDeclinedEnabled(String key, String value);

    boolean getlesschatNotificationsMergedEnabled(String key);
    void setlesschatNotificationsMergedEnabled(String key, String value);

    boolean getlesschatNotificationsCommentedEnabled(String key);
    void setlesschatNotificationsCommentedEnabled(String key, String value);

    // push notifications are enabled and push options
    boolean getlesschatNotificationsEnabledForPush(String key);
    void setlesschatNotificationsEnabledForPush(String key, String value);

    NotificationLevel getNotificationLevel(String key);
    void setNotificationLevel(String key, String value);

    NotificationLevel getNotificationPrLevel(String key);
    void setNotificationPrLevel(String key, String value);

}
