package com.resource.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.resource.config.OAuthResourceConfig;
import com.resource.controller.response.IntrospectionResponse;
import com.resource.data.model.AccessToken;
import com.resource.service.OAuthResourceService;
import com.resource.util.EncodeClientCredentials;

/**
 * ResourceController
 * <p>リソースサーバーコントローラー</p>
 * 
 * @since 2019/03/28
 */
@RestController
public class ResourceController {
	@Autowired
	private OAuthResourceService oAuthResourceService;

	@Autowired
	private OAuthResourceConfig oAuthResourceConfig;
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * <p>リソース情報を取得する</p>
	 * 
	 * @param auth httpヘッダーのAuthorization
	 * @return ResponseEntity<Object>
	 */
	@CrossOrigin(origins = "http://localhost:9000")
	@RequestMapping(value = "/resource", method = RequestMethod.POST)
	public ResponseEntity<Object> resource(@RequestHeader("Authorization") String auth) {
		String inToken = null;

		if (auth != null && auth.toLowerCase().indexOf("bearer") == 0) {
			inToken = auth.replace("Bearer ", "");
		}

		boolean isActive = false;
		isActive = isEnableAccessToken(inToken);
		if (!isActive) {
			// アクセストークンが無効ならhttpステータスを返す
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

		// リソースを返す
		Map<String, String> map = new HashMap<String, String>();
		map.put("resource_id", "protected-resource-1");
		map.put("resource_secret", "protected-resource-secret-1");
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	/**
	 * <p>アクセストークンの使用可否を確認する</p>
	 * @param inToken アクセストークン
	 * @return boolean 使用可否フラグ
	 */
	private boolean isEnableAccessToken(String inToken) {
		// イントロスペクションが有効なら認可サーバーに問い合わせる
		if (oAuthResourceConfig.isIntrospectionEnable()) {
			String resourceId = oAuthResourceConfig.getResourceId();
			String resourceSecret = oAuthResourceConfig.getResourceSecret();
			String encodeClientCredentials = EncodeClientCredentials.encode(resourceId, resourceSecret);

			MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
			formParams.add("token", inToken);

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + encodeClientCredentials);
			httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

			RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(formParams, httpHeaders,
					HttpMethod.POST, URI.create(oAuthResourceConfig.getIntrospectionEndpoint()));

			ResponseEntity<IntrospectionResponse> responseEntity = null;
			try {
				responseEntity = restTemplate.exchange(requestEntity, IntrospectionResponse.class);
			} catch (RestClientException e) {
				// RestClientException
				return false;
			}

			HttpStatus httpStatus = responseEntity.getStatusCode();
			if (httpStatus != HttpStatus.OK) {
				// httpステータスが200以外
				return false;
			}

			// 認可サーバーでの認証結果を返す
			IntrospectionResponse introspectionResponse = responseEntity.getBody();
			boolean isActive = introspectionResponse.isActive();
			return isActive;
		}

		// 共用のDBで問い合わせる
		AccessToken accessToken = oAuthResourceService.getAccessToken(inToken);
		if (accessToken == null) {
			return false;
		}
		return true;
	}

}
