/*
 * Copyright (c) 2017.  ��Ϊ��Ϣ��Ȩ����
 */

package loongsun.com.facetest.reconova.api.response.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import loongsun.com.facetest.reconova.api.response.ApiModel;

/**
 * �����������ֵ
 */
public class DetectFaceResponse extends ApiModel {
	@JSONField(name = "result")
	private List<FaceDetectResult> result;
}
