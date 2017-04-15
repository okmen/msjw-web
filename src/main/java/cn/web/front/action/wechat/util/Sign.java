package cn.web.front.action.wechat.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信签名算法
 * @author gaoxigang
 *
 */
public class Sign {
	/**
	 * 获取sdk授权信息
	 */
	public synchronized static Map<String, Object> sign(String ticket,String url){
		Map<String, Object> map = new HashMap<String, Object>();
		String noncestr = UUID.randomUUID().toString();
		Long tempTimestamp = System.currentTimeMillis();
		String timestamp = tempTimestamp.toString().substring(0, 10);
		String string = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
		String sha1 = SHA1.SHA1Digest(string);
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("signature", sha1);
		map.put("appId", Constants.APP_ID);
		return map;
	}
}
