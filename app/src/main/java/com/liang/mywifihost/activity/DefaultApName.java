package com.liang.mywifihost.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.liang.mywifihost.R;

import static android.R.attr.password;
import static android.R.attr.start;
import static android.os.Build.VERSION_CODES.M;
import static com.liang.mywifihost.R.id.edit_name;
import static com.liang.mywifihost.R.id.edit_password;

public class DefaultApName extends AppCompatActivity {

    private EditText edit_name;
    private ImageView toolbar_back;
    private ImageView img_wraning_name;
    private ImageView img_wraning_password;
    private Switch switch_password_look;
    private Switch switch_ap_open;
    private Switch switch_remember;
    private EditText edit_password;
    private Button btn_save;
    private TextView view_visbile;
    private LinearLayout lin_password;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_ap_name);

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

        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_password=(EditText)findViewById(R.id.edit_password);
        btn_save=(Button)findViewById(R.id.default_btn_save);
        toolbar_back=(ImageView)findViewById(R.id.toolbar_back);
        img_wraning_name=(ImageView)findViewById(R.id.ap_name_warning_name);
        img_wraning_password=(ImageView)findViewById(R.id.ap_name_warning_password);
        switch_password_look=(Switch)findViewById(R.id.ap_name_switch_look);
        switch_ap_open=(Switch)findViewById(R.id.ap_name_switch_ap_open);
        switch_remember=(Switch)findViewById(R.id.ap_name_switch_remember);
        lin_password=(LinearLayout)findViewById(R.id.ap_name_lin_password);

        btn_save.setOnClickListener(new MyOnClickListener());
        toolbar_back.setOnClickListener(new MyOnClickListener());
        switch_remember.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        switch_ap_open.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        switch_password_look.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //sharedpreference
        sharedPreferences=getSharedPreferences("MyKeyValue", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        edit_password.addTextChangedListener(mEditView_password);
        edit_name.addTextChangedListener(mEditView_name);
        switch_ap_open.setChecked(sharedPreferences.getBoolean("isOpenAp",false));
        switch_remember.setChecked(sharedPreferences.getBoolean("isRememberPassword",false));

        //setText
        if (!sharedPreferences.getString("name","-1").equals("-1") ) {
            edit_name.setText(sharedPreferences.getString("name",""));
        }
        if (!sharedPreferences.getString("password","-1").equals("-1")){
            edit_password.setText(sharedPreferences.getString("password",""));
        }
    }

    /**
     * EditText_password 字符变化监听
     */
    TextWatcher mEditView_password=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i("haha",s+" "+start+ " "+count+" "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("haha",s+" "+start+ " "+before+" "+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("haha",s+" ");
            if (s.length()>7 && !edit_name.getText().toString().equals("") ){
                btn_save.setEnabled(true);
            }else {
                btn_save.setEnabled(false);
            }

            if (  s.length()>7){
                img_wraning_password.setVisibility(View.GONE);
            }else {
                img_wraning_password.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * EditText_name 字符变化监听
     */
    TextWatcher mEditView_name=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            Log.i("haha",s+" "+start+ " "+count+" "+after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            Log.i("haha",s+" "+start+ " "+before+" "+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
//            Log.i("haha",s+" ");
            if (s.length() > 0 &&s.length()<10 && edit_password.getText().toString().length() > 7
                    || s.length()>0 && s.length()<10 && sharedPreferences.getBoolean("isOpenAp",false)){
                btn_save.setEnabled(true);
            }else {
                btn_save.setEnabled(false);
            }

            if (s.length()>0 && s.length()<10){
                img_wraning_name.setVisibility(View.GONE);
            }else {
                img_wraning_name.setVisibility(View.VISIBLE);
            }
        }
    };


    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.default_btn_save:

                    if (!edit_name.getText().toString().equals("")) {

                        if (!sharedPreferences.getBoolean("isOpenAp",false)) {
                            if (sharedPreferences.getBoolean("isRememberPassword",false)){
                                editor.putString("password", edit_password.getText().toString());
                            }else {
                                editor.putString("password", "-1");
                            }

                        }else {
                            if (!edit_name.getText().toString().equals(sharedPreferences.getString("name","-1"))){
                                editor.putString("password","-1");
                            }
                        }

                        editor.putString("name", edit_name.getText().toString());
                        editor.commit();

                        finish();
                    } else {
                        Toast.makeText(DefaultApName.this, "名称为空", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.toolbar_back:
                    finish();
                    break;
                case R.id.edit_name:
                    if (edit_name.getText().equals("")){
                        btn_save.setEnabled(false);
                    }else {
                        btn_save.setEnabled(true);
                    }
                    break;
                default:break;
            }
        }
    }

    private class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.ap_name_switch_ap_open:

                    editor.putBoolean("isOpenAp",isChecked);
                    editor.commit();
                    if (isChecked){
                        switch_remember.setVisibility(View.GONE);
                        lin_password.setVisibility(View.GONE);
                    }else {
                        switch_remember.setVisibility(View.VISIBLE);
                        lin_password.setVisibility(View.VISIBLE);
                    }

                    if(isChecked==false && !edit_name.getText().equals("")
                            && edit_password.getText().length()>7){
                        btn_save.setEnabled(true);
                    }else if ( isChecked==true && !edit_name.getText().equals("")){
                        btn_save.setEnabled(true);
                    }else {
                        btn_save.setEnabled(false);
                    }

                    break;
                case R.id.ap_name_switch_look:
                    if(isChecked){
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    }else {
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }

                    break;
                case R.id.ap_name_switch_remember:
                    editor.putBoolean("isRememberPassword",isChecked);
                    editor.commit();
                    break;
                default:
                    break;
            }
        }
    }
}
