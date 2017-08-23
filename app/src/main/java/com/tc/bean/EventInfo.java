package com.tc.bean;

import java.io.Serializable;

/**
 * Created by zhao on 17-8-19.
 */

public class EventInfo implements Serializable {

    public String wName;
    public String wNum;
    public String wType;
    public String wTime;
    public String wX;
    public String wY;
    public String wAddress;
    public String wTel;
    public String wPerson;

    public boolean isCrime(){
        return "刑事案件".equals(wType);
    }

    public boolean isCivil(){
        return "民事案件".equals(wType);
    }



}
