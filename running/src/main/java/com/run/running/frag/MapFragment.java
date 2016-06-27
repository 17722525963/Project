package com.run.running.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.run.running.R;
import com.run.running.app.BaseFragment;

import org.xutils.view.annotation.ContentView;

/**
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_map)
public class MapFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCallback.onMapFragmentRefresh();
    }
}
