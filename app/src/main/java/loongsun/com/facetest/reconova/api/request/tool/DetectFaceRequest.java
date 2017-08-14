/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.request.tool;

import loongsun.com.facetest.reconova.api.Constants;
import loongsun.com.facetest.reconova.api.exception.IllegalParamException;
import loongsun.com.facetest.reconova.api.request.FcsApiRequest;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 通过base64图片数据 检测人脸图片请求
 * <p>
 * /api/v1/tool/detectFace
 */
public class DetectFaceRequest extends FcsApiRequest {
	
	public DetectFaceRequest(FcsApiClient client, String imageBase64Data) {
		super(client);
		this.imageBase64Data = imageBase64Data;
	}
	
	public DetectFaceRequest(FcsApiClient client) {
		super(client);
	}
	
	private String imageBase64Data;
	
	public String getImageBase64Data() {
		return imageBase64Data;
	}
	
	public void setImageBase64Data(String imageBase64Data) {
		this.imageBase64Data = imageBase64Data;
	}
	
	public String getApiMethod() {
		return "POST";
	}
	
	public MediaType getApiRequestContentType() {
		return MediaType.parse(Constants.MediaTypes.X_WWW_FORM_URLENCODED);
	}
	
	public String getApiEndpoint() {
		return API_PREFIX + "tool/detectFace";
	}
	
	public RequestBody getApiRequestBody() {
		RequestBody body = new FormBody.Builder()
				.add("image", imageBase64Data)
				.build();
		
		return body;
	}

	@Override
	public void validateAndSetDefaults() throws IllegalParamException {
		System.out.println("set defaults");
	}
}
