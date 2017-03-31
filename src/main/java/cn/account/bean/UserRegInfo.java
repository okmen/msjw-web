package cn.account.bean;

import java.io.Serializable;
import java.util.Date;

public class UserRegInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7077901054675496523L;

	private long userId;
	private String username;
	private String password;
	private String email;
	private String img;
	private long creatOn; // 秒级时间戳
	private long lastLogin; // 秒级时间戳
	private int status; // 状态
	private int isPublish; // 是否马甲(发布权限)
	private String nickname;
	private int sex;
	private Date birthday;
	private String area;
	private long grade; // 积分
	private long growth; // 成长值
	private int isOfficial; // 是否官方
	private String mobilePhone;
	private int hairType; // 发型类型
	private int OSType; // 操作系统,1:Android,2:ios,3:其他
	private int lastLoginIp; // 长整形的IP地址
	private long costPassword; // 支付密码
	private long costGrade; // 消费等级
	private double balance; // 余额
	private long couponMoney; // 所有优惠劵总金额
	private long companyId; // 集团ID

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getHairType() {
		return hairType;
	}

	public long getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(long couponMoney) {
		this.couponMoney = couponMoney;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public void setHairType(int hairType) {
		this.hairType = hairType;
	}

	public int getOSType() {
		return OSType;
	}

	public void setOSType(int oSType) {
		OSType = oSType;
	}

	public int getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(int lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public long getCostPassword() {
		return costPassword;
	}

	public void setCostPassword(long costPassword) {
		this.costPassword = costPassword;
	}

	public long getCostGrade() {
		return costGrade;
	}

	public void setCostGrade(long costGrade) {
		this.costGrade = costGrade;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public long getGrade() {
		return grade;
	}

	public void setGrade(long grade) {
		this.grade = grade;
	}

	public long getGrowth() {
		return growth;
	}

	public void setGrowth(long growth) {
		this.growth = growth;
	}

	public int getIsOfficial() {
		return isOfficial;
	}

	public void setIsOfficial(int isOfficial) {
		this.isOfficial = isOfficial;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public UserRegInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(int isPublish) {
		this.isPublish = isPublish;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public long getCreatOn() {
		return creatOn;
	}

	public void setCreatOn(long creatOn) {
		this.creatOn = creatOn;
	}

	public long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

}
