package cn.web.front.action.wechat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jaxen.dom4j.Dom4jXPath;

public class WechatPostParamsUtil {
	private static Logger logger = Logger.getLogger(WechatPostParamsUtil.class);
	/**
	 * 解析微信端主动发送过来的消息
	 * @param request
	 * @return
	 */
	public static Map<String, String> parseXml(HttpServletRequest request){
		// 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>(); 
		try {
			String xml = getPostParams4String(request);
	        // 从request中取得输入流
	        // 读取输入流
	       /* SAXReader reader = new SAXReader();
	        Document document = reader.read(inputStream);*/
	        Document document = DocumentHelper.parseText(xml);
	        // 得到xml根元素
	        Element root = document.getRootElement();
	        // 得到根元素的所有子节点
	        List<Element> elementList = root.elements(); 
	        //原报文
	        map.put("xml", xml);     
	        // 遍历所有子节点
	        for (Element e : elementList){
	        	map.put(e.getName(), e.getText());
	        }
		} catch (Exception e) {
			logger.info("解析微信message消息异常",e);
		}
        return map;
	}
	
	public static String getPostParams4String(HttpServletRequest request) {
		InputStream inputStream = null;
		String sInput = "";
		try {
			inputStream = request.getInputStream();
			String responseData = "";
			BufferedReader dataInput = new BufferedReader(
					new InputStreamReader(inputStream,"utf-8"));
			while ((responseData = dataInput.readLine()) != null)
				sInput = sInput + responseData;
			
		} catch (IOException e) {
			logger.error("获取post参数异常",e);
			return "";
		}finally{
			// 释放资源
	        try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        inputStream = null;
		}
		return sInput;
	}
}
