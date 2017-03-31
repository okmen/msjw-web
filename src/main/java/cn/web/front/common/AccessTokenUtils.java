package cn.web.front.common;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import cn.sdk.util.DESUtils;
import cn.sdk.util.StringUtil;
import cn.web.front.constants.SecretConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AccessTokenUtils {
    private static Logger log = Logger.getLogger(AccessTokenUtils.class);
    public static String getAccessToken(HttpServletRequest request) {
        String accesstoken = "";
        try {
            accesstoken = request.getParameter("access-token");//5.4.2之后客户端统一把accessToken放在request请求里。
            if (StringUtil.isBlank(accesstoken)) {
                //5.4.2之前的客户端accessToken存放位置不同
                accesstoken = request.getHeader("accesstoken");//客户端把accessToken放置在请求头
                String requestString = request.getParameter("request");
                JSONObject jsonString = JSON.parseObject(requestString);
                if (accesstoken == null) {
                    accesstoken = jsonString.getString("accessToken");// h5不能把accesstoken放到head里，放置在请求里所以做兼容
                }
            }
        } catch (Exception e) {
            log.error("获取accesstoken失败", e);
        }
        return accesstoken;
    }

    public static String getUserId(HttpServletRequest httpRequest) throws Exception {
        
        final String request = httpRequest.getParameter("request");
        
        if (StringUtils.isNotBlank(request)) {
            
            final JSONObject jsonRequest = JSON.parseObject(request);
            String userId = jsonRequest.getString("userId");
            
            if (StringUtils.isNotBlank(userId)) {
                
                return DESUtils.decrypt(userId, SecretConstants.CHOUMEI_OLD_DESKEY);
            }
        }
        
        return StringUtils.EMPTY;
    }

}
