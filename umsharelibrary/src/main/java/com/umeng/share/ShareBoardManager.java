package com.umeng.share;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


/**
 * 分享管理
 */
public class ShareBoardManager {
    private Activity mActivity;
    private List<String> platforms = new ArrayList<>();
    private ShareEntity mShareEntity;
    private ShareConfig mShareConfig;
    private ShareListener mShareListener;

    public ShareBoardManager(Activity activity, ShareEntity shareEntity, ShareConfig shareConfig, ShareListener shareListener) {
        platforms.add(ShareConfig.PLATFORM_WEIXIN);
        platforms.add(ShareConfig.PLATFORM_WEIXIN_CIRCLE);
        platforms.add(ShareConfig.PLATFORM_QQ);
        platforms.add(ShareConfig.PLATFORM_QZONE);
        platforms.add(ShareConfig.PLATFORM_SINA);
        this.mActivity = activity;
        this.mShareEntity = shareEntity;
        this.mShareConfig = shareConfig;
        this.mShareListener = shareListener;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms.clear();
        this.platforms = platforms;
    }

    /**
     * 显示分享平台
     */
    public void showShareDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.share_ad_dialog_WindowStyle);
        View view = configDialogWithoutCancelCallBack(dialog);
    }

    private View configDialogWithoutCancelCallBack(final Dialog dialog) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.dialog_more_share, null);
        GridView gridview_platform = (GridView) view.findViewById(R.id.gridview_platform);
        gridview_platform.setNumColumns(5);

        final SharePlatformsAdapter adapter = new SharePlatformsAdapter(mActivity, platforms);

        gridview_platform.setAdapter(adapter);
        gridview_platform.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mShareEntity.platform = (String) adapter.getItem(position);
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                mShareConfig.share(mActivity, mShareEntity, mShareListener);
            }
        });

        View tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
            }
        });

        dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.share_ad_dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return view;
    }
}
