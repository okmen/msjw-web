package cn.web.front.action.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.account.service.IAccountService;
import cn.sdk.exception.ResultCode;
import cn.sdk.util.StringUtil;
import cn.web.front.action.wechat.service.WechatService;
import cn.web.front.action.wechat.util.AccessTokenFactory;
import cn.web.front.action.wechat.util.Constants;
import cn.web.front.action.wechat.util.SHA1;
import cn.web.front.action.wechat.util.WebService4Wechat;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/wechat")
@SuppressWarnings(value = "all")
public class WechatAction extends BaseAction {
	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;

	@RequestMapping(value = "/doGet.html", method = RequestMethod.GET)
	public void getway(
				HttpServletRequest request,HttpServletResponse response){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		try {
			PrintWriter out = response.getWriter();
			if(WechatService.check(signature,timestamp,nonce)) {
				out.print(echostr);
			}else{
				out.print("");
			}
		} catch (IOException e) {
			logger.error("微信请求验证服务器异常",e);
		}
	}

	@RequestMapping(value = "/doGet.html", method = RequestMethod.POST)
	public void message(HttpServletRequest request, HttpServletResponse response){
		try {
			request.setCharacterEncoding("utf-8");
			logger.info("post message");
			String mesasge = WechatService.processRequest(request);
			outString(response, mesasge);
		} catch (IOException e) { 
			logger.error("接收微信post消息异常",e);
		}
	}
	
	@RequestMapping(value = "/createMenu.html", method = RequestMethod.GET) 
	public void createMenu(HttpServletRequest request, HttpServletResponse response){
		try {
			WebService4Wechat.createMenu(AccessTokenFactory.getToken());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	@RequestMapping(value = "/test.html", method = RequestMethod.GET) 
	public void test(HttpServletRequest request, HttpServletResponse response){
		try {
			outString(response, "ok");
			//response.sendRedirect("http://zsc.tunnel.qydev.com/&openId=aaaaaaaaaaaa");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
