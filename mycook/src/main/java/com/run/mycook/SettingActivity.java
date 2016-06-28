package com.run.mycook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.run.mycook.entity.History;
import com.run.mycook.utils.DataCleanManager;
import com.run.mycook.utils.DbXutil;
import com.run.runlibrary.BaseActivity;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.setting_cacheSize)
    private TextView cacheSize;

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoConfig = DbXutil.getDaoConfig();
        db = x.getDb(daoConfig);

        getSize();
    }

    private void getSize() {
        try {
            cacheSize.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Event(value = {R.id.setting_clean_cache, R.id.setting_update})
    private void setting(View view) {
        switch (view.getId()) {
            case R.id.setting_clean_cache:
                new AlertDialog.Builder(this).setTitle("确定要清除缓存？").setMessage("").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DataCleanManager.clearAllCache(SettingActivity.this);
                            cacheSize.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
                            db.delete(History.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case R.id.setting_update:

                break;
        }
    }

}
