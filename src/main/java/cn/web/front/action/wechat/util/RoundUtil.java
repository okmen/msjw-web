package cn.web.front.action.wechat.util;

import java.util.HashMap;
import java.util.Map;
import cn.message.model.wechat.TemplateDataModel;
import cn.message.model.wechat.TemplateDataModel.Property;
import cn.message.service.ITemplateMessageService;
import cn.sdk.util.MD5;

/**
 * 大转盘活动类
 * @author gaoxigang
 *
 */
public class RoundUtil {
	//测试模板id   
	//static String templateId = "Uao_Tj_n3NNx2MxKBS0SXxeHXjwxMFNLcFBRnipS2Fw";
	static String templateId = "51zvsAtU9X-8QSfOxENoEkxFnkXaEF6ADT8nir_PFJ8";
	
	//static String ydDomain = "http://tmp.szydweixin.com/static/html/super_lottery/h5/round_8/index.html";//测试地址 
	static String ydDomain = "http://mp.szydweixin.com/static/html/super_lottery/h5/round_8/index.html";
	static String secret = "szyd_supper_lottery";
	static String super_lottery_id = "7";//大转盘id
	
	public static boolean sendTemplateMessage(ITemplateMessageService templateMessageService,String toUserName,String mobile){
		Map<String, Property> map = new HashMap<String, Property>();
		map.put("first", new TemplateDataModel().new Property("星级用户移动大转盘",""));
		map.put("keyword1", new TemplateDataModel().new Property("星级用户们，快来点击进入大转盘抽奖领流量吧！（仅限深圳移动用户）","#0abece"));
		map.put("keyword2", new TemplateDataModel().new Property("2017年5月17日-2017年5月31日","#0abece"));
		map.put("remark", new TemplateDataModel().new Property("蜀黍送流量啦！对！就是你！1000M流量等你来转（仅限深圳移动用户）", ""));
		return templateMessageService.sendMessage(toUserName, templateId, getRoundUrl(mobile), map);
	}
	
	public static String getRoundUrl(String mobile){
		String uniCode = MD5.MD5Encode(mobile + secret);
		StringBuffer sb = new StringBuffer();
		sb.append(ydDomain);
		sb.append("?super_lottery_id=").append(super_lottery_id);
		sb.append("&uni_code=").append(uniCode);
		sb.append("&mobi=").append(mobile);
		System.out.println(sb.toString());
		return sb.toString();
	}
}
