package com.taojiji.view.picture.imageloader;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Doc  图片加载策略，可在项目中使用自己的图片加载框架
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public interface UIImageLoader extends Serializable {
    /**
     * @param imageView    ImageView
     * @param path         图片加载路径
     * @param height       高度
     * @param width        宽度
     * @param defaultImage 默认图
     */
    void imageLoader(ImageView imageView, String path, int height, int width, int defaultImage);

    /**
     * @param imageView ImageView
     * @param path      图片加载路径
     * @param height    高度
     * @param width     宽度
     */
    void imageLoader(ImageView imageView, String path, int height, int width);

    /**
     * @param imageView    ImageView
     * @param path         图片加载路径
     * @param defaultImage 默认图
     */
    void imageLoader(ImageView imageView, String path, int defaultImage);

    /**
     * @param imageView ImageView
     * @param path      图片加载路径
     */
    void imageLoader(ImageView imageView, String path);
}
