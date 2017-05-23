package cn.web.wechat;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.message.model.wechat.TemplateDataModel;
import cn.message.model.wechat.TemplateDataModel.Property;
import cn.web.front.action.wechat.util.GsonUtil;


public class DaySender {
	static Logger logger = Logger.getLogger(DaySender.class);
	// 测试模板id
	static String templateId = "pFy7gcEYSklRmg32165BUBwM3PFbUbBSLe0IPw3ZuY4";
	
	/**
	 * 发送模板消息
	 * @param accessToken
	 * @param templateDataModel
	 * @return
	 */
	private static String sendTemplateMessage(String accessToken,String requestParams) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+ accessToken;
		return HttpRequest.sendPost4Wechat(url, requestParams);
	}
	
	public static void main(String[] args) {
		//记录号=举报序列号
		String reportSerialNumber = "w20170519869317";
		//查询密码
		String password = "111454";
		
		Map<String, Property> map = new HashMap<String, Property>();
		map.put("first", new TemplateDataModel().new Property("随手拍举报通知","#212121"));
		map.put("keyword1", new TemplateDataModel().new Property(reportSerialNumber,"#212121"));
		map.put("keyword2", new TemplateDataModel().new Property(password,"#212121"));
		map.put("remark", new TemplateDataModel().new Property("举报状态：已记录\r\n您已完成本次举报流程，可通过深圳交警微信公众平台【交警互动】板块《举报信息查询》栏目输入您的记录号与查询密码进行查询，感谢您使用深圳交警微信公众平台。", "#212121"));
		
		TemplateDataModel model = new TemplateDataModel();
		model.setTouser("oPyqQjrWYVTrhSwtgsarNd-WFk98");
		model.setTemplate_id(templateId);
		model.setUrl("http://szjj.u-road.com/h5/#/takePicturesSuccess1?reportSerialNumber=" + reportSerialNumber + "&password=" + password);
		model.setData(map);
		String accessToken = HttpRequest.sendGet("http://szjj.u-road.com/api/wechat/asdasdasdas.html?key=qxwsed@!s1334");
		String json = GsonUtil.toJson(model);
		String xx = sendTemplateMessage(accessToken, json);
		
		System.out.println(xx);
		
		
	}
	/**
	 * 发送随手拍举报模板消息
	 * @param templateId 随手拍模板id
	 * @param openId 接受者openId
	 * @param callBackUrl 微信跳转地址
	 * @param reportSerialNumber 记录号=举报序列号
	 * @param password 查询密码
	 * @return
	 */
	/*public static boolean sendReadilyShootTemplate(String templateId,String openId,String callBackUrl,String reportSerialNumber,String password){
		Map<String, Property> map = new HashMap<String, Property>();
		map.put("first", new TemplateDataModel().new Property("随手拍举报通知","#212121"));
		map.put("keyword1", new TemplateDataModel().new Property(reportSerialNumber,"#212121"));
		map.put("keyword2", new TemplateDataModel().new Property(password,"#212121"));
		map.put("remark", new TemplateDataModel().new Property("举报状态：已记录\r\n您已完成本次举报流程，可通过深圳交警微信公众平台【交警互动】板块《举报信息查询》栏目输入您的记录号与查询密码进行查询，感谢您使用深圳交警微信公众平台。", "#212121"));
		
		TemplateDataModel model = new TemplateDataModel();
		
		model.setTouser(openId);
		model.setTemplate_id(templateId);
		model.setUrl("http://szjj.u-road.com/h5/#/takePicturesSuccess1?reportSerialNumber=" + reportSerialNumber + "&password=" + password);
		model.setData(map);
		String accessToken = HttpRequest.sendGet("http://szjj.u-road.com/api/wechat/asdasdasdas.html?key=qxwsed@!s1334");
		String json = GsonUtil.toJson(model);
		String xx = sendTemplateMessage(accessToken, json);
	}
	*/
	
}
