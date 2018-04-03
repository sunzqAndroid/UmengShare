package com.umeng.share;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 分享内容实体
 */

public class ShareEntity {

    /**
     * 分享平台
     **/
    public String platform;

    /**
     * 标题
     */
    public String title;
    /**
     * 内容
     */
    public String text;
    /**
     * 图文分享的图片地址
     */
    public String imageUrl;
    /**
     * 图文分享的本地图片文件
     */
    public File imageFile;
    /**
     * 图文分享的资源图片id
     */
    public int resId;
    /**
     * 图文分享的bitmap
     */
    public Bitmap bitmap;
    /**
     * 图文分享的图片文件byte数组
     */
    public byte[] bytes;
    /**
     * 链接分享的缩略图地址
     */
    public String thumbUrl;
    /**
     * 缩略图的本地图片文件
     */
    public File thumbFile;
    /**
     * 缩略图的资源图片id
     */
    public int thumbResId;
    /**
     * 缩略图的bitmap
     */
    public Bitmap thumbBitmap;
    /**
     * 缩略图文件byte数组
     */
    public byte[] thumbBytes;
    /**
     * 链接分享的摘要
     */
    public String description;
    /**
     * 链接地址
     */
    public String url;
}
