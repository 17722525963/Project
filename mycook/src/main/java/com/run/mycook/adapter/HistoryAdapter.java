package com.run.mycook.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.run.mycook.R;
import com.run.mycook.entity.History;
import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.recyclerview.CommonAdapter;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import java.util.List;

/**
 * Created by zsr on 2016/6/21.
 */
public class HistoryAdapter extends CommonAdapter<History> {

    public HistoryAdapter(Context context, int layoutId, List<History> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, History history) {
        ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(133), DensityUtil.dip2px(100))//图片大小
                .setRadius(DensityUtil.dip2px(10))//imageview圆角半径
                .setUseMemCache(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();
        holder.setText(R.id.history_title, history.getTitle());
        holder.setImageFromNetwork(R.id.history_img, history.getImg(), imageOptions);
    }
}
