package com.tc.bean;

import org.json.JSONObject;

/**
 * ×÷Õß£º³Âº× on 2017/8/23.
 * °æ±¾£ºv1.0
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