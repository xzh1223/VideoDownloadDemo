package com.example.xzh.videodownloaddemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xzh.videodownloaddemo.activity.DownloadActivity;
import com.example.xzh.videodownloaddemo.R;
import com.example.xzh.videodownloaddemo.bean.VideoBean;

import java.util.List;

/**
 * Created by xzh on 2017/12/5.
 * <p>
 * coffee list adapter
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private final List<VideoBean> mList;
    private final Context mContext;

    public VideoAdapter(Context context, List<VideoBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rv_video, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoBean videoBean = mList.get(position);
        if (videoBean != null) {
            holder.tvVideoTitle.setText(videoBean.getTitle());
            holder.tvAllProgress.setText(videoBean.getAllSize());
            holder.ivDownloadStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    videoBean.isDownloaded = !videoBean.isDownloaded;
                    if (videoBean.isDownloaded) {
                        ((DownloadActivity) mContext).startDownload(
                                position,
                                videoBean.getFileUrl(),
                                holder.tvCurrentProgress,
                                holder.tvAllProgress,
                                holder.pbLoad,
                                holder.tvDownloadSpeed,
                                holder.ivDownloadStatus,
                                holder.tvSplit);
                        // image.cover.color
                        holder.ivDownloadStatus.setImageResource(R.color.md_grey_300);
                        // progressBar.color
                        holder.pbLoad.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_red));
                    } else {
                        ((DownloadActivity) mContext).pauseDownload(position);
                        // image.cover.color
                        holder.ivDownloadStatus.setImageResource(R.color.md_grey_800);
                        // progressBar.color
                        holder.pbLoad.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.pg_grey));
                    }
                }
            });
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVideoImage;
        ImageView ivDownloadStatus;
        TextView tvVideoTitle;
        ProgressBar pbLoad;
        TextView tvCurrentProgress;
        TextView tvAllProgress;
        TextView tvDownloadSpeed;
        TextView tvSplit;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ivVideoImage = itemView.findViewById(R.id.iv_video_image);
            ivDownloadStatus = itemView.findViewById(R.id.iv_download_status);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            pbLoad = itemView.findViewById(R.id.pb_load);
            tvCurrentProgress = itemView.findViewById(R.id.tv_current_progress);
            tvAllProgress = itemView.findViewById(R.id.tv_all_progress);
            tvDownloadSpeed = itemView.findViewById(R.id.tv_download_speed);
            tvSplit = itemView.findViewById(R.id.tv_split);
        }
    }

}
