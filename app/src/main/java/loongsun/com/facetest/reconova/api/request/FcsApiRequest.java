/*
 * Copyright (c) 2017.  ��Ϊ��Ϣ��Ȩ����
 */

package loongsun.com.facetest.reconova.api.request;

import com.alibaba.fastjson.JSON;

import loongsun.com.facetest.reconova.api.exception.IllegalParamException;
import loongsun.com.facetest.reconova.api.response.FcsApiResponse;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import okhttp3.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * fcs api ���� interface
 */
public abstract class FcsApiRequest {
	
	public String API_PREFIX = "/api/v1/";
	
	protected FcsApiClient client;
	
	
	private static final Logger logger = Logger.getLogger(FcsApiRequest.class.getName());
	
	
	public FcsApiRequest(FcsApiClient client) {
		this.client = client;
	}
	
	/**
	 * ��ȡ����ʽ
	 *
	 * @return
	 */
	public abstract String getApiMethod();
	
	
	/**
	 * ��ȡ�����ʽ
	 *
	 * @return
	 */
	public abstract MediaType getApiRequestContentType();
	
	/**
	 * ��ȡAPI�����˵�
	 *
	 * @return
	 */
	public abstract String getApiEndpoint();
	
	/**
	 * ��ȡ������
	 *
	 * @return
	 */
	public abstract RequestBody getApiRequestBody();

//	abstract void validateRequestParams();
	/**
	 * ��֤����������Ĭ��ֵ
	 *
	 * @throws
	 */
	public abstract void validateAndSetDefaults() throws IllegalParamException;
	
	public FcsApiResponse execute() {
		Request request = new Request.Builder()
				.url(client.generateUrlPrefix() + getApiEndpoint())

				.method(getApiMethod(),
						getApiRequestBody())
				.build();
		
		Call call = okHttpClient().newCall(request);
		String responseBodyString = null;
		
		FcsApiResponse responseData = null;
		try {
			Response response = call.execute();
			int responseCode = response.code();
			if (responseCode < 400)
			{
				responseBodyString = response.body().string();
				logger.info(responseBodyString);
				responseData = JSON.parseObject(responseBodyString, FcsApiResponse.class);

			}
			else
			{
				responseBodyString = response.body().string();
				if(responseBodyString!=null)
				throw new RuntimeException("error:" + responseBodyString);
			}
			logger.log(Level.SEVERE, response.code() + "");
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return responseData;
	}
	
	private OkHttpClient okHttpClient() {
		return this.client.getOkHttpClient();
	}
	
}
