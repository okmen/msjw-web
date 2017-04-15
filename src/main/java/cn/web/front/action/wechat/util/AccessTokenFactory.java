package cn.web.front.action.wechat.util;
import java.util.Map;
import org.apache.log4j.Logger;
/**
 * AccessToken和ticket 缓存类
 * @author gxg
 *
 */
public class AccessTokenFactory {
	static Logger logger = Logger.getLogger(AccessTokenFactory.class);
	private static String TOKEN = null;
	private static String TICKET = null;
	private static Long EXPIRE = 0L;
	//两小时有效
	private static Long expire_in = 7200000L;
	
	public static String getToken(){
		check();
		return TOKEN;
	}
	
	public static String getTicket(){
		check();
		return TICKET;
	}
	
	/**
	 *  每次获取这个类的属性 需要先check一次  保证是有效的
	 * @return
	 */
	private synchronized static void check() {
		// 检查token是否过期
		if (null == TOKEN || EXPIRE <= System.currentTimeMillis()) {
			initTokenAndTicket();
		}
	}

	/**
	 * 获得新token
	 * 
	 * @return
	 */
	private synchronized static void initTokenAndTicket() {
		try {
			Map<String, Object> map = WebService4Wechat.getAccessToken();
			Object token = map.get("access_token");
			if(null != token && !"".equals(token)){
				TOKEN = token.toString();
				EXPIRE = System.currentTimeMillis()+expire_in;
				
				Map<String, Object> jsapMap = WebService4Wechat.getJsapiTicket(TOKEN);
				Object ticket = jsapMap.get("ticket");
				if(null != ticket && !"".equals(ticket)){
					TICKET = ticket.toString();
					logger.info("ticket："+ticket);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+"获取 token失败");
		}
	}
}
