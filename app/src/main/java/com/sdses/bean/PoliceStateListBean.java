package com.sdses.bean;

import java.io.Serializable;

public class PoliceStateListBean implements Serializable {
	/** 
	* @Fields serialVersionUID : TODO(jqinfo) 
	*/
	private static final long serialVersionUID = 1L;
	//jq ��������д
	private String jqName="";//��������
	private String jqNum="";//������
	private String jqPosition="";//����λ��
	private String jqTime="";//����ʱ��
	private String wx=""; //����
	private String wy="";//γ��
	private String bjrPhone="";
	private String bjrName="";
	
	public String getJqNum() {
		return jqNum;
	}
	public void setJqNum(String jqNum) {
		this.jqNum = jqNum;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	public String getWy() {
		return wy;
	}
	public void setWy(String wy) {
		this.wy = wy;
	}
	public String getBjrPhone() {
		return bjrPhone;
	}
	public void setBjrPhone(String bjrPhone) {
		this.bjrPhone = bjrPhone;
	}
	public String getBjrName() {
		return bjrName;
	}
	public void setBjrName(String bjrName) {
		this.bjrName = bjrName;
	}
	public String getJqName() {
		return jqName;
	}
	public void setJqName(String jqName) {
		this.jqName = jqName;
	}
	public String getJqPosition() {
		return jqPosition;
	}
	public void setJqPosition(String jqPosition) {
		this.jqPosition = jqPosition;
	}
	public String getJqTime() {
		return jqTime;
	}
	public void setJqTime(String jqTime) {
		this.jqTime = jqTime;
	}
	
}
