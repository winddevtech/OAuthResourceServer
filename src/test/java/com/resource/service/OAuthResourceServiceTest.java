package com.resource.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.data.model.AccessToken;
import com.resource.data.repository.AccessTokenRepository;
import com.resource.service.OAuthResourceService;

/**
 * OAuthResourceServiceTest
 * <p>OAuthResourceServiceのテストクラス</p>
 * 
 * @since 2019/05/05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuthResourceServiceTest {
	@Autowired
	private OAuthResourceService oAuthResourceService;
	
	@Autowired
	private AccessTokenRepository accessTokenRepository;

	/**
	 * <p>指定のアクセストークンに該当するアクセストークンモデルを取得する</p>
	 * getAccessTokenメソッドのテスト
	 * @return void
	 */
	@Test
	@Sql(statements = {
	"INSERT INTO access_token (access_token, client_id, scope, user_id) VALUES ('accessToken1', 'clientId', 'scope1', 1)" })
	public void 指定のアクセストークンに該当するアクセストークンモデルを取得する() {
		String targetAccessToken = "accessToken1";
		
		AccessToken accessToken = oAuthResourceService.getAccessToken(targetAccessToken);
		assertThat(accessToken.getAccessToken(), is(notNullValue()));
		
		// 次回テスト時の為に削除する
		accessTokenRepository.delete(accessToken);
	}

}
