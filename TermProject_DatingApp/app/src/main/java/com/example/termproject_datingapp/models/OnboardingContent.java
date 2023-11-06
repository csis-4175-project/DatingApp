package com.example.termproject_datingapp.models;

import java.util.ArrayList;

public class OnboardingContent {

    private int imageId;
    private String header;
    private String messages;

    public OnboardingContent() {
    }

    public int getImageId() {
        return imageId;
    }

    public String getHeader() {
        return header;
    }

    public String getMessages() {
        return messages;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
