package com.resource.controller.response;

import lombok.Data;

/**
 * IntrospectionResponse
 * <p>認可サーバーのイントロスペクションコントローラーからのレスポンス</p>
 * 
 * @since 2019/03/31
 */
@Data
public class IntrospectionResponse {
	/** トークンの有効性 **/
	private boolean active;

	/** トークンの発行者 **/
	private String iss;

	/** トークンの受け手 **/
	private String aud;

	/** トークンの対象者 **/
	private String sub;

	/** ユーザー名 **/
	private String username;

	/** スコープ **/
	private String scope;

	/** クライアントID **/
	private String clientId;
}
