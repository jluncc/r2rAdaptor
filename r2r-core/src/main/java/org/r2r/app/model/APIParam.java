package org.r2r.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIParam {
    @NonNull
    private String key;

    /**
     * 是否使用同步方式处理请求. 若使用异步, 请求任务会交给线程池中的线程处理, 直接返回.
     * <br/>
     * 默认使用同步方式
     */
    private boolean sync = true;

    private Map<String, Object> params;
}
