package com.resource.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

/**
 * OAuthResourceConfig
 * <p>リソースサーバー用OAuth設定</p>
 * 
 * @since 2019/03/28
 */
@Getter
@Configuration
public class OAuthResourceConfig {
	/** イントロスペクションの使用可否フラグ **/
	@Value("${oauth2.provider.introspection-enable}")
	private boolean introspectionEnable;

	/** イントロスペクションエンドポイント **/
	@Value("${oauth2.provider.endpoint.introspection}")
	private String introspectionEndpoint;

	/** リソースID **/
	@Value("${oauth2.resource.resource-id}")
	private String resourceId;

	/** リソースシークレット **/
	@Value("${oauth2.resource.resource-secret}")
	private String resourceSecret;
}
