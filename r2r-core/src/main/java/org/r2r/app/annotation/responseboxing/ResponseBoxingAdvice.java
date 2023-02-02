package org.r2r.app.annotation.responseboxing;

import lombok.extern.slf4j.Slf4j;
import org.r2r.app.model.ApiResponse;
import org.r2r.app.util.JsonUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ResponseBoxingAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return !returnType.getDeclaringClass().isAnnotationPresent(IgnoreResponseBoxing.class) &&
                Objects.nonNull(returnType.getMethod()) &&
                !returnType.getMethod().isAnnotationPresent(IgnoreResponseBoxing.class) &&
                !returnType.getGenericParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                return JsonUtils.toJson(ApiResponse.ok(body));
            } catch (Exception e) {
                return ApiResponse.fail(-1, "返回String出现异常");
            }
        }
        if (body instanceof ApiResponse<?>) {
            return body;
        }
        return ApiResponse.ok(body);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> exceptionHandle(Exception exception) {
        log.warn("[exceptionHandle]exception occurred", exception);
        return ApiResponse.fail(-1, exception.getMessage());
    }

}
