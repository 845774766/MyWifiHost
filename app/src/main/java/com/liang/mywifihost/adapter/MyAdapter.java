package com.liang.mywifihost.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.liang.mywifihost.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    private Context context = null;
    private LayoutInflater inflater = null;
    private List<HashMap<String, Object>> list = null;

    public MyAdapter(Context context, ArrayList<HashMap<String, Object>> checkList) {

        this.context = context;
        this.list = checkList;
        inflater = LayoutInflater.from(context);
        init();
        Log.i("haha",checkList.size()+" checkList.size()");
    }

    // 初始化 设置所有checkbox都为未选择
    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHolder holder = new ViewHolder();

        if (view == null) {
            view = inflater.inflate(R.layout.bmob_down_load_listview_item, null);
            holder.tv = (TextView) view.findViewById(R.id.bmob_down_load_listview_item_txt_count);
            holder.cb = (CheckBox) view.findViewById(R.id.bmob_down_load_listview_item_checkbox);
            view.setTag(holder);
        }else {
            view.getTag();
        }

        HashMap<String, Object> map = list.get(position);
        if (map != null) {
            if (map.containsKey("multClass"))
                holder.cb.setText(map.get("multClass").toString());
            if (map.containsKey("multClass"))
                holder.tv.setText(map.get("class_number").toString());
        }
        Log.i("haha",list.get(position).get("multClass").toString()+"  "+list.get(position).get("class_number").toString());
        holder.cb.setChecked(isSelected.get(position));
        return view;
    }

    public class ViewHolder {
        public TextView tv = null;
        public CheckBox cb = null;
    }
}