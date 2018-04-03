package com.umeng.share;

/**
 * 分享回调
 */

public interface ShareListener {

    void onStart(String platform);

    void onResult(String platform);

    void onError(String platform, Throwable t);

    void onCancel(String platform);
}
