package com.run.running.activity.paymethod.wechat;

import android.os.Bundle;
import android.os.Environment;

import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.constants.WechatConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.view.annotation.ContentView;

/**
 * 微信添加到收藏界面
 * Created by zsr on 2016/6/13.
 */
@ContentView(R.layout.activity_addfavoritetowx)
public class AddFavoriteToWXActivity extends BaseActivity {

    private static final int THUMB_SIZE = 150;

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private IWXAPI api;

    private static final int MMAlertSelect1 = 0;
    private static final int MMAlertSelect2 = 1;
    private static final int MMAlertSelect3 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, WechatConstants.APP_ID);
    }
}
