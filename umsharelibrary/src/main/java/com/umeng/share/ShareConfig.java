package com.umeng.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;
import java.util.Map;

/**
 * Created by ibm on 2017/8/25.
 */

public class ShareConfig {
    public final static String PLATFORM_WEIXIN = "weixin";
    public final static String PLATFORM_WEIXIN_CIRCLE = "weixin_circle";
    public final static String PLATFORM_QQ = "qq";
    public final static String PLATFORM_QZONE = "qzone";
    public final static String PLATFORM_SINA = "sina";

    private ShareListener mShareListener;
    private AuthListener mAuthListener;
    private Activity mActivity;

    public static void get(Context context) {
        UMShareAPI.get(context);
    }

    /**
     * 设置是否开启友盟分享debug模式
     *
     * @param debug
     */
    public static void setDebug(boolean debug) {
        Config.DEBUG = debug;
    }


    public static void setWeixin(String id, String secret) {
        PlatformConfig.setWeixin(id, secret);
    }

    public static void setSinaWeibo(String key, String secret, String redirectUrl) {
        PlatformConfig.setSinaWeibo(key, secret, redirectUrl);
    }

    public static void setQQZone(String id, String key) {
        PlatformConfig.setQQZone(id, key);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if (mShareListener != null) {
                mShareListener.onStart(platform.toString());
            } else {
                ShareToast.showMessage(mActivity, "分享中");
            }
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (mShareListener != null) {
                mShareListener.onResult(platform.toString());
            } else {
                ShareToast.showMessage(mActivity, "分享成功");
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (mShareListener != null) {
                mShareListener.onError(platform.toString(), t);
            } else {
                ShareToast.showMessage(mActivity, "分享失败");
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (mShareListener != null) {
                mShareListener.onCancel(platform.toString());
            } else {
                ShareToast.showMessage(mActivity, "分享取消");
            }
        }
    };

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if (mAuthListener != null) {
                mAuthListener.onStart(platform.toString());
            } else {
                ShareToast.showMessage(mActivity, "开始授权");
            }
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (mAuthListener != null) {
                mAuthListener.onComplete(platform.toString(), action, data);
            } else {
                ShareToast.showMessage(mActivity, "授权成功");
                UMShareAPI.get(mActivity).deleteOauth(mActivity, platform, null);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if (mAuthListener != null) {
                mAuthListener.onError(platform.toString(), action, t);
            } else {
                ShareToast.showMessage(mActivity, "授权失败\n" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            if (mAuthListener != null) {
                mAuthListener.onCancel(platform.toString(), action);
            } else {
                ShareToast.showMessage(mActivity, "授权取消");
            }
        }
    };

    /**
     * 分享
     *
     * @param activity
     * @param shareEntity
     * @param shareListener
     */
    public void share(Activity activity, ShareEntity shareEntity, ShareListener shareListener) {
        if (shareEntity.platform.equals(ShareConfig.PLATFORM_WEIXIN) || shareEntity.platform.equals(ShareConfig.PLATFORM_WEIXIN_CIRCLE)) {
            if (!ShareConfig.isWxInstall(activity)) {//未安装微信客户端
                return;
            }
        } else if (shareEntity.platform.equals(ShareConfig.PLATFORM_QQ) || shareEntity.platform.equals(ShareConfig.PLATFORM_QZONE)) {
            if (!ShareConfig.isQQInstall(activity)) {//未安装QQ客户端
                return;
            }
        }
        mActivity = activity;
        SHARE_MEDIA share_media = getShareMedia(activity, shareEntity.platform);
        if (share_media == null) {
            return;
        }
        ShareAction shareAction = new ShareAction(activity);
        shareAction.withText(shareEntity.text);
        if (shareEntity.bitmap != null) {
            shareAction.withMedia(new UMImage(activity, shareEntity.bitmap));
        } else if (shareEntity.bytes != null) {
            shareAction.withMedia(new UMImage(activity, shareEntity.bytes));
        } else if (shareEntity.imageFile != null && shareEntity.imageFile.exists()) {
            shareAction.withMedia(new UMImage(activity, shareEntity.imageFile));
        } else if (!TextUtils.isEmpty(shareEntity.imageUrl)) {
            shareAction.withMedia(new UMImage(activity, shareEntity.imageUrl));
        } else {
            if (shareEntity.resId != 0) {
                shareAction.withMedia(new UMImage(activity, shareEntity.resId));
            } else {
                shareAction.withMedia(new UMImage(activity, R.drawable.ic_launcher));
            }
        }
        if (!TextUtils.isEmpty(shareEntity.url)) {
            UMWeb web = new UMWeb(shareEntity.url);
            web.setTitle(shareEntity.title);
            if (shareEntity.thumbBitmap != null) {
                web.setThumb(new UMImage(activity, shareEntity.thumbBitmap));
            } else if (shareEntity.thumbBytes != null) {
                web.setThumb(new UMImage(activity, shareEntity.thumbBytes));
            } else if (shareEntity.thumbFile != null && shareEntity.thumbFile.exists()) {
                web.setThumb(new UMImage(activity, shareEntity.thumbFile));
            } else if (!TextUtils.isEmpty(shareEntity.thumbUrl)) {
                web.setThumb(new UMImage(activity, shareEntity.thumbUrl));
            } else {
                if (shareEntity.thumbResId != 0) {
                    web.setThumb(new UMImage(activity, shareEntity.thumbResId));
                } else {
                    web.setThumb(new UMImage(activity, R.drawable.ic_launcher));
                }
            }
            web.setDescription(shareEntity.description);
            shareAction.withMedia(web);
        }
        shareAction.setPlatform(share_media);
        if (shareListener != null) {
            mShareListener = shareListener;
            shareAction.setCallback(umShareListener);
        }
        shareAction.share();
    }

    /**
     * 授权
     *
     * @param activity
     * @param platform
     * @param authListener
     */
    public void auth(Activity activity, String platform, AuthListener authListener) {
        if (platform.equals(ShareConfig.PLATFORM_WEIXIN)) {
            if (!ShareConfig.isWxInstall(activity)) {//未安装微信客户端
                return;
            }
        } else if (platform.equals(ShareConfig.PLATFORM_QQ) || platform.equals(ShareConfig.PLATFORM_QZONE)) {
            if (!ShareConfig.isQQInstall(activity)) {//未安装QQ客户端
                return;
            }
        }
        mActivity = activity;
        SHARE_MEDIA share_media = getShareMedia(activity, platform);
        if (share_media == null) {
            return;
        }
        mAuthListener = authListener;
        UMShareAPI.get(activity).getPlatformInfo(activity, share_media, umAuthListener);
    }

    /**
     * 弹出分享平台面板
     *
     * @param activity
     * @param shareEntity
     * @param shareListener
     */
    public void open(Activity activity, ShareEntity shareEntity, ShareListener shareListener) {
        open(activity, shareEntity, null, shareListener);
    }

    /**
     * 弹出只包含指定分享平台的面板
     *
     * @param activity
     * @param shareEntity
     * @param platforms
     * @param shareListener
     */
    public void open(Activity activity, ShareEntity shareEntity, List<String> platforms, ShareListener shareListener) {
        mActivity = activity;
        ShareBoardManager shareBoardManager = new ShareBoardManager(activity, shareEntity, this, shareListener);
        if (platforms != null && platforms.size() > 0) {
            shareBoardManager.setPlatforms(platforms);
        }
        shareBoardManager.showShareDialog();
    }

    /**
     * 删除指定平台授权信息
     *
     * @param activity
     * @param platform
     */
    public static void deleteOauth(Activity activity, String platform) {
        SHARE_MEDIA share_media = getShareMedia(activity, platform);
        if (share_media == null) {
            return;
        }
        UMShareAPI.get(activity).deleteOauth(activity, share_media, null);
    }

    /**
     * 转换分享平台
     *
     * @param activity
     * @param platform
     * @return
     */
    private static SHARE_MEDIA getShareMedia(Activity activity, String platform) {
        SHARE_MEDIA share_media = null;
        switch (platform.toLowerCase()) {
            case PLATFORM_WEIXIN:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case PLATFORM_WEIXIN_CIRCLE:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case PLATFORM_QQ:
                share_media = SHARE_MEDIA.QQ;
                break;
            case PLATFORM_QZONE:
                share_media = SHARE_MEDIA.QZONE;
                break;
            case PLATFORM_SINA:
                share_media = SHARE_MEDIA.SINA;
                break;
            default:
                ShareToast.showMessage(activity, "暂不支持该平台");
        }
        return share_media;
    }

    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }

    public static void release(Activity activity) {
        UMShareAPI.get(activity).release();
    }

    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWxInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        ShareToast.showMessage(context, "未安装微信客户端");
        return false;
    }

    /**
     * 检测是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        ShareToast.showMessage(context, "未安装QQ客户端");
        return false;
    }
}
