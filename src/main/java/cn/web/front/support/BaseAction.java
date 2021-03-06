package cn.web.front.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import cn.sdk.bean.ErrorBean;
import cn.sdk.exception.HttpPingAnException;
import cn.sdk.exception.WebServiceException;
import cn.sdk.util.MsgCode;
import cn.sdk.util.StringUtil;
import cn.sdk.util.SzgaMsgCode;

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
			baseBean.setMsg(webServiceException.getMessage());
		}else if(exception instanceof HttpPingAnException){
			HttpPingAnException pingAnException = (HttpPingAnException) exception;
			baseBean.setCode(String.valueOf(pingAnException.getCode()));
			baseBean.setMsg(MsgCode.httpPingAnCallMsg);
		}else{
			baseBean.setCode(MsgCode.exception);
        	baseBean.setMsg(MsgCode.systemMsg);
		}
    	logger.error("DealException 错误!", exception);
    	
    	renderJSON(baseBean);
    }
    /**
     * 获取客户端ip
     * @param request
     * @return
     */
    public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
    
    /**
     * 校验参数非空
     * @param request
     * @param response
     * @param params
     */
    protected boolean checkParamNotNull(HttpServletRequest request,HttpServletResponse response,String...params){
    	if(null != params){
    		for (int i = 0; i < params.length; i++) {
        		String key = params[i];
        		String value = request.getParameter(key);
        		if(StringUtil.isBlank(value)){
    				outString(response, new ErrorBean(SzgaMsgCode.PRARAM_ERROR, key+"不能为空").toJson());
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    
    /**
	 * 获取POST参数
	 * @param req
	 * @return
	 */
	public String getPostParams(HttpServletRequest req) {
		String sInput = "";
		try {
			String responseData = "";
			BufferedReader dataInput = new BufferedReader(
					new InputStreamReader(req.getInputStream(),"utf-8"));
			while ((responseData = dataInput.readLine()) != null)
				sInput = sInput + responseData;
			dataInput.close();
			logger.info("PARSE DATA FOR POST:"+sInput);
			return sInput;
		} catch (IOException e) {
			logger.error("获取post参数异常");
		}
		return null;
	}
}
