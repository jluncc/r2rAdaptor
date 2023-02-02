package org.r2r.app.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.r2r.app.model.ApiHandleContext;

import java.util.Optional;

@Slf4j
@UtilityClass
public class ApiHandleContextUtils {

    private static final ThreadLocal<ApiHandleContext> HANDLE_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static ApiHandleContext getApiHandleContext() {
        return HANDLE_CONTEXT_THREAD_LOCAL.get();
    }

    public static void setApiHandleContext(ApiHandleContext apiHandleContext) {
        HANDLE_CONTEXT_THREAD_LOCAL.set(apiHandleContext);
    }

    public static String getLogId() {
        return Optional.ofNullable(getApiHandleContext()).map(ApiHandleContext::getLogId).orElse("");
    }

}
