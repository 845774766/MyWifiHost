package com.liang.mywifihost.activity;

import android.graphics.Color;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.mywifihost.R;
import com.liang.mywifihost.adapter.MyAdapter;
import com.liang.mywifihost.bmob.AllClassData;
import com.liang.mywifihost.bmob.BmobManager;
import com.liang.mywifihost.custom.SFProgrssDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class BmobDownLoadActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView view_visbile;
    private ImageView toolbar_back;
    private ImageView toolbar_ok;
    private ListView bmobDownLoadListview;
    private TextView bmobDownLoadTextview;

    private ArrayList<HashMap<String,Object>> list_class;
    private ArrayList<String> list_choose;
    private HashMap<String,Object> map1;
    private MyAdapter adapter;
    private BmobManager bmobManager;
    private BmobQuery<AllClassData> query;

    private SFProgrssDialog m_customProgrssDialog;
    public static BmobDownLoadActivity instances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmob_down_load);
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

        showCustomProgrssDialog("网络数据加载中...");
        bmobManager=new BmobManager(this);

        instances=this;

        bmobDownLoadListview=(ListView) findViewById(R.id.bmob_down_load_listview);
        bmobDownLoadTextview=(TextView) findViewById(R.id.bmob_down_load_text);
        toolbar_back=(ImageView) findViewById(R.id.toolbar_back);
        toolbar_ok=(ImageView) findViewById(R.id.toolbar_end_1);

        toolbar_back.setOnClickListener(this);
        toolbar_ok.setOnClickListener(this);
        toolbar_ok.setImageResource(R.drawable.ok);

        showCheckBoxListView();
    }

    // 显示带有checkbox的listview
    public void showCheckBoxListView() {

        list_class = new ArrayList<>();
        list_choose=new ArrayList<>();
        query = new BmobQuery<AllClassData>();
        query.addWhereEqualTo("theSame", 1);
//        query.setLimit(50); //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.findObjects(new FindListener<AllClassData>() {
            @Override
            public void done(List<AllClassData> object, BmobException e) {
                if(e==null){
                    for (int i=0;i<object.size();i++) {
                        map1=new HashMap<>();
                        map1.put("class_number",object.get(i).getClass_number());
                        map1.put("multClass",object.get(i).getMultClass());
                        map1.put("createdAt",object.get(i).getCreatedAt());
                        list_class.add(map1);
                    }

                    //初始化listview
                    if (list_class!=null) {
                        if (list_class.size() == 0){
                            bmobDownLoadListview.setVisibility(View.GONE);
                        }else {
                            bmobDownLoadListview.setVisibility(View.VISIBLE);
                            if (!list_class.get(0).containsKey("error")) {
                                adapter = new MyAdapter(BmobDownLoadActivity.this, list_class);
                                bmobDownLoadListview.setAdapter(adapter);
                            } else {
                                Toast.makeText(BmobDownLoadActivity.this, "加在网络数据" + list_class.get(0).get("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(BmobDownLoadActivity.this,"查询时，出错误了，，，",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(BmobDownLoadActivity.this,"出错误了："+e.getMessage()+","+e.getErrorCode(),Toast.LENGTH_SHORT).show();
                }

                hideCustomProgressDialog();
            }
        });

        bmobDownLoadListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) view.getTag();
                holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态
                MyAdapter.isSelected.put(position, holder.cb.isChecked()); // 同时修改map的值保存状态
                if (holder.cb.isChecked() == true) {
                    if (!list_choose.contains(position+""))
                        list_choose.add(position+"");
                } else {
                    if (list_choose.contains(position+""))
                        list_choose.remove(position+"");
                }
            }
        });

    }

    /**
     * loading 开始
     * @param msg
     */
    public final void showCustomProgrssDialog(String msg) {
        if (null == m_customProgrssDialog)
            m_customProgrssDialog = SFProgrssDialog
                    .createProgrssDialog(this);
        if (null != m_customProgrssDialog) {
            m_customProgrssDialog.setMessage(msg);
            m_customProgrssDialog.show();
            m_customProgrssDialog.setCancelable(false);
        }
    }

    /**
     * loading 结束
     */
    public final void hideCustomProgressDialog() {
        if (null != m_customProgrssDialog) {
            m_customProgrssDialog.dismiss();
            m_customProgrssDialog = null;
        }
    }

        @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_end_1:
                showCustomProgrssDialog("下载中..");
                if (list_choose.size() != 0){
                    for (int i=0;i<list_choose.size();i++){
                        bmobManager.downLoadToDatabase("class_major",list_class.get(Integer.parseInt(list_choose.get(i))).get("multClass"));
                    }

                }else {
                    Toast.makeText(BmobDownLoadActivity.this,"您还没有勾选下载项呢哦",Toast.LENGTH_SHORT).show();
                }

                break;
            default:break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        hideCustomProgressDialog();
    }

}
