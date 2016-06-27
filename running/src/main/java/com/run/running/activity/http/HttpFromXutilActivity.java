package com.run.running.activity.http;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.entity.http.Weather;
import com.run.running.utils.http_xutil.MyCallBack;
import com.run.running.utils.http_xutil.XUtil;
import com.run.running.utils.http_xutil.xutils.DownloadManager;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.ex.HttpException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsr on 2016/5/31.
 */
@ContentView(R.layout.activity_httpfromxutil)
public class HttpFromXutilActivity extends BaseActivity {

    @ViewInject(R.id.show)
    private TextView show;

    @ViewInject(R.id.http_xutil_url)
    private TextView et_url;

    @Event(value = {R.id.http_xutil_get, R.id.http_xutil_download, R.id.http_xutil_upload, R.id.http_xutil_xutil, R.id.http_xutil_cahce})
    private void Click(View view) throws DbException {
        switch (view.getId()) {
            case R.id.http_xutil_get:
                get();
                break;
            case R.id.http_xutil_download://下载
                String url = "http://dl.bintray.com/wyouflf/maven/org/xutils/xutils/3.3.34/xutils-3.3.34.aar";
//                findViewById(R.id.show_download_item).setVisibility(View.VISIBLE);
                download(url);
                break;
            case R.id.http_xutil_upload:
                upload();
                break;
            case R.id.http_xutil_xutil:
                xutil();
                break;
            case R.id.http_xutil_cahce:
                cache();
                break;
        }
    }

    /**
     * 带缓存的请求示例
     */
    private void cache() {
//        BaiduParams params = new BaiduParams();
//        params.wd = "running";
//        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
//        params.setCacheMaxAge(1000 * 60);
//        Callback.Cancelable cancelable = x.http().get(params, new Callback.CacheCallback<String>() {
//
//            private boolean hasError = false;
//            private String result = null;
//
//            @Override
//            public boolean onCache(String result) {
//                //得到缓存数据，缓存过期后不会进入这个方法
//                //如果服务端没有返回过期时间，参考params.setCacheMaxAge()这个方法
//                //客户端会根据返回的header 中max-age或expires 来确定本地缓存是否给onCache()方法
//                //如果服务端没有返回max-age或expires ，那么缓存将一直保存，除非这里定义了返回false的逻辑，那么xutil将请求新数据，来覆盖他
//                //如果返回true ，将不再请求网络
//                //如果返回fasle 继续请求网络，但会在请求头中加上ETag，Last-Modidied等信息
////              //如果服务端返回304，则表示数据没有更新，不继续加载数据
//                this.result = result;
//                return false;//true ：信任数据，不再发起网络请求;false 不信任缓存数据
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                //如果服务端返回304 或onCache选择了信任缓存，这时result为null
//                if (result != null) {
//                    this.result = result;
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                hasError = true;
//                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
//                if (ex instanceof HttpException) {//网络错误
//                    HttpException httpException = (HttpException) ex;
//                    int responseCode = httpException.getCode();
//                    String responseMsg = httpException.getMessage();
//                    String errorResult = httpException.getResult();
//                    //.....
//                } else {
//                    //其他类型的错误
//                }
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onFinished() {
//                if (!hasError && result != null) {
//                    // 成功获取数据
//                    Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        String url = "http://v.juhe.cn/weather/index";
        Map<String, String> map = new HashMap<>();
        XUtil.get(url, map, new Callback.CacheCallback<String>() {
            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) {
                //得到缓存数据，缓存过期后不会进入这个方法
                //如果服务端没有返回过期时间，参考params.setCacheMaxAge()这个方法
                //客户端会根据返回的header 中max-age或expires 来确定本地缓存是否给onCache()方法
                //如果服务端没有返回max-age或expires ，那么缓存将一直保存，除非这里定义了返回false的逻辑，那么xutil将请求新数据，来覆盖他
                //如果返回true ，将不再请求网络
                //如果返回fasle 继续请求网络，但会在请求头中加上ETag，Last-Modidied等信息
//              //如果服务端返回304，则表示数据没有更新，不继续加载数据
                this.result = result;
                return false;//true ：信任数据，不再发起网络请求;false 不信任缓存数据
            }

            @Override
            public void onSuccess(String result) {
                //如果服务端返回304 或onCache选择了信任缓存，这时result为null
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                ToastUtil.showToast(HttpFromXutilActivity.this, ex.getMessage());
                if (ex instanceof HttpException) {//网络错误
                    HttpException httpException = (HttpException) ex;
                    int responseCode = httpException.getCode();
                    String responseMsg = httpException.getMessage();
                    String errorResult = httpException.getResult();
                    //.....
                } else {
                    //其他类型的错误
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtil.showToast(HttpFromXutilActivity.this, "cancelled");
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    ToastUtil.showToast(HttpFromXutilActivity.this, result);
                }
            }
        });
    }

    /**
     * 模仿官方下载、暂停等
     */
    private void xutil() throws DbException {
        //添加到下载列表
        String url = "http://dl.bintray.com/wyouflf/maven/org/xutils/xutils/3.3.34/xutils-3.3.34.aar";
        String label = "Running_" + System.nanoTime();
        DownloadManager.getInstance().startDownload(
                url, label,
                "/sdcard/Running/" + label + ".aar", true, false, null);
        //打开下载列表页
        this.startActivity(new Intent(this, HttpFromXutilDownloadActivity.class));
    }

    /**
     * 下载文件
     *
     * @param url
     */
    private void download(final String url) {


        String label = "running" + System.nanoTime();
        String savePath = "/sdcard/Running/" + label;

//        XUtil.downLoadFile(url, savePath, new MyCallBack<File>() {
//            @Override
//            public void onSuccess(File result) {
//                super.onSuccess(result);
//                et_url.setText("下载成功~");
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//                super.onError(ex, isOnCallback);
//                et_url.setText("下载失败：" + ex.getMessage());
//            }
//
//        });

        XUtil.downLoadFile(url, savePath, new Callback.ProgressCallback<File>() {

            //创建一个带进度条的提示对话框
            ProgressDialog progressDialog = new ProgressDialog(HttpFromXutilActivity.this);


            @Override
            public void onWaiting() {
                progressDialog.setTitle(url);
                progressDialog.setMessage("正在处理数据···");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置进度条样式
                progressDialog.setCancelable(false);
//                progressDialog.setButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        onCancelled(new CancelledException("下载被取消~"));
////                        onFinished();
//                    }
//                });

                //遗留问题：添加按钮后，可以推出Dialog，下载在后台继续进行，但是重新点击下载时会重新启动一个下载项~~~
                //主要问题：怎样取消一个正在下载的项（让它退出下载？）
            }

            @Override
            public void onStarted() {
                progressDialog.setMessage("开始下载~");
                progressDialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                progressDialog.dismiss();
                ToastUtil.showToast(HttpFromXutilActivity.this, "下载完成·");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                et_url.setText(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                ToastUtil.showToast(HttpFromXutilActivity.this, cex.getMessage());
            }

            @Override
            public void onFinished() {
            }
        });

    }
// 1 2 4 7
// 1 2 6 7 4

    /**
     * 上传文件，支持多文件上传
     */
    private void upload() {
        String url = "";//图片上传地址
        Map<String, Object> map = new HashMap<String, Object>();
        //传参
        map.put("", "");
        XUtil.upLoadFile(url, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }


    private void get() {
        String url = "http://v.juhe.cn/weather/index";
        Map<String, String> map = new HashMap<>();
        map.put("cityname", "深圳");
        map.put("key", "c7bd2889ea70ba023faf0f324ef5dc6b");
        XUtil.get(url, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Gson gson = new Gson();
                Weather str = gson.fromJson(result, Weather.class);

                Weather.Today today = str.getResult().getToday();

                String city = today.getCity();
                String temperature = today.getTemperature();
                String weather = today.getWeather();
                String wind = today.getWind();
                String week = today.getWeek();
                String date_y = today.getDate_y();
                String dressing_advice = today.getDressing_advice();

                String message = "城市：" + city + "\n" + "温度：" + temperature + "\n" + "天气：" + weather + "\n" + "风向：" + wind + "\n" + "穿衣指数：" + dressing_advice + "\n" + "时间：" + week + "\n" + "日期：" + date_y;
                show.setText(message);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }
        });
    }

}
