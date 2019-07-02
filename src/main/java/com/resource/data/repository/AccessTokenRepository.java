package com.resource.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.resource.data.model.AccessToken;

/**
 * AccessTokenRepository
 * <p>アクセストークンリポジトリ</p>
 * 
 * @since 2019/03/30
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
	/**
	 * <p>アクセストークンに一致する内容を探し出す</p>
	 * @param accessToken アクセストークン
	 * @return AccessToken アクセストークンモデル
	 */
	@Transactional(readOnly = true)
	@Query(value = "select * from access_token where access_token like :access_token limit 1", nativeQuery = true)
	public AccessToken findByAccessToken(@Param("access_token") String accessToken);
}
