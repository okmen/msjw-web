/**
 * 
 */
package cn.web.front.action.alipay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import cn.message.model.alipay.AlipayPostMessageModel;
import cn.message.model.alipay.message.IMessage;
import cn.message.service.IAlipayService;
import cn.web.front.action.alipay.util.ParamsUtil;
/**
 * @author gaoxigang
 * 
 */
public class Getway extends HttpServlet {
	Logger logger = Logger.getLogger(Getway.class);
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//不管支付宝推送什么消息过来 都必须返回此消息,否则支付宝会重试 更严重会使生活号无法使用
		String responseMsg = ParamsUtil.getBaseResponse();
		
		Map<String, String> params = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("GBK");
			params = ParamsUtil.getRequestParams(request);
			// 验证签名 - 这里暂时不验了 看看会发生什么情况
			// this.verifySign(params);

			String bizContent = params.get("biz_content");
			// 将XML转化成json对象
			JSONObject json = (JSONObject) new XMLSerializer().read(bizContent);
			String fromUserId = json.getString("FromUserId");
			String fromAlipayUserId = json.getString("FromAlipayUserId");
			String msgType = json.getString("MsgType");
			
			String eventType = null;
			String actionParam = null;
			String content = null;
			if(IMessage.MESSAGE_TYPE_EVENT.equals(msgType)){
				eventType = json.getString("EventType");
				actionParam = json.getString("ActionParam");
			}
			
			if(IMessage.MESSAGE_TYPE_TEXT.equals(msgType)){
				JSONObject object = json.getJSONObject("Text");
				content = object.getString("Content");
			}
			//组装参数model
			AlipayPostMessageModel model = new AlipayPostMessageModel(
					fromUserId, fromAlipayUserId, msgType, eventType, actionParam,
					content);
			
			//手动取得service　
			ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
			IAlipayService alipayService = (IAlipayService)ctx.getBean("alipayService");
			alipayService.processPostMessage(model);
		} catch (Exception e) {
			logger.info("处理支付宝推送消息异常："+params, e);
		}finally{
			response.setContentType("text/xml;charset=utf-8");
			outString(response, responseMsg);
		}
	}

	protected void outString(HttpServletResponse response, String str) {
		try {
			PrintWriter out = response.getWriter();
			out.print(str);
			logger.info(str);
		} catch (Exception e) {
			logger.error("写字符串异常");
		}
	}

}
