package cn.web.front.action.wechat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.message.model.wechat.WechatPostMessageModel;
import cn.message.model.wechat.message.IMessage;
import cn.message.service.IMobileMessageService;
import cn.message.service.IWechatService;
import cn.web.front.action.wechat.util.WechatPostParamsUtil;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/wechat")
@SuppressWarnings(value = "all")
public class WechatAction extends BaseAction {
	@Autowired
	@Qualifier("mobileMessageService")
	private IMobileMessageService mobileMessageService;
	
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;

	@RequestMapping(value = "/doGet.html", method = RequestMethod.GET)
	public void getway(
				HttpServletRequest request,HttpServletResponse response){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		try {
			boolean bool = wechatService.checkServer(signature, timestamp, nonce);
			if(bool) {
				outString(response, echostr);
			}else{
				outString(response,"");
			}
		} catch (Exception e) {
			logger.error("微信请求验证服务器异常",e);
		}
	}

	@RequestMapping(value = "/doGet.html", method = RequestMethod.POST)
	public void message(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
			//获取post
			Map<String, String> requestMap = WechatPostParamsUtil.parseXml(request);
			String fromUserName = requestMap.get("FromUserName");
	        String toUserName = requestMap.get("ToUserName");
	        String msgType = requestMap.get("MsgType");
	        String event = requestMap.get("Event"); 
	        String content = requestMap.get("Content");
	        String msgId = requestMap.get("MsgId");
	        String eventKey = requestMap.get("EventKey");
			IMessage mesasge = wechatService.processPostMessage(new WechatPostMessageModel(fromUserName, toUserName, msgType, event,content,msgId,eventKey));
			if(null != mesasge){
				outString(response, mesasge.toXml());
			} else{
				outString(response, "");
			}
		} catch (IOException e) { 
			logger.error("接收微信post消息异常",e);
		}
	}
	
	@RequestMapping(value = "/createMenu.html", method = RequestMethod.GET) 
	public void createMenu(HttpServletRequest request, HttpServletResponse response){
		try {
			String result = wechatService.createMenu();
			outString(response, result);
		} catch (Exception e) {
			logger.info("创建菜单异常",e);
			outString(response, "error");
		}
	}
	
	@RequestMapping(value = "/queryMenu.html", method = RequestMethod.GET) 
	public void queryMenu(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");
			String result = wechatService.queryMenu();
			outString(response, result);
		} catch (Exception e) {
			logger.error("查询菜单异常",e);
			outString(response, "error");
		}
	}
}
