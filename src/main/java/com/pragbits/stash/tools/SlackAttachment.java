package com.pragbits.stash.tools;
import java.util.LinkedList;
import java.util.List;

public class SlackAttachment {

    private String[] mrkdwn_in = new String[]{
            "pretext",
            "text",
            "title",
            "fields",
            "fallback"
    };

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getPretext() {
        return pretext;
    }

    public void setPretext(String pretext) {
        this.pretext = pretext;
    }

    public String getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorName() {
        return author_name;
    }

    public void setAuthorName(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthorIcon() {
        return author_icon;
    }

    public void setAuthorIcon(String author_icon) {
        this.author_icon = author_icon;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addField(SlackAttachmentField slackAttachmentField) {
        this.fields.add(slackAttachmentField);
    }

    public void removeField(int index) {
        this.fields.remove(index);
    }


    private List<SlackAttachmentField> fields = new LinkedList<SlackAttachmentField>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_link() {
        return title_link;
    }

    public void setTitle_link(String title_link) {
        this.title_link = title_link;
    }

    private String title;
    private String title_link;
    private String fallback;
    private String pretext;
    private String color;
    private String text;
    private String author_name;
    private String author_icon;
}
