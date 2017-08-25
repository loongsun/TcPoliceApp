package com.tc.bean;

/**
 * ×÷Õß£º³Âº× on 2017/8/23.
 * °æ±¾£ºv1.0
 */


public class ImageListBean {
    private String imageUrl;



    public ImageListBean(String item) {

        this.imageUrl = item;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}