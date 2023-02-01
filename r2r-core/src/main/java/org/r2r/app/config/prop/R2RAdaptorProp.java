package org.r2r.app.config.prop;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "r2r-adaptor")
public class R2RAdaptorProp {
    @Nonnull
    private ApiLogProp apiLog;
}
