package com.resource.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * EncodeClientCredentials
 * <p>クライアント情報エンコード</p>
 * 
 * @since 2019/03/30
 */
public class EncodeClientCredentials {

	/**
	 * <p>クライアント情報をエンコードする</p>
	 * 
	 * @param clientId クライアントID
	 * @param clientSecret クライアントシークレット
	 * @return String エンコードしたクライアント情報
	 */
	public static String encode(String clientId, String clientSecret) {
		String encodeClientCredentials = Base64.getEncoder()
				.encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
		return encodeClientCredentials;
	}
}
