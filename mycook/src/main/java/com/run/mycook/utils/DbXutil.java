package com.run.mycook.utils;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Xutil 中的DButil 数据库操作工具类
 * Created by zsr on 2016/6/12.
 */
public class DbXutil {

    static DbManager.DaoConfig daoConfig;

    public static DbManager.DaoConfig getDaoConfig() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("cook.db")
                    .setDbVersion(1)
                    .setAllowTransaction(true)//是否开启事务
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            //开启WAL，对写入速度提升巨大
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            //数据库更新操作
                            // db.addColumn(...);
                            // db.dropTable(...);
                            // ...
                            // or
                            // db.dropDb();
                        }
                    });
        }
        return daoConfig;
    }

}
