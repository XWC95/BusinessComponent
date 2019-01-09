package com.taojiji.view.picture.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.taojiji.view.picture.R;
import com.taojiji.view.picture.SelectorSpec;
import com.taojiji.view.picture.bean.ImageItem;
import com.taojiji.view.picture.bean.SelectImageHelper;
import com.taojiji.view.picture.weight.SuperCheckBox;

import java.util.List;

/**
 * Doc  相册图片Adapter
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CAMERA_ITEM = 0X001F;
    private static final int IMAGE_ITEM = 0X002F;
    private Context mContext;
    private List<ImageItem> mImageItems;
    private RecyclerView mRecyclerView;
    private int mImageResize;
    private OnItemClickListener mOnItemClickListener;
    private SelectImageHelper mSelectItemBean;
    private OnImageSelectListener mOnImageSelectListener;
    private SelectorSpec mSelectorSpec;
    private boolean mIsOpenCamera;
    private OnTakePhotoListener mOnTakePhotoListener;

    public ImagesAdapter(List<ImageItem> imageItems, RecyclerView recyclerView, SelectImageHelper selectImageBean) {
        this.mImageItems = imageItems;
        this.mSelectItemBean = selectImageBean;
        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
        this.mSelectorSpec = SelectorSpec.getInstance();
        this.mIsOpenCamera = mSelectorSpec.isOpenCamera();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public void setOnTakePhotoListener(OnTakePhotoListener onTakePhotoListener) {
        mOnTakePhotoListener = onTakePhotoListener;
    }

    public void setOnImageSelectListener(OnImageSelectListener imageSelectListener) {
        this.mOnImageSelectListener = imageSelectListener;
    }

    public void setNewData(List<ImageItem> list) {
        this.mImageItems = list;
        notifyDataSetChanged();
    }

    public List<ImageItem> getAllImageItem() {
        return mImageItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case CAMERA_ITEM:
                return new CameraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.maven_picture_item_camera_list, viewGroup, false));
            case IMAGE_ITEM:
                return new ImagesViewHolder(LayoutInflater.from(mContext).inflate(R.layout.maven_picture_item_images_list, viewGroup, false));
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CameraViewHolder) {
            bindCameraViewHolder(viewHolder);
        } else if (viewHolder instanceof ImagesViewHolder) {
            bindImagesViewHolder(viewHolder);
        }
    }

    private void bindCameraViewHolder(RecyclerView.ViewHolder viewHolder) {
        if (null != mOnTakePhotoListener) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnTakePhotoListener.onTakePhoto();
                }
            });
        }
    }

    private void bindImagesViewHolder(final RecyclerView.ViewHolder viewHolder) {
        final ImagesViewHolder imagesViewHolder = (ImagesViewHolder) viewHolder;
        if (null != mOnImageSelectListener) {
            imagesViewHolder.mSuperCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectItemBean.getSelectCount() >= mSelectorSpec.getMaxSelectImage() && imagesViewHolder.mSuperCheckBox.isChecked()) {
                        imagesViewHolder.mSuperCheckBox.setChecked(false);
                        String maxSelect = String.format(mContext.getString(R.string.maven_picture_max_select_image), String.valueOf(mSelectorSpec.getMaxSelectImage()));
                        Toast.makeText(mContext, maxSelect, Toast.LENGTH_LONG).show();
                    } else {
                        mOnImageSelectListener.onImageSelect(imagesViewHolder.mSuperCheckBox, viewHolder.getLayoutPosition());
                    }
                }
            });
        }
        if (null != mOnItemClickListener) {
            imagesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getSurePosition(viewHolder.getLayoutPosition()));
                }
            });
        }
        ImageItem item = getItem(viewHolder.getLayoutPosition());
        imagesViewHolder.mSuperCheckBox.setChecked(mSelectItemBean.contains(item));
        imagesViewHolder.mSuperCheckBox.setVisibility(mSelectorSpec.getMaxSelectImage() == 1 ? View.GONE : View.VISIBLE);
        imagesViewHolder.mMaskView.setVisibility(mSelectItemBean.contains(item) ? View.VISIBLE : View.GONE);
        mSelectorSpec.getImageLoader().imageLoader(imagesViewHolder.mImage, item.path, getImageResize(mContext), getImageResize(mContext), R.drawable.maven_picture_ic_default_image);
    }

    private int getSurePosition(int position) {
        return mIsOpenCamera ? position - 1 : position;
    }

    public ImageItem getItem(int position) {
        return mImageItems.get(getSurePosition(position));
    }

    private int getImageResize(Context context) {
        if (mImageResize == 0) {
            RecyclerView.LayoutManager lm = mRecyclerView.getLayoutManager();
            if (null != lm) {
                int spanCount = ((GridLayoutManager) lm).getSpanCount();
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                int availableWidth = screenWidth - context.getResources().getDimensionPixelSize(R.dimen.maven_picture_image_grid_spacing) * (spanCount - 1);
                mImageResize = availableWidth / spanCount;
            }
        }
        return mImageResize;
    }

    @Override
    public int getItemViewType(int position) {
        return (mIsOpenCamera && position == 0) ? CAMERA_ITEM : IMAGE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mImageItems == null ? 0 : (mIsOpenCamera ? mImageItems.size() + 1 : mImageItems.size());
    }

    public List<ImageItem> getData() {
        return mImageItems;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnTakePhotoListener {
        void onTakePhoto();
    }

    public interface OnImageSelectListener {
        void onImageSelect(SuperCheckBox checkBox, int position);
    }

    private static class CameraViewHolder extends RecyclerView.ViewHolder {
        public CameraViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;
        SuperCheckBox mSuperCheckBox;
        View mMaskView;

        ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mSuperCheckBox = itemView.findViewById(R.id.checkbox);
            mImage = itemView.findViewById(R.id.image);
            mMaskView = itemView.findViewById(R.id.mask);
        }
    }
}
