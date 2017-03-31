package cn.web.front.common;

import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;

import cn.sdk.util.DESUtils;
import cn.sdk.util.Distance;
import cn.sdk.util.StringUtil;
import cn.web.front.constants.SecretConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseAction {

    private final static Logger logger = LoggerFactory.getLogger(BaseAction.class);

    protected final static double DEFAULT_LNG = 114.066102;
    protected final static double DEFAULT_LAT = 22.546843;


    @Resource(name = "env")
    protected int environment;

    // 统一校验参数
    protected boolean checkRequestParams(HttpServletRequest req, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String value = req.getParameter(args[i]);
            if (value == null || "".equals(value)) {
                return false;
            }
        }
        return true;
    }

    // 同一校验大于0的参数
    protected boolean checkRequestParamsBiggerThanZero(HttpServletRequest req, String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String value = req.getParameter(args[i]);
                if (!StringUtils.isNumeric(value) || Long.parseLong(value) <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // 统一的错误模板
    protected ModelAndView getErrorView(String errorCode, String version, String message) {

        return getErrorView(Integer.parseInt(errorCode), version, message, StringUtils.EMPTY);
    }

    protected ModelAndView getErrorView(String errorCode) {

        return getErrorView(Integer.parseInt(errorCode), StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    protected ModelAndView getErrorView(final int errorCode, final String version, final String message, final String debug) {

        ModelAndView view = new ModelAndView("base/error");
        view.addObject("version", version);
        view.addObject("code", errorCode);
        view.addObject("message", message);
        view.addObject("debug", debug);
        return view;
    }

    // 带文字错误模板
    protected ModelAndView getErrorInfoView(String errorCode, String content) {
        ModelAndView view = new ModelAndView("base/errorMsg");
        HashMap<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("status", errorCode);
        modelMap.put("response", content);
        view.addAllObjects(modelMap);
        return view;
    }

    // 统一的成功模板
    protected ModelAndView getSuccessView() {

        return getSuccessView(StringUtils.EMPTY, "成功");
    }

    protected ModelAndView getSuccessView(final String version, final String message) {

        ModelAndView view = new ModelAndView("base/success");
        view.addObject("version", version);
        view.addObject("message", message);
        return view;
    }

    protected ModelAndView initSuccessView(String viewPath) {
        ModelAndView view = new ModelAndView(viewPath);
        view.addObject("status", "success");
        return view;
    }

    protected void printRequestParams(HttpServletRequest req) {
        if (req != null) {
            // 添加try_catch， 不能因为打印异常而导致接口使用不正常
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("requestURI==").append(req.getRequestURI()).append("!");
                @SuppressWarnings("unchecked")
                Enumeration<String> names = req.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    sb.append(name).append("=").append(req.getParameter(name)).append(",");
                }
                logger.debug(sb.toString());
            } catch (Exception e) {
            }
        }

    }

    protected ModelAndView initEncryptView() {
        ModelAndView view = new ModelAndView("base/encryptData");
        return view;
    }

    /**
     * 返回加密统一模板,用嵌套模板方式，传递token,ModelAndView，和嵌套的模板路径
     * 
     * @param view
     * @param accessToken
     * @param innerViewPath
     * @return
     */
    protected ModelAndView getIsEncryptInnerView(ModelAndView view, String accessToken, String innerViewPath) {
        return getIsEncryptInnerView(view, accessToken, innerViewPath, true);
    }

    protected ModelAndView getIsEncryptInnerView(ModelAndView view, String accessToken, String innerViewPath, boolean isEncrypt) {
        view.addObject("status", "success");
        view.addObject("innerViewPath", innerViewPath);
        view.addObject("isEncrypt", isEncrypt);
        if (StringUtils.isBlank(accessToken)) {
            accessToken = "";
        }
        SecretKeySpec secretKeySpec = null;
        /**
         * byte[] bytes = accessValidService.getAesCache(accessToken); if (bytes
         * == null) { secretKeySpec = AESCachedUtils.getSkforAES(accessToken);
         * accessValidService.setAesCache(accessToken,
         * secretKeySpec.getEncoded()); } else { secretKeySpec = new
         * SecretKeySpec(bytes, "AES"); }
         */

        view.addObject("secretKeySpec", secretKeySpec);
        logger.debug("getIsEncryptInnerView~~~~~~secretKeySpec,  " + secretKeySpec + "~~~~~加密嵌套模板innerViewPath," + innerViewPath);
        return view;
    }

    protected Long getUserId(final HttpServletRequest httpRequest) {

        return NumberUtils.toLong(ObjectUtils.defaultIfNull(httpRequest.getAttribute("userId"), "0").toString());
    }

    protected ModelAndView getBaseView(HttpServletRequest request, Map<String, Object> modelMap, String vmName) {
        ModelAndView view = new ModelAndView("base/baseVmWithCompanyStatus");
        String version = request.getParameter("version") != null ? request.getParameter("version") : "";
        if (modelMap == null)
            modelMap = new HashMap<String, Object>();
        modelMap.put("version", version);
        modelMap.put("vmName", vmName);
        view.addAllObjects(modelMap);
        
        long userId = getUserId(request);
        if(userId <= 0) {
            return view;
        }
        view.addAllObjects(modelMap);
        return view;
    }

    protected String getUserIdStr(HttpServletRequest request) {
        String userId = "";
        try {
            String requestString = request.getParameter("request");
            JSONObject userIdString = JSON.parseObject(requestString);
            if (!userIdString.containsKey("userId"))
                return null;
            String userIdDES = userIdString.getString("userId");
            userId = DESUtils.decrypt(userIdDES, SecretConstants.CHOUMEI_OLD_DESKEY);
            if (StringUtil.isBlank(userId) || !StringUtil.isNumber(userId)) {
                logger.error("userId des解密失败, raw userId:" + userIdDES + ", decrypted userId:" + userId);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取userId失败", e);
        }
        return userId;
    }

    /**
     * 获取距离格式化后的字符串
     * 
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    protected String getDistanceStr(double lat1, double lng1, double lat2, double lng2) {
        double dist = Distance.GetDistance(lat1, lng1, lat2, lng2);
        // 大于1km需要转换单位
        if (dist > 1000)
            return new DecimalFormat("0.0").format(dist / 1000) + " km";
        else
            return new DecimalFormat("0").format(dist) + " m";
    }

    protected String formatDistance(double dist) {
        if (dist > 1000)
            return new DecimalFormat("0.0").format(dist / 1000) + " km";
        else
            return new DecimalFormat("0").format(dist) + " m";
    }

    /**
     * 隐藏号码中间4位，如：18612382605 --> 186****2605
     */
    protected String handlePhone(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            char[] numArray = phone.toCharArray();
            int i = 0;
            StringBuilder handledPhone = new StringBuilder();
            for (char num : numArray) {
                if (i > 2 && i < 7) {
                    handledPhone.append("*");
                } else {
                    handledPhone.append(num);
                }
                i++;
            }
            return handledPhone.toString();
        }
        return null;
    }

}