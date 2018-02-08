package com.example.xzh.videodownloaddemo.bean;

/**
 * Created by xzh on 2018/2/7.
 */

public class VideoBean {
    private int id;
    private String image;
    private String title;
    private String allSize;
    private String fileUrl;
    public boolean isDownloaded = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAllSize() {
        return allSize;
    }

    public void setAllSize(String allSize) {
        this.allSize = allSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
