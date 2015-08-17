package com.pragbits.stash;

import com.pragbits.stash.soy.SelectFieldOption;

public enum NotificationLevel implements SelectFieldOption {
    MINIMAL("Minimal"),
    COMPACT("Compact"),
    VERBOSE("Verbose");

    private final String text;

    NotificationLevel(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public String value() {
        return name();
    }

}
