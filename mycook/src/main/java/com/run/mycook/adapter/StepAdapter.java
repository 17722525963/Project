package com.run.mycook.adapter;

import android.content.Context;

import com.run.mycook.R;
import com.run.mycook.entity.JHCook;
import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by zsr on 2016/6/18.
 */
public class StepAdapter extends CommonAdapter<JHCook.Steps> {

    public StepAdapter(Context context, int layoutId, List<JHCook.Steps> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, JHCook.Steps steps) {
        holder.setText(R.id.step_step, steps.getStep());
        holder.setImageFromNetwork(R.id.step_img, steps.getImg());
    }
}
