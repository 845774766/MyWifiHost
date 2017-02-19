package com.liang.mywifihost.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.mywifihost.R;
import com.liang.mywifihost.adapter.DrawerListviewAdapter;
import com.liang.mywifihost.anim.MyAnimator;
import com.liang.mywifihost.network.Mobile_MobManager;
import com.liang.mywifihost.network.Wifi_APManager;
import com.liang.mywifihost.network.Wifi_WifiManager;
import com.liang.mywifihost.sqlite.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity{


    private Button btn_Open_Ap;
    private Button btn_Setting;
    private Button btn_late;
    private Button btn_All_Data;
    private TextView view_visbile;
    private TextView view_visbile_drawer;
    private Button btn_Set_Default_Name;
    private EditText drawer_edit;
    private Button drawer_btn_query;
    private Button drawer_btn_up;
    private Button drawer_btn_sync;
    private LinearLayout drawer_lin_data;
    private TextView drawer_txt_data;
    private TextView drawer_txt_fill_listview;
    private Button drawer_btn_data;

    private android.support.v4.widget.DrawerLayout drawerLayout;
    private LinearLayout drawerLinearLayout;
    private ListView drawerListView;
    private ArrayList<HashMap<String,Object>> list;
    private ArrayList<String> list_repeat=new ArrayList<>();
    private HashMap<String,Object> map;

    private DatabaseManager databaseManager;
    private DrawerListviewAdapter drawerListviewAdapter;
    private MyAnimator myAnimator;

    private Wifi_APManager apManager;
    private Wifi_WifiManager wifiManager;
    private Mobile_MobManager mobManager;
    private SharedPreferences sharedPreferences;

    private boolean isOnClicked=true;
    private int lastTimeArrSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //沉浸式状态栏 版本必须大于5.0
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            drawerLinearLayout=(LinearLayout)findViewById(R.id.drawer_linlayout);
            view_visbile=(TextView)findViewById(R.id.main_visbile);
            view_visbile_drawer=(TextView)drawerLinearLayout.findViewById(R.id.drawer_vi);
            view_visbile_drawer.setVisibility(View.GONE);
            view_visbile.setVisibility(View.GONE);
        }

        sharedPreferences=getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);

        wifiManager =new Wifi_WifiManager(this);
        apManager=new Wifi_APManager(this);
        mobManager=new Mobile_MobManager(this);
        myAnimator=new MyAnimator();

        initView();
        inAppSetNetwork();
    }

    /**
     * 主界面初始化
     */
    private void initView(){
        btn_Open_Ap=(Button)findViewById(R.id.main_open_hotspot);
        btn_Setting=(Button)findViewById(R.id.main_btn_setting);
        btn_late=(Button)findViewById(R.id.main_btn_look_late);
        btn_All_Data=(Button)findViewById(R.id.main_btn_look_database);
        btn_Set_Default_Name=(Button)findViewById(R.id.setting_btn_default);

        btn_Open_Ap.setOnClickListener(new MyOnClickListener());
        btn_Setting.setOnClickListener(new MyOnClickListener());
        btn_late.setOnClickListener(new MyOnClickListener());
        btn_All_Data.setOnClickListener(new MyOnClickListener());
        btn_Set_Default_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DefaultApName.class));
            }
        });

        initDrawerLayout();
    }

    /**
     * 侧滑栏界面初始化
     */
    private void initDrawerLayout(){
        drawerLayout=(android.support.v4.widget.DrawerLayout)findViewById(R.id.main_drawer_layout);
        drawerLinearLayout=(LinearLayout)findViewById(R.id.drawer_linlayout);
        drawerListView=(ListView)drawerLinearLayout.findViewById(R.id.drawer_listview);
        drawer_edit=(EditText)drawerLinearLayout.findViewById(R.id.drawer_edit);
        drawer_btn_query=(Button)drawerLinearLayout.findViewById(R.id.drawer_btn_query);
        drawer_btn_up=(Button)drawerLinearLayout.findViewById(R.id.drawer_btn_up);
        drawer_btn_sync=(Button)drawerLinearLayout.findViewById(R.id.drawer_btn_sync);
        drawer_lin_data=(LinearLayout)drawerLayout.findViewById(R.id.drawer_lin_data);
        drawer_txt_data=(TextView)drawerLayout.findViewById(R.id.drawer_txt_data);
        drawer_txt_fill_listview=(TextView)drawerLayout.findViewById(R.id.drawer_txt_fill_list);
        drawer_btn_data=(Button)drawerLayout.findViewById(R.id.drawer_btn_data_finish);

        drawer_btn_query.setOnClickListener(new MyOnClickListener());
        drawer_btn_up.setOnClickListener(new MyOnClickListener());
        drawer_btn_sync.setOnClickListener(new MyOnClickListener());
        drawer_btn_data.setOnClickListener(new MyOnClickListener());

        databaseManager=new DatabaseManager(this);
        drawerListviewAdapter=new DrawerListviewAdapter(this);

        initDrawerListview(DatabaseManager.ORDER_BY_TIME_DAY_ASC);
    }

    /**
     * 实现侧滑栏listview的分组显示
     * @param OrderBy listview 正倒排序
     */
    public void initDrawerListview(String OrderBy){
        list_repeat=new ArrayList<>();
        list_repeat=databaseManager.getRepeatingData(databaseManager.query(DatabaseManager.TABLE_LATE,DatabaseManager.TABLE_QUERY_COLUMNS_LATE,
                null,null,OrderBy),"time_year_day");
        drawerListviewAdapter=new DrawerListviewAdapter(this);
        for (int i = list_repeat.size() - 1 ; i > -1 ; i--) {
            drawerListviewAdapter.addSeparatorItem(list_repeat.get(i));
            list = new ArrayList<>();
            list = databaseManager.query(DatabaseManager.TABLE_LATE, DatabaseManager.TABLE_QUERY_COLUMNS_LATE,
                    DatabaseManager.WHERE_TIME_DAY, new String[]{list_repeat.get(i)}, DatabaseManager.ORDER_BY_TIME_ID_DESC);
            for (int k = 0; k < list.size(); k++) {
                map = new HashMap<>();
                map.put("time_time", list.get(k).get("time_time"));
                map.put("class", list.get(k).get("class"));
                map.put("name", list.get(k).get("name"));
                drawerListviewAdapter.addItem(map);
            }
        }

        lastTimeArrSize=databaseManager.queryAll(DatabaseManager.TABLE_LATE).size();
        drawerListView.setAdapter(drawerListviewAdapter);

        if (list_repeat.size()!=0){
            drawer_txt_fill_listview.setVisibility(View.GONE);
        }else {
            drawer_txt_fill_listview.setVisibility(View.VISIBLE);
            drawer_txt_fill_listview.setText("您还没有记录迟到的人");
        }

    }

    /**
     * 侧滑栏姓名搜索
     * @param whatName 要搜索的姓名
     */
    private void queryDrawerListiewData(String whatName){
        int Late_size=0;
        list_repeat=new ArrayList<>();
        list_repeat=databaseManager.getRepeatingData(databaseManager.query(DatabaseManager.TABLE_LATE,DatabaseManager.TABLE_QUERY_COLUMNS_LATE,
                DatabaseManager.WHERE_NAME,new String[]{whatName},DatabaseManager.ORDER_BY_TIME_DAY_DESC),"time_year_day");
        drawerListviewAdapter=new DrawerListviewAdapter(this);
        for (int i = list_repeat.size() - 1 ; i > -1 ; i--) {
            drawerListviewAdapter.addSeparatorItem(list_repeat.get(i));
            list = new ArrayList<>();
            list = databaseManager.query(DatabaseManager.TABLE_LATE, DatabaseManager.TABLE_QUERY_COLUMNS_LATE,
                    DatabaseManager.WHERE_TIMEDAY_NAME, new String[]{list_repeat.get(i),whatName}, DatabaseManager.ORDER_BY_TIME_ID_DESC);
            for (int k = 0; k < list.size(); k++) {
                map = new HashMap<>();
                map.put("time_time", list.get(k).get("time_time"));
                map.put("class", list.get(k).get("class"));
                map.put("name", list.get(k).get("name"));
                drawerListviewAdapter.addItem(map);
            }
            Late_size+=list.size();
        }

        drawerListView.setAdapter(drawerListviewAdapter);

        if (list_repeat.size()!=0){
            drawer_txt_fill_listview.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,"共查询到 "+Late_size+" 条数据",Toast.LENGTH_SHORT).show();
        }else {
            drawer_txt_fill_listview.setVisibility(View.VISIBLE);
            drawer_txt_fill_listview.setText("暂无此人的迟到记录");
        }

    }

    Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            drawer_lin_data.setVisibility(View.GONE);
        }
    };

    /**
     * 设置侧滑栏的通知提醒
     * @param text 提示文字
     * @param saveTime 停留时间，若小于 0 则一直停留   (单位：毫秒)
     */
    public void setDrawerToast(String text,int saveTime){

        drawer_lin_data.setVisibility(View.VISIBLE);
        drawer_txt_data.setText(text);
        if (saveTime > 0){
            mHander.sendEmptyMessageAtTime(1,saveTime);
        }
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_open_hotspot:

                    if (!sharedPreferences.getString("name","-1").equals("-1")) {
                        if (!apManager.isWifiApEnabled()) {

                            apManager.startWifiAp(sharedPreferences.getString("name", "嘿嘿"), sharedPreferences.getString("password", "12345678")
                                    ,sharedPreferences.getBoolean("isOpenAp",false));


                            //如果直接打开热点不成功，则需关闭Wifi后重试
                            if (!apManager.isWifiApEnabled()){
                                wifiManager.closeWifi();
                                apManager.startWifiAp(sharedPreferences.getString("name", "嘿嘿"), sharedPreferences.getString("password", "12345678")
                                        ,sharedPreferences.getBoolean("isOpenAp",false));
                                Toast.makeText(MainActivity.this,"记得关闭自己的数据连接哦",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,ApListResult.class));
                            }else {
                                Toast.makeText(MainActivity.this,"记得关闭自己的数据连接哦",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,ApListResult.class));
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "请关闭热点后重试", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "请填写默认APP_NAME内信息", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.main_btn_setting:
                    startActivity(new Intent(MainActivity.this,Setting.class));
                    break;
                case R.id.main_btn_look_late:
                    drawerLayout.openDrawer(drawerLinearLayout);
                    break;
                case R.id.main_btn_look_database:
                    startActivity(new Intent(MainActivity.this,LookAllData.class));
                    break;
                case R.id.drawer_btn_query:
                    if (drawer_edit.getText().toString().equals("")){
                        initDrawerListview(DatabaseManager.ORDER_BY_TIME_DAY_ASC);
                    }else {
                        queryDrawerListiewData(drawer_edit.getText().toString());
                    }

                    break;
                case R.id.drawer_btn_up:
                    myAnimator.rotatey540X_AnimRun(v);
                    isOnClicked=!isOnClicked;

                    if (isOnClicked){
                        drawer_btn_up.setBackgroundResource(R.drawable.down);
                        initDrawerListview(DatabaseManager.ORDER_BY_TIME_DAY_ASC);
                    }else {
                        drawer_btn_up.setBackgroundResource(R.drawable.up);
                        initDrawerListview(DatabaseManager.ORDER_BY_TIME_DAY_DESC);
                    }

                    break;
                case R.id.drawer_btn_sync:
                    myAnimator.rotate720AnimRun(v);
                    int dataSize = databaseManager.queryAll(DatabaseManager.TABLE_LATE).size();
                    if (lastTimeArrSize != dataSize) {
                        if (!isOnClicked){
                            isOnClicked=!isOnClicked;
                            myAnimator.rotatey540X_AnimRun(findViewById(R.id.drawer_btn_up));
                            drawer_btn_up.setBackgroundResource(R.drawable.down);
                        }

//                        setDrawerToast("更新了 "+(dataSize-lastTimeArrSize)+ " 个新数据",500);
                        initDrawerListview(DatabaseManager.ORDER_BY_TIME_DAY_ASC);

                    }else {
//                        setDrawerToast("暂无新数据",2000);
                        Toast.makeText(MainActivity.this,"暂无新数据",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.drawer_btn_data_finish:
                    drawer_lin_data.setVisibility(View.GONE);
                    break;
                default:break;
            }
        }
    }

    /**
     * 进入app时要执行的网络设置
     */
    private void inAppSetNetwork() {
        super.onResume();

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
            Toast.makeText(MainActivity.this,"记得关闭自己的数据连接哦",Toast.LENGTH_SHORT).show();

        //关闭数据连接
//        mobManager.setDataConnectionState(false);

    }

    @Override
    protected void onDestroy() {
        super.onPause();
        //关闭wifi热点
        apManager.closeWifiAp();

        //恢复进入软件时的wifi状态
        if (Wifi_WifiManager.isInOpenWifi)
            wifiManager.openWifi();

        Log.i("haha","onDestroy 顺利执行 ");
    }
}
