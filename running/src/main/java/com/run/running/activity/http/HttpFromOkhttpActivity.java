package com.run.running.activity.http;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by zsr on 2016/6/6.
 */
@ContentView(R.layout.activity_httpfromokhttp)
public class HttpFromOkhttpActivity extends BaseActivity {

    @ViewInject(R.id.okhttp_get_detail)
    private TextView getDetail;

    @ViewInject(R.id.okhttp_upload_detail)
    private TextView uploaddetail;

    @ViewInject(R.id.okhttp_download_detail)
    private TextView downloadDetail;

    @ViewInject(R.id.okhttp_download_progress)
    private ProgressBar progressBar;

    @ViewInject(R.id.okhttp_image_detail)
    private TextView imageDetail;

    @ViewInject(R.id.okhttp_imageview)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cookie(包含Session)
        //PersistentCookieStore //持久化cookie
        // SerializableHttpCookie //持久化cookie
        //MemoryCookieStore //cookie信息存在内存中
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));

        //Log
        //初始化OkhttpClient时，通过设置拦截器实现，框架中提供了一个LoggerInterceptor，当然你可以自行实现一个Interceptor 。

        //Https
        //设置可访问的Https网站
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

        //https
        //设置具体的证书
        //HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(证书的inputstream, null, null);

        //Https
        //双向认证
        //HttpsUtils.getSslSocketFactory(证书的inputstream,本地证书的inputstream,本地证书的密码)

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .addInterceptor(new LoggerInterceptor("TAG"))//Log
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        progressBar.setMax(100);
    }

    @Event(value = {R.id.okhttp_get, R.id.okhttp_upload, R.id.okhttp_download, R.id.okhttp_image})
    private void okHttp(View view) {
        switch (view.getId()) {
            case R.id.okhttp_get:
                get();
                break;
            case R.id.okhttp_upload:
                upload();
//                postUpload();
                break;
            case R.id.okhttp_download:
                download();
                break;
            case R.id.okhttp_image:
                image();
                break;

        }
    }



    /**
     * 加载Image
     */
    private void image() {
        String url = "http://www.wed114.cn/jiehun/uploads/allimg/160426/39_160426110624_1.jpg";
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        imageDetail.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        imageView.setImageBitmap(response);
                    }
                });

    }

    /**
     * 下载
     */
    private void download() {
        String url = "http://dl.bintray.com/wyouflf/maven/org/xutils/xutils/3.3.34/xutils-3.3.34.aar";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "xutils-3.3.34.aar") {//参数：保存的文件夹路径，保存的文件名
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        downloadDetail.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        downloadDetail.setText("下载成功~" + response.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        progressBar.setProgress((int) (100 * progress));
                    }
                });
    }

    /**
     * post表单上传文件
     */
    private void postUpload() {
        File file = new File("");
        String url = "";

        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        OkHttpUtils
                .post()
                .addFile("mFile", "messenger_01.png", file)
                .addFile("mFile", "test1.txt", file)
                .url(url)
                .params(params)
                .headers(headers)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

    }

    /**
     * 文件上传
     */
    private void upload() {
        File file = new File(Environment.getExternalStorageDirectory(), "xxx.png");
        if (!file.exists()) {
            ToastUtil.showToast(HttpFromOkhttpActivity.this, "文件不存在，请修改文件路径");
            return;
        }
        OkHttpUtils
                .postFile()
                .url("上传服务器地址地址")
                .file(file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }


    /**
     * get请求
     */
    private void get() {
        String url = "http://japi.juhe.cn/funny/type.from";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("key", "1797aaeee81523a81b0546d9d736a7e9")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getDetail.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        getDetail.setText(response);
                    }
                });
    }


}
