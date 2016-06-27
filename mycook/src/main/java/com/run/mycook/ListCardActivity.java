package com.run.mycook;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.run.mycook.adapter.CookAdapter;
import com.run.mycook.entity.JHCook;
import com.run.mycook.utils.MyCallBack;
import com.run.runlibrary.BaseActivity;
import com.run.runlibrary.adapter.recyclerview.OnItemClickListener;
import com.run.runlibrary.netstate.NetWorkUtil;
import com.run.runlibrary.utils.XUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsr on 2016/6/18.
 */
@ContentView(R.layout.activity_card_list)
public class ListCardActivity extends BaseActivity {

    @ViewInject(R.id.card_list_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.card_list_recyclerview)
    private RecyclerView recyclerView;

    @ViewInject(R.id.list_nonetwork_refresh)
    private RelativeLayout noNetRefresh;

    @ViewInject(R.id.list_nodata)
    private RelativeLayout noData;

    private String title;
    private CookAdapter cookAdapter;
    private List<JHCook.Data> list;

    private Dialog progressDialog;

    private final int MSG = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG:


                    initProgressDialog();
//
//                    recyclerView.setLayoutManager(new LinearLayoutManager(ListCardActivity.this));//设置布局管理器
//                    recyclerView.setHasFixedSize(true);//如果可以确定每个Item 的高度是固定的，设置这个选项可以提高性能

                    getDataFromJuHe();//获取数据  聚合数据API


                    try {
                        cookAdapter.notifyDataSetChanged();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initProgressDialog();

        title = getIntent().getStringExtra("name");

        getDataFromJuHe();//获取数据  聚合数据API

        init();

    }

    private void initProgressDialog() {
        progressDialog = new Dialog(ListCardActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView dialogMsg = (TextView) progressDialog.findViewById(R.id.dialog_loading);
        dialogMsg.setText("卖力加载中~");
        progressDialog.show();
    }


    private void init() {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));//设置title颜色
        setSupportActionBar(toolbar);//设置ToolBar生效
//
//        //以下两行代码设置左上角的菜单图标的效果
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用、可点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//给左上角图标左边加上一个返回的图标

        recyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        recyclerView.setHasFixedSize(true);//如果可以确定每个Item 的高度是固定的，设置这个选项可以提高性能

    }


    private void getDataFromJuHe() {
        String url = "http://apis.juhe.cn/cook/query.php";
        Map<String, String> map = new HashMap<String, String>();
        map.put("menu", title);
        map.put("key", "3bb3902808582e2b0673399c379bb54d");
        XUtil.get(url, map, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                Gson gson = new Gson();
                JHCook jhCook = gson.fromJson(result, JHCook.class);

                if (jhCook.getResultcode().equals("200")) {
                    list = jhCook.getResult().getData();
                } else {
                    noData.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onFinished() {
                super.onFinished();
                refresh();
                progressDialog.dismiss();
                noNetRefresh.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                progressDialog.dismiss();
            }
        });
    }

    private void refresh() {
        List<JHCook.Data> data = new ArrayList<JHCook.Data>();
        for (int i = 0; i < list.size(); i++) {
            JHCook.Data data1 = new JHCook.Data(list.get(i).getTitle(), list.get(i).getImtro(), list.get(i).getAlbums(), list.get(i).getTags());
            data.add(data1);
        }

        cookAdapter = new CookAdapter(ListCardActivity.this, R.layout.item_card, data);
        recyclerView.setAdapter(cookAdapter);


        cookAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(ListCardActivity.this, DetailActivity.class);

                intent.putExtra("title", list.get(position).getTitle());
                intent.putExtra("tag", list.get(position).getTags());
                intent.putExtra("imtro", list.get(position).getImtro());
                intent.putExtra("ingredients", list.get(position).getIngredients());
                intent.putExtra("burden", list.get(position).getBurden());
                intent.putExtra("albums", list.get(position).getAlbums().get(0));
                intent.putExtra("steps", (Serializable) list.get(position).getSteps());

                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @Override
    public void onDisConnect() {
        super.onDisConnect();
        noNetRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnect(NetWorkUtil.netType type) {
        super.onConnect(type);
    }

    @Event(R.id.list_nonetwork_refresh)
    private void refresh(View view) {
        handler.sendEmptyMessage(MSG);
    }
}
