package com.tc.bean;

/**
 * ���ߣ��º� on 2017/8/23.
 * �汾��v1.0
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