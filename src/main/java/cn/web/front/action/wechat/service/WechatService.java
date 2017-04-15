package cn.web.front.action.wechat.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.web.front.action.wechat.model.message.IMessage;
import cn.web.front.action.wechat.model.message.response.BaseResponseMessage;
import cn.web.front.action.wechat.model.message.response.TextResponseMessage;
import cn.web.front.action.wechat.util.Constants;
import cn.web.front.action.wechat.util.SHA1;
public class WechatService {
	private static Logger logger = Logger.getLogger(WechatService.class.getName());

	public static boolean check(String signature, String timestamp, String nonce) {
		String [] array = new String[]{Constants.TOKEN,timestamp,nonce};
		Arrays.sort(array);
		String str = "";
		for (int i = 0; i < array.length; i++) {
			str += array[i];
		}
		SHA1 sha1 = new SHA1();
		String sha1Str = sha1.Digest(str,"UTF-8").toLowerCase();
		return signature.equals(sha1Str) ? true : false;
	}

	public static String processRequest(HttpServletRequest req) {
		String xml = "";
		try {
			 Map<String, String> requestMap = parseXml(req);
			 for (String key : requestMap.keySet()) {
				logger.info(key+":"+requestMap.get(key)+"\n");
			}
			
			 String fromUserName = requestMap.get("FromUserName");
	         // 开发者微信号
	         String toUserName = requestMap.get("ToUserName");
	         // 消息类型
	         String msgType = requestMap.get("MsgType");
	         //事件类型
	         String event = requestMap.get("Event"); 
	 
	         BaseResponseMessage message = new TextResponseMessage(fromUserName,toUserName,new Date().getTime(),"敬请期待");
	         
	         if(msgType.equals(IMessage.MESSAGE_TYPE_TEXT)){
	        	 message =  new TextResponseMessage(fromUserName,toUserName,new Date().getTime(),"在线客服待开通");
	         }
	         
	         if(msgType.equals(IMessage.MESSAGE_TYPE_EVENT)){
	        	 if("CLICK".equals(event)){
	        		 message =  new TextResponseMessage(fromUserName,toUserName,new Date().getTime(),"敬请期待");
	        	 }
	        	 
	        	 //用户打开公众号会推送这个包过来 (第一次推送的包)
	        	 if("LOCATION".equals(event)){
	        		 //纬度
	        		 String Latitude = requestMap.get("Latitude");
	        		 //经度
	        		 String Longitude = requestMap.get("Longitude");
	        		 try {
	     			} catch (Exception e) {
	     				logger.error("添加微信用户异常",e);
	     			}
	        	 }
	         }
	         xml = message.toXml();
		} catch (Exception e) {
			logger.error("处理微信消息包异常",e);
			return "";
		}
		return "".equals(xml) ? "" : xml;
	}
	
	/**
	 * 解析微信端主动发送过来的消息
	 * @param request
	 * @return
	 */
	private static Map<String, String> parseXml(HttpServletRequest request){
		InputStream inputStream = null;
		// 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
		try {
	        // 从request中取得输入流
	        inputStream = request.getInputStream();
	        // 读取输入流
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(inputStream);
	        // 得到xml根元素
	        Element root = document.getRootElement();
	        // 得到根元素的所有子节点
	        List<Element> elementList = root.elements();
	        // 遍历所有子节点
	        for (Element e : elementList)
	            map.put(e.getName(), e.getText());
		} catch (Exception e) {
			logger.info("解析message消息异常",e);
		}finally{
			// 释放资源
	        try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        inputStream = null;
		}
        return map;
	}
}
