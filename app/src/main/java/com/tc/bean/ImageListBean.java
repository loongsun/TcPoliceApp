package com.tc.bean;

import org.json.JSONObject;

/**
 * ���ߣ��º� on 2017/8/23.
 * �汾��v1.0
 */


public class ImageListBean {
    private String imageUrl;



    public ImageListBean(JSONObject item) {

        this.imageUrl = item.optString("imageUrl");

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}