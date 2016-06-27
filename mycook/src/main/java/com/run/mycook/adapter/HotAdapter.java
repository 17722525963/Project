package com.run.mycook.adapter;

import android.content.Context;

import com.run.mycook.R;
import com.run.mycook.entity.Hot;
import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.abslistview.CommonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 */
public class HotAdapter extends CommonAdapter<Hot> {

    public HotAdapter(Context context, int layoutId, List<Hot> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Hot hot) {
        holder.setText(R.id.hot_title, hot.getTitle());
    }
}
