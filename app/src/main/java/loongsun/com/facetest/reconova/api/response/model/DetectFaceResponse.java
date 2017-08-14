/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.response.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import loongsun.com.facetest.reconova.api.response.ApiModel;

/**
 * 检测人脸返回值
 */
public class DetectFaceResponse extends ApiModel {
	@JSONField(name = "result")
	private List<FaceDetectResult> result;
}
