package com.resource.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.resource.util.EncodeClientCredentials;

/**
 * EndodeClientCredentialsTest
 * <p>EncodeClientCredentialsのテストクラス</p>
 * 
 * @since 2019/05/03
 */
public class EndodeClientCredentialsTest {
	/**
	 * <p>クライアント情報がエンコードされている</p>
	 * encodeのテストクラス
	 * @return void
	 */
	@Test
	public void クライアント情報がエンコードされている() {
		String clientId = "client-id-1";
		String clientSecret = "client-secret-1";
		String encodeStr = EncodeClientCredentials.encode(clientId, clientSecret);
		assertThat(encodeStr, notNullValue());
	}

}
