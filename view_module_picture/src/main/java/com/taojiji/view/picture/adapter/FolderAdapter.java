package com.taojiji.view.picture.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taojiji.view.picture.R;
import com.taojiji.view.picture.SelectorSpec;
import com.taojiji.view.picture.bean.ImageFolder;
import com.taojiji.view.picture.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹目录
 * <p>
 * author: ym.li
 * since: 2018/11/3
 */

public class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<ImageFolder> mFolderList;
    private Context mContext;
    private SelectorSpec mSelectorSpec;
    private int mSelectItemPosition;
    private OnFolderSelectListener mFolderSelectListener;

    public FolderAdapter(Context context, List<ImageFolder> folderList) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mFolderList = folderList == null ? new ArrayList<ImageFolder>() : folderList;
        this.mSelectorSpec = SelectorSpec.getInstance();
    }

    public void setFolderSelectListener(OnFolderSelectListener folderSelectListener) {
        this.mFolderSelectListener = folderSelectListener;
    }

    public void setNewData(List<ImageFolder> folders) {
        this.mFolderList = folders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.maven_picture_item_folder_list, viewGroup, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        FolderViewHolder holder = (FolderViewHolder) viewHolder;
        final ImageFolder folder = getItem(viewHolder.getAdapterPosition());
        ArrayList<ImageItem> images = folder.images;
        holder.tvFolderName.setText(folder.name);
        holder.ivSelect.setImageResource(mSelectItemPosition == viewHolder.getAdapterPosition() ? R.drawable.maven_picture_list_selected : R.drawable.maven_picture_list_unselected);
        if (images != null && !images.isEmpty()) {
            String imageCount = mContext.getString(R.string.maven_picture_how_image_count, String.valueOf(images.size()));
            holder.tvFolderSize.setText(imageCount);
            mSelectorSpec.getImageLoader().imageLoader(((FolderViewHolder) viewHolder).ivImage, images.get(0).path);
        } else {
            holder.tvFolderSize.setText(R.string.maven_picture_zero_image);
            holder.ivImage.setImageBitmap(null);
        }
        if (null != mFolderSelectListener) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFolderSelectListener.OnFolderSelect(viewHolder.getAdapterPosition());
                }
            });
        }
    }

    public ImageFolder getItem(int position) {
        return mFolderList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFolderList.size();
    }

    public void updateSelectItem(int position) {
        this.mSelectItemPosition = position;
        notifyDataSetChanged();
    }

    public interface OnFolderSelectListener {
        void OnFolderSelect(int position);
    }

    private static class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivSelect;
        TextView tvFolderName;
        TextView tvFolderSize;

        FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivSelect = itemView.findViewById(R.id.iv_select);
            tvFolderName = itemView.findViewById(R.id.tv_folder_name);
            tvFolderSize = itemView.findViewById(R.id.tv_folder_size);
        }
    }
}
