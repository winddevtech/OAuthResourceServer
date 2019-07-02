package com.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * BeanConfig
 * <p>Bean定義クラス</p>
 * 
 * @since 2019/05/04
 */
@Configuration
public class BeanConfig {
	/**
	 * <p>RestTemplateのBean</p>
	 * 
	 * @return RestTemplate
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
