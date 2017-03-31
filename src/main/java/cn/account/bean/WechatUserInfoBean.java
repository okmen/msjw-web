package cn.account.bean;

import java.io.Serializable;
import java.util.Date;

public class WechatUserInfoBean implements Serializable {

	private static final long serialVersionUID = 9188598100745528152L;
	
	private int id;
	private String openId;
	
	/** 昵称 */
	private String nickname;
	/** 头像URL */
	private String headImgUrl;
	private Date updateTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
