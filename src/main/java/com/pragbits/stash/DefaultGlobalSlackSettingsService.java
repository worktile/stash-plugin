package com.pragbits.stash;


import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import org.apache.xpath.operations.Bool;

public class DefaultGlobalSlackSettingsService implements SlackGlobalSettingsService {

    private final PluginSettings pluginSettings;

    public DefaultGlobalSlackSettingsService(PluginSettingsFactory pluginSettingsFactory) {
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
    public boolean getSlackNotificationsEnabled(String key) {
        return Boolean.parseBoolean((String) pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsOpenedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsOpenedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsReopenedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsReopenedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsUpdatedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsUpdatedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsApprovedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsApprovedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsUnapprovedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsUnapprovedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsDeclinedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsDeclinedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsMergedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsMergedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsCommentedEnabled(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsCommentedEnabled(String key, String value) {
        pluginSettings.put(key, value);
    }

    @Override
    public boolean getSlackNotificationsEnabledForPush(String key) {
        return Boolean.parseBoolean((String)pluginSettings.get(key));
    }

    @Override
    public void setSlackNotificationsEnabledForPush(String key, String value) {
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
