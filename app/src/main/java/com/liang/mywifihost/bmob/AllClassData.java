package com.liang.mywifihost.bmob;


import cn.bmob.v3.BmobObject;

/**
 * 此类是为解决bmob数据库中查询所有专业名单
 * 而另建的一张表便于高效查询和简单
 *
 * Created by 广靓 on 2017/2/20.
 */

public class AllClassData extends BmobObject {

    private int theSame;  //相同的数据
    private String multClass;   //不同的专业
    private Integer class_number;

    public void setTheSame(int theSame) {
        this.theSame = theSame;
    }

    public int getTheSame() {
        return theSame;
    }

    public void setMultClass(String multClass) {
        this.multClass = multClass;
    }

    public String getMultClass() {
        return multClass;
    }

    public void setClass_number(Integer class_number) {
        this.class_number = class_number;
    }

    public Integer getClass_number() {
        return class_number;
    }
}
