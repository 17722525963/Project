package com.run.runlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.run.runlibrary.netstate.NetWorkUtil;
import com.run.runlibrary.netstate.NetworkStateReceiver;
import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.x;


/**
 * Created by zsr on 2016/4/26.
 */
public abstract class BaseActivity extends AutoLayoutActivity {

    InputMethodManager inputMethodManager;//软键盘
    protected Resources res;
    protected BaseApplication baseApplication;
    protected static final String TAG = BaseActivity.class.getName();
    private boolean translucentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getLayout() != 0) {
//            setContentView(getLayout());//设置布局文件
//        }
        x.view().inject(this);

        AppManager.getAppManager().addActivity(this);

        res = this.getApplicationContext().getResources();

        baseApplication = (BaseApplication) this.getApplication();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        findById();
//
//        setListener();
//
//        logic();

        NetworkStateReceiver.registerNetworkStateReceiver(this);//注册网络监听

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {//获取焦点
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkStateReceiver.unRegisterNetworkStateReceiver(this);
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 获取布局文件
     *
     * @return
     */
//    protected abstract int getLayout();
//
//    /**
//     * findViewById方法
//     */
//    protected abstract void findById();
//
//    /**
//     * 设置监听器
//     */
//    protected abstract void setListener();
//
//    protected abstract void logic();
    public void onConnect(NetWorkUtil.netType type) {
    }


    public void onDisConnect() {
    }

}
