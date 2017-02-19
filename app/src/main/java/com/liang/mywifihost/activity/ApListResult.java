package com.liang.mywifihost.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.mywifihost.R;
import com.liang.mywifihost.Utility.TimeTool;
import com.liang.mywifihost.Utility.Utility;
import com.liang.mywifihost.anim.MyAnimator;
import com.liang.mywifihost.custom.CustomDialog;
import com.liang.mywifihost.custom.CustomPopuwindow;
import com.liang.mywifihost.sqlite.DatabaseManager;
import com.liang.mywifihost.network.Wifi_APManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ApListResult extends AppCompatActivity {

    private EditText edit_class;
    private Button btn_query;
//    private Button btn_sync;
    private Button btn_late;
    private TextView txt_late;
    private TextView txt_change;
    private LinearLayout lin_late;
    private ListView listView_late;
    private ListView listviewAll;
    private TextView view_visbile;
    private ImageView toolbar_back;
    private ImageView toolbar_AutoSync;
    private ImageView toolbar_sync;
    private ImageView toolbar_backall;
    private Switch popuSwitch;

    private Wifi_APManager apManager;
    private DatabaseManager databaseManager;
    private CustomDialog.Builder builder;
    private CustomPopuwindow popuwindow;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MyAnimator myAnimator;

    private ArrayList<HashMap<String, Object>> list_late;
    private ArrayList<HashMap<String,Object>> list_builder;
    private View popuView;
    private String editText_data;
    private int query_count=0;
    private int time_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_list_result);

        //沉浸式状态栏 版本必须大于5.0
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            view_visbile=(TextView)findViewById(R.id.toolbar_visbile);
            view_visbile.setVisibility(View.GONE);
        }

        //热点及WIFI
        apManager = new Wifi_APManager(this);
        databaseManager = new DatabaseManager(this);

        myAnimator=new MyAnimator();

        initView();

    }

    /**
     * 初始化界面
     */
    private void initView(){

        edit_class = (EditText) findViewById(R.id.ap_edit_class);
        btn_query = (Button) findViewById(R.id.ap_btn_query);
//        btn_sync = (Button) findViewById(R.id.sync);
        btn_late = (Button) findViewById(R.id.ap_list_btn_late);
        txt_late = (TextView) findViewById(R.id.ap_list_txt_late);
        txt_change = (TextView) findViewById(R.id.ap_list_txt_change);
        lin_late = (LinearLayout) findViewById(R.id.ap_list_lin_late);
        listviewAll = (ListView) findViewById(R.id.ap_listview);
        listView_late = (ListView) findViewById(R.id.ap_list_listview);
        toolbar_back=(ImageView) findViewById(R.id.toolbar_back);
        toolbar_backall=(ImageView) findViewById(R.id.toolbar_end_3);
        toolbar_AutoSync=(ImageView) findViewById(R.id.toolbar_end_1);
        toolbar_sync=(ImageView) findViewById(R.id.toolbar_end_2);

        toolbar_backall.setImageResource(R.drawable.back_1);
        toolbar_AutoSync.setImageResource(R.drawable.item);
        toolbar_sync.setImageResource(R.drawable.sync_while);
        toolbar_sync.setVisibility(View.VISIBLE);

        sharedPreferences = getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        edit_class.setText(sharedPreferences.getString("query_class", ""));
        if (!sharedPreferences.getBoolean("isAutoSync",false)){
//            btn_sync.setVisibility(View.VISIBLE);
            mHander.removeMessages(1);
        }else {
//            btn_sync.setVisibility(View.GONE);
            mHander.sendEmptyMessage(1);
        }

        btn_query.setOnClickListener(new MyOnClickListener());
//        btn_sync.setOnClickListener(new MyOnClickListener());
        toolbar_back.setOnClickListener(new MyOnClickListener());
        toolbar_backall.setOnClickListener(new MyOnClickListener());
        toolbar_AutoSync.setOnClickListener(new MyOnClickListener());
        toolbar_sync.setOnClickListener(new MyOnClickListener());

        //初始化listview
        initListView(mergeData());
        initPopuWindows();
    }

    Handler mHander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (query_count == 0) {
                        initListView(mergeData());
                    }else if (query_count == 1){
                        query_count++;
                        syncLateListView();
                        if (list_late.size() > 0) {
                            listView_late.setVisibility(View.VISIBLE);
                            btn_late.setVisibility(View.VISIBLE);
                        }
                    }else {
                        syncLateListView();
                    }

                    if (sharedPreferences.getBoolean("isAutoSync",false)){
                        mHander.sendEmptyMessageDelayed(1,1400);
                        myAnimator.rotate720AnimRun(findViewById(R.id.toolbar_end_2));
                    }else if(time_count < 3){
                        time_count++;
                        mHander.sendEmptyMessageDelayed(1,1400);
                        myAnimator.rotate720AnimRun(findViewById(R.id.toolbar_end_2));
                    }else {
                        myAnimator.rotate720AnimRun(findViewById(R.id.toolbar_end_2));
                    }
                    break;
            }
//            Log.i("haha","一键查询界面，自动刷新执行中");
        }
    };

    /**
     * 初始化 R.layout.popu_ap_list_item 界面监听
     */
    private void initPopuWindows(){

        popuView=View.inflate(ApListResult.this,R.layout.popu_ap_list_item,null);
        popuwindow=new CustomPopuwindow(ApListResult.this,popuView,2);
        popuSwitch=(Switch)popuView.findViewById(R.id.popu_ap_list_switch);
        popuSwitch.setChecked(sharedPreferences.getBoolean("isAutoSync",false));
        popuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                editor.putBoolean("isAutoSync",isChecked);
                editor.commit();

//                if (isChecked) {
//                    btn_sync.setVisibility(View.GONE);
//                }else {
//                    btn_sync.setVisibility(View.VISIBLE);
//                }
                popuwindow.dismiss();

                mHander.sendEmptyMessage(1);
            }
        });
    }

    /**
     * 初始化迟到listview
     */
    private void initLateListView(ArrayList<HashMap<String, Object>> list) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(ApListResult.this, list,
                R.layout.ap_list_re_listview_item,
                new String[]{"name", "class", "number"},
                new int[]{R.id.ap_list_item_name, R.id.ap_list_item_class, R.id.ap_list_item_number});
        listView_late.setAdapter(simpleAdapter);

        Utility.setListViewHeightBasedOnChildren(listView_late);


        btn_late.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化listview
     */
    private void initListView(ArrayList<HashMap<String, Object>> list) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(ApListResult.this, list,
                R.layout.ap_list_re_listview_item,
                new String[]{"name", "class", "number"},
                new int[]{R.id.ap_list_item_name, R.id.ap_list_item_class, R.id.ap_list_item_number});
        listviewAll.setAdapter(simpleAdapter);

        Utility.setListViewHeightBasedOnChildren(listviewAll);

        initListOnClick(list);
    }

    /**
     * 刷新查询界面视图列表
     */
    private void syncLateListView(){
        list_late = new ArrayList<>();
        list_late = databaseManager.getRestArrListData(databaseManager.query(
                DatabaseManager.TABLE_IP, DatabaseManager.WHERE_CLASS, new String[]{editText_data}),
                getIpAndClassData(editText_data));

        initLateListView(list_late);

        //班级查询
        initListView(getIpAndClassData(editText_data));

        //计数
        txt_late.setText("以下为迟到人员 （人数为： " + list_late.size()  + " ）");
        txt_change.setText("以下为在线人员 （人数为： " + getIpAndClassData(editText_data).size()+ " ）");
    }

    /**
     * 合并ip数组和数据库查询结果,
     * 若IP和数据库中数据同时存在则显示数据库中结果，否则显示IP
     */
    private ArrayList<HashMap<String, Object>> mergeData() {

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;
        for (int i = 0; i < apManager.getConnectedIP().size(); i++) {
            map = new HashMap<>();
            if (databaseManager.query_isHave(DatabaseManager.TABLE_IP, DatabaseManager.WHERE_IP, apManager.getConnectedIP().get(i))) {
                map = databaseManager.query(DatabaseManager.TABLE_IP, DatabaseManager.WHERE_IP, new String[]{apManager.getConnectedIP().get(i)}).get(0);
            } else {
                map.put("name", apManager.getConnectedIP().get(i));
                map.put("class", "");
                map.put("number", "点击备注");
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 现在在线的人中，中筛选出每个IP和传入字符串后的数据列表
     *
     * @param string_class 传入要循环筛选的数据
     * @return
     */
    private ArrayList<HashMap<String, Object>> getIpAndClassData(String string_class) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < apManager.getConnectedIP().size(); i++) {
            list = databaseManager.query(DatabaseManager.TABLE_IP, DatabaseManager.WHERE_IP_CLASS,
                    new String[]{apManager.getConnectedIP().get(i), string_class});
        }
        return list;
    }

    /**
     * listview 监听
     */
    private void initListOnClick(final ArrayList<HashMap<String, Object>> list) {
        listviewAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //dialog
                builder = new CustomDialog.Builder(ApListResult.this);

                builder.showEditTextName(View.VISIBLE);
                builder.showEditTextClass(View.VISIBLE);
                builder.showEditTextNumber(View.VISIBLE);
                builder.showTextView(View.GONE);

                builder.setEditTextName(list.get(position).get("name").toString());
                builder.setEditTextClass(list.get(position).get("class").toString());
                builder.setEditTextNumber(list.get(position).get("number").toString());

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (databaseManager.query_isHave(DatabaseManager.TABLE_IP,
                                DatabaseManager.WHERE_IP, apManager.getConnectedIP().get(position))){
                        }
                        if (!builder.getEditTextName().toString().equals("")
                                && !builder.getEditTextClass().toString().equals("")
                                && !builder.getEditTextNumber().toString().equals("")) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("name", builder.getEditTextName().toString());
                            map.put("class", builder.getEditTextClass().toString());
                            map.put("number", builder.getEditTextNumber().toString());
                            if (!databaseManager.query_isHave(DatabaseManager.TABLE_IP,
                                    DatabaseManager.WHERE_IP, apManager.getConnectedIP().get(position))) {
                                map.put("ip", apManager.getConnectedIP().get(position));
                                if (databaseManager.insert(DatabaseManager.TABLE_IP, map) == -1)
                                    Toast.makeText(ApListResult.this, "备注失败，请重试", Toast.LENGTH_SHORT).show();
                            } else {
                                if (databaseManager.updata(DatabaseManager.TABLE_IP, map, apManager.getConnectedIP().get(position)) == -1)
                                    Toast.makeText(ApListResult.this, "重新备注失败，请重试", Toast.LENGTH_SHORT).show();
                            }

                            //刷新列表
                            initListView(mergeData());

                            dialog.dismiss();
                        } else {
                            Toast.makeText(ApListResult.this, "填写数据全部为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.create().show();


            }
        });
    }


    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ap_btn_query:
                    if (!edit_class.getText().toString().equals("")) {
                        btn_late.setVisibility(View.GONE);
                        listView_late.setVisibility(View.GONE);
                        lin_late.setVisibility(View.VISIBLE);
//                        btn_sync.setVisibility(View.GONE);

                        time_count = 0;
                        query_count = 1;
                        editText_data=edit_class.getText().toString();
                        
                        //迟到人员列表
                        mHander.sendEmptyMessage(1);

                        //sharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("query_class", edit_class.getText().toString());
                        editor.commit();

                        

                        toolbar_backall.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ApListResult.this, "请填入专业名称", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.sync:
                    initListView(mergeData());
                    break;
                case R.id.ap_list_btn_late:
                    HashMap<String, Object> map;
                    for (int i = 0; i < list_late.size(); i++) {
                        map = new HashMap<>();
                        map.put("time_year_day", TimeTool.getTimeNew().get("year")+"年 "+
                            TimeTool.getTimeNew().get("month")+"月 "+TimeTool.getTimeNew().get("day")+"日");
                        map.put("time_time", TimeTool.getTimeNew().get("hour")+":"+TimeTool.getTimeNew().get("minute"));
                        map.put("name", list_late.get(i).get("name"));
                        map.put("class", list_late.get(i).get("class"));

                        databaseManager.insert(DatabaseManager.TABLE_LATE, map);
                    }

                    Toast.makeText(ApListResult.this,"记录成功",Toast.LENGTH_SHORT).show();
                    btn_late.setVisibility(View.GONE);
                    break;
                case R.id.toolbar_back:
                    mHander.removeMessages(1);
                    finish();
                    break;
                case R.id.toolbar_end_2:
                    if (sharedPreferences.getBoolean("isAutoSync",false)){
                        editor.putBoolean("isAutoSync",false);
                        editor.commit();
                        popuSwitch.setChecked(false);
                        mHander.removeMessages(1);
                    }else {
                        mHander.sendEmptyMessage(1);
                    }
                    break;
                case R.id.toolbar_end_3:
                    toolbar_backall.setVisibility(View.GONE);
                    btn_late.setVisibility(View.VISIBLE);
                    listView_late.setVisibility(View.VISIBLE);
                    lin_late.setVisibility(View.GONE);
//                    if (!sharedPreferences.getBoolean("isAutoSync",false)){
//                        btn_sync.setVisibility(View.VISIBLE);
//                    }else {
//                        btn_sync.setVisibility(View.GONE);
//                    }
                    initListView(mergeData());
                    query_count = 0;
                    txt_change.setText("以下为所有连入wifi人员");
                    break;
                case R.id.toolbar_end_1:
                    popuwindow.showPopupWindow((View) findViewById(R.id.toolbar_end_1).getParent());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        apManager.closeWifiAp();
    }
}
