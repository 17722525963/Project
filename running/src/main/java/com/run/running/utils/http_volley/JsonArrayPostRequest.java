package com.run.running.utils.http_volley;

import android.util.LruCache;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by zsr on 2016/6/6.
 */
public class JsonArrayPostRequest extends Request<JSONArray> {

    private Map<String, String> mMap;
    private Response.Listener<JSONArray> mListener;
    private LruCache lruCache;

    public JsonArrayPostRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener, Map<String, String> map) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mMap = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONArray jsonArray) {
        mListener.onResponse(jsonArray);
    }
}
