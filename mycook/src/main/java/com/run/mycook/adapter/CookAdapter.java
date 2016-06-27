package com.run.mycook.adapter;


import android.content.Context;

import com.run.mycook.R;
import com.run.mycook.entity.JHCook;
import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.recyclerview.CommonAdapter;

import java.util.List;


/**
 * Created by zsr on 2016/6/17.
 */
public class CookAdapter extends CommonAdapter<JHCook.Data> {

    public CookAdapter(Context context, int layoutId, List<JHCook.Data> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, JHCook.Data data) {
        holder.setText(R.id.card_title, data.getTitle());
        holder.setText(R.id.card_imtro, data.getImtro());
        holder.setImageFromNetwork(R.id.card_img, data.getAlbums().get(0));
        holder.setText(R.id.card_tag, data.getTags());
    }

}
