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
 * 通过两个特征base64数据比对相似度
 */
public class FeatureMatchRequest extends FcsApiRequest {
	
	public FeatureMatchRequest(FcsApiClient client) {
		super(client);
	}
	
	public FeatureMatchRequest(FcsApiClient client, String feature1, String feature2) {
		super(client);
		this.feature1 = feature1;
		this.feature2 = feature2;
	}
	
	/**
	 * 特征值1
	 */
	private String feature1;
	
	/**
	 * 特征值2
	 */
	private String feature2;
	
	public String getFeature1() {
		return feature1;
	}
	
	public void setFeature1(String feature1) {
		this.feature1 = feature1;
	}
	
	public String getFeature2() {
		return feature2;
	}
	
	public void setFeature2(String feature2) {
		this.feature2 = feature2;
	}
	
	public String getApiMethod() {
		return "POST";
	}
	
	public MediaType getApiRequestContentType() {
		return MediaType.parse(Constants.MediaTypes.X_WWW_FORM_URLENCODED);
	}
	
	public String getApiEndpoint() {
		return API_PREFIX + "tool/featureMatch";
	}
	
	public RequestBody getApiRequestBody() {
		RequestBody body = new FormBody.Builder()
				.add("feature1", this.feature1)
				.add("feature2", this.feature2)
				.build();
		
		return body;
	}

	@Override
	public void validateAndSetDefaults() throws IllegalParamException {
		System.out.println("set defaults");
	}
}
