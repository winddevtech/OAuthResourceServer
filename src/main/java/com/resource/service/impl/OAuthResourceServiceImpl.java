package com.resource.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resource.data.model.AccessToken;
import com.resource.data.repository.AccessTokenRepository;
import com.resource.service.OAuthResourceService;

/**
 * OAuthResourceServiceImpl
 * <p>OAuthリソースサービス実装クラス</p>
 * 
 * @since 2019/03/30
 */
@Service
public class OAuthResourceServiceImpl implements OAuthResourceService {
	@Autowired
	private AccessTokenRepository accessTokenRepository;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public AccessToken getAccessToken(String accessToken) {
		return accessTokenRepository.findByAccessToken(accessToken);
	}
}
