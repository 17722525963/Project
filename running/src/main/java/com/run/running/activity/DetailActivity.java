package com.run.running.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.run.runlibrary.BaseActivity;
import com.run.running.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by zsr on 2016/5/18.
 */

@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseActivity {

    @ViewInject(R.id.detail_coordinatorLayout)
    private CoordinatorLayout coordinatorLayout;

    private PackageManager packageManager;
    private PackageInfo packageInfo;
    private Signature[] signatures;
    private StringBuilder builder;
    private String signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        packageManager = getPackageManager();
        builder = new StringBuilder();
    }

    @Event(R.id.detail_fab)
    private void showSnackBar(View view) {
        String pkgname = "com.run.running";
        boolean isEmpty = TextUtils.isEmpty(pkgname);
        if (isEmpty) {
            Toast.makeText(this, "应用程序的包名不能为空！", Toast.LENGTH_SHORT);
        } else {
            try {
                /** 通过包管理器获得指定包名包含签名的包信息 **/
                packageInfo = packageManager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);
                /******* 通过返回的包信息获得签名数组 *******/
                signatures = packageInfo.signatures;
                /******* 循环遍历签名数组拼接应用签名 *******/
                for (Signature signature : signatures) {
                    builder.append(signature.toCharsString());
                }
                /************** 得到应用签名 **************/
                signature = builder.toString();
                System.out.println("============================" + signature);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
