package com.fyo.ggboo.infra.message;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * MessageSource (ReloadableResourceBundleMessageSource 상속)
 * 
 * @author boolancpain
 */
public class CustomMessageSource extends ReloadableResourceBundleMessageSource {
    public String getMessage(String code) {
        return super.getMessage(code, null, "error", getDefaultLocale());
    }

    public String getMessage(String code, Object... args) {
        return super.getMessage(code, args, "error", getDefaultLocale());
    }
}