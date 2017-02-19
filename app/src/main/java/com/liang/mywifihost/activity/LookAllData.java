package com.liang.mywifihost.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.mywifihost.R;
import com.liang.mywifihost.custom.CustomDialog;
import com.liang.mywifihost.custom.CustomPopuwindow;
import com.liang.mywifihost.sqlite.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;

import static com.liang.mywifihost.R.id.toolbar_ed_query;
import static com.liang.mywifihost.R.id.toolbar_end_1_ed;

public class LookAllData extends AppCompatActivity {

    private TextView view_visbile;
    private ImageView toolbar_back;
    private ImageView toolbar_add;
    private ImageView toolbar_query;
    private EditText toolbar_edittext;
    private ListView listView;
    private TextView txt_fillListView;

    private DatabaseManager databaseManager;
    private CustomPopuwindow popuwindow;
    private CustomDialog.Builder builder;

    private ArrayList<HashMap<String,Object>> list;
    private   HashMap<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_all_data);

        //沉浸式状态栏 版本必须大于5.0
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            view_visbile=(TextView)findViewById(R.id.toolbar_visbile_ed);
            view_visbile.setVisibility(View.GONE);
        }


        initView();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        toolbar_back=(ImageView) findViewById(R.id.toolbar_back_ed);
        toolbar_query=(ImageView) findViewById(R.id.toolbar_ed_query);
        toolbar_add=(ImageView) findViewById(R.id.toolbar_end_1_ed);
        toolbar_edittext=(EditText)findViewById(R.id.toolbar_edittext);
        listView=(ListView)findViewById(R.id.all_data_listview);
        txt_fillListView=(TextView)findViewById(R.id.all_data_txt_fill_list);

        toolbar_add.setImageResource(R.drawable.add);

        databaseManager=new DatabaseManager(this);

        toolbar_back.setOnClickListener(new MyOnClickListener());
        toolbar_query.setOnClickListener(new MyOnClickListener());
        toolbar_add.setOnClickListener(new MyOnClickListener());

        initListView(databaseManager.query(DatabaseManager.TABLE_IP,DatabaseManager.TABLE_QUERY_COLUMNS_IP
                ,null,null,DatabaseManager.ORDER_BY_CLASS_ASC),"");

    }

    /**
     * 初始化listView
     * @param list 列表要传入的数据
     */
    private void initListView(final ArrayList<HashMap<String,Object>> list,String ToastType){

        SimpleAdapter simpleAdapter=new SimpleAdapter(LookAllData.this,list,
                R.layout.all_data_listview_item,new String[]{"name","class","number"},
                new int[]{R.id.all_data_item_name,R.id.all_data_item_class,R.id.all_data_item_number});
        listView.setAdapter(simpleAdapter);

        if (list.size()==0){
            txt_fillListView.setVisibility(View.VISIBLE);
        }else {
            txt_fillListView.setVisibility(View.GONE);
        }

        if (ToastType.equals("add")) {
            Toast.makeText(LookAllData.this, "添加数据成功", Toast.LENGTH_SHORT).show();
        }else if (ToastType.equals("updata")){
            Toast.makeText(LookAllData.this, "更新数据成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(LookAllData.this, "共搜索出 " + list.size() + " 个结果", Toast.LENGTH_SHORT).show();
        }

        //ListView监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                builder=new CustomDialog.Builder(LookAllData.this);
                builder.setEditTextIp(list.get(position).get("ip").toString());
                builder.setEditTextName(list.get(position).get("name").toString());
                builder.setEditTextClass(list.get(position).get("class").toString());
                builder.setEditTextNumber(list.get(position).get("number").toString());

                builder.showEditTextIp(View.VISIBLE);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!builder.getEditTextIp().equals("") || !builder.getEditTextName().equals("") ||
                                !builder.getEditTextClass().equals("") || !builder.getEditTextNumber().equals("")) {

                            map = new HashMap<String, Object>();
                            map.put("ip", builder.getEditTextIp());
                            map.put("name", builder.getEditTextName());
                            map.put("class", builder.getEditTextClass());
                            map.put("number", builder.getEditTextNumber());
                            databaseManager.updata(DatabaseManager.TABLE_IP, map, builder.getEditTextIp().toString());
                            dialog.dismiss();

                            initListView(databaseManager.query(DatabaseManager.TABLE_IP,DatabaseManager.TABLE_QUERY_COLUMNS_IP
                                    ,null,null,DatabaseManager.ORDER_BY_CLASS_ASC),"updata");
                        }else {
                            Toast.makeText(LookAllData.this,"部分数据为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
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
                case R.id.toolbar_back_ed:
                    finish();
                    break;
                case toolbar_ed_query:
                    if (toolbar_edittext.getText().toString().equals("")){
                        initListView(databaseManager.queryAll(DatabaseManager.TABLE_IP),"");
                    }else {
                        initListView(databaseManager.query(DatabaseManager.TABLE_IP,
                                DatabaseManager.WHERE_CLASS, new String[]{toolbar_edittext.getText().toString()}),"");
                    }

                    break;
                case toolbar_end_1_ed:
                    builder=new CustomDialog.Builder(LookAllData.this);
                    builder.showEditTextIp(View.VISIBLE);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!databaseManager.query_isHave(DatabaseManager.TABLE_IP,
                                    DatabaseManager.WHERE_IP,builder.getEditTextIp().toString())){

                                if (!builder.getEditTextIp().equals("") || !builder.getEditTextName().equals("") ||
                                        !builder.getEditTextClass().equals("") || !builder.getEditTextNumber().equals("")) {

                                    map = new HashMap<String, Object>();
                                    map.put("ip", builder.getEditTextIp());
                                    map.put("name", builder.getEditTextName());
                                    map.put("class", builder.getEditTextClass());
                                    map.put("number", builder.getEditTextNumber());
                                    databaseManager.insert(DatabaseManager.TABLE_IP,map);
                                    dialog.dismiss();

                                    initListView(databaseManager.query(DatabaseManager.TABLE_IP,DatabaseManager.TABLE_QUERY_COLUMNS_IP
                                            ,null,null,DatabaseManager.ORDER_BY_CLASS_ASC),"add");
                                }else {
                                    Toast.makeText(LookAllData.this,"部分数据为空",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                               Toast.makeText(LookAllData.this,"添加不成功，数据库中已有此数据",Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
                default:
                    break;
            }
        }
    }
}
