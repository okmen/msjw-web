package cn.web.front.action.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.web.front.action.wechat.model.WeiXinOauth2Token;
import cn.web.front.action.wechat.util.OpenIdUtil;
import cn.web.front.action.wechat.util.Constants;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/oauth")
@SuppressWarnings(value = "all")
public class OauthAction extends BaseAction{
	/**
	 * 微信用户授权获取openId 回调函数
	 * 前端应当缓存openId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/callback.html")
	public void callback4OpenId(HttpServletRequest request,HttpServletResponse response){
		String code = request.getParameter("code");
		String openId ="";
		if (!"authdeny".equals(code)) {
			WeiXinOauth2Token weiXinOauth2Token = OpenIdUtil
					.getOauth2AccessToken(Constants.APP_ID,
							Constants.APP_SECRET, code);
			openId = weiXinOauth2Token.getOpenId();
			//这个accessToken不同于之前的
			String oauthToken = weiXinOauth2Token.getAccessToken();
			try {
				logger.info("---------------openId:"+openId);
				response.sendRedirect("http://np.tunnel.qydev.com?openId="+openId);
			} catch (Exception e) {
				logger.error("获取openId异常",e);
			}
		}
	}
}
