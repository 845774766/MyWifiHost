package com.liang.mywifihost.wifi;


import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class WifiAPManager {

    private WifiManager mWifiManager;
    private Context mContext;

    public WifiAPManager(Context context){
        this.mContext=context;
        mWifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }


    /**
     * 创建热点
     * @param mSSID 热点名称
     * @param mPasswd 热点密码
     */
    public void startWifiAp(String mSSID,String mPasswd){
       Method method1=null;
       try {
           method1=mWifiManager.getClass().getMethod("setWifiApEnabled",
                   WifiConfiguration.class,boolean.class);
           WifiConfiguration netConfig=new WifiConfiguration();

           netConfig.SSID=mSSID;
           netConfig.preSharedKey=mPasswd;
           netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
           netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
           netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
           netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
           netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
           netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
           netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
           netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
           method1.invoke(mWifiManager,netConfig,true);

       } catch (NoSuchMethodException e) {
           e.printStackTrace();
       } catch (InvocationTargetException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       }
    }

    /**
     * 检查是否开启Wifi热点
     * @return
     */
    public boolean isWifiApEnabled(){
        try {
            Method method=mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public void closeWifiAp(){
        WifiManager wifiManager= (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled()){
            try {
                Method method=wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config= (WifiConfiguration) method.invoke(wifiManager);
                Method method2=wifiManager.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class,boolean.class);
                method2.invoke(wifiManager,config,false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开热点手机获得其他连接手机IP的方法
     * @return 其他手机IP 数组列表
     */
    public ArrayList<String> getConnectedIP(){
        ArrayList<String> connectedIp=new ArrayList<>();
        try {
            BufferedReader br=new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line=br.readLine())!=null){
                String[] splitted=line.split(" +");
                if (splitted !=null && splitted.length>=4){
                    String ip=splitted[0];
                    if (!ip.equalsIgnoreCase("ip")){
                        connectedIp.add(ip);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectedIp;
    }
}