/*
 * Copyright (c) 2017.  ��Ϊ��Ϣ��Ȩ����
 */

package loongsun.com.facetest.reconova.api.request.tool;

import loongsun.com.facetest.reconova.api.Constants;
import loongsun.com.facetest.reconova.api.exception.IllegalParamException;
import loongsun.com.facetest.reconova.api.request.FcsApiRequest;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ImageMatchRequest extends FcsApiRequest {
	
	public ImageMatchRequest(FcsApiClient client) {
		super(client);
	}
	
	public ImageMatchRequest(FcsApiClient client, String image1, String image2) {
		super(client);
		this.image1 = image1;
		this.image2 = image2;
	}
	
	/**
	 * ͼƬ1 base64����
	 */
	private String image1;
	
	/**
	 * ͼƬ2 base64����
	 */
	private String image2;
	
	public String getImage1() {
		return image1;
	}
	
	public String getImage2() {
		return image2;
	}
	
	public void setImage2(String image2) {
		this.image2 = image2;
	}
	
	public void setImage1(String image1) {
		this.image1 = image1;
	}
	
	public String getApiMethod() {
		return "POST";
	}
	
	public MediaType getApiRequestContentType() {
		return MediaType.parse(Constants.MediaTypes.X_WWW_FORM_URLENCODED);
	}
	
	public String getApiEndpoint() {
		return API_PREFIX + "tool/imageMatch";
		
	}
	
	public RequestBody getApiRequestBody() {
		
		RequestBody body = new FormBody.Builder()
				.add("image1", this.image1)
				.add("image2", this.image2)
				.build();
		
		return body;
	}
	@Override
	public void validateAndSetDefaults() throws IllegalParamException {
		System.out.println("set defaults");
	}
}
