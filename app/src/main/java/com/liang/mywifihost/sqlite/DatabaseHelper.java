package com.liang.mywifihost.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;

import static android.R.attr.version;

/**
 * 数据库帮助类
 * Created by 广靓 on 2017/2/5.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME="MyDatabase";
    public static String TABLE_ip="Table_SeeClass";
    public static String TABLE_late="Table_SeeLate";
    private static int TABLE_VERSION=1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_ip+" (ip text unsigned primary key, name text , class text, number text)");
        db.execSQL("CREATE TABLE "+TABLE_late+" (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " name text , class text, time_year_day text,time_time text)");
        System.out.println("数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
