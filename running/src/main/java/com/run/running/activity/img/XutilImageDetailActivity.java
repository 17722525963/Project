package com.run.running.activity.img;

import android.os.Bundle;
import android.widget.ImageView;

import com.run.running.R;
import com.run.running.activity.BaseActivity;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by zsr on 2016/5/31.
 */
@ContentView(R.layout.activity_xutilimagedetail)
public class XutilImageDetailActivity extends BaseActivity {

    @ViewInject(R.id.xutil_image__img)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageOptions imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                //.setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER).build();

        x.image().bind(imageView, getIntent().getStringExtra("url"), imageOptions);
    }
}
