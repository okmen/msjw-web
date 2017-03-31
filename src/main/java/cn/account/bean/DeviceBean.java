package cn.account.bean;

import java.io.Serializable;

public class DeviceBean implements Serializable{

    
    private static final long serialVersionUID = -529127075818694811L;
    
    private long id;
    private String deviceUuid; //设备唯一号
    private int osType;//系统类型
    private long userId;//用户id
    private long addTime;//记录设备的时间
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    
    public String getDeviceUuid() {
        return deviceUuid;
    }
    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
    public int getOsType() {
        return osType;
    }
    public void setOsType(int osType) {
        this.osType = osType;
    }
    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getAddTime() {
        return addTime;
    }
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
    

}
