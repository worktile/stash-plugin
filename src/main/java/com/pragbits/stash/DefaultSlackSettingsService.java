package com.pragbits.stash;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.stash.repository.Repository;
import com.atlassian.stash.user.Permission;
import com.atlassian.stash.user.PermissionValidationService;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultSlackSettingsService implements SlackSettingsService {

    static final ImmutableSlackSettings DEFAULT_CONFIG = new ImmutableSlackSettings(
            false,  // pr settings override enabled
            false,  // pull requests enabled
            true,   // opened
            true,   // reopened
            true,   // updated
            true,   // approved
            true,   // unapproved
            true,   // declined
            true,   // merged
            true,   // commented
            false,  // push enabled
            NotificationLevel.VERBOSE,
            NotificationLevel.VERBOSE,
            "",     // channel name override
            "");    // webhook override

    static final String KEY_SLACK_OVERRIDE_NOTIFICATION = "slackNotificationsOverrideEnabled";
    static final String KEY_SLACK_NOTIFICATION = "slackNotificationsEnabled";
    static final String KEY_SLACK_OPENED_NOTIFICATION = "slackNotificationsOpenedEnabled";
    static final String KEY_SLACK_REOPENED_NOTIFICATION = "slackNotificationsReopenedEnabled";
    static final String KEY_SLACK_UPDATED_NOTIFICATION = "slackNotificationsUpdatedEnabled";
    static final String KEY_SLACK_APPROVED_NOTIFICATION = "slackNotificationsApprovedEnabled";
    static final String KEY_SLACK_UNAPPROVED_NOTIFICATION = "slackNotificationsUnapprovedEnabled";
    static final String KEY_SLACK_DECLINED_NOTIFICATION = "slackNotificationsDeclinedEnabled";
    static final String KEY_SLACK_MERGED_NOTIFICATION = "slackNotificationsMergedEnabled";
    static final String KEY_SLACK_COMMENTED_NOTIFICATION = "slackNotificationsCommentedEnabled";
    static final String KEY_SLACK_NOTIFICATION_PUSH = "slackNotificationsEnabledForPush";
    static final String KEY_SLACK_NOTIFICATION_LEVEL = "slackNotificationLevel";
    static final String KEY_SLACK_NOTIFICATION_PR_LEVEL = "slackNotificationPrLevel";
    static final String KEY_SLACK_CHANNEL_NAME = "slackChannelName";
    static final String KEY_SLACK_WEBHOOK_URL = "slackWebHookUrl";

    private final PluginSettings pluginSettings;
    private final PermissionValidationService validationService;

    private final Cache<Integer, SlackSettings> cache = CacheBuilder.newBuilder().build(
            new CacheLoader<Integer, SlackSettings>() {
                @Override
                public SlackSettings load(@Nonnull Integer repositoryId) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> data = (Map) pluginSettings.get(repositoryId.toString());
                    return data == null ? DEFAULT_CONFIG : deserialize(data);
                }
            }
    );

    public DefaultSlackSettingsService(PluginSettingsFactory pluginSettingsFactory, PermissionValidationService validationService) {
        this.validationService = validationService;
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(PluginMetadata.getPluginKey());
    }

    @Nonnull
    @Override
    public SlackSettings getSlackSettings(@Nonnull Repository repository) {
        validationService.validateForRepository(checkNotNull(repository, "repository"), Permission.REPO_READ);

        try {
            //noinspection ConstantConditions
            return cache.get(repository.getId());
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    @Nonnull
    @Override
    public SlackSettings setSlackSettings(@Nonnull Repository repository, @Nonnull SlackSettings settings) {
        validationService.validateForRepository(checkNotNull(repository, "repository"), Permission.REPO_ADMIN);
        Map<String, String> data = serialize(checkNotNull(settings, "settings"));
        pluginSettings.put(Integer.toString(repository.getId()), data);
        cache.invalidate(repository.getId());

        return deserialize(data);
    }

    // note: for unknown reason, pluginSettngs.get() is not getting back the key for an empty string value
    // probably I don't know someyhing here. Applying a hack
    private Map<String, String> serialize(SlackSettings settings) {
        ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
                .put(KEY_SLACK_OVERRIDE_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsOverrideEnabled()))
                .put(KEY_SLACK_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsEnabled()))
                .put(KEY_SLACK_OPENED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsOpenedEnabled()))
                .put(KEY_SLACK_REOPENED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsReopenedEnabled()))
                .put(KEY_SLACK_UPDATED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsUpdatedEnabled()))
                .put(KEY_SLACK_APPROVED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsApprovedEnabled()))
                .put(KEY_SLACK_UNAPPROVED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsUnapprovedEnabled()))
                .put(KEY_SLACK_DECLINED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsDeclinedEnabled()))
                .put(KEY_SLACK_MERGED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsMergedEnabled()))
                .put(KEY_SLACK_COMMENTED_NOTIFICATION, Boolean.toString(settings.isSlackNotificationsCommentedEnabled()))
                .put(KEY_SLACK_NOTIFICATION_PUSH, Boolean.toString(settings.isSlackNotificationsEnabledForPush()))
                .put(KEY_SLACK_NOTIFICATION_LEVEL, settings.getNotificationLevel().toString())
                .put(KEY_SLACK_NOTIFICATION_PR_LEVEL, settings.getNotificationPrLevel().toString())
                .put(KEY_SLACK_CHANNEL_NAME, settings.getSlackChannelName().isEmpty() ? " " : settings.getSlackChannelName())
                .put(KEY_SLACK_WEBHOOK_URL, settings.getSlackWebHookUrl().isEmpty() ? " " : settings.getSlackWebHookUrl())
                .build();

        return  immutableMap;
    }

    // note: for unknown reason, pluginSettngs.get() is not getting back the key for an empty string value
    // probably I don't know something here. Applying a hack
    private SlackSettings deserialize(Map<String, String> settings) {
        return new ImmutableSlackSettings(
                Boolean.parseBoolean(settings.get(KEY_SLACK_OVERRIDE_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_OPENED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_REOPENED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_UPDATED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_APPROVED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_UNAPPROVED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_DECLINED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_MERGED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_COMMENTED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_SLACK_NOTIFICATION_PUSH)),
                settings.containsKey(KEY_SLACK_NOTIFICATION_LEVEL) ? NotificationLevel.valueOf(settings.get(KEY_SLACK_NOTIFICATION_LEVEL)) : NotificationLevel.VERBOSE,
                settings.containsKey(KEY_SLACK_NOTIFICATION_PR_LEVEL) ? NotificationLevel.valueOf(settings.get(KEY_SLACK_NOTIFICATION_PR_LEVEL)) : NotificationLevel.VERBOSE,
                settings.get(KEY_SLACK_CHANNEL_NAME).toString().equals(" ") ? "" : settings.get(KEY_SLACK_CHANNEL_NAME).toString(),
                settings.get(KEY_SLACK_WEBHOOK_URL).toString().equals(" ") ? "" : settings.get(KEY_SLACK_WEBHOOK_URL).toString()
        );
    }

}
