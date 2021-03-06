package com.chrischen.ad.advice;

import com.chrischen.ad.annotation.IgnoreResponseAdvice;
import com.chrischen.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by Chris Chen
 */
@RestControllerAdvice
@SuppressWarnings("all")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {

        //if the class has the annotation "IgnoreResponseAdvice", we do not response
        if (methodParameter.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
            return false;
        }

        //if the method has the annotation "IgnoreResponseAdvice", we do not response
        if (methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class
        )) {
            return false;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        CommonResponse<Object> response = new CommonResponse<>(0, "");

        //if o is null
        if (null == o) {
            return response;
        }
        //if o is already type of CommonResponse
        else if (o instanceof CommonResponse) {
            response = (CommonResponse<Object>) o;
        }
        //o is a regular object
        else {
            response.setData(o);
        }
        return response;
    }
}
