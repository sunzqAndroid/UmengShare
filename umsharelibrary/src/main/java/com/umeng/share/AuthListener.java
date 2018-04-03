package com.umeng.share;

import java.util.Map;

/**
 * 第三方授权回调
 */

public interface AuthListener {

    void onStart(String platform);

    void onComplete(String platform, int action, Map<String, String> data);

    void onError(String platform, int action, Throwable t);

    void onCancel(String platform, int action);
}
