package com.taojiji.view.picture.bean;

import java.io.Serializable;

public class ImageItem implements Serializable {
    public String name;       //图片的名字
    public String path;       //图片的路径
    public int height;        //图片的高度
    public int width;        //图片的高度
    public long size;         //图片大小
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间

    public ImageItem(String path, long time, String name, String mimeType, int width, int height, long size) {
        this.path = path;
        this.mimeType = mimeType;
        this.addTime = time;
        this.name = name;
        this.width = width;
        this.height = height;
        this.size = size;
    }

    public ImageItem(String path, String name) {
        this.path = path;
        this.name = name;
    }

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.addTime == other.addTime;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
