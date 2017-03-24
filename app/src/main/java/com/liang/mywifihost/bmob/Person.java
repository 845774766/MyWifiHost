package com.liang.mywifihost.bmob;


import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {

    private String ip;
    private String name;
    private String class_major;
    private String number;

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return ip;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setClass_major(String class_major) {
        this.class_major = class_major;
    }
    public String getClass_major() {
        return class_major;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }
}