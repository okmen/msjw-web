package cn.web.front.action.wechat;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.message.bean.WxMembercard;
import cn.message.model.wechat.WechatPostMessageModel;
import cn.message.model.wechat.message.IEvent;
import cn.message.model.wechat.message.IMessage;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
import cn.sdk.util.StringUtil;
import cn.web.front.action.wechat.util.HttpRequest;
import cn.web.front.action.wechat.util.RoundUtil;
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
	
	@Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;

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
			outString(response,"");
			logger.error("微信请求验证服务器异常",e);
		}
	}

	@RequestMapping(value = "/doGet.html", method = RequestMethod.POST)
	public void message(HttpServletRequest request, HttpServletResponse response){
		try {
			long begin = System.currentTimeMillis();
			
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
	        
	        String cardId = requestMap.get("CardId");
	        String code = requestMap.get("UserCardCode");
	        String outerStr = requestMap.get("OuterStr");
	        String isGiveByFriend = requestMap.get("IsGiveByFriend");
	        String giveOpenId = requestMap.get("FriendUserName");
	        
	        String xml = requestMap.get("xml");
	        String keyStandard = requestMap.get("KeyStandard");
	        logger.info("xml:"+xml);
	        //领卡消息
	        if(IMessage.MESSAGE_TYPE_EVENT.equals(msgType) && IEvent.EVENT_USER_GET_CARD.toLowerCase().equals(event)){
	        	logger.info("领卡消息xml:"+xml);
	        }
	        
	        if(IMessage.MESSAGE_TYPE_EVENT.equals(msgType) && IEvent.EVENT_TYPE_SCAN.toLowerCase().equals(event)){
	        	 logger.info("微信消息xml:"+xml);
	        	 String pinganResult = "";
	    		 if("code128".equals(keyStandard)){
	    			 String url="http://code.stcpay.com:8088/ysth-traffic-front/weixin/msg.do"; 
	    			 //发送报文到平安获得返回报文
	    			 pinganResult=HttpRequest.sendPost(url, xml, 4900);
	    			 logger.info("平安返回报文:"+pinganResult);
	    		 }
	    		 outString(response,pinganResult);		
	    		 return;
	        }
			
			IMessage mesasge = wechatService.processPostMessage(new WechatPostMessageModel(fromUserName, toUserName, msgType, event,eventKey,content,msgId,cardId,code,outerStr,isGiveByFriend,giveOpenId));
			if(null != mesasge){
				outString(response, mesasge.toXml());
			} else{
				response.setContentType("text/xml;charset=UTF-8");
				outString(response, "success");
			}
			long end = System.currentTimeMillis();
			
			if(end-begin > 5000){
				logger.info("doGet响应超过5秒 耗时:"+(end-begin)+",xml:"+xml);
			}
		} catch (IOException e) {
			response.setContentType("text/xml;charset=UTF-8");
			outString(response, "success");
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
	
	@RequestMapping(value = "/asdasdasdas.html", method = RequestMethod.GET) 
	public void asdasdasdasd(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");
			String key = request.getParameter("key");
			if("qxwsed@!s1334".equals(key)){
				String accessToken = wechatService.queryAccessToken();
				outString(response, accessToken);
				return;
			}
		} catch (Exception e) {
			logger.error("获取token异常",e);
			outString(response, "error");
		}
	}
	
	@RequestMapping(value = "/getJsapiTicket.html", method = RequestMethod.GET) 
	public void getJsapiTicket(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");  
			String ticket = wechatService.getJsapiTicket();
			outString(response, ticket);
		} catch (Exception e) {
			logger.error("获取jsapi ticket异常",e);
			outString(response, "error"); 
		}
	}
	
	@RequestMapping(value = "/getApiTicket.html", method = RequestMethod.GET) 
	public void getApiTicket(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");  
			String apiTicket = wechatService.getApiTicket();
			outString(response, apiTicket);
		} catch (Exception e) {
			logger.error("获取apiTicket异常",e);
			outString(response, "error"); 
		}
	}
	
}
