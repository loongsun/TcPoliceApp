

package com.tc.client;

import android.util.Log;

import okhttp3.*;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * FCS API 客户端
 */
public class FcsApiClient {
	private static final Logger logger = Logger.getLogger(FcsApiClient.class.getName());
	/**
	 * 请求协议 默认http
	 */
	private String protocol = "http";
	
	/**
	 * 请求 主机地址
	 */
	private String host;
	
	/**
	 * 请求主机端口
	 */
	private int port;
	
	/**
	 * 请求登陆用户名
	 */
	private String username;
	
	/**
	 * 请求登陆用户密码
	 */
	private String password;
	
	/**
	 * OkHttpClient
	 */
	
	private OkHttpClient okHttpClient;
	
	private FcsApiClient(boolean isAut) {
		if(isAut)
		this.okHttpClient = new OkHttpClient.Builder()
				//鉴权
				.authenticator(apiAuthenticator())
				.build();
		else
			this.okHttpClient = new OkHttpClient.Builder()
					.build();
	}
//	private FcsApiClient() {
//		this.okHttpClient = new OkHttpClient.Builder()
//				//鉴权
//				.authenticator(apiAuthenticator())
//				.build();
//	}
	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}
	
	public void setOkHttpClient(OkHttpClient okHttpClient) {
		this.okHttpClient = okHttpClient;
	}
	
	public static FcsApiClient createFcsApiClient(boolean isAut)
	{
		return new FcsApiClient(isAut);
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 获取请求 api 完整地址
	 *
	 * @return
	 */
	public String generateUrlPrefix() {
		return protocol + "://" + host + ":" + port;
	}

//	public void execute(FcsApiRequest apiRequest) {
//		Request request = new Request.Builder()
//				.url(generateUrl(apiRequest))
//				.method(apiRequest.getApiMethod(),
//						apiRequest.getApiRequestBody())
//				.build();
//
//		Call call = okHttpClient.newCall(request);
//
//		try {
//			Response response = call.execute();
//			int responseCode = response.code();
//			if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
//				logger.log(Level.SEVERE, response.code() + "");
//			}
//			logger.info(response.body().string());
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 鉴权
	 *
	 * @return
	 */
	public Authenticator apiAuthenticator() {
		Authenticator authenticator = new Authenticator() {
			@Override
			public Request authenticate(Route route, Response response) throws IOException {
				//请求三次以上授权失败，放弃本次请求
				if (responseCount(response) > 3) {
					return null;
				}
				
				String credential = Credentials.basic(username, password);
				Log.e("apiAuthenticator",username+"_"+password+"***********"+credential);
				return response.request()
						.newBuilder()
						.header("Authorization", credential)
						.build();
			}
		};
		
		return authenticator;
	}
	
	/**
	 * 获取请求响应次数
	 *
	 * @param response
	 * @return
	 */
	private int responseCount(Response response) {
		int count = 1;
		while ((response = response.priorResponse()) != null) {
			count++;
		}
		return count;
	}
	
	
}
