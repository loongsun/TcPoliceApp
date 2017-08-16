/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.response.model;

import com.alibaba.fastjson.annotation.JSONField;

import loongsun.com.facetest.reconova.api.response.ApiModel;

/**
 * 人脸检测结果，单条记录
 */
public class FaceDetectResult extends ApiModel {
	
	@JSONField(name = "face_image")
	private String faceImage;
	private String feature;
	private int age;
	private int gender;
	
	@JSONField(name = "face_rect")
	private FaceRect faceRect;
	
	public String getFaceImage() {
		return faceImage;
	}
	
	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	
	public String getFeature() {
		return feature;
	}
	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public int getGender() {
		return gender;
	}
	
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public FaceRect getFaceRect() {
		return faceRect;
	}
	
	public void setFaceRect(FaceRect faceRect) {
		this.faceRect = faceRect;
	}
}
