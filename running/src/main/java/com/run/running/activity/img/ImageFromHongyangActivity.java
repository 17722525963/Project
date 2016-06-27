package com.run.running.activity.img;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.run.runlibrary.image.hongyang.ImageLoader;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.constants.Images;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Hongyang大神图片加载框架练习
 * Created by zsr on 2016/5/25.
 */
@ContentView(R.layout.activity_imagefromhongyang)
public class ImageFromHongyangActivity extends BaseActivity {

    @ViewInject(R.id.hongyang_gridview)
    private GridView mGridView;

    private String[] mUrlStrs = Images.imageThumbUrls;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        setAdapter();

        goToDetail();
    }

    private void goToDetail() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = (String) view.getTag();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(view//
                        , (int) view.getWidth() / 2, (int) view.getHeight() / 2,//拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用0,0表示从无到全屏
                Intent intent = new Intent(ImageFromHongyangActivity.this, ImageFromHongyangDetailActivity.class);
                intent.putExtra("imageurl", url);
                ActivityCompat.startActivity(ImageFromHongyangActivity.this, intent, options.toBundle());
            }
        });
    }


    private void setAdapter() {
        if (mGridView == null) {
            return;
        }
        if (mUrlStrs != null) {
            mGridView.setAdapter(new MyAdapter(this, 0, mUrlStrs));
        } else {
            mGridView.setAdapter(null);
        }
    }


    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, String[] datas) {
            super(context, 0, datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_imagefromhongyang, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.hongyang_img);
            imageView.setImageResource(R.drawable.pictures_no);
            mImageLoader.loadImage(getItem(position), imageView, true);
            return convertView;
        }
    }
}
