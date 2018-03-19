/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package cn.web.front.action.alipay.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import cn.message.model.alipay.AlipayServiceEnvConstants;

/**
 * 解析HttpServletRequest参数
 * 
 * @author taixu.zqq
 * @version $Id: RequestUtil.java, v 0.1 2014年7月23日 上午10:48:10 taixu.zqq Exp $
 */
public class ParamsUtil {
	static Logger logger = Logger.getLogger(ParamsUtil.class);

    /**
     * 获取所有request请求参数key-value
     * 
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request){
        Map<String, String> params = new HashMap<String, String>();
        
        if(null != request){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String key : paramsKey){
            	String value = request.getParameter(key);
                params.put(key, value);
                logger.debug(key+":"+value);
            }
        }
        return params;
    }
    
    /**
	 * 获取基本的返回数据
	 * @return
	 */
	public static String getBaseResponse() {
		// 固定响应格式，必须按此格式返回
		StringBuilder builder = new StringBuilder();
		builder.append("<success>").append(Boolean.TRUE.toString())
				.append("</success>");
		builder.append("<biz_content>")
				//.append(AlipayServiceEnvConstants.PUBLIC_KEY)
				.append(AlipayServiceEnvConstants.PUBLIC_KEY_TEST)
				.append("</biz_content>");
		return builder.toString();
	}
}
