package com.run.running.frag;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.run.running.R;
import com.run.running.app.BaseFragment;
import com.run.running.entity.db.PersonTable;
import com.run.running.utils.db.DbXutil;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作
 * Created by zsr on 2016/5/18.
 */
@ContentView(R.layout.frag_db)
public class DBFragment extends BaseFragment {

    @ViewInject(R.id.edit_age)
    private EditText age;

    @ViewInject(R.id.edit_name)
    private EditText name;

    @ViewInject(R.id.edit_sex)
    private EditText sex;

    @ViewInject(R.id.edit_salary)
    private EditText salary;

    @ViewInject(R.id.db_xutil_detail)
    private TextView xutilSqliteDetail;

    DbManager.DaoConfig daoConfig = DbXutil.getDaoConfig();
    DbManager db = x.getDb(daoConfig);

    @Event(value = {R.id.db_xutil_insert, R.id.db_xutil_query, R.id.db_xutil_selector, R.id.db_xutil_update, R.id.db_xutil_delete, R.id.db_xutil_xingning})
    private void db(View view) {
        switch (view.getId()) {
            case R.id.db_xutil_insert:
                insert();//增
                break;
            case R.id.db_xutil_query:
//                query();//查 根据Id
//                queryFindFirst();//返回当前表里面的第一条数据
                findAll();//返回当前表里面的所有数据
                break;
            case R.id.db_xutil_selector:
//                selector();//该方法主要是用来进行一些特定条件的查找
//                dbmodel();
                finddbmodelall();
                break;
            case R.id.db_xutil_update:
//                update1();
                update2();
                break;
            case R.id.db_xutil_delete:
//                delete1();
//                delete2();
                deleteAll();
                break;
            case R.id.db_xutil_xingning:
                xingneng();
                break;
        }
    }

    private void xingneng() {
        xutilSqliteDetail.setText("请等待~");
        x.task().run(new Runnable() {//异步执行
            @Override
            public void run() {
                DbManager db = x.getDb(daoConfig);
                String result = "";

                List<PersonTable> personTableList = new ArrayList<PersonTable>();
                for (int i = 0; i < 1000; i++) {
                    PersonTable personTable = new PersonTable();
                    personTable.setAge(88);
                    personTable.setSalary("9999");
                    personTable.setSex("美女");
                    personTable.setName("测试");
                    personTableList.add(personTable);
                }

                long start = System.currentTimeMillis();
                for (PersonTable personTable : personTableList) {
                    try {
                        db.save(personTable);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                result += "插入1000条数据：" + (System.currentTimeMillis() - start) + "ms\n";


                start = System.currentTimeMillis();
                try {
                    personTableList = db.selector(PersonTable.class).orderBy("id", true).limit(1000).findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                result += "查找1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

                start = System.currentTimeMillis();
                try {
                    db.delete(personTableList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                result += "删除1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

                //批量插入
                personTableList = new ArrayList<PersonTable>();
                for (int i = 0; i < 1000; i++) {
                    PersonTable personTable = new PersonTable();
                    personTable.setAge(88);
                    personTable.setSalary("9999");
                    personTable.setSex("美女");
                    personTable.setName("测试");
                    personTableList.add(personTable);
                }

                start = System.currentTimeMillis();
                try {
                    db.save(personTableList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                result += "批量插入1000条数据:" + (System.currentTimeMillis() - start) + "ms\n";

                try {
                    personTableList = db.selector(PersonTable.class).orderBy("id", true).limit(1000).findAll();
                    db.delete(personTableList);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                final String finalResult = result;
                x.task().post(new Runnable() {//UI同步执行
                    @Override
                    public void run() {
                        xutilSqliteDetail.setText(finalResult);
                    }
                });
            }
        });
    }

    private void deleteAll() {
        //删除所有数据
        try {
            List<PersonTable> person = db.findAll(PersonTable.class);
            db.delete(person);
            xutilSqliteDetail.setText("数据库所有内容删除!");
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void delete2() {
        //删除name 为小琪 这条信息的数据
        try {
            PersonTable person = db.selector(PersonTable.class).where("name", "=", "小琪").findFirst();
            db.delete(person);
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void delete1() {
        //删除表中id为5的记录
        try {
            db.deleteById(PersonTable.class, 5);
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void update2() {
        //修改所有"美女"工资为6000
        try {
            List<PersonTable> persons = db.findAll(PersonTable.class);
            for (PersonTable person : persons) {
                person.setSalary("6000");
                db.update(person, String.valueOf(WhereBuilder.b("sex", "=", "美女")), "salary");
            }
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void update1() {
        //修改id为1 的age 为25
        try {
            PersonTable personTable = db.findById(PersonTable.class, 1);
            personTable.setAge(25);
            db.update(personTable, "age");
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void finddbmodelall() {
        //findDbModelAll的用法
        //该方法的用途就是返回满足sqlInfo信息的所有数据的字段的一个集合。


//        查找person表中年龄age大于25里面的所有人的姓名
        try {
            List<DbModel> persons = db.findDbModelAll(new SqlInfo("select * from person where age>25"));
            for (DbModel person : persons) {
                if (person.toString().isEmpty()) {
                    xutilSqliteDetail.setText("暂无数据");
                } else {
                    xutilSqliteDetail.setText(person.getString("name"));
                }
            }
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void dbmodel() {
        //查找person表中第一条数据的那个人的年龄age是多少。
        try {
            DbModel model = db.findDbModelFirst(new SqlInfo("select * from person"));
            xutilSqliteDetail.setText(model.getString("age"));
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void selector() {
        try {
            //查找person表里面age大于30并且性别为man的数据
            List<PersonTable> persons = db.selector(PersonTable.class).where("age", ">", "30").and("sex", "=", "man").findAll();
            for (PersonTable person : persons) {
                if (person.toString().isEmpty()) {
                    xutilSqliteDetail.setText("数据为空~");
                } else {
                    xutilSqliteDetail.setText(person.toString());
                }
            }
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void findAll() {
        try {
            List<PersonTable> person = db.findAll(PersonTable.class);

            if (person.isEmpty()) {
                xutilSqliteDetail.setText("数据为空~");
            } else {
                xutilSqliteDetail.setText(person.toString());
            }
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void queryFindFirst() {
        try {
            PersonTable person = db.findFirst(PersonTable.class);
            xutilSqliteDetail.setText(person.toString());
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void query() {
        try {
            PersonTable person = db.findById(PersonTable.class, "2");
            xutilSqliteDetail.setText(person.toString());
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }
    }

    private void insert() {
        String nameStr = name.getText().toString();
        int ageStr = Integer.parseInt(age.getText().toString());
        String sexStr = sex.getText().toString();
        String salaryStr = salary.getText().toString();
        try {
            PersonTable person = new PersonTable();
            person.setName(nameStr);
            person.setAge(ageStr);
            person.setSex(sexStr);
            person.setSalary(salaryStr);
            db.save(person);
        } catch (DbException e) {
            xutilSqliteDetail.setText(e.getMessage());
        }

    }

}
