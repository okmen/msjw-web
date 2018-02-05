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

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alipay.api.AlipayApiException;

import cn.message.model.alipay.AlipayPostMessageModel;
import cn.message.model.alipay.AlipayServiceEnvConstants;
import cn.message.model.alipay.message.IEvent;
import cn.message.model.alipay.message.IMessage;
import cn.message.service.IAlipayService;
import cn.web.front.action.alipay.util.AlipayBaseUtil;
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
		// 不管支付宝推送什么消息过来 都必须返回此消息,否则支付宝会重试 更严重会使生活号无法使用
		String responseMsg = ParamsUtil.getBaseResponse();
		
		String eventType = null;
		String actionParam = null;
		String content = null;
		Map<String, String> params = new HashMap<String, String>();
		try {
			request.setCharacterEncoding("GBK");
			params = ParamsUtil.getRequestParams(request);
			// 验证签名 -
			// this.verifySign(params);
			String bizContent = params.get("biz_content");
			// 将XML转化成json对象
			JSONObject json = (JSONObject) new XMLSerializer().read(bizContent);
			
			String msgType = json.getString("MsgType");
			logger.info("支付宝消息推送biz_content="+bizContent);
			if (IMessage.MESSAGE_TYPE_EVENT.equals(msgType)) {
				eventType = json.getString("EventType");
				actionParam = json.getString("ActionParam");
				
				if(IEvent.EVENT_TYPE_NOTIFY.equals(eventType)){
					try {
						JSONObject fromObject = JSONObject.fromObject(actionParam);
						String eventBizType = fromObject.getString("eventBizType");
						//删除卡包消息
						//驾驶证：{"eventBizType": "removeCertDoc","certDocType":"证件类型","drivingLicenseNo":"对应证件号","userId":"用户id"}
						//行驶证：{"eventBizType": "removeCertDoc","certDocType":"证件类型","plateNo":"对应车牌号","userId":"用户id"}
						if("removeCertDoc".equals(eventBizType)){
							logger.info("【支付宝卡包】删除卡包消息biz_content：" + bizContent);
							String certDocType = fromObject.getString("certDocType");
							String userId = fromObject.getString("userId");
							String drivingLicenseNo = fromObject.getString("drivingLicenseNo");
							String plateNo = fromObject.getString("plateNo");
							
							// 手动取得service　
							ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
							IAlipayService alipayService = (IAlipayService) ctx.getBean("alipayService");
							boolean isSuccess = alipayService.updateCardReceiveType(drivingLicenseNo, certDocType, userId);
							if(isSuccess){
								logger.info("【支付宝卡包】修改卡包状态成功，certDocType="+certDocType+"，userId="+userId+"，drivingLicenseNo="+drivingLicenseNo+"，plateNo="+plateNo);
							}else{
								logger.info("【支付宝卡包】修改卡包状态失败，certDocType="+certDocType+"，userId="+userId+"，drivingLicenseNo="+drivingLicenseNo+"，plateNo="+plateNo);
							}
						}
					} catch (Exception e) {
						logger.error("【支付宝卡包】删除卡包处理异常，biz_content="+bizContent, e);
						e.printStackTrace();
					}
				}
			}
			// 验证网关消息 不做处理 直接走finally
			if (!IEvent.EVENT_TYPE_VERIFYGW.equals(eventType)) {
				String fromUserId = json.getString("FromUserId");
				String fromAlipayUserId = json.getString("FromAlipayUserId");
				if (IMessage.MESSAGE_TYPE_TEXT.equals(msgType)) {
					JSONObject object = json.getJSONObject("Text");
					content = object.getString("Content");
				}

				// 组装参数model
				AlipayPostMessageModel model = new AlipayPostMessageModel(
						fromUserId, fromAlipayUserId, msgType, eventType,
						actionParam, content);

				// 手动取得service　
				ApplicationContext ctx = WebApplicationContextUtils
						.getRequiredWebApplicationContext(this.getServletContext());
				IAlipayService alipayService = (IAlipayService) ctx
						.getBean("alipayService");
				alipayService.processPostMessage(model);
			}
		} catch (Exception e) {
			logger.info("处理支付宝推送消息异常：" + params.toString(), e);
		} finally {
			response.setContentType("text/xml;charset=utf-8");
			try {
				/*responseMsg = AlipayBaseUtil.encryptAndSign(responseMsg,
						AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
						AlipayServiceEnvConstants.PRIVATE_KEY,
						AlipayServiceEnvConstants.CHARSET, false, true,
						AlipayServiceEnvConstants.SIGN_TYPE);*/
				responseMsg = AlipayBaseUtil.encryptAndSign(responseMsg,
						AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY_TEST,
						AlipayServiceEnvConstants.PRIVATE_KEY_TEST,
						AlipayServiceEnvConstants.CHARSET_TEST, false, true,
						AlipayServiceEnvConstants.SIGN_TYPE_TEST);
			} catch (AlipayApiException e) {
				logger.info("alipay 加签异常", e);
			}
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
