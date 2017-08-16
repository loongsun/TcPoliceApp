/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.response.model;

import com.alibaba.fastjson.annotation.JSONField;

import loongsun.com.facetest.reconova.api.response.ApiModel;

/**
 * 人脸框位置
 */
public class FaceRect extends ApiModel {
	
	@JSONField(name = "top")
	private int top;
	
	@JSONField(name = "left")
	private int left;
	
	@JSONField(name = "bottom")
	private int bottom;
	
	@JSONField(name = "right")
	private int right;
	
	public int getTop() {
		return top;
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	
	public int getRight() {
		return right;
	}
	
	public void setRight(int right) {
		this.right = right;
	}
}
