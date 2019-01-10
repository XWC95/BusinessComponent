package com.from.view.picture.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.from.view.photoview.OnPhotoTapListener;
import com.from.view.photoview.PhotoView;
import com.from.view.picture.SelectorSpec;
import com.from.view.picture.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览ViewPager
 * <p>
 * author: ym.li
 * since: 2018/11/3
 */

public class PreviewAdapter extends PagerAdapter {
    private List<ImageItem> mImageList;
    private SelectorSpec mSelectorSpec;
    private OnPhotoTapListener mOnPhotoTapListener;
    private Context mContext;
    private List<PhotoView> mPhotoViews = new ArrayList<>(4);

    public PreviewAdapter(List<ImageItem> imageItems, Context context) {
        this.mContext = context;
        this.mImageList = null == imageItems ? new ArrayList<ImageItem>() : imageItems;
        this.mSelectorSpec = SelectorSpec.getInstance();
        buildPhotoView();
    }

    private void buildPhotoView() {
        for (int i = 0; i < 4; i++) {
            PhotoView photoView = new PhotoView(mContext);
            photoView.setAdjustViewBounds(true);
            mPhotoViews.add(photoView);
        }
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.mOnPhotoTapListener = onPhotoTapListener;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof PhotoView) {
            PhotoView photoView = (PhotoView) object;
            photoView.setImageBitmap(null);
            mPhotoViews.add(photoView);
            container.removeView((View) object);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = mPhotoViews.remove(0);
        ImageItem imageItem = mImageList.get(position);
        container.addView(photoView);
        mSelectorSpec.getImageLoader().imageLoader(photoView, imageItem.path);
        if (null != mOnPhotoTapListener) {
            photoView.setOnPhotoTapListener(mOnPhotoTapListener);
        }
        return photoView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}
