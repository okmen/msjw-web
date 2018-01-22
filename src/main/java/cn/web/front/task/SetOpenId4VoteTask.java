package cn.web.front.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.sdk.util.HttpClientUtil;

/**
 * @date 2017年6月16日
 */
public class SetOpenId4VoteTask implements Runnable, Serializable {
	private final static Logger logger = LoggerFactory.getLogger(SetOpenId4VoteTask.class);
	
	private String openId;
	
	public SetOpenId4VoteTask(String openId){
		this.openId = openId;
	}
	
	public void run() {
		try {
//			HttpRequest.sendPost("http://testjava.chudaokeji.com/wechat/setAuthOpenid.html", "openId=", 3000);
			Map<String, String> map = new HashMap<>();
			map.put("openId", openId);
//			HttpClientUtil.post("http://testjava.chudaokeji.com/wechat/setAuthOpenid.html", map, null);
			HttpClientUtil.post("http://gzh.stc.gov.cn/api/wechat/setAuthOpenid.html", map, null);
			logger.info("深圳交警授权openId : " + openId);
		} catch (Exception e) {
			logger.error("SetOpenId4VoteTask 异常:",e);
		}
	}
}