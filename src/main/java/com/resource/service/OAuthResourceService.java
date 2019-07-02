package com.resource.service;

import com.resource.data.model.AccessToken;

/**
 * OAuthResourceService
 * <p>OAuthリソースサービス</p>
 * 
 * @since 2019/03/30
 */
public interface OAuthResourceService {
	/**
	 * <p>アクセストークンデータを取得する</p>
	 * @param accessToken アクセストークン
	 * @return AccessToken アクセストークンモデル
	 */
	public AccessToken getAccessToken(String accessToken);
}
