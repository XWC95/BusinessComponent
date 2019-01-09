package com.taojiji.view.picture.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

import com.taojiji.view.picture.bean.ImageFolder;
import com.taojiji.view.picture.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取系统相册中的图片
 *
 * @author ym.li
 * @version 2.9.1
 * @since 2018年11月12日11:13:19
 */
public class ImageDataSource {
    /**
     * 从SDCard加载图片
     *
     * @param context  上下文
     * @param callback DataCallback
     */
    public static void loadImageForSDCard(final Context context, final DataCallback callback) {
        //由于扫描图片是耗时的操作，所以要在子线程处理。
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                //扫描图片
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = context.getContentResolver();
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.WIDTH,
                        MediaStore.Images.Media.HEIGHT,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.MIME_TYPE},
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED);
                ArrayList<ImageItem> images = new ArrayList<>();
                //读取扫描到的图片
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        //获取图片名称
                        String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        //获取图片时间
                        long time = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                        //获取图片类型
                        String mimeType = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                        //获取图片宽度
                        int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                        //获取图片宽度
                        int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                        //获取图片的大小
                        int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                        //过滤未下载完成或者不存在的文件
                        if (!"downloading".equals(getExtensionName(path)) && checkImgExists(path)) {
                            images.add(new ImageItem(path, time, name, mimeType, width, height, size));
                        }
                    }
                    mCursor.close();
                }
                Collections.reverse(images);
                callback.onSuccess(splitFolder(images));
            }
        }).start();
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    private static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 检查图片是否存在。ContentResolver查询处理的数据有可能文件路径并不存在。
     *
     * @param filePath 文件路径
     * @return 文件夹是否存在
     */
    private static boolean checkImgExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 把图片按文件夹拆分，第一个文件夹保存所有的图片
     *
     * @param images 图片文件
     * @return 返回图片文件集合
     */
    private static ArrayList<ImageFolder> splitFolder(ArrayList<ImageItem> images) {
        ArrayList<ImageFolder> folders = new ArrayList<>();
        folders.add(new ImageFolder("全部图片", images));
        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String path = images.get(i).path;
                String name = getFolderName(path);
                if (null != name && name.length() > 0) {
                    ImageFolder folder = getFolder(name, folders);
                    folder.addImage(images.get(i));
                }
            }
        }
        return folders;
    }

    /**
     * 根据图片路径，获取图片文件夹名称
     *
     * @param path 图片路径
     * @return 返回文件夹路径
     */
    private static String getFolderName(String path) {
        if (null != path && path.length() > 0) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    private static ImageFolder getFolder(String name, List<ImageFolder> folders) {
        if (!folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                ImageFolder folder = folders.get(i);
                if (name.equals(folder.name)) {
                    return folder;
                }
            }
        }
        ImageFolder newFolder = new ImageFolder(name);
        folders.add(newFolder);
        return newFolder;
    }

    /**
     * 资源加载回调
     */
    public interface DataCallback {
        /**
         * 获取相册图片资源回调
         *
         * @param folders List
         */
        void onSuccess(ArrayList<ImageFolder> folders);
    }
}
