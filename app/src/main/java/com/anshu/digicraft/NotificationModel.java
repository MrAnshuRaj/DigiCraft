package com.anshu.digicraft;

import java.util.Map;

public class NotificationModel{
    private String title;
    private String body;
    private String sender;
    private String createdAt;

    private Map<String, String> data;

    public NotificationModel(String title, String body, String sender, String createdAt, Map<String, String> data) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.createdAt = createdAt;
        this.data = data;
    }

//    public NotificationModel(String title, String body, String sender, String formattedTime, String customKey, String action, String screen) {
//
//    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getSender() { return sender; }
    public String getCreatedAt() { return createdAt; }

    public Map<String, String> getData() { return data; }

}
