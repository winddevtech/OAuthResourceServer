package com.resource.data.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.data.model.AccessToken;
import com.resource.data.repository.AccessTokenRepository;

/**
 * AccessTokenRepositoryTest
 * <p>AccessTokenRepositoryのテストクラス</p>
 * 
 * @since 2019/05/05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccessTokenRepositoryTest {
	@Autowired
	private AccessTokenRepository accessTokenRepository;
	
	@Before
	public void setUp() throws Exception {
		accessTokenRepository.deleteAll();
	}

	/**
	 * <p>指定のアクセストークンに該当するモデルが1件取得できる</p>
	 * findByAccessTokenメソッドのテスト
	 * @return void
	 */
	@Test
	public void 指定のアクセストークンに該当するモデルが1件取得できる() {
		String targetAccessToken = "test-access-token1";
		AccessToken accessToken = new AccessToken();
		accessToken.setClientId("clientId");
		accessToken.setAccessToken(targetAccessToken);
		accessToken.setScope("scope1");
		accessToken.setUserId(1L);
		accessTokenRepository.save(accessToken);
		
		AccessToken resultAccessToken = accessTokenRepository.findByAccessToken(targetAccessToken);

		assertThat(resultAccessToken.getAccessToken(), is(targetAccessToken));
	}

	/**
	 * <p>アクセストークンを指定しない場合はモデルが取得できない</p>
	 * findByAccessTokenメソッドのテスト
	 * @return void
	 */
	@Test
	public void アクセストークンを指定しない場合はモデルが取得できない() {
		String targetAccessToken = "test-access-token2";
		AccessToken accessToken = new AccessToken();
		accessToken.setClientId("clientId");
		accessToken.setAccessToken(targetAccessToken);
		accessToken.setScope("scope1");
		accessToken.setUserId(1L);
		accessTokenRepository.save(accessToken);
		
		AccessToken resultAccessToken = accessTokenRepository.findByAccessToken(null);

		assertThat(resultAccessToken, is(nullValue()));
	}
	
	/**
	 * <p>アクセストークンがDBに存在しない場合はモデルが取得できない</p>
	 * findByAccessTokenメソッドのテスト
	 * @return void
	 */
	@Test
	public void アクセストークンがDBに存在しない場合はモデルが取得できない() {
		String targetAccessToken = "non_exist_token";
		AccessToken resultAccessToken = accessTokenRepository.findByAccessToken(targetAccessToken);
		assertThat(resultAccessToken, is(nullValue()));
	}

}
