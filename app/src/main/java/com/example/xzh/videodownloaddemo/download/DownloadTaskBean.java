package com.example.xzh.videodownloaddemo.download;

/**
 * Created by xzh on 2018/2/7.
 */

public class DownloadTaskBean {
    private int position;
    private DownloadTask downloadTask;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DownloadTask getDownloadTask() {
        return downloadTask;
    }

    public void setDownloadTask(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
    }

    @Override
    public String toString() {
        return "DownloadTaskBean{" +
                "position=" + position +
                ", downloadTask=" + downloadTask +
                '}';
    }
}
