package com.run.running.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.run.running.activity.MainActivity;
import com.run.running.interfaces.OnFragmentRefreshListener;

/**
 * Created by zsr on 2016/5/19.
 */
public class BaseFragment extends com.run.runlibrary.BaseFragment {

    public OnFragmentRefreshListener mCallback;
    public MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.startRefresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //此方法用来确认当前的Activity容器是否已经继承了该接口，如果没有则抛出异常
        try {
            mCallback = (OnFragmentRefreshListener) activity;
        } catch (ClassCastException es) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
