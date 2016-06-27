package com.run.running.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.run.running.R;
import com.run.running.activity.img.BigImageActivity;
import com.run.running.activity.img.ImageFromHongyangActivity;
import com.run.running.activity.img.ImageFromPicassoActivity;
import com.run.running.activity.img.ImageFromUILActivity;
import com.run.running.activity.img.ImageFromVolleyActivity;
import com.run.running.activity.img.ImageFromXutilActivity;
import com.run.running.activity.img.WorldMapActivity;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * 图片操作
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_image)
public class ImageFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCallback.onImageFragmentRefresh();
    }

    @Event(value = {R.id.image_hy_kj, R.id.image_picasso, R.id.image_volley, R.id.image_uil, R.id.image_bigimage, R.id.image_worldmap, R.id.image_xutil})
    private void classes(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.image_hy_kj:
                intent.setClass(getActivity(), ImageFromHongyangActivity.class);
                break;
            case R.id.image_picasso:
                intent.setClass(getActivity(), ImageFromPicassoActivity.class);
                break;
            case R.id.image_volley:
                intent.setClass(getActivity(), ImageFromVolleyActivity.class);
                break;
            case R.id.image_uil:
                intent.setClass(getActivity(), ImageFromUILActivity.class);
                break;
            case R.id.image_bigimage:
                intent.setClass(getActivity(), BigImageActivity.class);
                break;
            case R.id.image_worldmap:
                intent.setClass(getActivity(), WorldMapActivity.class);
                break;
            case R.id.image_xutil:
                intent.setClass(getActivity(), ImageFromXutilActivity.class);
                break;
        }
        startActivity(intent);
    }
}
