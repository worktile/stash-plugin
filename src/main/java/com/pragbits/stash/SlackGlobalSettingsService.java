package com.pragbits.stash;

public interface SlackGlobalSettingsService {

    // hook and channel name
    String getWebHookUrl(String key);
    void setWebHookUrl(String key, String value);

    String getChannelName(String key);
    void setChannelName(String key, String value);

    // pull requests are enabled and pr events
    boolean getSlackNotificationsEnabled(String key);
    void setSlackNotificationsEnabled(String key, String value);

    boolean getSlackNotificationsOpenedEnabled(String key);
    void setSlackNotificationsOpenedEnabled(String key, String value);

    boolean getSlackNotificationsReopenedEnabled(String key);
    void setSlackNotificationsReopenedEnabled(String key, String value);

    boolean getSlackNotificationsUpdatedEnabled(String key);
    void setSlackNotificationsUpdatedEnabled(String key, String value);

    boolean getSlackNotificationsApprovedEnabled(String key);
    void setSlackNotificationsApprovedEnabled(String key, String value);

    boolean getSlackNotificationsUnapprovedEnabled(String key);
    void setSlackNotificationsUnapprovedEnabled(String key, String value);

    boolean getSlackNotificationsDeclinedEnabled(String key);
    void setSlackNotificationsDeclinedEnabled(String key, String value);

    boolean getSlackNotificationsMergedEnabled(String key);
    void setSlackNotificationsMergedEnabled(String key, String value);

    boolean getSlackNotificationsCommentedEnabled(String key);
    void setSlackNotificationsCommentedEnabled(String key, String value);

    // push notifications are enabled and push options
    boolean getSlackNotificationsEnabledForPush(String key);
    void setSlackNotificationsEnabledForPush(String key, String value);

    NotificationLevel getNotificationLevel(String key);
    void setNotificationLevel(String key, String value);

    NotificationLevel getNotificationPrLevel(String key);
    void setNotificationPrLevel(String key, String value);

}
