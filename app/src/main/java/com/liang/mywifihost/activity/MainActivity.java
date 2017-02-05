package com.liang.mywifihost.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liang.mywifihost.R;
import com.liang.mywifihost.wifi.WifiAPManager;
import com.liang.mywifihost.wifi.WifiManager;

public class MainActivity extends Activity {

    private EditText edit_name;
    private EditText edit_password;
    private Button Open_Ap;
    private Button query_list;

    private WifiAPManager apManager;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_password=(EditText)findViewById(R.id.edit_password);
        Open_Ap=(Button)findViewById(R.id.open_hotspot);
        query_list=(Button)findViewById(R.id.query_list);
        wifiManager=new WifiManager(this);
        apManager=new WifiAPManager(this);

        Open_Ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.isWifiActive()){
                    Toast.makeText(MainActivity.this,"请关闭wwifi",Toast.LENGTH_SHORT).show();
                }else {
                    if (!apManager.isWifiApEnabled()){
                        apManager.startWifiAp(edit_name.getText().toString(),edit_password.getText().toString());
                    }else {
                        Toast.makeText(MainActivity.this,"热点已打开",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        query_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<apManager.getConnectedIP().size();i++){
                    Log.i("haha",apManager.getConnectedIP().get(i));
                }
            }
        });

    }
}
