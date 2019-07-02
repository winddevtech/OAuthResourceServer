package com.resource.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.config.OAuthResourceConfig;

/**
 * OAuthResourceConfigTest
 * <p>OAuthResourceConfigのテストクラス</p>
 * 
 * @since 2019/05/03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuthResourceConfigTest {
	@Autowired
	private OAuthResourceConfig oAuthResourceConfig;

	/**
	 * <p>イントロスペクションの使用可否フラグを取得する</p>
	 */
	@Test
	public void イントロスペクションの使用可否フラグを取得する() {
		assertThat(oAuthResourceConfig.isIntrospectionEnable(), is(true));
	}
	
	/**
	 * <p>イントロスペクションエンドポイントを取得する</p>
	 */
	@Test
	public void イントロスペクションエンドポイントを取得する() {
		assertThat(oAuthResourceConfig.getIntrospectionEndpoint(), notNullValue());
	}
	
	/**
	 * <p>リソースIDを取得する</p>
	 */
	@Test
	public void リソースIDを取得する() {
		assertThat(oAuthResourceConfig.getResourceId(), notNullValue());
	}
	
	/**
	 * <p>リソースシークレットを取得する</p>
	 */
	@Test
	public void リソースシークレットを取得する() {
		assertThat(oAuthResourceConfig.getResourceSecret(), notNullValue());
	}

}
