package com.liyongzhen.teamup.models;

/**
 * Created by Administrator on 8/24/2017.
 */

public class ChatModel {

    private String fromId;
    private String toId;
    private String text;
    private long timestamp;
    private String imageUrl;
    private String videoUrl;

    private int imageWidth;
    private int imageHeight;

    public ChatModel() {
        fromId = "";
        toId = "";
        text = "";
        timestamp = 0;
        imageWidth = 0;
        imageHeight = 0;
        imageUrl = "";
        videoUrl = "";
    }

    public ChatModel(String fromId, String toId, String text, long timestamp, String imageUrl, int imageWidth, int imageHeight) {
        this.fromId = fromId;
        this.toId = toId;
        this.text = text;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.videoUrl = "";
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getFromId() {
        return fromId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getToId() {
        return toId;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
