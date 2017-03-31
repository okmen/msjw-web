package cn.account.bean;

import java.io.Serializable;

public class UserOpenidBean implements Serializable {

    private static final long serialVersionUID = 6269417708512714184L;
    
    private long id;
    private String openid;//微信用户在某公众号的唯一标识
    private long userId;//用户id
    private int status;//绑定状态
    private long bindTime;//绑定时间
    private long unBindTime;//取绑时间
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getBindTime() {
        return bindTime;
    }
    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }
    public long getUnBindTime() {
        return unBindTime;
    }
    public void setUnBindTime(long unBindTime) {
        this.unBindTime = unBindTime;
    }
    
    

}
