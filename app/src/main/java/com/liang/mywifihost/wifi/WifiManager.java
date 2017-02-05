package com.liang.mywifihost.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;

import static android.content.res.Resources.getSystem;

/**
 * Created by 广靓 on 2017/2/5.
 */

public class WifiManager {
    private Context mContext;
    private android.net.wifi.WifiManager wifiManager;

    public WifiManager(Context context){
        this.mContext=context;
    }

    /**
     * 判断wifi是否打开
     * @return
     */
    public boolean isWifiActive(){
        ConnectivityManager mConnectivity=(ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivity!=null){
            NetworkInfo[] infos=mConnectivity.getAllNetworkInfo();
            if (infos !=null){
                for (NetworkInfo ni:infos){
                    if ("WIFI".equals(ni.getTypeName())&&ni.isConnected())
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得热点手机IP地址
     * @return ip
     */
    public String getIp(){
        DhcpInfo info=wifiManager.getDhcpInfo();
        int iii=info.serverAddress;
        String ip=intToIp(iii);
        return ip;
    }

    /**
     * 获得ip地址算法
     * @param i
     * @return IP
     */
    private String intToIp(int i){
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

}
