package cn.account.service;

import java.util.List;
import java.util.Map;

import cn.account.bean.DeviceBean;
import cn.account.bean.Token;
import cn.account.bean.UserOpenidBean;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;

/**
 * @author suntao
 */
public interface IAccountService {
	
	
	/**
	 * 插入微信用户信息
	 * @param wechatUserInfo
	 * @return 成功则返回纪录id，失败返回0
	 */
	int insertWechatUserInfo(WechatUserInfoBean wechatUserInfo);
    
	/**
	 * 通过id获取用户微信信息
	 * @param id
	 * @return
	 */
	WechatUserInfoBean getWechatUserInfoById(int id);
	
	/**
	 * 获取全部对象List
	 * @return
	 */
	List<WechatUserInfoBean> getAllWechatUserInfoBeanList();



//	/**
//	 * 添加新用户
//	 * 
//	 * @param UserRegInfo userRegInfo
//	 * @return
//	 */
//	public UserRegInfo addNewUser(UserRegInfo userRegInfo);
//	
//	
//	/**
//     * 根据userId来获取accessToken
//     * 
//     * @param userId
//     * @return
//     */
//    public String getAccessTokenByUserId(long userId);
//
//    /**
//     * 获取缓存的加密accessToken和accessToken的对应关系
//     * 
//     * @param encyptAccessToken
//     * @return
//     */
//    public String getAccessTokenFromEncypt(String encyptAccessToken);
//
//    /**
//     * 插入加密accessToken和accessToken的对应关系
//     * 
//     * @param encyptAccessToken
//     * @param AccessToken
//     */
//    public void insertEncyptAccessToken(String encyptAccessToken, String AccessToken);
//    
//    /**
//     * 获取并插入Token
//     * @param userId
//     * @return
//     */
//    public Token getAccessToken(long userId);
//    
//    
//    /**
//     * 检查accessToken是否过期
//     * 
//     * @param accessToken
//     * @param userId
//     * @return 是否成功
//     */
//    public boolean isAccessTokenValidate(String accessToken, String userId);
//    
//    /**
//     * 根据refreshToken来获取accessToken
//     * 
//     * @param refreshToken
//     * @return 是否成功
//     */
//    public Map<String, String> getAccessTokenByRefreshToken(String userId, String refreshToken);
//    
//    /**
//     * 绑定微信
//     * 
//     * @param userOpenidBean
//     * @author shengfenglai
//     * @return long
//     */
//    public long  addBindOpenid(UserOpenidBean userOpenidBean);
//    
//    /**
//     * 取消绑定微信
//     * 
//     * @param userOpenidBean
//     * @author shengfenglai
//     * @return long 
//     */
//    public long cancelBindOpenid(UserOpenidBean userOpenidBean);
//    
//    /**
//     * 通过openid拿到userId
//     * @param openid
//     * @return userId
//     * @author shengfenglai
//     */
//    public long getUserIdByOpenid(String openid);
//    
//    /**
//     * 通过userId拿到openid
//     * @param userId 
//     * @return 
//     * @author shengfenglai
//     */
//    public String getOpenidByUserId(long userId);
//    
//    /**
//     * 获取DeviceBean
//     * @param deviceUuid 设备号
//     * @param osType 系统类型
//     * @return
//     */
//    public DeviceBean getDevice(String deviceUuid,int osType);
//    
//    /**
//     * 记录设备号
//     * @param deviceUuid 设备号
//     * @param osType 系统类型
//     * @param userId 用户id
//     */
//    public void addDevice(String deviceUuid,int osType,long userId);
//    
//    /**
//     * 更新cm_devices表的user_id
//     * @param deviceUuid 设备号
//     * @param osType 系统类型
//     * @param userId 用户id
//     * @return
//     */
//    public boolean updateDevice(String deviceUuid,int osType,long userId);
//    
    
}
