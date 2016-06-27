package com.run.running.utils.http_xutil;

import org.xutils.common.Callback;

/**
 * Created by zsr on 2016/6/2.
 */
public class MyProgressCallBack<ResultType> implements Callback.ProgressCallback<ResultType>{
    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }

    @Override
    public void onSuccess(ResultType result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
