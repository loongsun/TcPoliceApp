package com.sdses.bean;

import java.io.Serializable;

public class PoliceStateListBean implements Serializable {
	/** 
	* @Fields serialVersionUID : TODO(jqinfo) 
	*/
	private static final long serialVersionUID = 1L;
	//jq 代表警情缩写
	private String jqName="";//警情名称
	private String jqNum="";//警情编号
	private String jqPosition="";//警情位置
	private String jqTime="";//警情时间
	private String wx=""; //经度
	private String wy="";//纬度
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
