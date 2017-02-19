package com.liang.mywifihost.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.liang.mywifihost.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.liang.mywifihost.R.id.toolbar_back;

public class Setting extends AppCompatActivity {

    private Switch swt_close_wifi;
    private TextView view_visbile;
    private Switch swt_record_wifi;
    private Switch swt_prompt_mobile;
    private ImageView toolbar_back;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        sharedPreferences=getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();


        swt_close_wifi=(Switch)findViewById(R.id.setting_close_wifi);
        swt_prompt_mobile=(Switch)findViewById(R.id.setting_prompt_mobile);
        swt_record_wifi=(Switch)findViewById(R.id.setting_record_network);
        toolbar_back=(ImageView)findViewById(R.id.toolbar_back);

        swt_close_wifi.setChecked(sharedPreferences.getBoolean("close_wifi",false));
        swt_prompt_mobile.setChecked(sharedPreferences.getBoolean("prompt_mobile",false));
        swt_record_wifi.setChecked(sharedPreferences.getBoolean("record_network",false));

        swt_close_wifi.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        swt_prompt_mobile.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        swt_record_wifi.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        toolbar_back.setOnClickListener(new MyOnClickListener());
    }

    /**
     * switch监听类
     */
    private class MyOnCheckChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.setting_close_wifi:
                    editor.putBoolean("close_wifi",isChecked);
                    editor.commit();
                    break;
                case R.id.setting_prompt_mobile:
                    editor.putBoolean("prompt_mobile",isChecked);
                    editor.commit();
                    break;
                case R.id.setting_record_network:
                    editor.putBoolean("record_network",isChecked);
                    editor.commit();
                    break;
                default:break;
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.toolbar_back:
                    finish();
                    break;
                default:break;
            }
        }
    }

}
