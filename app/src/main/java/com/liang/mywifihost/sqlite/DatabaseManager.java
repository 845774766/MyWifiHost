package com.liang.mywifihost.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static android.R.attr.data;
import static android.R.attr.focusable;
import static android.R.attr.name;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * Created by 广靓 on 2017/2/5.
 */

public class DatabaseManager {

    //数据库选择的一些条件，便于使用
    public static String[] TABLE_QUERY_COLUMNS_IP=new String[]{"ip","name","class","number"};
    public static String[] TABLE_QUERY_COLUMNS_LATE=new String[]{"name","class","time_year_day","time_time"};
    public static String TABLE_IP=DatabaseHelper.TABLE_ip;
    public static String TABLE_LATE=DatabaseHelper.TABLE_late;
    public static String WHERE_IP="ip=?";
    public static String WHERE_NAME="name=?";
    public static String WHERE_CLASS="class=?";
    public static String WHERE_NUMBER="number=?";
    public static String WHERE_TIME_DAY="time_year_day=?";
    public static String WHERE_TIME_TIME="time_time=?";
    public static String WHERE_TIMEDAY_NAME="time_year_day=? and name=?";
    public static String WHERE_IP_CLASS="ip=? and class=?";
    public static String ORDER_BY_TIME_DAY_ASC="time_year_day asc";
    public static String ORDER_BY_TIME_DAY_DESC="time_year_day desc";
    public static String ORDER_BY_TIME_DAY_TIME_ASC="time_time asc";
    public static String ORDER_BY_TIME_DAY_TIME_DESC="time_time desc";
    public static String ORDER_BY_TIME_ID_DESC="id desc";
    public static String ORDER_BY_TIME_ID_ASC="id asc";
    public static String ORDER_BY_CLASS_ASC="class asc";

    private Context mContext;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context){
        this.mContext=context;

        databaseHelper=new DatabaseHelper(context);
        database=databaseHelper.getWritableDatabase();
    }

    /**
     * 去除map中的杂质数据，防止应用时出错
     * @param map 未处理的数据
     * @return 去除后的数据
     */
    public ContentValues getContentValues(HashMap<String,Object> map){
        ContentValues values=new ContentValues();
        if (map.containsKey("ip"))
            if (!(map.get("ip").toString()).equals(""))
                values.put("ip",map.get("ip").toString());
        if (map.containsKey("name"))
            if (!(map.get("name").toString()).equals(""))
                values.put("name",map.get("name").toString());
        if (map.containsKey("class"))
            if (!(map.get("class").toString()).equals(""))
                values.put("class",map.get("class").toString());
        if (map.containsKey("number"))
            if (!(map.get("number").toString()).equals(""))
                values.put("number",map.get("number").toString());
        if (map.containsKey("id"))
            if (!(map.get("id").toString()).equals(""))
                values.put("id",map.get("id").toString());
        if (map.containsKey("time_year_day"))
            if (!(map.get("time_year_day").toString()).equals(""))
                values.put("time_year_day",map.get("time_year_day").toString());
        if (map.containsKey("time_time"))
            if (!(map.get("time_time").toString()).equals(""))
                values.put("time_time",map.get("time_time").toString());

        return values;
    }

    /**
     * 插入数据
     * @param map 数据信息
     * @return 失败返回-1
     */
    public long insert(String table,HashMap<String,Object> map){
        return database.insert(table,null,getContentValues(map));
    }

    /**
     * 更新数据
     * @param map 要更新的数据
     * @param where_ip 查询条件
     * @return 更新数据的行数
     */
    public int updata(String table,HashMap<String,Object> map, String where_ip){
        return database.update(table,getContentValues(map),"ip=?", new String[]{where_ip});
    }

    /**
     * 根 根据条件查询
     * @param table 数据库表名
     * @param columns 要查询的列名 （静态变量）
     * @param where_what 选择条件  （静态变量）
     * @param where_args_what 选择条件实参
     * @param orderBy 排序字符串
     * @return 以传入表名为键的 ArrayList<HashMap<String,Object>>
     */
    public ArrayList<HashMap<String,Object>> query(String table,String[] columns,String where_what,String[] where_args_what,String orderBy){

        Cursor cursor;
        cursor= database.query(table,columns,where_what,where_args_what,null,null,orderBy);

        return cursorToArrList(cursor);
    }

    /**
     * 根 根据条件查询
     * @param table 数据库表名
     * @param columns 要查询的列名 （静态变量）
     * @param where_what 选择条件  （静态变量）
     * @param where_args_what 选择条件实参
     * @return 以传入表名为键的 ArrayList<HashMap<String,Object>>
     */
    public ArrayList<HashMap<String,Object>> query(String table,String[] columns,String where_what,String[] where_args_what){

        Cursor cursor;
        cursor= database.query(table,columns,where_what,where_args_what,null,null,null);

        return cursorToArrList(cursor);
    }

    /**
     * 根据条件进行查询,并自动选择默认主方法的columns
     * @param table 数据库表名
     * @param where_what 选择条件  （静态变量）
     * @param where_args_what 选择条件实参
     */
    public ArrayList<HashMap<String,Object>> query(String table,String where_what,String[] where_args_what){
        if (table.equals(TABLE_IP))
            return query(TABLE_IP,TABLE_QUERY_COLUMNS_IP,where_what,where_args_what);
        else
            return query(TABLE_LATE,TABLE_QUERY_COLUMNS_LATE,where_what,where_args_what);
    }

    /**
     * 判断是否存在给定信息
     * @param table 表名
     * @param where_what 选择条件
     * @param where_args_what 选择实参
     * @return 有，返回true
     */
    public boolean query_isHave(String table,String where_what,String where_args_what){
        return query(table,where_what, new String[]{where_args_what}).size() > 0;
    }

    /**
     * 查询全部数据
     */
    public ArrayList<HashMap<String,Object>> queryAll(String table){
        return query(table,null,null,null);
    }

    /**
     * 将指针类型数据转换为ArrayList类型
     * @param cursor
     * @return
     */
    public ArrayList<HashMap<String,Object>> cursorToArrList(Cursor cursor){
        ArrayList<HashMap<String,Object>> list=new ArrayList<>();
        HashMap<String,Object> map;
        while (cursor.moveToNext()){
            map=new HashMap<>();
            for (int i=0;i<cursor.getColumnCount();i++) {
                map.put(cursor.getColumnName(i),cursor.getString(cursor.getColumnIndexOrThrow(cursor.getColumnName(i))));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 删除数据
     * @param where_what 根据所给条件删除所在行
     * @param where_args_what 条件实参
     * @return 受影响的行
     */
    public int delete(String table,String where_what,String where_args_what){
        return database.delete(table,where_what, new String[]{where_args_what});
    }

    //以下为数据库常用数据操作方法

    /**
     * 筛选出 (多的arrlist - 少的arrlist) 后的数据
     *
     * @param big_list   多的arrlist
     * @param small_list 少的arrlist
     * @return (多的arrlist - 少的arrlist) 后的数据
     */
    public ArrayList<HashMap<String, Object>> getRestArrListData(
            ArrayList<HashMap<String, Object>> big_list, ArrayList<HashMap<String, Object>> small_list) {

        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        if (big_list.size() > small_list.size()) {
            boolean isHaveTheIP;
            for (int i = 0; i < big_list.size(); i++) {
                isHaveTheIP = false;
                for (int k = 0; k < small_list.size(); k++) {
                    if (big_list.get(i).get("ip").equals(small_list.get(k).get("ip"))) {
                        isHaveTheIP = true; //如果大数据中含有小数据则记录true
                    }
                }

                if (!isHaveTheIP) {
                    list.add(big_list.get(i));
                }
            }
        }
        return list;
    }

    /**
     * 将传入数据进行去重
     * 不改变传入数据顺序
     * @param list 传入数据
     * @param RepeatingDataString 要去重复的HashMap的Key
     * @return 字符串数组清单
     */
    public ArrayList<String> getRepeatingData(ArrayList<HashMap<String,Object>> list, String RepeatingDataString){
        ArrayList<String> getList=new ArrayList<>();
        if (list.size()>1) {
            String temp = null;
            for (int i = 0; i < list.size() - 1; i++) {

                temp = list.get(i).get(RepeatingDataString).toString();

                if (i == 0)
                    getList.add(temp);

                if (!list.get(i + 1).get(RepeatingDataString).equals(temp)) {
                    getList.add(list.get(i + 1).get(RepeatingDataString).toString());
                }
            }
        }else if (list.size()==1){
            getList.add(list.get(0).get(RepeatingDataString).toString());
        }
        return getList;
    }
}
