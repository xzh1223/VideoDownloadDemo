package com.example.xzh.videodownloaddemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xzh.videodownloaddemo.R;
import com.example.xzh.videodownloaddemo.adapter.VideoAdapter;
import com.example.xzh.videodownloaddemo.bean.VideoBean;
import com.example.xzh.videodownloaddemo.download.DownloadListener;
import com.example.xzh.videodownloaddemo.download.DownloadTask;
import com.example.xzh.videodownloaddemo.download.DownloadTaskBean;
import com.example.xzh.videodownloaddemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {

    private RecyclerView rvVideo;
    Context context = this;
    private List<VideoBean> mList = new ArrayList<>();
    private VideoAdapter mAdapter;
    private List<DownloadTaskBean> downloadTaskBeans = new ArrayList<>();
    private DownloadTask downloadTask;
//    private ImageView mIvDownloadStatus;
//    private TextView mTvCurrentProgress;
//    private TextView mTvAllProgress;
//    private ProgressBar mPbLoad;
//    private TextView mTvDownloadSpeed;
//    private TextView mTvSplit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * initial view
     */
    private void initView() {
        findViews();
        getData();
        setRecyclerView();
    }

    /**
     * set recyclerView
     */
    private void setRecyclerView() {
        rvVideo.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new VideoAdapter(context, mList);
        rvVideo.setAdapter(mAdapter);
    }

    /**
     * get data from http
     */
    private void getData() {
        VideoBean videoBean = new VideoBean();
        videoBean.setId(1);
        videoBean.setImage("image");
        videoBean.setTitle("喵喵喵");
        videoBean.setFileUrl("http://101.132.43.200:8080/coffee.apk");
        videoBean.setAllSize("4.2M");
        VideoBean videoBean2 = new VideoBean();
        videoBean2.setId(2);
        videoBean2.setImage("image");
        videoBean2.setTitle("喵喵喵");
        videoBean2.setFileUrl("http://101.132.43.200:8080/coffee2.apk");
        videoBean2.setAllSize("4.2M");
        mList.add(videoBean);
        mList.add(videoBean2);
    }

    /**
     * findViewById
     */
    private void findViews() {
        rvVideo = findViewById(R.id.rv_video);
    }

    /**
     * start download
     *
     * @param position          click's location
     * @param fileUrl           file's url
     * @param tvCurrentProgress current progress
     * @param tvAllProgress     all progress
     * @param pbLoad            progressBar
     * @param tvDownloadSpeed   download speed
     * @param ivDownloadStatus  image for click
     * @param tvSplit
     */
    public void startDownload(int position,
                              String fileUrl,
                              TextView tvCurrentProgress,
                              TextView tvAllProgress,
                              ProgressBar pbLoad,
                              TextView tvDownloadSpeed,
                              ImageView ivDownloadStatus,
                              TextView tvSplit) {
        // create and start download task
        downloadTask = new DownloadTask(
                downloadListener,
                tvCurrentProgress,
                tvAllProgress,
                pbLoad,
                tvDownloadSpeed,
                ivDownloadStatus,
                tvSplit);
        downloadTask.execute(fileUrl);

        // get adapter's holder.view
//        mTvCurrentProgress = tvCurrentProgress;
//        mTvAllProgress = tvAllProgress;
//        mPbLoad = pbLoad;
//        mIvDownloadStatus = ivDownloadStatus;
//        mTvDownloadSpeed = tvDownloadSpeed;
//        mTvSplit = tvSplit;

        saveDownloadTask(position);
    }

    /**
     * save downloadTask
     */
    private void saveDownloadTask(int position) {
        DownloadTaskBean downloadTaskBean = new DownloadTaskBean();
        downloadTaskBean.setPosition(position);
        downloadTaskBean.setDownloadTask(downloadTask);
        int num = 0;
        for (int i = 0; i < downloadTaskBeans.size(); i++) {
            if (downloadTaskBeans.get(i).getPosition() == position) {
                downloadTaskBeans.set(i, downloadTaskBean);
                num++;
            }
        }
        if (num == 0) {
            downloadTaskBeans.add(downloadTaskBean);
        }
    }

    /**
     * pause download
     *
     * @param position click's location
     */
    public void pauseDownload(int position) {
        for (int i = 0; i < downloadTaskBeans.size(); i++) {
            if (downloadTaskBeans.get(i).getPosition() == position) {
                downloadTaskBeans.get(i).getDownloadTask().cancel(true);
            }
        }

    }

    /**
     * custom listener to get download progress (but it is unable)
     */
    DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress, String currentProgress, String allProgress) {

        }

        @Override
        public void onSuccess(String path) {
            ToastUtil.toast(context, "下载成功");
        }

        @Override
        public void onFailed() {
            ToastUtil.toast(context, "下载失败");
        }
    };

}
