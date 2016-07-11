package com.run.running.activity;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.run.runlibrary.adapter.recyclerview.OnItemClickListener;
import com.run.runlibrary.manager.SystemBarTintManager;
import com.run.running.R;
import com.run.running.adapter.LeftMenuAdapter;
import com.run.running.entity.LeftMenu;
import com.run.running.frag.ChartFragment;
import com.run.running.frag.DBFragment;
import com.run.running.frag.Html5Fragment;
import com.run.running.frag.HttpFragment;
import com.run.running.frag.ImageFragment;
import com.run.running.frag.MVPFragment;
import com.run.running.frag.MainFragment;
import com.run.running.frag.MapFragment;
import com.run.running.frag.SDKFragment;
import com.run.running.interfaces.OnFragmentRefreshListener;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * 程序入口：Mainactivity 绑定 MainFragment
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener, OnFragmentRefreshListener {

    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;

    @ViewInject(R.id.drawer_left)
    private DrawerLayout mDrawerLayout;

    @ViewInject(R.id.left_frame_list)
    private RecyclerView mMenuRecyclerView;

    @ViewInject(R.id.left_frame)
    private FrameLayout leftFrameLayout;

    @ViewInject(R.id.content_swipe)
    private SwipeRefreshLayout swipeRefreshLayout;

    @ViewInject(R.id.content_frame)
    private FrameLayout content_frame;

    private Handler mHandler = new Handler();

    private String tag;

    private String[] titles = {"主页", "图片加载", "Http相关", "MVP模式", "数据库操作", "地图操作", "其他平台SDK", "图表控件", "Html5嵌入"};
    private int[] imgs = {R.drawable.ic_home_black_24dp, R.drawable.ic_image_black_24dp, R.drawable.ic_vertical_align_center_black_24dp, R.drawable.ic_mvp_black_24dp, R.drawable.ic_db_black_24dp, R.drawable.ic_map_black_24dp, R.drawable.ic_sdk_black_24dp, R.drawable.ic_receipt_black_24dp, R.drawable.ic_web_24dp};

    private ActionBarDrawerToggle mActionBarDrawerToggle;


    private List<LeftMenu> mDatas;

    private FragmentManager fragmentManager;

    private ContextMenuDialogFragment mMenuDialogFragment;

    private Fragment contentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolBar();

        initLeftMenuList();

        initRightMenuFragment();

        initStatusBar();

        fragmentManager = getSupportFragmentManager();

        addMainFragment();//MainActivity打开首先展示MainFragment

        initRefresh();

        startRefresh();//界面打开调用refresh 并调用onRefresh方法

    }

    public void startRefresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        refreshListener.onRefresh();
    }

    private void initRefresh() {
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    public SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                        。。。。刷新操作。。。。刷新当前Fragment
                    //重新加载当前Fragment
                    //刷新完成
//                    Toast.makeText(MainActivity.this, "Fragment===" + getVisibleFragment(), Toast.LENGTH_SHORT).show();

//                    Fragment frag_html5 = getVisibleFragment();
//
//                    fragmentManager.beginTransaction().replace(R.id.content_frame, frag_html5).commit();


                    test()
                    ;

                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 3000);
        }
    };

    private void test() {
        switch (getVisibleFragment().getTag()) {
            case "0":
                onMainFragmentRefresh();
                break;
            case "1":
                onImageFragmentRefresh();
                break;
            case "2":
                onHttpFragmentRefresh();
                break;
            case "3":
                onMVPFragmentRefresh();
                break;
            case "4":
                onDbFragmentRefresh();
                break;
            case "5":
                onMapFragmentRefresh();
                break;
            case "6":
                onSDKFragmentRefresh();
                break;
//            case "7":
//                onPayMethodFragmentRefresh();
//                break;
            case "7":
                onChartFragmentRefresh();
                break;
            case "8":
                onHtml5FragmentRefresh();
                break;
        }
    }

    /**
     * 获取Activity当前展示的Fragment
     *
     * @return
     */
    private Fragment getVisibleFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }


    /**
     * 添加主Fragment到首页
     */
    private void addMainFragment() {
        Fragment mFragment = fragmentManager.findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = new MainFragment();
            fragmentManager.beginTransaction().add(R.id.content_frame, mFragment, "0").commit();

            mToolbar.setTitle(titles[0]);
        }

    }


    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
    }

    private void initRightMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObject());
        menuParams.setClosableOutside(false);

        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private void initLeftMenuList() {
        mDatas = getDatas();

        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(MainActivity.this, R.layout.item_left_menu, mDatas);
        mMenuRecyclerView.setAdapter(leftMenuAdapter);


        leftMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

                tag = "" + position;
                switch (position) {
                    case 0:
                        contentFragment = new MainFragment();
                        mToolbar.setTitle(titles[0]);
                        break;
                    case 1:
                        contentFragment = new ImageFragment();
                        mToolbar.setTitle(titles[1]);
                        break;
                    case 2:
                        contentFragment = new HttpFragment();
                        mToolbar.setTitle(titles[2]);
                        break;
                    case 3:
                        contentFragment = new MVPFragment();
                        mToolbar.setTitle(titles[3]);
                        break;
                    case 4:
                        contentFragment = new DBFragment();
                        mToolbar.setTitle(titles[4]);
                        break;
                    case 5:
                        contentFragment = new MapFragment();
                        mToolbar.setTitle(titles[5]);
                        break;
                    case 6:
                        contentFragment = new SDKFragment();
                        mToolbar.setTitle(titles[6]);
                        break;
//                    case 7:
//                        contentFragment = new PayMethodFragment();
//                        mToolbar.setTitle(titles[7]);
//                        break;
                    case 7:
                        contentFragment = new ChartFragment();
                        mToolbar.setTitle(titles[7]);
                        break;
                    case 8:
                        contentFragment = new Html5Fragment();
                        mToolbar.setTitle(titles[8]);
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.content_frame, contentFragment, tag).commit();
                mDrawerLayout.closeDrawer(leftFrameLayout);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void initToolBar() {

        mToolbar.setTitle("Title");
        mToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));//设置title颜色
        setSupportActionBar(mToolbar);//设置ToolBar生效

        //以下两行代码设置左上角的菜单图标的效果
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置左上角菜单图标的点击效果
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

//    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
//        invalidateOptionsMenu();
//        String backStackName = fragment.getClass().getName();
//        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
//        if (!fragmentPopped) {
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(containerId, fragment, backStackName)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            if (addToBackStack)
//                transaction.addToBackStack(backStackName);
//            transaction.commit();
//        }
//    }

    /**
     * 当Activity彻底运行起来之后回调onPostCreate方法
     *
     * @param savedInstanceState
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //需要将ActionBarDrawerToggle与DrawerLayout的状态同步
        //将ActionBarDrawerToggle中的drawer图标，设置为ActionBar中的Home-Button的图标
        mActionBarDrawerToggle.syncState();
    }

    /**
     * Activity配置发生变化时调用该方法
     * 如：屏幕旋转
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<MenuObject> getMenuObject() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_close_white_24dp);

        MenuObject login = new MenuObject("登录");
        login.setResource(R.drawable.ic_login_hezuo_white_24dp);

        MenuObject erweima = new MenuObject("二维码扫描");
        erweima.setResource(R.drawable.ic_erweima_white_24dp);

        MenuObject update = new MenuObject("版本更新");
        update.setResource(R.drawable.ic_update_white_24dp);

        MenuObject share = new MenuObject("分享");
        share.setResource(R.drawable.ic_share_white_24dp);

        menuObjects.add(close);
        menuObjects.add(login);
        menuObjects.add(erweima);
        menuObjects.add(update);
        menuObjects.add(share);
        return menuObjects;
    }


    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Toast.makeText(this, "Clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
    }

    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public List<LeftMenu> getDatas() {
        List<LeftMenu> data = new ArrayList<LeftMenu>();
        for (int i = 0; i < titles.length; i++) {
            LeftMenu leftMenu = new LeftMenu(titles[i], imgs[i]);
            data.add(leftMenu);
        }
        return data;
    }

    @Override
    public void onMainFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Main", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDbFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Db", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHttpFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Http", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Image", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Map", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMVPFragmentRefresh() {
        Toast.makeText(MainActivity.this, "MVP", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onPayMethodFragmentRefresh() {
//        Toast.makeText(MainActivity.this, "payMethod", Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onSDKFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Sdk", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChartFragmentRefresh() {
        Toast.makeText(MainActivity.this, "Chart", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHtml5FragmentRefresh() {
        Toast.makeText(MainActivity.this, "html5", Toast.LENGTH_LONG).show();
    }

}
