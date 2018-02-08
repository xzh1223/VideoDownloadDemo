package com.example.xzh.videodownloaddemo.download;

/**
 * Created by xzh on 2018/2/2.
 *
 */

public interface DownloadListener {
    void onProgress(int progress, String currentProgress, String allProgress);

    void onSuccess(String path);

//    void onPaused();

    void onFailed();
}
