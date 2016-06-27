package com.run.running.activity.img;

import android.os.Bundle;
import android.widget.ImageView;

import com.run.runlibrary.image.hongyang.ImageLoader;
import com.run.running.R;
import com.run.running.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zsr on 2016/5/25.
 */
@ContentView(R.layout.activity_imagefromhongyangdetail)
public class ImageFromHongyangDetailActivity extends BaseActivity {

    @ViewInject(R.id.hongyang_img_detail)
    private ImageView image;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageLoader = ImageLoader.getInstance(1, ImageLoader.Type.LIFO);

        final String path = getIntent().getStringExtra("imageurl");

        mImageLoader.loadImage(path, image, true);
        //此处获取到的图片是缓存中的压缩过的图片,需要进行处理

    }


}
