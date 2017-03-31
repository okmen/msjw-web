package cn.web.front.resolver;

import java.lang.reflect.Constructor;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cn.web.front.annotation.ApiRequestParam;

public class ApiRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(ApiRequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {

        Class<?> clazz = parameter.getParameterType();

        Constructor<?> constructor = clazz.getConstructor(new Class[] { String.class });
        
        ApiRequestParam requestParam = parameter.getParameterAnnotation(ApiRequestParam.class);
        
        return constructor.newInstance(webRequest.getParameter(requestParam.value()));

    }

}
