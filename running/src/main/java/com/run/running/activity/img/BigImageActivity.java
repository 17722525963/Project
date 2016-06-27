package com.run.running.activity.img;

import android.os.Bundle;

import com.run.runlibrary.image.largeimg.LargeImageView;
import com.run.running.R;
import com.run.running.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zsr on 2016/5/28.
 */
@ContentView(R.layout.activity_image_bigimg)
public class BigImageActivity extends BaseActivity {

    @ViewInject(R.id.bigImage)
    private LargeImageView largeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            InputStream inputStream = getAssets().open("qm.jpg");
            largeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
