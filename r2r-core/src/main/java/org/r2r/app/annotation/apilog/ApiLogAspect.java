package org.r2r.app.annotation.apilog;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.r2r.app.config.prop.R2RAdaptorProp;
import org.r2r.app.util.JsonUtils;
import org.r2r.app.util.RequestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class ApiLogAspect {

    @Resource
    private R2RAdaptorProp r2rAdaptorProp;

    @Pointcut("execution(public * org.r2r.app.controller..*(..)) && " +
            "!@annotation(org.r2r.app.annotation.apilog.IgnoreApiLog)")
    protected void apiLog() {
    }

    @Around(value = "apiLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder(getRequestInputInfo(proceedingJoinPoint));

        long startTime = System.currentTimeMillis();
        Object proceed = proceedingJoinPoint.proceed();
        long costTime = System.currentTimeMillis() - startTime;

        String returnType = null;
        String returnVal = "null";
        if (Objects.nonNull(proceed)) {
            returnType = proceed.getClass().getSimpleName();
            returnVal = JsonUtils.toJson(proceed);
        }

        stringBuilder.append(" -END- ")
                .append(String.format("returnType:%s|", returnType))
                .append(String.format("returnVal:%s|", returnVal))
                .append(String.format("cost:%sms", costTime));
        log.info(stringBuilder.toString());
        return proceed;
    }

    private String getRequestInputInfo(JoinPoint joinPoint) {
        HttpServletRequest httpServletRequest = RequestUtils.getHttpServletRequest();
        if (Objects.isNull(httpServletRequest)) {
            return "";
        }
        return getUriHeadersParamsStr(httpServletRequest) + getBodyStr(joinPoint);
    }

    private String getUriHeadersParamsStr(HttpServletRequest request) {
        StringBuilder apiInputInfo = new StringBuilder();
        String uri = RequestUtils.getRequestURI();
        apiInputInfo.append(String.format("uri:%s|", uri));
        apiInputInfo.append(String.format("headers:{%s}|", getHeadersDetail(request)));
        apiInputInfo.append(String.format("params:{%s}|", getParamsDetail(request)));
        return apiInputInfo.toString();
    }

    private String getHeadersDetail(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (Objects.isNull(headerNames)) {
            return "";
        }

        StringBuilder headerBuilder = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String header = request.getHeader(name);
            // 只打印配置的header字段
            if (r2rAdaptorProp.getApiLog().getHeaders().contains(name)) {
                headerBuilder.append(name).append(":").append(header).append(",");
            }
        }

        return delLastComma(headerBuilder);
    }

    private String delLastComma(StringBuilder stringBuilder) {
        if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    private String getParamsDetail(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollectionUtils.isEmpty(parameterMap)) {
            return "";
        }

        StringBuilder paramsBuilder = new StringBuilder();
        parameterMap.forEach((key, value) -> {
            try {
                paramsBuilder.append(key).append(":").append(JsonUtils.toJson(value)).append(",");
            } catch (Exception e) {
                log.warn("paramsBuilder handle error.");
            }
        });
        return delLastComma(paramsBuilder);
    }

    private String getBodyStr(JoinPoint joinPoint) {
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            return String.format("method:%s|", methodSignature.getMethod().getName()) +
                    String.format("body:{%s}", getBodyDetail(methodSignature.getParameterNames(), joinPoint.getArgs()));
        }
        return "";
    }

    private String getBodyDetail(String[] parameterNames, Object[] args) {
        StringBuilder bodyBuilder = new StringBuilder();
        if (ArrayUtils.isNotEmpty(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                bodyBuilder.append(parameterName).append(":");
                try {
                    if (args[i] != null) {
                        bodyBuilder.append(JsonUtils.toJson(args[i]));
                    } else {
                        bodyBuilder.append("null");
                    }
                } catch (Exception e) {
                    log.warn("params handle error.");
                }
                bodyBuilder.append(",");
            }
        }
        return delLastComma(bodyBuilder);
    }

    @AfterThrowing(pointcut = "apiLog()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        String errorLog = "exception occurred --- " + getBodyStr(joinPoint) +
                String.format(" --- uri:%s;", RequestUtils.getRequestURI()) +
                String.format("type:%s;", ex.getClass().getSimpleName()) +
                String.format("msg:%s;", ex.getMessage());
        log.warn(errorLog);
    }

}
