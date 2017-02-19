package com.liang.mywifihost.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.liang.mywifihost.network.Wifi_APManager;
import com.liang.mywifihost.network.Wifi_WifiManager;
import com.liang.mywifihost.sqlite.DatabaseManager;

import java.util.HashMap;

/**
 * Created by 广靓 on 2017/2/5.
 */

public class application extends Application {

    private DatabaseManager databaseManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

//        sharedPreferences=getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);
//        editor=sharedPreferences.edit();
//
//        databaseManager=new DatabaseManager(this);
//        if (!sharedPreferences.getBoolean("first",false)){
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("ip", "01");
//            map.put("name", "吴广靓");
//            map.put("class", "信息管理与信息系统");
//            map.put("number", "201542781");
//            databaseManager.insert(DatabaseManager.TABLE_IP, map);
//        }
//
//        editor.putBoolean("first",true);
//        editor.commit();

    }
}
