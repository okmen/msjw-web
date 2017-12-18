package cn.web.front.action.wechat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.gson.Gson;

import cn.message.service.IWechatService;
import cn.sdk.bean.BaseBean;
import cn.sdk.bean.ErrorBean;
import cn.sdk.bean.SuccessBean;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.web.front.action.wechat.model.OutJsonModel;
import cn.web.front.action.wechat.util.ConstantsErrorCode;
import cn.web.front.action.wechat.util.GsonUtil;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/h5")
@SuppressWarnings(value = "all")
public class H5Action extends BaseAction {
	@Autowired
	@Qualifier("wechatService")
	private IWechatService wechatService;
	
	/**
	 * h5获取config参数 用于调用sdk
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sdkConfig.html")
	public void sdkConfig(HttpServletRequest request,HttpServletResponse response){
		String url = request.getParameter("url");
		logger.debug("h5 url 参数:"+url);
		try {
			if(!StringUtils.isNotBlank(url)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "url不能为空"));
				return;
			}
			
			Map<String, Object> map = wechatService.sdkConfig(url);
			if(null == map){
				renderJSON(new ErrorBean(MsgCode.paramsError, "sdk config 签名失败"));
				return;
			}
			renderJSON(new SuccessBean(MsgCode.success, map));
		} catch (Exception e) {
			DealException(new ErrorBean(), e);
			logger.error("服务器异常:"+url, e);
		}
	}
	
	/**
	 * h5获取cardconfig参数 用于调用卡券
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/cardConfig.html")
	public void cardConfig(HttpServletRequest request,HttpServletResponse response){
		String openId = request.getParameter("openId");
		String cardId = request.getParameter("cardId");
		try {
			if(!StringUtils.isNotBlank(openId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "openId不能为空"));
				return;
			}
			
			if(!StringUtils.isNotBlank(cardId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "cardId不能为空"));
				return;
			}
			
			Map<String, Object> map = wechatService.cardConfig(openId, cardId);
			if(null == map){
				renderJSON(new ErrorBean(MsgCode.paramsError, "card config 签名失败"));
				return;
			}
			renderJSON(new SuccessBean(MsgCode.success, map));
		} catch (Exception e) {
			DealException(new ErrorBean(), e);
			logger.error("服务器异常:openId="+openId + ",cardId="+cardId, e);
		}
	}
	
	/**
	 * 驾驶证激活
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/activeJsCard.html")
	public void activeJsCard(HttpServletRequest request,HttpServletResponse response){
		String openId = request.getParameter("openid");
		String cardId = request.getParameter("card_id");
		String encryptCode = request.getParameter("encrypt_code");
		try {
			if(StringUtil.isBlank(openId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "openid不能为空"));
				return;
			}
			
			if(StringUtil.isBlank(cardId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "card_id不能为空"));
				return;
			}
			
			if(StringUtil.isBlank(encryptCode)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "encrypt_code不能为空"));
				return;
			}
			
			boolean bool = wechatService.activeJsCard(openId, cardId, encryptCode);
			if(!bool){
				outString(response, new ErrorBean(MsgCode.exception, MsgCode.systemMsg).toJson());
				return;
			}
			outString(response, new SuccessBean(MsgCode.success, null).toJson());
		} catch (Exception e) {
			DealException(new ErrorBean(), e);
			logger.error("服务器异常:openId="+openId + ",cardId="+cardId, e);
		}
	}
	
	/**
	 * 行驶证激活
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/activeXsCard.html")
	public void activeXsCard(HttpServletRequest request,HttpServletResponse response){
		String openId = request.getParameter("openid");
		String cardId = request.getParameter("card_id");
		String encryptCode = request.getParameter("encrypt_code");
		try {
			if(StringUtil.isBlank(openId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "openid不能为空"));
				return;
			}
			
			if(StringUtil.isBlank(cardId)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "card_id不能为空"));
				return;
			}
			
			if(StringUtil.isBlank(encryptCode)){
				renderJSON(new ErrorBean(MsgCode.paramsError, "encrypt_code不能为空"));
				return;
			}
			
			boolean bool = wechatService.activeXsCard(openId, cardId, encryptCode);
			if(!bool){
				outString(response, new ErrorBean(MsgCode.exception, MsgCode.systemMsg).toJson());
				return;
			}
			outString(response, new SuccessBean(MsgCode.success, null).toJson());
		} catch (Exception e) {
			DealException(new ErrorBean(), e);
			logger.error("服务器异常:openId="+openId + ",cardId="+cardId, e);
		}
	}
}
