package com.run.running.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.run.running.R;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;

/**
 * MVP架构学习
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_mvp)
public class MVPFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCallback.onMVPFragmentRefresh();
    }
}
