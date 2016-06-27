package com.run.running.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.run.running.R;
import com.run.running.activity.http.HttpFromOkhttpActivity;
import com.run.running.activity.http.HttpFromVolleyActivity;
import com.run.running.activity.http.HttpFromXutilActivity;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_http)
public class HttpFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(value = {R.id.http_xutil, R.id.http_okHttp, R.id.http_Volley})
    private void classes(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.http_xutil:
                intent.setClass(getActivity(), HttpFromXutilActivity.class);
                break;
            case R.id.http_Volley:
                intent.setClass(getActivity(), HttpFromVolleyActivity.class);
                break;
            case R.id.http_okHttp:
                intent.setClass(getActivity(), HttpFromOkhttpActivity.class);
                break;
        }
        startActivity(intent);
    }
}
