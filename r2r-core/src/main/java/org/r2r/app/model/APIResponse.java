package org.r2r.app.model;

import lombok.Data;

@Data
public class APIResponse<T> {
    private int code;
    private String message;
    private T data;
    private long logId;
}
