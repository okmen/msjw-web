package cn.web.front.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import cn.message.service.IWechatService;
import cn.sdk.util.HttpClientUtil;

/**
 * @date 2017年6月16日
 */
public class SetOpenId4VoteTask implements Runnable, Serializable {
	private final static Logger logger = LoggerFactory.getLogger(SetOpenId4VoteTask.class);
	
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;
	
	private String openId;
	
	public SetOpenId4VoteTask(String openId){
		this.openId = openId;
	}
	
	public void run() {
		try {
		/*	HttpRequest.sendPost("http://testjava.chudaokeji.com/wechat/setAuthOpenid.html", "openId=", 3000);
			Map<String, String> map = new HashMap<>();
			map.put("openId", openId);
			HttpClientUtil.post("http://testjava.chudaokeji.com/wechat/setAuthOpenid.html", map, null);
			HttpClientUtil.post("http://gzh.stc.gov.cn/api/wechat/setAuthOpenid.html", map, null);*/
			wechatService.setAuthOpenid(openId);
		} catch (Exception e) {
			logger.error("SetOpenId4VoteTask 异常:",e);
		}
	}
}