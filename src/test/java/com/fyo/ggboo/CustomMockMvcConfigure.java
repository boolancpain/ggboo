package com.fyo.ggboo;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;
/**
 * utf-8 필터를 추가하기 위해 MockMvcBuilderCustomizer 구현체 bean 생성
 * 
 * @author boolancpain
 */
@Component
public class CustomMockMvcConfigure implements MockMvcBuilderCustomizer {
	
	@Override
	public void customize(ConfigurableMockMvcBuilder<?> builder) {
		builder.addFilters(new CharacterEncodingFilter("UTF-8", true));
	}
}