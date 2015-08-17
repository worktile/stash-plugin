package com.pragbits.stash.tools;


public class ChannelSelector {

    private String globalChannel;
    private String localChannel;
    private String selectedChannel;

    public  ChannelSelector(String globalChannel, String localChannel) {
        this.globalChannel = globalChannel;
        this.localChannel = localChannel;
        this.selectedChannel = "";

        setChannel();
    }

    public String getSelectedChannel() {
        return selectedChannel;
    }

    private void setChannel() {
        if (!globalChannel.isEmpty()) {
            selectedChannel = globalChannel;
        }
        if (!localChannel.isEmpty()) {
            selectedChannel = localChannel;
        }


    }
}
