package com.developerdesk.xmppchat.datamodel;

public class ChatDataModel {

    private String message;
    private boolean isOwnMessage;

    public ChatDataModel(String message, boolean isOwnMessage) {
        this.message = message;
        this.isOwnMessage = isOwnMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOwnMessage() {
        return isOwnMessage;
    }

    public void setOwnMessage(boolean ownMessage) {
        isOwnMessage = ownMessage;
    }
}
