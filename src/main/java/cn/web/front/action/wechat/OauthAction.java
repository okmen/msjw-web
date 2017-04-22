package cn.web.front.action.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.message.bean.WechatUserInfo;
import cn.message.service.ITemplateMessageService;
import cn.message.service.IWechatService;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/oauth")
@SuppressWarnings(value = "all")
public class OauthAction extends BaseAction{
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;

	@Autowired
	@Qualifier("templateMessageService")
	private ITemplateMessageService templateMessageService;
	/**
	 * 微信用户授权获取openId 回调函数
	 * 前端应当缓存openId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/callback.html")
	public void callback4OpenId(HttpServletRequest request,HttpServletResponse response){
		String code = request.getParameter("code");
		String state = request.getParameter("state");//前端会带过来一个url
		try {
			//获取微信用户信息
			WechatUserInfo wechatUserInfo = wechatService.callback4OpenId(code, state);
			logger.info("WechatUserInfo:"+wechatUserInfo.toString());
			response.setCharacterEncoding("utf-8");
			response.sendRedirect(state+
					"?openId="+wechatUserInfo.getOpenId()+
					"&headimgurl="+wechatUserInfo.getHeadUrlImg()+
					"&nickname="+java.net.URLEncoder.encode(wechatUserInfo.getNickName(), "UTF-8"));
		} catch (Exception e) {
			logger.error("callback获取openId异常 ",e);
		}
	}
}
