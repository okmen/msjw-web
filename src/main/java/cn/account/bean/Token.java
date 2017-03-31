package cn.account.bean;

import java.io.Serializable;

/**
 * accessToken 验证token
 * refreshToken 刷新token
 */
public class Token implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7632282750322972722L;
	private String userId;
	private String accessToken;
	private String refreshToken;
	private String isLogin;


    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
}
