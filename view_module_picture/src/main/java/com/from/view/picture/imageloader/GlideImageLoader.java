package com.from.view.picture.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Doc  默认为Glide加载图片
 *
 * @author ym.li
 * @version 2.9.0
 * @since 2018/11/2/002
 */
public class GlideImageLoader implements UIImageLoader {
    @Override
    public void imageLoader(ImageView imageView, String path, int height, int width, int defaultImage) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(path)
                .apply(new RequestOptions()
                        .placeholder(defaultImage)
                        .error(defaultImage)
                        .override(width, height))
                .into(imageView);
    }

    @Override
    public void imageLoader(ImageView imageView, String path, int height, int width) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(path)
                .apply(new RequestOptions()
                        .override(width, height))
                .into(imageView);
    }

    @Override
    public void imageLoader(ImageView imageView, String path, int defaultImage) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(path)
                .apply(new RequestOptions()
                        .placeholder(defaultImage)
                        .error(defaultImage))
                .into(imageView);
    }

    @Override
    public void imageLoader(ImageView imageView, String path) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(path)
                .apply(new RequestOptions())
                .into(imageView);
    }
}
