package cn.web.front.support;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.sdk.exception.CMBaseException;
import cn.sdk.exception.ResultCode;
import cn.sdk.exception.ValidationException;
import cn.sdk.util.HttpUtils;

/**
 * Created by wubinhong on 8/20/15.
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /** 环境变量，0: [dev, test]; 1: [uat, online]*/
    @Resource(name = "env")
    private int env;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        printParameters(request);
        
        logger.error("exception stack trace detail: ", ex);
        
        handleException(request, response, ex);
        
        return new ModelAndView("/anti/result");
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        Result<String> result = new Result<String>();
        result.setVersion(request.getParameter("version"));
        result.setCode(ResultCode.SUCCESS);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        request.setAttribute("result", result);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Consts.UTF_8.name());
        if(ex instanceof CMBaseException) {
        	CMBaseException cmException = (CMBaseException) ex;
            result.setCode(cmException.getErrorCode());
            result.setMessage(cmException.getMessage());
        } else if(ex instanceof MissingServletRequestParameterException) {	/** 兼容spring mvc {@link @RequestParam#required} */ 
            result.setCode(ResultCode.SYS_INVALID_REQUEST_PARAMS);
            result.setMessage("参数校验错误");
        } else if(ex instanceof ValidationException) {
            ValidationException validationException = (ValidationException) ex;
            result.setCode(validationException.getCode());
            result.setMessage(validationException.getMessage());
        } else {
            result.setCode(ResultCode.SYS_UNKNOWN);
            if(Consts.ENV_DEV == env) {
                result.setMessage(ex.getMessage());
            } else {
                result.setMessage(ResultCode.SYS_UNKNOWN_MSG);
            }
        }
    }
    
    private void printParameters(HttpServletRequest request) {
        logger.warn("\n\n\n\n********************************** Print All Parameters of HTTP Request For This Exception **********************************");

        logger.warn("URI[{}], from IP[{}]", request.getRequestURI(), HttpUtils.findRealIp(request));
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> entity : parameters.entrySet()) {
            logger.warn("{} = {}", entity.getKey(), StringUtils.join(entity.getValue(), ","));
        }
    }
}
