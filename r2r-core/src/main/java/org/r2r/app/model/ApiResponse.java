package org.r2r.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.r2r.app.util.ApiHandleContextUtils;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String msg;
    private T data;
    private String logId;

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(0, "ok", data, ApiHandleContextUtils.getLogId());
    }

    public static <T> ApiResponse<T> fail(Integer code, String msg) {
        return new ApiResponse<>(code, msg, null, ApiHandleContextUtils.getLogId());
    }

    public static boolean isOk(ApiResponse<?> apiResponse) {
        return Objects.nonNull(apiResponse) && apiResponse.getCode() == 0;
    }

}
