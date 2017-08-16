/*
 * Copyright (c) 2017.  ��Ϊ��Ϣ��Ȩ����
 */

package loongsun.com.facetest.reconova.api.response;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.JSONObject;
/**
 * fcs ��ȷ��Ӧ��Ϣ��
 */
public class FcsApiResponse {
	
	@JSONField(name = "errorCode")
	private int errorCode;
	
	@JSONField(name = "data")
	private JSONObject data;
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public JSONObject getData() {
		return data;
	}
	
	public void setData(JSONObject data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "FcsApiResponse{" +
				"errorCode=" + errorCode +
				", data=" + data.toJSONString() +
				'}';
	}
}
