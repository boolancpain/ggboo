package com.fyo.ggboo.infra.message;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MessageSource set
 * 
 * @author boolancpain
 */
@Configuration
public class MessageSourceConfig implements WebMvcConfigurer {
	
	/**
	 * MessageSource bean 등록
	 */
	@Bean
	public MessageSource messageSource() {
		CustomMessageSource messageSource = new CustomMessageSource();
		messageSource.setBasename("classpath:/messages/message");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name()); // default 'UTF-8'
		messageSource.setUseCodeAsDefaultMessage(true); // 메세지를 찾지 못한 경우 예외를 발생시키지 않고 코드를 메세지로 사용
		messageSource.setDefaultLocale(Locale.KOREA); // default 'ko_KR'
		return messageSource;
	}
	
	/**
	 * MessageSource를 validation에서 활용할 수 있도록 bean 등록
	 * 
	 * @param messageSource
	 */
	@Bean
	public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource);
		return localValidatorFactoryBean;
	}
}