package com.sdses.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-10.
 */

public class FaceBean implements Serializable {
    private String faceReName="";
    private String faceReSex="";
    private String faceReDbType="";
    private String faceReId2Num="";
    private String faceReRyMs="";
    private String faceReSim="";
    private String faceUro="";

    public String getFaceUro() {
        return faceUro;
    }

    public void setFaceUro(String faceUro) {
        this.faceUro = faceUro;
    }

    public String getFaceReName() {
        return faceReName;
    }

    public void setFaceReName(String faceReName) {
        this.faceReName = faceReName;
    }

    public String getFaceReSex() {
        return faceReSex;
    }

    public void setFaceReSex(String faceReSex) {
        this.faceReSex = faceReSex;
    }

    public String getFaceReDbType() {
        return faceReDbType;
    }

    public void setFaceReDbType(String faceReDbType) {
        this.faceReDbType = faceReDbType;
    }

    public String getFaceReId2Num() {
        return faceReId2Num;
    }

    public void setFaceReId2Num(String faceReId2Num) {
        this.faceReId2Num = faceReId2Num;
    }

    public String getFaceReRyMs() {
        return faceReRyMs;
    }

    public void setFaceReRyMs(String faceReRyMs) {
        this.faceReRyMs = faceReRyMs;
    }

    public String getFaceReSim() {
        return faceReSim;
    }

    public void setFaceReSim(String faceReSim) {
        this.faceReSim = faceReSim;
    }
}
