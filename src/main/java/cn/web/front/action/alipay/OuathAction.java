package cn.web.front.action.alipay;

import java.net.URLEncoder;
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
import cn.sdk.thread.BilinThreadPool;
import cn.web.front.action.alipay.util.ParamsUtil;
import cn.web.front.support.BaseAction;
import cn.web.front.task.SetOpenId4VoteTask;

@Controller
@RequestMapping(value = "/oauthAlipay")
@SuppressWarnings(value = "all")
public class OuathAction extends BaseAction {
	Logger logger = Logger.getLogger(OuathAction.class);
	
	@Autowired
	@Qualifier("alipayService")
	private IAlipayService alipayService;
	
	@RequestMapping(value = "/callback.html")
	public void callback(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> params = ParamsUtil.getRequestParams(request);
		//换取用户信息的code
		String code = params.get("auth_code");
		//需要跳转的url
		String state = params.get("state");
		logger.debug("回调参数：" + params);
		try {
			//查询用户信息
			AlipayUserInfo alipayUserInfo = alipayService.callback4UserId(code);
			logger.info("alipay 获取用户信息："+alipayUserInfo.toString());
			
			StringBuffer sBuffer = new StringBuffer();
			sBuffer
			.append("alipayId=").append(alipayUserInfo.getAlipayId()).append("&")
			.append("avatar=").append(alipayUserInfo.getAvatar()).append("&")
			.append("nickname=").append(alipayUserInfo.getNickName()).append("&")
			.append("mobile=").append(alipayUserInfo.getMobile());
			String redirectUrl = state + "?" + URLEncoder.encode(sBuffer.toString(), "UTF-8");
			logger.info("返回的redirectUrl：" + redirectUrl);
			//String redirectUrl = state + "?alipayId=" + alipayUserInfo.getAlipayId() + "&avatar=" + alipayUserInfo.getAvatar() + "&nickname=" + java.net.URLEncoder.encode(nickName, "UTF-8");
			response.sendRedirect(redirectUrl);
		} catch (Exception e) {
			logger.error("callback异常 ", e);
		}
	}
}
