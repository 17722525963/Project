package com.run.running.activity.http;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.run.runlibrary.utils.ToastUtil;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.entity.http.Book;
import com.run.running.utils.http_volley.GsonRequest;
import com.run.running.utils.http_volley.JsonObjectPostRequest;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zsr on 2016/6/3.
 */
@ContentView(R.layout.activity_httpfromvolley)
public class HttpFromVolleyActivity extends BaseActivity {

    @ViewInject(R.id.http_volley_get_detail)
    private TextView getDetail;

//    @ViewInject(R.id.http_volley_xml_detail)
//    private TextView xmlDetail;

    @ViewInject(R.id.http_volley_book_detail)
    private TextView bookdetail;

    @ViewInject(R.id.http_volley_book_detail2)
    private TextView bookdetail2;

    @ViewInject(R.id.http_volley_gson_detail)
    private TextView gson;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //首先需要构建一个RequestQueue
        queue = Volley.newRequestQueue(this);//
    }

    @Event(value = {R.id.http_volley_get, R.id.http_volley_book, R.id.http_volley_book2, R.id.http_volley_gson})
    private void volley(View view) {
        switch (view.getId()) {
            case R.id.http_volley_get:
                basic();
                break;
            case R.id.http_volley_book:
                book();
                break;
            case R.id.http_volley_book2:
                book2();
                break;
            case R.id.http_volley_gson:
                gsonTest();
                break;
        }
    }

    private void gsonTest() {
        String url = "http://japi.juhe.cn/book/recommend.from?key=857939540b45cc5ae934c85917a9d8c5";

        GsonRequest<Book> bookGsonRequest = new GsonRequest<Book>(Request.Method.GET, url, Book.class, new Response.Listener<Book>() {
            @Override
            public void onResponse(Book book) {
                gson.setText(book.getResult().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                gson.setText(volleyError.getMessage());
            }
        });
        queue.add(bookGsonRequest);
    }

    /**
     * 手动添加参数
     */
    private void book2() {
        String url = "http://japi.juhe.cn/book/recommend.from";

        Map<String, String> map = new HashMap<String, String>();
        map.put("key", "857939540b45cc5ae934c85917a9d8c5");


//        JsonArrayPostRequest jsonArrayPostRequest = new JsonArrayPostRequest(url, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//                bookdetail2.setText(jsonArray.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                bookdetail2.setText(volleyError.getMessage());
//            }
//        }, map);
//
//        queue.add(jsonArrayPostRequest);

        JsonObjectPostRequest jsonObjectPostRequest = new JsonObjectPostRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                bookdetail2.setText(jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                bookdetail2.setText(volleyError.getMessage());
            }
        }, map);

        queue.add(jsonObjectPostRequest);
    }

    private void book() {
        String url = "http://japi.juhe.cn/book/recommend.from?key=857939540b45cc5ae934c85917a9d8c5";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                ToastUtil.showToast(HttpFromVolleyActivity.this, jsonObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                bookdetail.setText(volleyError.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

    /**
     * 自定义XmlRequest请求xml格式的数据
     */
//    private void xml() {
//        XMLRequest xmlRequest = new XMLRequest(
//                "http://flash.weather.com.cn/wmaps/xml/china.xml",
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser response) {
//                        try {
//                            int eventType = response.getEventType();
//                            while (eventType != XmlPullParser.END_DOCUMENT) {
//                                switch (eventType) {
//                                    case XmlPullParser.START_TAG:
//                                        String nodeName = response.getName();
//                                        if ("city".equals(nodeName)) {
//                                            String pName = response.getAttributeValue(0);
//                                            xmlDetail.append(pName);
//                                        }
//                                        break;
//                                }
//                                eventType = response.next();
//                            }
//                        } catch (XmlPullParserException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                xmlDetail.setText("Error Message:" + error.getMessage());
//                Log.e(">>????????", error.getMessage());
//            }
//        });
//        queue.add(xmlRequest);
//    }
    private void basic() {
        String url = "http://www.baidu.com";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ToastUtil.showToast(HttpFromVolleyActivity.this, s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getDetail.setText("Error message:\n" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        queue.add(stringRequest);
    }

}
