/*
 * Copyright (c) 2017.  瑞为信息版权所有
 */

package loongsun.com.facetest.reconova.api.response;

/**
 * api错误响应
 */
public class FcsErrorResponse extends ApiModel {
	private long timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
