package cn.web.front.action.wechat;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.message.bean.WechatUserInfo;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
import cn.sdk.util.DateUtil;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/oauth")
@SuppressWarnings(value = "all")
public class OauthAction extends BaseAction{
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;

	@Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
	/**
	 * 微信用户授权获取openId 回调函数
	 * 前端应当缓存openId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/callback.html")
	public void callback4OpenId(HttpServletRequest request,HttpServletResponse response){
		String code = request.getParameter("code");
		String state = request.getParameter("state");//前端会带过来一个url
		logger.info("state:"+state);
		//获取微信用户信息
		WechatUserInfo wechatUserInfo = wechatService.callback4OpenId(code, state);
		try {
			//response.setCharacterEncoding("utf-8");
			//
			Map<String, String> map = new HashMap<String, String>();
			map.put("serviceName", "测试模板消息:"+DateUtil.convert2String(new Date().getTime()));
			boolean bool = templateMessageService.sendMessage(wechatUserInfo.getOpenId(),"www.baidu.com",map);
			logger.info("模板消息发送："+bool);
			
			response.sendRedirect(state+
					"?openId="+wechatUserInfo.getOpenId()+
					"&headimgurl="+wechatUserInfo.getHeadUrlImg()+
					"&nickname="+java.net.URLEncoder.encode(wechatUserInfo.getNickName(), "UTF-8"));
		} catch (Exception e) {
			logger.error("获取openId异常 ,展示错误页面",e);
		}
	}
}
