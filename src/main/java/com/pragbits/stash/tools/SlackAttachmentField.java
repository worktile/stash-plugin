package com.pragbits.stash.tools;
import com.google.gson.annotations.*;

public class SlackAttachmentField {


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public boolean isShort() {
        return isShort;
    }

    public void setShort(boolean isShort) {
        this.isShort = isShort;
    }

    private String title;
    private String value;
    @SerializedName("short")
    private boolean isShort;
}