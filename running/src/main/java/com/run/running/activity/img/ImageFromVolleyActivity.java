package com.run.running.activity.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.run.runlibrary.adapter.ViewHolder;
import com.run.runlibrary.adapter.abslistview.CommonAdapter;
import com.run.running.R;
import com.run.running.activity.BaseActivity;
import com.run.running.constants.Images;
import com.run.running.entity.ImageVolley;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by zsr on 2016/5/26.
 */
@ContentView(R.layout.activity_imagefromvolley)
public class ImageFromVolleyActivity extends BaseActivity {

    @ViewInject(R.id.volley_list)
    private ListView mListView;


    private String[] URLS = Images.imageThumbUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView.setAdapter(new VolleyImageViewListAdapter(ImageFromVolleyActivity.this, URLS));
    }

    private class MyAdapter extends CommonAdapter<ImageVolley> {

        private ImageLoader imageLoader;
        private Context context;

        public MyAdapter(Context context, int layoutId, List<ImageVolley> datas) {
            super(context, layoutId, datas);

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        }

        @Override
        public void convert(ViewHolder holder, ImageVolley imageVolley) {
            holder.setText(R.id.item_volley_img_textView, imageVolley.getText());
        }
    }

    private class VolleyImageViewListAdapter extends BaseAdapter {

        private static final String TAG = "VolleyImageViewListAdapter";

        private Context mContext;
        private String[] urlArrays;
        private ImageLoader mImageLoader;

        public VolleyImageViewListAdapter(Context context, String[] urls) {
            this.mContext = context;
            this.urlArrays = urls;
            RequestQueue mQueue = Volley.newRequestQueue(context);
            mImageLoader = new ImageLoader(mQueue, new BitmapCache());
        }

        @Override
        public int getCount() {
            return urlArrays.length;
        }

        @Override
        public Object getItem(int position) {
            return urlArrays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_volley_img, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.item_volley_img_img);
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.item_volley_img_textView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String url = "";
            url = urlArrays[position % urlArrays.length];

            viewHolder.mTextView.setText(position + "+" + urlArrays.length);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.mImageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
            mImageLoader.get(url, listener);
            return convertView;
        }

        class ViewHolder {
            TextView mTextView;
            ImageView mImageView;
        }

    }

    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }
    }
}
