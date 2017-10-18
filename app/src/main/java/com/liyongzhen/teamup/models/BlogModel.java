package com.liyongzhen.teamup.models;

/**
 * Created by Administrator on 8/24/2017.
 */

public class BlogModel {

    private String fromId;
    private String postId;
    private String text;
    private long timestamp;
    private String imageUrl;
    private String isBlock;

    private int imageWidth;
    private int imageHeight;

    public BlogModel() {
        fromId = "";
        postId = "";
        text = "";
        timestamp = 0;
        imageWidth = 0;
        imageHeight = 0;
        imageUrl = "";
        isBlock = "false";
    }

    public BlogModel(String fromId, String postId, String text, long timestamp, String imageUrl, int imageWidth, int imageHeight) {
        this.fromId = fromId;
        this.postId = postId;
        this.text = text;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.isBlock = "false";
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

    public String getPostId() {
        return postId;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public String getIsBlock() {
        return isBlock;
    }
}
