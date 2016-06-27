package com.run.running.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsr on 2016/6/13.
 */
public class WechatUtil {

    private static final String TAG = "WechatUtil";
    
    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

//    public static byte[] httpGet(final String url) {
//        if (url == null || url.length() == 0) {
//            Log.e(TAG, "httpGet, url is null");
//            return null;
//        }
//
//        HttpClient httpClient = getNewHttpClient();
//        HttpGet httpGet = new HttpGet(url);
//
//        try {
//            HttpResponse resp = httpClient.execute(httpGet);
//            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
//                return null;
//            }
//
//            return EntityUtils.toByteArray(resp.getEntity());
//
//        } catch (Exception e) {
//            Log.e(TAG, "httpGet exception, e = " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] httpPost(String url, String entity) {
//        if (url == null || url.length() == 0) {
//            Log.e(TAG, "httpPost, url is null");
//            return null;
//        }
//
//        HttpClient httpClient = getNewHttpClient();
//
//        HttpPost httpPost = new HttpPost(url);
//
//        try {
//            httpPost.setEntity(new StringEntity(entity));
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            HttpResponse resp = httpClient.execute(httpPost);
//            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//                Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
//                return null;
//            }
//
//            return EntityUtils.toByteArray(resp.getEntity());
//        } catch (Exception e) {
//            Log.e(TAG, "httpPost exception, e = " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
}
