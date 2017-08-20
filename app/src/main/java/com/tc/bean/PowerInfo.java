package com.tc.bean;

import java.io.Serializable;

/**
 * Created by zhao on 17-8-19.
 */

public class PowerInfo implements Serializable{

    public String powerType;//警车民警
    public String powerX; // 坐标 经度
    public String powerY; // 维度

    public boolean isPoliceType(){
        return "民警".equals(powerType);
    }

    public boolean isCarType(){
        return "警车".equals(powerType);
    }


}
