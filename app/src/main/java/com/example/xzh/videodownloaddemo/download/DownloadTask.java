package com.example.xzh.videodownloaddemo.download;

import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xzh on 2018/2/2.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final int TYPE_FAILED = 0;
    private static final int TYPE_SUCCESS = 1;
    private static final int TYPE_PAUSED = 2;
    private final DownloadListener listener;
    private final TextView mTvDownloadSpeed;
    private final ImageView mIvDownloadStatus;
    private final TextView mTvSplit;
    private int lastProgress = 0;
    private String mPath;
    private Double mAllLength = 0.0;
    private Handler handler;
    long total_data = TrafficStats.getTotalRxBytes();
//    private boolean isPause = false;

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Integer doInBackground(String... params) {
        if (isCancelled()) {
            return TYPE_PAUSED;
        } else {
            InputStream is = null;
            RandomAccessFile savedFile = null;
            File file = null;
            try {
                long downloadedLength = 0; // download file's length
                // get file's info and save to path
                String downloadUrl = params[0];
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath();
                mPath = directory + fileName;
                file = new File(directory + fileName);

                if (file.exists()) {
                    downloadedLength = file.length(); // get downloaded file's length
                }

                long contentLength = getContentLength(downloadUrl);
                if (contentLength == 0) {
                    return TYPE_FAILED;
                } else if (contentLength == downloadedLength) {
                    return TYPE_SUCCESS;
                }

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                        .url(downloadUrl)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                if (response != null) {
                    is = response.body().byteStream();
                    savedFile = new RandomAccessFile(file, "rw");
                    savedFile.seek(downloadedLength);
                    byte[] b = new byte[1024];
                    int total = 0;
                    int len;
                    while ((len = is.read(b)) != -1) {
//                        if (isPause) {
//                            return TYPE_PAUSED;
//                        } else {
                            total += len;
                            savedFile.write(b, 0, len);
                            int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                            publishProgress(progress);
//                        }
                    }
                    response.body().close();
                    return TYPE_SUCCESS;
                }
            } catch (Exception e) {
                return TYPE_FAILED;
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (savedFile != null) {
                        savedFile.close();
                    }
//                if (file != null) {
//                    file.delete();
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return TYPE_FAILED;
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        final int progress = values[0];
        if (progress > lastProgress) {
            // get allProgress and currentProgress
            String allProgress = doubleToString(mAllLength / 1024 / 1024) + "M";
            String currentProgress = doubleToString(mAllLength / 1024 / 1024 * progress / 100) + "M";

            getSpeed();
            // set UI
            mTvCurrentProgress.setText(currentProgress);
            mTvAllProgress.setText(allProgress);
            mPbLoad.setProgress(progress);
            mTvSplit.setVisibility(View.VISIBLE);
            mPbLoad.setVisibility(View.VISIBLE);

            listener.onProgress(progress, currentProgress, allProgress);
            lastProgress = progress;
        }
    }

    /**
     * get speed
     */
    private void getSpeed() {
        mTvDownloadSpeed.setText(getNetSpeed() + "kb/s");
        Log.e("speed", "getSpeed: " + getNetSpeed());
    }

    /**
     *  sum download speed
     * @return speed
     */
    private int getNetSpeed() {
        long traffic_data = TrafficStats.getTotalRxBytes() - total_data;
        total_data = TrafficStats.getTotalRxBytes();
        return (int)traffic_data / 1024 ;
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param integer The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_SUCCESS:
                mTvCurrentProgress.setVisibility(View.GONE);
                mTvDownloadSpeed.setVisibility(View.GONE);
                mPbLoad.setVisibility(View.GONE);
                mIvDownloadStatus.setVisibility(View.GONE);
                mTvSplit.setVisibility(View.GONE);

                listener.onSuccess(mPath);
                break;
//            case TYPE_PAUSED:
//                listener.onPaused();
            default:
                break;
        }
    }

    /**
     * pause download
     */
//    public void pauseDownload() {
//        isPause = true;
//    }

    /**
     * get file's length from origin apk file
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            mAllLength = Double.valueOf(contentLength);
            response.close();
            return contentLength;
        }
        return 0;
    }

    private TextView mTvCurrentProgress;
    private TextView mTvAllProgress;
    private ProgressBar mPbLoad;

    public DownloadTask(
            DownloadListener listener,
            TextView tvCurrentProgress,
            TextView tvAllProgress,
            ProgressBar pbLoad,
            TextView tvDownloadSpeed,
            ImageView ivDownloadStatus,
            TextView tvSplit) {
        this.listener = listener;
        this.mTvCurrentProgress = tvCurrentProgress;
        this.mTvAllProgress = tvAllProgress;
        this.mPbLoad = pbLoad;
        this.mTvDownloadSpeed = tvDownloadSpeed;
        this.mIvDownloadStatus = ivDownloadStatus;
        this.mTvSplit = tvSplit;
    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.0").format(num);
    }
}
