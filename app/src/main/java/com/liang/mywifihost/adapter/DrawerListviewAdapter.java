package com.liang.mywifihost.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liang.mywifihost.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by 广靓 on 2017/2/9.
 */

public class DrawerListviewAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    private ArrayList<HashMap<String,Object>> data = new ArrayList<>();
    HashMap<String,Object> map;
    private LayoutInflater inflater;
    private TreeSet<Integer> set = new TreeSet<Integer>();

    public DrawerListviewAdapter(Context context){
        inflater=LayoutInflater.from(context);
    }

    public void addItem(HashMap<String,Object> item){
        data.add(item);
    }

    public void addSeparatorItem(String item){
        map=new HashMap<>();
        map.put("time",item);
        data.add(map);

        set.add(data.size() - 1);
    }

    public int getItemViewType(int position){
        return set.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type =getItemViewType(position);
        if (convertView == null){
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.drawer_listview_item_2,null);
                    holder.item_2_txt_name=(TextView)convertView.findViewById(R.id.drawer_listview_item_2_name);
                    holder.item_2_txt_class=(TextView)convertView.findViewById(R.id.drawer_listview_item_2_class);
                    holder.item_2_txt_time=(TextView)convertView.findViewById(R.id.drawer_listview_item_2_time);
                    break;
                case TYPE_SEPARATOR:
                    convertView= inflater.inflate(R.layout.drawer_listview_item_1,null);
                    holder.item_1_txt=(TextView)convertView
                            .findViewById(R.id.drawer_listview_item_1_text);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (data.get(position).containsKey("class"))
            holder.item_2_txt_class.setText(data.get(position).get("class").toString());
        if (data.get(position).containsKey("name"))
            holder.item_2_txt_name.setText(data.get(position).get("name").toString());
        if (data.get(position).containsKey("time_time"))
            holder.item_2_txt_time.setText(data.get(position).get("time_time").toString());
        if (data.get(position).containsKey("time"))
            holder.item_1_txt.setText(data.get(position).get("time").toString());
        return convertView;
    }

    public static class ViewHolder {
        public TextView item_1_txt;
        public TextView item_2_txt_class;
        public TextView item_2_txt_name;
        public TextView item_2_txt_time;
    }
}
