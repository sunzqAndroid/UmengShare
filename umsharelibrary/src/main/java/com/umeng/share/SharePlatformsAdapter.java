package com.umeng.share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 分享平台展示适配器
 */
public class SharePlatformsAdapter extends BaseAdapter {

    private Context mContext;
    List<String> platforms;
    private int colums = 3;

    public SharePlatformsAdapter(Context mContext, List<String> platforms) {
        this.mContext = mContext;
        this.platforms = platforms;
    }

    public void setColums(int colums) {
        this.colums = colums;
    }

    @Override
    public int getCount() {
        return platforms != null ? platforms.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (platforms.size() > position) {
            return platforms.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final String platform = platforms.get(position).toLowerCase();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            View view = LayoutInflater.from(mContext).inflate(R.layout.share_item_platform, parent, false);
            viewHolder.view_share_platform_icon = view.findViewById(R.id.view_share_platform_icon);
            viewHolder.tv_share_platform_title = (TextView) view.findViewById(R.id.tv_share_platform_title);
            view.setTag(viewHolder);
            convertView = view;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int resId = 0;
        resId = AppR.getBitmapRes(mContext, "umeng_socialize_" + platform);
        if (resId > 0) {
            viewHolder.view_share_platform_icon.setBackgroundResource(resId);
        }
        resId = AppR.getStringRes(mContext, "umeng_socialize_" + platform);
        if (resId > 0) {
            viewHolder.tv_share_platform_title.setText(resId);
        }
        return convertView;
    }

    class ViewHolder {
        View view_share_platform_icon;
        TextView tv_share_platform_title;
    }
}
