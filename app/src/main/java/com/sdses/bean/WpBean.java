package com.sdses.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-09.
 */

public class WpBean implements Serializable {

    private String wpreCode;
    private String wpreName;
    private String wpreSccj;
    private String wpreWplb;
    private String wpreScrq;
    private String wpreSpgb;

    public String getWpreCode() {
        return wpreCode;
    }

    public void setWpreCode(String wpreCode) {
        this.wpreCode = wpreCode;
    }

    public String getWpreName() {
        return wpreName;
    }

    public void setWpreName(String wpreName) {
        this.wpreName = wpreName;
    }

    public String getWpreSccj() {
        return wpreSccj;
    }

    public void setWpreSccj(String wpreSccj) {
        this.wpreSccj = wpreSccj;
    }

    public String getWpreWplb() {
        return wpreWplb;
    }

    public void setWpreWplb(String wpreWplb) {
        this.wpreWplb = wpreWplb;
    }

    public String getWpreScrq() {
        return wpreScrq;
    }

    public void setWpreScrq(String wpreScrq) {
        this.wpreScrq = wpreScrq;
    }

    public String getWpreSpgb() {
        return wpreSpgb;
    }

    public void setWpreSpgb(String wpreSpgb) {
        this.wpreSpgb = wpreSpgb;
    }
}
