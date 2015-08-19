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

public class DefaultLesschatSettingsService implements LesschatSettingsService {

    static final ImmutableLesschatSettings DEFAULT_CONFIG = new ImmutableLesschatSettings(
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

    static final String KEY_lesschat_OVERRIDE_NOTIFICATION = "lesschatNotificationsOverrideEnabled";
    static final String KEY_lesschat_NOTIFICATION = "lesschatNotificationsEnabled";
    static final String KEY_lesschat_OPENED_NOTIFICATION = "lesschatNotificationsOpenedEnabled";
    static final String KEY_lesschat_REOPENED_NOTIFICATION = "lesschatNotificationsReopenedEnabled";
    static final String KEY_lesschat_UPDATED_NOTIFICATION = "lesschatNotificationsUpdatedEnabled";
    static final String KEY_lesschat_APPROVED_NOTIFICATION = "lesschatNotificationsApprovedEnabled";
    static final String KEY_lesschat_UNAPPROVED_NOTIFICATION = "lesschatNotificationsUnapprovedEnabled";
    static final String KEY_lesschat_DECLINED_NOTIFICATION = "lesschatNotificationsDeclinedEnabled";
    static final String KEY_lesschat_MERGED_NOTIFICATION = "lesschatNotificationsMergedEnabled";
    static final String KEY_lesschat_COMMENTED_NOTIFICATION = "lesschatNotificationsCommentedEnabled";
    static final String KEY_lesschat_NOTIFICATION_PUSH = "lesschatNotificationsEnabledForPush";
    static final String KEY_lesschat_NOTIFICATION_LEVEL = "lesschatNotificationLevel";
    static final String KEY_lesschat_NOTIFICATION_PR_LEVEL = "lesschatNotificationPrLevel";
    static final String KEY_lesschat_CHANNEL_NAME = "lesschatChannelName";
    static final String KEY_lesschat_WEBHOOK_URL = "lesschatWebHookUrl";

    private final PluginSettings pluginSettings;
    private final PermissionValidationService validationService;

    private final Cache<Integer, LesschatSettings> cache = CacheBuilder.newBuilder().build(
            new CacheLoader<Integer, LesschatSettings>() {
                @Override
                public LesschatSettings load(@Nonnull Integer repositoryId) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> data = (Map) pluginSettings.get(repositoryId.toString());
                    return data == null ? DEFAULT_CONFIG : deserialize(data);
                }
            }
    );

    public DefaultLesschatSettingsService(PluginSettingsFactory pluginSettingsFactory, PermissionValidationService validationService) {
        this.validationService = validationService;
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey(PluginMetadata.getPluginKey());
    }

    @Nonnull
    @Override
    public LesschatSettings getlesschatSettings(@Nonnull Repository repository) {
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
    public LesschatSettings setlesschatSettings(@Nonnull Repository repository, @Nonnull LesschatSettings settings) {
        validationService.validateForRepository(checkNotNull(repository, "repository"), Permission.REPO_ADMIN);
        Map<String, String> data = serialize(checkNotNull(settings, "settings"));
        pluginSettings.put(Integer.toString(repository.getId()), data);
        cache.invalidate(repository.getId());

        return deserialize(data);
    }

    // note: for unknown reason, pluginSettngs.get() is not getting back the key for an empty string value
    // probably I don't know someyhing here. Applying a hack
    private Map<String, String> serialize(LesschatSettings settings) {
        ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder()
                .put(KEY_lesschat_OVERRIDE_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsOverrideEnabled()))
                .put(KEY_lesschat_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsEnabled()))
                .put(KEY_lesschat_OPENED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsOpenedEnabled()))
                .put(KEY_lesschat_REOPENED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsReopenedEnabled()))
                .put(KEY_lesschat_UPDATED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsUpdatedEnabled()))
                .put(KEY_lesschat_APPROVED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsApprovedEnabled()))
                .put(KEY_lesschat_UNAPPROVED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsUnapprovedEnabled()))
                .put(KEY_lesschat_DECLINED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsDeclinedEnabled()))
                .put(KEY_lesschat_MERGED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsMergedEnabled()))
                .put(KEY_lesschat_COMMENTED_NOTIFICATION, Boolean.toString(settings.islesschatNotificationsCommentedEnabled()))
                .put(KEY_lesschat_NOTIFICATION_PUSH, Boolean.toString(settings.islesschatNotificationsEnabledForPush()))
                .put(KEY_lesschat_NOTIFICATION_LEVEL, settings.getNotificationLevel().toString())
                .put(KEY_lesschat_NOTIFICATION_PR_LEVEL, settings.getNotificationPrLevel().toString())
                .put(KEY_lesschat_CHANNEL_NAME, settings.getlesschatChannelName().isEmpty() ? " " : settings.getlesschatChannelName())
                .put(KEY_lesschat_WEBHOOK_URL, settings.getlesschatWebHookUrl().isEmpty() ? " " : settings.getlesschatWebHookUrl())
                .build();

        return  immutableMap;
    }

    // note: for unknown reason, pluginSettngs.get() is not getting back the key for an empty string value
    // probably I don't know something here. Applying a hack
    private LesschatSettings deserialize(Map<String, String> settings) {
        return new ImmutableLesschatSettings(
                Boolean.parseBoolean(settings.get(KEY_lesschat_OVERRIDE_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_OPENED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_REOPENED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_UPDATED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_APPROVED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_UNAPPROVED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_DECLINED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_MERGED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_COMMENTED_NOTIFICATION)),
                Boolean.parseBoolean(settings.get(KEY_lesschat_NOTIFICATION_PUSH)),
                settings.containsKey(KEY_lesschat_NOTIFICATION_LEVEL) ? NotificationLevel.valueOf(settings.get(KEY_lesschat_NOTIFICATION_LEVEL)) : NotificationLevel.VERBOSE,
                settings.containsKey(KEY_lesschat_NOTIFICATION_PR_LEVEL) ? NotificationLevel.valueOf(settings.get(KEY_lesschat_NOTIFICATION_PR_LEVEL)) : NotificationLevel.VERBOSE,
                settings.get(KEY_lesschat_CHANNEL_NAME).toString().equals(" ") ? "" : settings.get(KEY_lesschat_CHANNEL_NAME).toString(),
                settings.get(KEY_lesschat_WEBHOOK_URL).toString().equals(" ") ? "" : settings.get(KEY_lesschat_WEBHOOK_URL).toString()
        );
    }

}
