/*
 * Copyright (c) 2017.  瑞为信息版权所有
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
 * fcs api 请求 interface
 */
public abstract class FcsApiRequest {
	
	public String API_PREFIX = "/api/v1/";
	
	protected FcsApiClient client;
	
	
	private static final Logger logger = Logger.getLogger(FcsApiRequest.class.getName());
	
	
	public FcsApiRequest(FcsApiClient client) {
		this.client = client;
	}
	
	/**
	 * 获取请求方式
	 *
	 * @return
	 */
	public abstract String getApiMethod();
	
	
	/**
	 * 获取请求格式
	 *
	 * @return
	 */
	public abstract MediaType getApiRequestContentType();
	
	/**
	 * 获取API方法端点
	 *
	 * @return
	 */
	public abstract String getApiEndpoint();
	
	/**
	 * 获取请求体
	 *
	 * @return
	 */
	public abstract RequestBody getApiRequestBody();

//	abstract void validateRequestParams();
	/**
	 * 验证参数并设置默认值
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
