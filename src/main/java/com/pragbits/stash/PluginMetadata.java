package com.pragbits.stash;

public class PluginMetadata {

    public static String getPluginKey() {
        return "com.pragbits.stash.stash2lesschat";
    }

    public static String getCompleteModuleKey(String moduleKey) {
        return getPluginKey() + ":" + moduleKey;
    }
}
