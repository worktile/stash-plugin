package com.pragbits.stash;

public class ImmutableLesschatSettings implements LesschatSettings {

    private final boolean lesschatNotificationsOverrideEnabled;
    private final boolean lesschatNotificationsEnabled;
    private final boolean lesschatNotificationsOpenedEnabled;
    private final boolean lesschatNotificationsReopenedEnabled;
    private final boolean lesschatNotificationsUpdatedEnabled;
    private final boolean lesschatNotificationsApprovedEnabled;
    private final boolean lesschatNotificationsUnapprovedEnabled;
    private final boolean lesschatNotificationsDeclinedEnabled;
    private final boolean lesschatNotificationsMergedEnabled;
    private final boolean lesschatNotificationsCommentedEnabled;
    private final boolean lesschatNotificationsEnabledForPush;
    private final NotificationLevel notificationLevel;
    private final NotificationLevel notificationPrLevel;
    private final String lesschatChannelName;
    private final String lesschatWebHookUrl;

    public ImmutableLesschatSettings(boolean lesschatNotificationsOverrideEnabled,
                                  boolean lesschatNotificationsEnabled,
                                  boolean lesschatNotificationsOpenedEnabled,
                                  boolean lesschatNotificationsReopenedEnabled,
                                  boolean lesschatNotificationsUpdatedEnabled,
                                  boolean lesschatNotificationsApprovedEnabled,
                                  boolean lesschatNotificationsUnapprovedEnabled,
                                  boolean lesschatNotificationsDeclinedEnabled,
                                  boolean lesschatNotificationsMergedEnabled,
                                  boolean lesschatNotificationsCommentedEnabled,
                                  boolean lesschatNotificationsEnabledForPush,
                                  NotificationLevel notificationLevel,
                                  NotificationLevel notificationPrLevel,
                                  String lesschatChannelName,
                                  String lesschatWebHookUrl) {
        this.lesschatNotificationsOverrideEnabled = lesschatNotificationsOverrideEnabled;
        this.lesschatNotificationsEnabled = lesschatNotificationsEnabled;
        this.lesschatNotificationsOpenedEnabled = lesschatNotificationsOpenedEnabled;
        this.lesschatNotificationsReopenedEnabled = lesschatNotificationsReopenedEnabled;
        this.lesschatNotificationsUpdatedEnabled = lesschatNotificationsUpdatedEnabled;
        this.lesschatNotificationsApprovedEnabled = lesschatNotificationsApprovedEnabled;
        this.lesschatNotificationsUnapprovedEnabled = lesschatNotificationsUnapprovedEnabled;
        this.lesschatNotificationsDeclinedEnabled = lesschatNotificationsDeclinedEnabled;
        this.lesschatNotificationsMergedEnabled = lesschatNotificationsMergedEnabled;
        this.lesschatNotificationsCommentedEnabled = lesschatNotificationsCommentedEnabled;
        this.lesschatNotificationsEnabledForPush = lesschatNotificationsEnabledForPush;
        this.notificationLevel = notificationLevel;
        this.notificationPrLevel = notificationPrLevel;
        this.lesschatChannelName = lesschatChannelName;
        this.lesschatWebHookUrl = lesschatWebHookUrl;
    }

    public boolean islesschatNotificationsOverrideEnabled() {
        return lesschatNotificationsOverrideEnabled;
    }

    public boolean islesschatNotificationsEnabled() {
        return lesschatNotificationsEnabled;
    }

    public boolean islesschatNotificationsOpenedEnabled() {
        return lesschatNotificationsOpenedEnabled;
    }

    public boolean islesschatNotificationsReopenedEnabled() {
        return lesschatNotificationsReopenedEnabled;
    }

    public boolean islesschatNotificationsUpdatedEnabled() {
        return lesschatNotificationsUpdatedEnabled;
    }

    public boolean islesschatNotificationsApprovedEnabled() {
        return lesschatNotificationsApprovedEnabled;
    }

    public boolean islesschatNotificationsUnapprovedEnabled() {
        return lesschatNotificationsUnapprovedEnabled;
    }

    public boolean islesschatNotificationsDeclinedEnabled() {
        return lesschatNotificationsDeclinedEnabled;
    }

    public boolean islesschatNotificationsMergedEnabled() {
        return lesschatNotificationsMergedEnabled;
    }

    public boolean islesschatNotificationsCommentedEnabled() {
        return lesschatNotificationsCommentedEnabled;
    }

    public boolean islesschatNotificationsEnabledForPush() {
        return lesschatNotificationsEnabledForPush;
    }

    public NotificationLevel getNotificationLevel() {
        return notificationLevel;
    }

    public NotificationLevel getNotificationPrLevel() {
        return notificationPrLevel;
    }

    public String getlesschatChannelName() {
        return lesschatChannelName;
    }

    public String getlesschatWebHookUrl() {
        return lesschatWebHookUrl;
    }

    @Override
    public String toString() {
        return "ImmutablelesschatSettings {" + "lesschatNotificationsOverrideEnabled=" + lesschatNotificationsOverrideEnabled +
                ", lesschatNotificationsEnabled=" + lesschatNotificationsEnabled +
                ", lesschatNotificationsOpenedEnabled=" + lesschatNotificationsOpenedEnabled +
                ", lesschatNotificationsReopenedEnabled=" + lesschatNotificationsReopenedEnabled +
                ", lesschatNotificationsUpdatedEnabled=" + lesschatNotificationsUpdatedEnabled +
                ", lesschatNotificationsApprovedEnabled=" + lesschatNotificationsApprovedEnabled +
                ", lesschatNotificationsUnapprovedEnabled=" + lesschatNotificationsUnapprovedEnabled +
                ", lesschatNotificationsDeclinedEnabled=" + lesschatNotificationsDeclinedEnabled +
                ", lesschatNotificationsMergedEnabled=" + lesschatNotificationsMergedEnabled +
                ", lesschatNotificationsCommentedEnabled=" + lesschatNotificationsCommentedEnabled +
                ", lesschatNotificationsEnabledForPush=" + lesschatNotificationsEnabledForPush +
                ", notificationLevel=" + notificationLevel +
                ", notificationPrLevel=" + notificationPrLevel +
                ", lesschatChannelName=" + lesschatChannelName +
                ", lesschatWebHookUrl=" + lesschatWebHookUrl + "}";
    }

}
