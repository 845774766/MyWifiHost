package com.liang.mywifihost.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.liang.mywifihost.activity.MainActivity;
import com.liang.mywifihost.network.Mobile_MobManager;
import com.liang.mywifihost.network.Wifi_APManager;
import com.liang.mywifihost.network.Wifi_WifiManager;
import com.liang.mywifihost.sqlite.DatabaseManager;

import java.util.HashMap;

import cn.bmob.v3.Bmob;

/**
 * Created by 广靓 on 2017/2/5.
 */

public class application extends Application {

    private DatabaseManager databaseManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Wifi_APManager apManager;
    private Wifi_WifiManager wifiManager;
    private Mobile_MobManager mobManager;

    @Override
    public void onCreate() {
        super.onCreate();

        //bmob初始化
        Bmob.initialize(this, "762e0afb2e5fcb90e86e10b81f3ab160");

        sharedPreferences=getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);

        wifiManager =new Wifi_WifiManager(this);
        apManager=new Wifi_APManager(this);
        mobManager=new Mobile_MobManager(this);
        inAppSetNetwork();

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

    /**
     * 进入app时要执行的网络设置
     */
    private void inAppSetNetwork() {

        //记录进入软件时的wifi状态
        if (sharedPreferences.getBoolean("record_network",false))
            wifiManager.InApp_isOpenWifi();

        //如果已打开wifi热点，则关闭
        if (apManager.isWifiApEnabled())
            apManager.closeWifiAp();

        //关闭WIFI
        if (wifiManager.isWifiActive()) {
            if (sharedPreferences.getBoolean("close_wifi", false))
                wifiManager.closeWifi();
        }

        //提示关闭数据连接
        if (sharedPreferences.getBoolean("prompt_mobile",false))
            Toast.makeText(this,"记得关闭自己的数据连接哦",Toast.LENGTH_SHORT).show();

        //关闭数据连接
//        mobManager.setDataConnectionState(false);

    }



}
