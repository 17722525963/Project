package com.run.mycook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.run.mycook.adapter.StepAdapter;
import com.run.mycook.entity.History;
import com.run.mycook.entity.JHCook;
import com.run.mycook.utils.DbXutil;
import com.run.runlibrary.BaseActivity;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 详情界面
 * Created by zsr on 2016/6/17.
 */
@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseActivity {

    @ViewInject(R.id.detail_backdrop)
    private ImageView backdrop;

    @ViewInject(R.id.detail_collapsingtoolbar)
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @ViewInject(R.id.detail_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.detail_show_burden)
    private TextView show_burden;

    @ViewInject(R.id.detail_show_tags)
    private TextView show_tags;

    @ViewInject(R.id.detail_show_imtro)
    private TextView show_imtro;

    @ViewInject(R.id.detail_show_ingredients)
    private TextView show_ingredients;

    @ViewInject(R.id.detail_show_steps_recyclerview)
    private RecyclerView recyclerView;


    private StepAdapter stepAdapter;

    private String title;
    private String albums;
    private String tag;
    private String imtro;
    private String ingredients;
    private String burden;
    private List<JHCook.Steps> list;

    private DbManager.DaoConfig daoConfig;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        daoConfig = DbXutil.getDaoConfig();
        db = x.getDb(daoConfig);

        getData();

        init();

        queryDB();

        insertDB();
    }

    private void queryDB() {

    }

    private void insertDB() {
//        插入数据：系判断数据库中是否有当前记录，
//        有 得到num   删除，添加新的   添加新的 num   展示：倒序展示
//        无 ，直接添加  列名num设置为1

        try {
            //查询title的所有数据
            List<History> histories = db.selector(History.class).where("title", "=", title).findAll();

            if (histories.size() > 0) {
                for (History historyTitle : histories) {
                    int num = historyTitle.getNum();
                    int id = historyTitle.getId();
                    db.deleteById(History.class, id);

                    //添加新数据
                    int newNum = num + 1;
                    History history = new History();
                    history.setTitle(title);
                    history.setImg(albums);
                    history.setNum(newNum);
                    db.save(history);
                }
            } else if (histories.size() == 0) {
                //无数据,直接添加 num= 1
                History history = new History();
                history.setTitle(title);
                history.setImg(albums);
                history.setNum(1);
                db.save(history);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initToolBarColor() {
        //设置ToolBar向上滑动后显示的颜色
        Bitmap bitmap = ((BitmapDrawable) backdrop.getDrawable()).getBitmap();
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    toolbar.setBackgroundColor(swatch.getRgb());
                }
            }
        });

    }

    private void init() {
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));//设置title颜色
        setSupportActionBar(toolbar);//设置ToolBar生效

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //设置CollapsingToolbarLayout图片
        x.image().bind(backdrop, albums);


        show_burden.setText(burden);
        show_imtro.setText(imtro);
        show_ingredients.setText(ingredients);
        show_tags.setText(tag);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        stepAdapter = new StepAdapter(DetailActivity.this, R.layout.item_step, list);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(stepAdapter);


    }


    private void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        tag = intent.getStringExtra("tag");
        albums = intent.getStringExtra("albums");
        imtro = intent.getStringExtra("imtro");
        ingredients = intent.getStringExtra("ingredients");
        burden = intent.getStringExtra("burden");
        list = (List<JHCook.Steps>) intent.getSerializableExtra("steps");


    }
}
