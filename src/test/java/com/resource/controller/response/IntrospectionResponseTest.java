package com.resource.controller.response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;


/**
 * IntrospectionResponseTest
 * <p>IntrospectionResponseのテストクラス</p>
 * @since 2019/06/09
 */
public class IntrospectionResponseTest {
	private IntrospectionResponse introspectionResponse = new IntrospectionResponse();

	/**
	 * <p>トークンの有効性を取得する</p>
	 */
	@Test
	public void トークンの有効性を取得する() {
		boolean isActive = true;
		introspectionResponse.setActive(isActive);
		assertThat(introspectionResponse.isActive(), is(isActive));
	}
	
	/**
	 * <p>トークンの発行者を取得する</p>
	 */
	@Test
	public void トークンの発行者を取得する() {
		String iss = "iss";
		introspectionResponse.setIss(iss);
		assertThat(introspectionResponse.getIss(), is(iss));
	}
	/**
	 * <p>トークンの受け手を取得する</p>
	 */
	@Test
	public void トークンの受け手を取得する() {
		String aud = "aud";
		introspectionResponse.setAud(aud);
		assertThat(introspectionResponse.getAud(), is(aud));
	}
	/**
	 * <p>トークンの対象者を取得する</p>
	 */
	@Test
	public void トークンの対象者を取得する() {
		String sub = "sub";
		introspectionResponse.setSub(sub);
		assertThat(introspectionResponse.getSub(), is(sub));
	}
	
	/**
	 * <p>ユーザー名を取得する</p>
	 */
	@Test
	public void ユーザー名を取得する() {
		String username = "username";
		introspectionResponse.setUsername(username);
		assertThat(introspectionResponse.getUsername(), is(username));
	}
	
	/**
	 * <p>スコープを取得する</p>
	 */
	@Test
	public void スコープを取得する() {
		String scope = "scope";
		introspectionResponse.setScope(scope);
		assertThat(introspectionResponse.getScope(), is(scope));
	}
	
	/**
	 * <p>クライアントIDを取得する</p>
	 */
	@Test
	public void クライアントIDを取得する() {
		String clientId = "clientId";
		introspectionResponse.setClientId(clientId);
		assertThat(introspectionResponse.getClientId(), is(clientId));
	}

}
