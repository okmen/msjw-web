package cn.web.front.support;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.sdk.bean.BaseBean;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;

/**
 * action基类，提供logger成员变量、公共方法以及用户封装返回消息的方法
 * @author wubinhong
 */
public class BaseAction extends cn.web.front.common.BaseAction {

    public static final String RESPONSE_ENCODING = "UTF-8";
    
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();
    protected ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();
    
    /**
     * 动态注入request和response对象，每次请求，在调用目标action方法前都会调用该方法，
     * 同时也解决了如果在action方法（void方法）中没有动态注入response对象，spring mvc会寻找默认的ModelAndView的错误
     **/
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){
        requestThreadLocal.set(request);
        responseThreadLocal.set(response);
    }
    
    /**
     * original json string render method, without {@link cn.web.front.support.Result} format
     * @param object object will be convert to JSON format string
     */
    protected void renderJSON(Object object) {
        String result = JSON.toJSONStringWithDateFormat(object, "YYYY-MM-dd HH:mm:ss");
        logger.debug("<== render object[{}] to JSON[{}]", 
                object == null ? null : String.format("%s@%s",object.getClass().getName(), object.hashCode()), result);
        HttpServletResponse response = responseThreadLocal.get();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(RESPONSE_ENCODING);
        ServletOutputStream out = null;
        try {
            byte[] bytes = result.getBytes(RESPONSE_ENCODING);
            out = response.getOutputStream();
            out.write(bytes);
        } catch (IOException e) {
            logger.error("http response write error:", e);
        } finally {
            try {
                if(out != null)
                    out.close();
            } catch (IOException e) {
                logger.error("http response close error:", e);
            }
        }
    }

    

    /**
     * 从request参数中获取参数，统一格式
     * @return
     */
    protected JSONObject getParams() {
        String req = requestThreadLocal.get().getParameter("request");
        logger.debug("<== obtain request[{}]", req);
        if(StringUtils.isEmpty(req))
            new JSONObject();
        return JSON.parseObject(req);
    }
    
    protected Long getUserId(final HttpServletRequest httpRequest) {
        
        return NumberUtils.toLong(ObjectUtils.defaultIfNull(httpRequest.getAttribute("userId"), "0").toString());
    }
    
    protected String getOsType(final String deviceType) {
        
        if(deviceType == null)
            return null;

        if (deviceType.matches("^(?i)IPHONE|IPAD|IPOD.*")) {
            return "IOS";
        } else if (deviceType.matches("^(?i)ANDROID.*")) {
            return "ANDROID";
        } else if (deviceType.matches("^(?i)WECHAT.*")) {
            return "WECHAT";
        } else {
            return null;
        }
    }
    
    protected boolean isAppDevice() {
        String rawDeviceType = requestThreadLocal.get().getParameter("device-type");
        String deviceType = getOsType(rawDeviceType);
        return !"WECHAT".equals(deviceType);
    }

    protected void outString(HttpServletResponse response,String str){
    	try {
    		PrintWriter out = response.getWriter();
        	out.print(str);
        	logger.info(str);
		} catch (Exception e) {
			logger.error("写字符串异常");
		}
    }
    
    protected void DealException(BaseBean baseBean,Exception exception){
    	if(exception instanceof WebServiceException){
			WebServiceException webServiceException = (WebServiceException) exception;
			baseBean.setCode(String.valueOf(webServiceException.getCode()));
			baseBean.setMsg(MsgCode.webServiceCallMsg);
		}else{
			baseBean.setCode(MsgCode.exception);
        	baseBean.setMsg(MsgCode.systemMsg);
		}
    	logger.error("getMyDriverLicense 错误!", exception);
    	
    	renderJSON(baseBean);
    }
    
    
}
