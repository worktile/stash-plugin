package com.pragbits.stash;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.apache.xpath.operations.Bool;

public class DefaultGlobalLesschatSettingsService implements LesschatGlobalSettingsService {

    private final PluginSettings pluginSettings;

    public DefaultGlobalLesschatSettingsService(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
    }

    @Override
    public String getWebHookUrl(String key) {
        Object retval = pluginSettings.get(key);
        if (null == retval) {
            return "";
        }

        return retval.toString();
    }

    @Override
    public void setWebHookUrl(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public String getChannelName(String key) {
        Object retval = pluginSettings.get(key);
        if (null == retval) {
            return "";
        }

        return retval.toString();
    }

    @Override
    public void setChannelName(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsEnabled(String key) {
        return Boolean.parseBoolean((String) pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsOpenedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsOpenedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsReopenedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsReopenedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsUpdatedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsUpdatedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsApprovedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsApprovedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsUnapprovedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsUnapprovedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsDeclinedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsDeclinedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsMergedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsMergedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsCommentedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsCommentedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getlesschatNotificationsEnabledForPush(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setlesschatNotificationsEnabledForPush(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public NotificationLevel getNotificationLevel(String key) {
        Object retval = pluginSettings.get(key);
        if (null == retval) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf((String)pluginSettings.get(key));
        }
    }

    @Override
    public void setNotificationLevel(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public NotificationLevel getNotificationPrLevel(String key) {
        Object retval = pluginSettings.get(key);
        if (null == retval) {
            return NotificationLevel.VERBOSE;
        } else {
            return NotificationLevel.valueOf((String)pluginSettings.get(key));
        }
    }

    @Override
    public void setNotificationPrLevel(String key, String value) {
        pluginSettings.put(key, value);
    }

}
