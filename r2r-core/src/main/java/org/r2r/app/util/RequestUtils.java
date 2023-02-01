package org.r2r.app.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
@UtilityClass
public class RequestUtils {

    public static HttpServletRequest getHttpServletRequest() {
        return Objects.isNull(RequestContextHolder.getRequestAttributes()) ?
                null : ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static String getRequestURI() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (Objects.isNull(httpServletRequest)) {
            return "";
        }
        return httpServletRequest.getRequestURI();
    }

}
