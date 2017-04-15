package cn.web.front.action.wechat;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.gson.Gson;

import cn.web.front.action.wechat.model.OutJsonModel;
import cn.web.front.action.wechat.model.WeiXinOauth2Token;
import cn.web.front.action.wechat.util.AccessTokenFactory;
import cn.web.front.action.wechat.util.OpenIdUtil;
import cn.web.front.action.wechat.util.Constants;
import cn.web.front.action.wechat.util.ConstantsErrorCode;
import cn.web.front.action.wechat.util.GsonUtil;
import cn.web.front.action.wechat.util.Sign;
import cn.web.front.support.BaseAction;

@Controller
@RequestMapping(value = "/h5")
@SuppressWarnings(value = "all")
public class H5Action extends BaseAction {
	/**
	 * h5获取config参数 用于调用sdk
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/sdkConfig.html")
	public void callback4OpenId(HttpServletRequest request,HttpServletResponse response){
		String url = request.getParameter("url");
		logger.info("h5 传过来的url:"+url);
		System.out.println("h5 传过来的url:"+url); 
		try {
			if(!StringUtils.isNotBlank(url)){
				outString(response, new OutJsonModel(ConstantsErrorCode.ERROR_CODE_000001).toJson());
				return;
			}
			//生成签名
			Map<String, Object> map = Sign.sign(AccessTokenFactory.getTicket(), url);
			String json = GsonUtil.toJson(map);
			outString(response, new OutJsonModel(ConstantsErrorCode.ERROR_CODE_000000, map).toJson());
		} catch (Exception e) {
			logger.error("服务器异常", e);
			outString(response, new OutJsonModel(ConstantsErrorCode.ERROR_CODE_10000).toJson());
		}
	}
}