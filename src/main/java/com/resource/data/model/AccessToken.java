package com.resource.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * AccessToken
 * <p>アクセストークンモデル</p>
 * 
 * @since 2019/03/30
 */
@Data
@Entity
public class AccessToken {
	/** 管理ID **/
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/** アクセストークン **/
	private String accessToken;

	/** クライアントID **/
	private String clientId;

	/** スコープ **/
	private String scope;

	/** ユーザーID **/
	private Long userId;
}
