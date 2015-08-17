package com.pragbits.stash;

public class PluginMetadata {

    public static String getPluginKey() {
        return "com.pragbits.stash.stash2slack";
    }

    public static String getCompleteModuleKey(String moduleKey) {
        return getPluginKey() + ":" + moduleKey;
    }
}
