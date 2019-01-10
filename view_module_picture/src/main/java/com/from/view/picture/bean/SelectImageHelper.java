package com.from.view.picture.bean;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 存储选中的ImageItem
 * <p>
 * author: ym.li
 * since: 2018/11/3
 */
public class SelectImageHelper {
    //是否选择当前目录下所有图片
    public boolean folderAllImage;
    //当前选中的图片
    public int selectPosition;
    //存储当前选中的图片
    private Set<ImageItem> mImageSet;
    //当前目录下所有图片
    private Set<ImageItem> mFolderAllImage;
    //图片选中监听回调
    private OnImageSelectUpdateListener mOnImageSelectUpdateListener;

    private SelectImageHelper() {

    }

    /**
     * instance
     *
     * @return SelectImageHelper
     */
    public static SelectImageHelper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * instance build
     *
     * @return SelectImageHelper
     */
    public static SelectImageHelper buildInstance() {
        SelectImageHelper bean = InstanceHolder.INSTANCE;
        bean.build();
        return bean;
    }

    private void build() {
        mImageSet = new LinkedHashSet<>();
        mFolderAllImage = new LinkedHashSet<>();
    }

    /**
     * 设置图片选中回调
     *
     * @param updateListener OnImageSelectUpdateListener
     */
    public void setOnImageSelectUpdateListener(OnImageSelectUpdateListener updateListener) {
        this.mOnImageSelectUpdateListener = updateListener;
    }

    /**
     * 刷新当前图片选中状态
     *
     * @param position 具体第几张图片
     */
    public void notifyImageItem(int position) {
        if (null != mOnImageSelectUpdateListener) {
            mOnImageSelectUpdateListener.notify(position);
        }
    }

    /**
     * 重置选中图片角标
     */
    public void resetSelectPosition() {
        this.selectPosition = 0;
    }

    /**
     * 图片选中时操作，如果没有选中则添加进集合，如果存在那么移除
     *
     * @param imageItem 图片
     */
    public void addImageItem(ImageItem imageItem) {
        if (mImageSet.contains(imageItem)) {
            mImageSet.remove(imageItem);
        } else {
            mImageSet.add(imageItem);
        }
    }

    public void addAllImageItem(List<ImageItem> imageItems) {
        mFolderAllImage.addAll(imageItems);
    }

    public boolean contains(ImageItem imageItem) {
        return mImageSet.contains(imageItem);
    }

    /**
     * 获取当前目录下所有图片或获取当前选中图片
     *
     * @return ArrayList<ImageItem>
     */
    public ArrayList<ImageItem> getAllImageItem() {
        return folderAllImage ? getFolderAllImage() : getSelectImageItem();
    }

    /**
     * 返回当前目录下所有图片
     *
     * @return ArrayList<ImageItem>
     */
    public ArrayList<ImageItem> getFolderAllImage() {
        //‘java.lang.Object[] java.util.Collection.toArray()’ on a null object
        if (mFolderAllImage == null || mFolderAllImage.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(mFolderAllImage);
    }

    /**
     * 获取当前选中的所有图片
     *
     * @return ArrayList<ImageItem>
     */
    public ArrayList<ImageItem> getSelectImageItem() {
        //‘java.lang.Object[] java.util.Collection.toArray()’ on a null object
        if (mImageSet == null || mImageSet.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(mImageSet);
    }

    /**
     * 获取当前目录下图片总和或选中图片总和
     *
     * @return 图片数量
     */
    public int getMaxImageCount() {
        return folderAllImage ? getFolderAllImageCount() : getSelectCount();
    }

    /**
     * 获取当前目录下图片数量总和
     *
     * @return 图片数量
     */
    public int getFolderAllImageCount() {
        return getFolderAllImage().size();
    }

    /**
     * 获取选中图片数量总和
     *
     * @return 图片数量
     */
    public int getSelectCount() {
        return mImageSet.size();
    }

    /**
     * 图片Select时回调方法
     */
    public interface OnImageSelectUpdateListener {
        /**
         * 具体图片
         *
         * @param position 第几张图片
         */
        void notify(int position);
    }

    private static class InstanceHolder {
        private static final SelectImageHelper INSTANCE = new SelectImageHelper();
    }
}
