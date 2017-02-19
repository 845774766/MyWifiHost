package com.liang.mywifihost.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.RemoteException;

import java.lang.reflect.Method;

/**
 * Created by 广靓 on 2017/2/8.
 */

public class Mobile_MobManager {

    private Context mContext;

    public Mobile_MobManager(Context context){
        this.mContext=context;
    }

    /**
     * 数据连接 开/关   咱不能用
     * @param state 开,true
     */
    public void setDataConnectionState( boolean state) {
        ConnectivityManager connectivityManager = null;
        Class connectivityManagerClz = null;
        try {
            connectivityManager = (ConnectivityManager) mContext
                    .getSystemService("connectivity");
            connectivityManagerClz = connectivityManager.getClass();
            Method method = connectivityManagerClz.getMethod(
                    "setMobileDataEnabled", new Class[] { boolean.class });
            method.invoke(connectivityManager, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
