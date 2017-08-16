

package com.tc.client;

import android.util.Log;

import okhttp3.*;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * FCS API �ͻ���
 */
public class FcsApiClient {
	private static final Logger logger = Logger.getLogger(FcsApiClient.class.getName());
	/**
	 * ����Э�� Ĭ��http
	 */
	private String protocol = "http";
	
	/**
	 * ���� ������ַ
	 */
	private String host;
	
	/**
	 * ���������˿�
	 */
	private int port;
	
	/**
	 * �����½�û���
	 */
	private String username;
	
	/**
	 * �����½�û�����
	 */
	private String password;
	
	/**
	 * OkHttpClient
	 */
	
	private OkHttpClient okHttpClient;
	
	private FcsApiClient(boolean isAut) {
		if(isAut)
		this.okHttpClient = new OkHttpClient.Builder()
				//��Ȩ
				.authenticator(apiAuthenticator())
				.build();
		else
			this.okHttpClient = new OkHttpClient.Builder()
					.build();
	}
//	private FcsApiClient() {
//		this.okHttpClient = new OkHttpClient.Builder()
//				//��Ȩ
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
	 * ��ȡ���� api ������ַ
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
	 * ��Ȩ
	 *
	 * @return
	 */
	public Authenticator apiAuthenticator() {
		Authenticator authenticator = new Authenticator() {
			@Override
			public Request authenticate(Route route, Response response) throws IOException {
				//��������������Ȩʧ�ܣ�������������
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
	 * ��ȡ������Ӧ����
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
