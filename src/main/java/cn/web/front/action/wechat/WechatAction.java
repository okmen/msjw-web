package cn.web.front.action.wechat;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.message.model.wechat.WechatPostMessageModel;
import cn.message.model.wechat.message.IEvent;
import cn.message.model.wechat.message.IMessage;
import cn.message.service.IMobileMessageService;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
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
	        String xml = requestMap.get("xml");
	        String keyStandard = requestMap.get("KeyStandard");
	        
	        if(IMessage.MESSAGE_TYPE_EVENT.equals(msgType) && IEvent.EVENT_TYPE_SCAN.equals(event)){
	        	 String pinganResult = "";
	    		 if("code128".equals(keyStandard)){
	    			 String url="http://code.stcpay.com:8088/ysth-traffic-front/weixin/msg.do"; 
	    			 //发送报文到平安获得返回报文
	    			 pinganResult=HttpRequest.sendPost(url, xml);
	    		 }
	    		 outString(response,pinganResult);		
	    		 return;
	        }
	        
	        if(IMessage.MESSAGE_TYPE_TEXT.equals(msgType) && "13922828820".equals(content.trim())){
				RoundUtil.sendTemplateMessage(templateMessageService, fromUserName, content.trim());
				outString(response,"");		
	    		return;
			}
			
			if(IMessage.MESSAGE_TYPE_TEXT.equals(msgType) && "13922828015".equals(content.trim())){
				RoundUtil.sendTemplateMessage(templateMessageService, fromUserName, content.trim());
				outString(response,"");		
	    		return;
			}
			
			if(IMessage.MESSAGE_TYPE_TEXT.equals(msgType) && "13631530641".equals(content.trim())){
				RoundUtil.sendTemplateMessage(templateMessageService, fromUserName, content.trim());
				outString(response,"");		
	    		return;
			}
			
			IMessage mesasge = wechatService.processPostMessage(new WechatPostMessageModel(fromUserName, toUserName, msgType, event,content,msgId,eventKey));
			if(null != mesasge){
				outString(response, mesasge.toXml());
			} else{
				outString(response, "");
			}
			
		} catch (IOException e) { 
			outString(response, "");
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
	
	@RequestMapping(value = "/round.html", method = RequestMethod.GET) 
	public void round(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setCharacterEncoding("utf-8");
//			欢迎语：星级用户移动大转盘
//			提醒内容：星级用户们，快来点击进入大转盘抽奖领流量吧！（仅限深圳移动用户）
//			时间：以下发时间为准
//			结束语：蜀黍送流量啦！对！就是你！1000M流量等你来转（仅限深圳移动用户）
			
//			张瑜	13922828820
//			赵一夔	13922828015
//			陈圆	13631530641
			boolean bool = RoundUtil.sendTemplateMessage(templateMessageService, "oTlk3s3LVb9RYwlBNF-Euurz9mS4", "13510823501");
			outString(response, bool+"");
			logger.info("模板消息发送："+bool);
		} catch (Exception e) {
			logger.error("发送模板消息异常",e);
			outString(response, "error");
		}
	}
}
