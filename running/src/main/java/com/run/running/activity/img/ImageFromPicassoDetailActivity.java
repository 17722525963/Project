package com.run.running.activity.img;

import android.os.Bundle;
import android.widget.ImageView;

import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zsr on 2016/5/26.
 */
@ContentView(R.layout.activity_imagefrompicassodetail)
public class ImageFromPicassoDetailActivity extends BaseActivity {

    @ViewInject(R.id.picasso_img_detail)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String path = getIntent().getStringExtra("imageurl");

        Picasso.with(ImageFromPicassoDetailActivity.this)
                .load(path)
                .placeholder(R.mipmap.ic_launcher)//设置未加载完成显示图片
                .error(R.drawable.pictures_no)//设置加载失败显示图片
                .resize(600, 800)//转换图片，设置大小为300*600
                .centerCrop()
                .into(imageView);

        //问题：本来是小图片，结果硬性被放大，图片失真···

    }


}
