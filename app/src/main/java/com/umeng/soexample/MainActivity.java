package com.umeng.soexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.share.AuthListener;
import com.umeng.share.ShareConfig;
import com.umeng.share.ShareEntity;
import com.umeng.share.ShareListener;
import com.umeng.share.ShareToast;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.loginByWX).setOnClickListener(this);
        findViewById(R.id.loginByQQ).setOnClickListener(this);
        findViewById(R.id.loginByWB).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                ShareEntity shareEntity = new ShareEntity();
                shareEntity.url = "http://www.offcn.com/";
                shareEntity.thumbUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503057617856&di=b725417493d65fa3240de4b5b61bdeef&imgtype=0&src=http%3A%2F%2Fimg9.3lian.com%2Fc1%2Fvector%2F10%2F01%2F155.jpg";
                shareEntity.imageUrl = "https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=ca5abb5b7bf0f736ccf344536b3cd87c/29381f30e924b899c83ff41c6d061d950a7bf697.jpg";
                shareEntity.text = "分享的文本";
                shareEntity.title = "链接标题";
                shareEntity.description = "链接描述内容";
                new ShareConfig().open(this, shareEntity, shareListener);
                break;
            case R.id.loginByWX:
                new ShareConfig().auth(this, ShareConfig.PLATFORM_WEIXIN, authListener);
                break;
            case R.id.loginByQQ:
                new ShareConfig().auth(this, ShareConfig.PLATFORM_QQ, authListener);
                break;
            case R.id.loginByWB:
                new ShareConfig().auth(this, ShareConfig.PLATFORM_SINA, authListener);
                break;
        }
    }

    private ShareListener shareListener = new ShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(String platform) {
            ShareToast.showMessage(mActivity, "分享中");
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(String platform) {
            ShareToast.showMessage(mActivity, "分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(String platform, Throwable t) {
            ShareToast.showMessage(mActivity, "分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(String platform) {
            ShareToast.showMessage(mActivity, "分享取消");
        }
    };

    private AuthListener authListener = new AuthListener() {
        @Override
        public void onStart(String platform) {
            ShareToast.showMessage(mActivity, "开始授权");
        }

        @Override
        public void onComplete(String platform, int action, Map<String, String> data) {
            String temp = "";
            for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }
            String uid = data.get("uid");
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            ShareToast.showMessage(mActivity, "授权成功" + "\n用户id：" + uid + "\n昵称：" + name + "\n性别：" + gender + "\n头像：" + iconurl);
            ShareConfig.deleteOauth(mActivity, platform);
        }

        @Override
        public void onError(String platform, int action, Throwable t) {
            ShareToast.showMessage(mActivity, "授权失败\n" + t.getMessage());
        }

        @Override
        public void onCancel(String platform, int action) {
            ShareToast.showMessage(mActivity, "授权取消");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareConfig.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareConfig.release(this);
    }
}
