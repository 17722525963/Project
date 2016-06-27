package com.run.mycook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.run.mycook.adapter.HistoryAdapter;
import com.run.mycook.adapter.HotAdapter;
import com.run.mycook.entity.History;
import com.run.mycook.entity.Hot;
import com.run.mycook.utils.DbXutil;
import com.run.mycook.utils.MyUtil;
import com.run.runlibrary.BaseActivity;
import com.run.runlibrary.adapter.recyclerview.OnItemClickListener;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(R.id.main_recyclerview_history)
    private RecyclerView histoRyecyclerView;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawer;

    @ViewInject(R.id.nav_view)
    private NavigationView navigationView;

    @ViewInject(R.id.main_search_clear)
    private ImageView clear;//清除按钮

    @ViewInject(R.id.main_search_edit)
    private EditText searchEdit;//输入数据

    @ViewInject(R.id.main_search_sure)
    private EditText serachSure;//确定按钮

    @ViewInject(R.id.history_hot_gridview)
    private GridView hotGridview;//热搜榜

    @ViewInject(R.id.history_delete)
    private ImageView historyDelete;

    @ViewInject(R.id.history_layout)
    private LinearLayout historyLayout;

    private List<History> historyList = new ArrayList<History>();//历史记录
    private List<Hot> hotSearchList = new ArrayList<Hot>();//热搜榜
    private List<Hot> hotList = new ArrayList<Hot>();//


    //    private HistoryAdapter historyAdapter;
    private HotAdapter hotAdapter;

    private String PACKAGE_NAME = "com.run.mycook";

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoConfig = DbXutil.getDaoConfig();
        db = x.getDb(daoConfig);

        ifFirst();//判断应用是否是第一次启动,如果是第一次启动，则添加数据

        queryDB();//从数据库中查询数据

        initToolbar();

        initSearch();

        initBottom();//底部历史记录
    }

    private void ifFirst() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
            int currentVersion = info.versionCode;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int lastVersion = preferences.getInt("version", 0);
            if (currentVersion > lastVersion) {
                //如果当前版本大于上次版本，该版本属于第一次启动

                //添加数据
                History history = new History();
                history.setTitle("秘制红烧肉");
                history.setImg("http://img.juhe.cn/cookbook/t/0/45_854851.jpg");
                history.setNum(3);
                db.saveOrUpdate(history);


                //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                preferences.edit().putInt("version", currentVersion).commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initSearch() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //跳转下页进行搜索
                    String name = searchEdit.getText().toString();
                    if (!name.isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, ListCardActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
    }

    @Event(value = {R.id.main_search_clear, R.id.main_search_sure, R.id.history_hot_refresh, R.id.history_delete})
    private void clear(View view) {
        switch (view.getId()) {
            case R.id.main_search_clear://Search clear
                searchEdit.setText("");
                clear.setVisibility(View.GONE);
                break;
            case R.id.main_search_sure://Search sure
                //跳转下页进行搜索
                String name = searchEdit.getText().toString();
                if (name.isEmpty()) {
                    Snackbar.make(view, "请先输入菜名/菜谱", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ListCardActivity.class);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                break;
            case R.id.history_hot_refresh://hot refresh
                //清空当前hotList
                hotList.clear();
                //在热搜版中随机找6个，构成新的list
                int[] index = MyUtil.randomArray(0, hotSearchList.size() - 1, 6);//随机6个不重复的数值
                for (int i = 0; i < 6; i++) {
//                    int index = (int) (Math.random() * hotSearchList.size());
                    hotList.add(hotSearchList.get(index[i]));
                }


                hotAdapter = new HotAdapter(MainActivity.this, R.layout.item_hot, hotList);
                hotGridview.setAdapter(hotAdapter);
                hotAdapter.notifyDataSetChanged();
                break;
            case R.id.history_delete://history delete
                historyLayout.setVisibility(View.GONE);
                try {
                    db.delete(History.class);
                    //添加数据
                    History history = new History();
                    history.setTitle("秘制红烧肉");
                    history.setImg("http://img.juhe.cn/cookbook/t/0/45_854851.jpg");
                    history.setNum(3);
                    db.saveOrUpdate(history);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void initBottom() {
        historyLayout.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        histoRyecyclerView.setLayoutManager(linearLayoutManager);
        HistoryAdapter historyAdapter = new HistoryAdapter(MainActivity.this, R.layout.item_history, historyList);
        histoRyecyclerView.setAdapter(historyAdapter);

        historyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(MainActivity.this, ListCardActivity.class);
                intent.putExtra("name", historyList.get(position).getTitle());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });

        //添加热门数据
        String[] list = {"紫菜蛋花汤", "糖醋里脊", "红烧狮子头", "酱猪蹄", "凉拌黄瓜", "酸菜鱼", "粉蒸排骨"};
        for (int i = 0; i < list.length; i++) {
            Hot hot = new Hot();
            hot.setTitle(list[i]);
            hotSearchList.add(hot);
        }

        //在热搜版中随机找6个，构成新的list
        int[] index = MyUtil.randomArray(0, hotSearchList.size() - 1, 6);//随机6个不重复的数值
        for (int i = 0; i < 6; i++) {
//            int index = (int) (Math.random() * hotSearchList.size());
            hotList.add(hotSearchList.get(index[i]));
        }


        hotAdapter = new HotAdapter(MainActivity.this, R.layout.item_hot, hotList);
        hotGridview.setAdapter(hotAdapter);

        hotGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListCardActivity.class);
                intent.putExtra("name", hotList.get(position).getTitle());
                startActivity(intent);
            }
        });
    }


    private void queryDB() {

        //查询数据
        try {
            historyList = db.selector(History.class).orderBy("id", true).findAll();//从数据库中查找最新添加的数据

            //搜索数据库中num>2 的数据
            List<DbModel> hots = db.findDbModelAll(new SqlInfo("select * from history where num>2"));
            for (DbModel hot : hots) {
                if (hot.toString().isEmpty()) {
                    //无数据
                } else {
                    Hot hot1 = new Hot();
                    hot1.setTitle(hot.getString("title"));
                    hotSearchList.add(hot1);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent();
        if (id == R.id.nav_aboutus) {
            //关于我们
            intent.setClass(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            //设置，其中需要包含清除缓存功能
            intent.setClass(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            //分享
        } else if (id == R.id.nav_send) {
            //意见建议（发送QQ的方式）
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
