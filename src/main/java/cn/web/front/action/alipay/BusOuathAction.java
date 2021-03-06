package cn.web.front.action.alipay;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.message.model.alipay.AlipayUserInfo;
import cn.message.service.IAlipayService;
import cn.web.front.action.alipay.util.ParamsUtil;
import cn.web.front.support.BaseAction;
/**
 * 支付宝商户应用授权
 * @author Mbenben
 *
 */
@Controller
@RequestMapping(value = "/busOuathAlipay")
@SuppressWarnings(value = "all")
public class BusOuathAction extends BaseAction {
Logger logger = Logger.getLogger(OuathAction.class);
	
	@Autowired
	@Qualifier("alipayService")
	private IAlipayService alipayService;
	
	@RequestMapping(value = "/callback.html")
	public void callback(HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> params = ParamsUtil.getRequestParams(request);
		
		
		//换取用户信息的code
		String code = params.get("auth_code");
		//需要跳转的url
		String state = params.get("state");
		try {
			//查询用户信息
			AlipayUserInfo alipayUserInfo = alipayService.callback4UserId(code);
			logger.info("alipay 获取用户信息："+alipayUserInfo.toString());
			
			String nickName = alipayUserInfo.getNickName();
			if (!StringUtils.isNotBlank(nickName)) {
				nickName = "未设置昵称";
			}
			String redirectUrl = state + "?openId=" + alipayUserInfo.getAlipayId() + "&headimgurl=" + alipayUserInfo.getAvatar() + "&nickname=" + java.net.URLEncoder.encode(nickName, "UTF-8");
			response.sendRedirect(redirectUrl);
		} catch (Exception e) {
			logger.error("alipay callback获取userInfo异常 ", e);
		}
	}
}
