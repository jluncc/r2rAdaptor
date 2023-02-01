package org.r2r.app.config.prop;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ApiLogProp {
    private Set<String> headers = new HashSet<>();
}
