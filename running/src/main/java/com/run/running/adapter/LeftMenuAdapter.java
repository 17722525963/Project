package com.run.running.adapter;

import android.content.Context;

import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.recyclerview.CommonAdapter;
import com.run.running.R;
import com.run.running.entity.LeftMenu;

import java.util.List;

/**
 * Created by zsr on 2016/5/18.
 */
public class LeftMenuAdapter extends CommonAdapter<LeftMenu> {

    public LeftMenuAdapter(Context context, int layoutId, List<LeftMenu> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, LeftMenu leftMenu) {
        holder.setImageResource(R.id.item_left_menu_img, leftMenu.getImg());
        holder.setText(R.id.item_left_menu_tv, leftMenu.getTitle());
    }
}
