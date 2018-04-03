package com.umeng.share;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 自定义toast
 */
public class ShareToast {
    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Toast toast = null;

    private static Object synObj = new Object();

    public static void showMessage(final Context context, final String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            if (toast != null) {
                                toast.cancel();
                                toast = null;
                            }
                            toast = makeText(context.getApplicationContext(), text);
                            if (toast != null) {
                                toast.show();
                            }
                        }
                    }
                });
            }
        }).start();
    }


    private static Toast makeText(Context context, String text) {
        if (context != null) {
            Toast result = new Toast(context);
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflate.inflate(R.layout.share_toast_layout, null);
            TextView tv_msg = (TextView) view.findViewById(R.id.text_mail_toast);
            TextView tv_tag = (TextView) view.findViewById(R.id.text_tag);
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            if (text != null && text.contains("\n")) {
                tv_tag.setVisibility(View.VISIBLE);
                int start = text.indexOf("\n");
                tv_msg.setText(text.substring(0, start));
                tv_tag.setText(text.substring(start + "\n".length()));
            } else {
                tv_tag.setVisibility(View.GONE);
                tv_msg.setText(text);
            }

            result.setView(view);
            result.setDuration(Toast.LENGTH_SHORT);
            return result;
        }
        return null;
    }

}
