package com.tc.bean;

/**
 * ���ߣ��º� on 2017/8/23.
 * �汾��v1.0
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