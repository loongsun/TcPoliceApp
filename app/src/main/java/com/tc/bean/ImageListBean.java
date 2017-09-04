package com.tc.bean;

/**
 * ×÷Õß£º³Âº× on 2017/8/23.
 * °æ±¾£ºv1.0
 */


public class ImageListBean {
    private String imageUrl;
    private String imageName;



    public ImageListBean(String item,String name) {

        this.imageUrl = item;
        this.imageName = name;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}