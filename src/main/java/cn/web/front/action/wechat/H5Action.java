package cn.web.front.action.wechat;

import java.io.IOException;
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
		logger.info("h5 url 参数:"+url);
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
}
