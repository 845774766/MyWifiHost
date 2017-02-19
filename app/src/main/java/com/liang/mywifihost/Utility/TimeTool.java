package com.liang.mywifihost.Utility;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by 广靓 on 2017/2/9.
 */

public class TimeTool {

    /**
     * 获得系统时间 年、月、日、小时、分钟
     * @return HashMap
     */
    public static HashMap<String,Object> getTimeNew(){
        HashMap<String,Object> map=new HashMap<>();
        Calendar calendar=Calendar.getInstance();
        map.put("year",calendar.get(Calendar.YEAR));
        map.put("month",calendar.get(Calendar.MONTH));
        map.put("day",calendar.get(Calendar.DAY_OF_MONTH));
        map.put("hour",calendar.get(Calendar.HOUR_OF_DAY));
        map.put("minute",calendar.get(Calendar.MINUTE));
        return map;
    }
}
