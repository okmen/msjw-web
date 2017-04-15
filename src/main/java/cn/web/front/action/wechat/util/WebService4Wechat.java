package cn.web.front.action.wechat.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import cn.web.front.action.wechat.model.MenuModel;
import cn.web.front.action.wechat.model.TemplateDataModel;
import cn.web.front.action.wechat.model.TemplateDataModel.Property;

/**
 * 
 * @author gaoxigang
 *
 */
public class WebService4Wechat {
	static Logger logger = Logger.getLogger(WebService4Wechat.class.getName());

	public static Map<String, Object> getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ Constants.APP_ID + "&secret=" + Constants.APP_SECRET;
		String result = HttpRequest.sendGet(url);
		Map<String, Object> map = GsonUtil.fromJson(result, Map.class);
		return map;
	}

	public static Map<String, Object> getJsapiTicket(String accessToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ accessToken + "&type=jsapi";
		String result = HttpRequest.sendGet(url);
		Map<String, Object> map = GsonUtil.fromJson(result, Map.class);
		return map;
	}

	public static Map<String, Object> sendOauth(String appId, String appSecret,
			String code) {
		String codeUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"
				+ "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		String requestUrl = codeUrl.replace("APPID", appId)
				.replace("SECRET", appSecret).replace("CODE", code);
		String result = HttpRequest.sendGet(requestUrl);
		Map<String, Object> map = GsonUtil.fromJson(result, Map.class);
		return map;
	}

	public static String sendTemplateMessage(String accessToken,
			TemplateDataModel templateDataModel) {
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
				+ accessToken;
		String json = GsonUtil.toJson(templateDataModel);
		/*
		 * String postParams = "{"+
		 * "\"touser\":\"oTlk3s3LVb9RYwlBNF-Euurz9mS4\"," +
		 * "\"template_id\":\"NPDRVXV-5SA2P2CK_rDeL4xRLg3aR3og1Sq3lcCuxOE\","+
		 * "\"url\":\"http://gxgnet.6655.la/recharge/index.jsp\"," +
		 * "\"data\":{" + "	\"serviceName\":{" +
		 * "		\"value\":\"全球通\",\"color\":\"#173177\"" + "	}" + "}}";
		 */
		/*
		 * String string = ""; try { byte[] b = json.getBytes("utf-8"); string =
		 * new String(b,"utf-8"); } catch (UnsupportedEncodingException e) {
		 * e.printStackTrace(); logger.info("json字符串转码异常"); }
		 */
		return HttpRequest.sendPost4Wechat(url, json);
	}
	
	public static String createMenu(String accessToken){
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		MenuModel model = new MenuModel();
		return HttpRequest.sendPost4Wechat(url, model.init());
	}

	public static void main(String[] args) {
		TemplateDataModel model = new TemplateDataModel();
		model.setTouser("oTlk3s3LVb9RYwlBNF-Euurz9mS4");
		model.setTemplate_id("NPDRVXV-5SA2P2CK_rDeL4xRLg3aR3og1Sq3lcCuxOE");
		model.setUrl("http://gxgnet.6655.la/recharge/index.jsp");
		Map<String, TemplateDataModel.Property> map = new HashMap<String, TemplateDataModel.Property>();
		Property property = model.new Property();
		property.setValue("动感地带");
		property.setColor("#173177");
		map.put("serviceName", property);
		model.setData(map);
		sendTemplateMessage(
				"BRgkTrrzhEeidLg_VXtvKiNHYOjuFo-00GOuTLS4UpyoodY1MVnhOp4-Rsw0bwK3QTdPoypTMEU_ZIPm80FfMXPK7AsNkQ16UIG9Z0H3Jx-GdL5emSNwccB7LwgYTs99MIMdAHAHDH",
				model);
	}
}
