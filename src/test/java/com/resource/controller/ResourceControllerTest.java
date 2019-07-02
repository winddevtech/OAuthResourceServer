package com.resource.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resource.config.OAuthResourceConfig;
import com.resource.controller.ResourceController;
import com.resource.controller.response.IntrospectionResponse;
import com.resource.data.model.AccessToken;
import com.resource.service.OAuthResourceService;
import com.resource.util.EncodeClientCredentials;

/**
 * ResourceControllerTest
 * <p>ResourceControllerのテストクラス</p>
 * 
 * @since 2019/03/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceControllerTest {
	private MockMvc mockMvc;

	@Mock
	private OAuthResourceService oAuthResourceService;

	@Mock
	private OAuthResourceConfig oAuthResourceConfig;
	
	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private ResourceController resourceController;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(resourceController).build();
	}

	/**
	 * <p>アクセストークンが無効なら401レスポンスを返す</p>
	 * resourceメソッドのテスト
	 * @throws Exception
	 * @return void
	 */
	@Test
	public void アクセストークンが無効なら401レスポンスを返す() throws Exception {
		String accessToken = "Bearer " + "accessToken";
		mockMvc.perform(post("/resource").header(HttpHeaders.AUTHORIZATION, accessToken))
				.andExpect(status().isUnauthorized());
	}

	/**
	 * <p>アクセストークンが有効ならリソースを返す</p>
	 * resourceメソッドのテスト
	 * @throws Exception
	 * @return void
	 */
	@Test
	public void アクセストークンが有効ならリソースを返す() throws Exception {
		String inToken = "accessToken";
		String authAccessToken = "Bearer " + inToken;
		
		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(false);
		AccessToken  accessToken = new AccessToken();
		accessToken.setAccessToken(inToken);
		accessToken.setId(1L);
		accessToken.setClientId("client_id");
		accessToken.setScope("scope1");
		accessToken.setUserId(1L);
		Mockito.when(oAuthResourceService.getAccessToken(inToken)).thenReturn(accessToken);
		
		MvcResult result = mockMvc.perform(post("/resource").header(HttpHeaders.AUTHORIZATION, authAccessToken))
				.andExpect(status().isOk())
				.andReturn();
		
		String contentAsString = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, String> map = objectMapper.readValue(contentAsString,  HashMap.class);
		assertThat(map.get("resource_id"), is("protected-resource-1"));
		assertThat(map.get("resource_secret"), is("protected-resource-secret-1"));
	}

	/**
	 * <p>イントロスペクションエンドポイントへの問い合わせでRestClientExceptionが発生する</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void イントロスペクションエンドポイントへの問い合わせでRestClientExceptionが発生する() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);

		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(true);
		Mockito.when(oAuthResourceConfig.getResourceId()).thenReturn("resourceId");
		Mockito.when(oAuthResourceConfig.getResourceSecret()).thenReturn("resourceSecret");
		Mockito.when(oAuthResourceConfig.getIntrospectionEndpoint()).thenReturn("http://localhost:9001/introspect");
		
		String accessToken = "";
		RequestEntity<MultiValueMap<String, String>> requestEntity = buildMockRequestEntity(accessToken, "resourceId", "resourceSecret");
		Mockito.when(restTemplate.exchange(requestEntity, IntrospectionResponse.class)).thenThrow( new RestClientException("RestClientException"));
		
		boolean enable = (boolean) method.invoke(resourceController, accessToken);
		assertThat(enable, is(false));
	}

	/**
	 * <p>イントロスペクションエンドポイントへの問い合わせでHTTPステータスコードが200以外</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void イントロスペクションエンドポイントへの問い合わせでHTTPステータスコードが200以外() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);

		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(true);
		Mockito.when(oAuthResourceConfig.getResourceId()).thenReturn("resourceId");
		Mockito.when(oAuthResourceConfig.getResourceSecret()).thenReturn("resourceSecret");
		Mockito.when(oAuthResourceConfig.getIntrospectionEndpoint()).thenReturn("http://localhost:9001/introspect");
		
		String accessToken = "";
		RequestEntity<MultiValueMap<String, String>> requestEntity = buildMockRequestEntity(accessToken, "resourceId", "resourceSecret");
		Mockito.when(restTemplate.exchange(requestEntity, IntrospectionResponse.class)).thenReturn( new ResponseEntity<>(HttpStatus.CHECKPOINT));
		
		boolean enable = (boolean) method.invoke(resourceController, accessToken);
		assertThat(enable, is(false));
	}

	/**
	 * <p>イントロスペクションエンドポイントへの問い合わせでアクセストークンが有効</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void イントロスペクションエンドポイントへの問い合わせでアクセストークンが有効() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);
		
		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(true);
		Mockito.when(oAuthResourceConfig.getResourceId()).thenReturn("resourceId");
		Mockito.when(oAuthResourceConfig.getResourceSecret()).thenReturn("resourceSecret");
		Mockito.when(oAuthResourceConfig.getIntrospectionEndpoint()).thenReturn("http://localhost:9001/introspect");

		String accessToken = "";
		RequestEntity<MultiValueMap<String, String>> requestEntity = buildMockRequestEntity(accessToken, "resourceId", "resourceSecret");
		IntrospectionResponse introspectionResponse = new IntrospectionResponse();
		introspectionResponse.setActive(true);
		Mockito.when(restTemplate.exchange(requestEntity, IntrospectionResponse.class)).thenReturn(new ResponseEntity<>(introspectionResponse, HttpStatus.OK));

		boolean enable = (boolean) method.invoke(resourceController, accessToken);
		assertThat(enable, is(true));
	}

	/**
	 * <p>イントロスペクションエンドポイントへの問い合わせでアクセストークンが無効</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void イントロスペクションエンドポイントへの問い合わせでアクセストークンが無効() throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);

		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(true);
		Mockito.when(oAuthResourceConfig.getResourceId()).thenReturn("resourceId");
		Mockito.when(oAuthResourceConfig.getResourceSecret()).thenReturn("resourceSecret");
		Mockito.when(oAuthResourceConfig.getIntrospectionEndpoint()).thenReturn("http://localhost:9001/introspect");

		String accessToken = "";
		RequestEntity<MultiValueMap<String, String>> requestEntity = buildMockRequestEntity(accessToken, "resourceId", "resourceSecret");
		IntrospectionResponse introspectionResponse = new IntrospectionResponse();
		introspectionResponse.setActive(false);
		Mockito.when(restTemplate.exchange(requestEntity, IntrospectionResponse.class)).thenReturn(new ResponseEntity<>(introspectionResponse, HttpStatus.OK));

		boolean enable = (boolean) method.invoke(resourceController, accessToken);
		assertThat(enable, is(false));
	}

	/**
	 * <p>共用DBでの問い合わせでアクセストークンが有効</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void 共用DBでの問い合わせでアクセストークンが有効() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);

		String inToken = "exist_token";
		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(false);
		AccessToken  accessToken = new AccessToken();
		accessToken.setAccessToken(inToken);
		accessToken.setId(1L);
		accessToken.setClientId("client_id");
		accessToken.setScope("scope1");
		accessToken.setUserId(1L);
		Mockito.when(oAuthResourceService.getAccessToken(inToken)).thenReturn(accessToken);
		
		boolean enable = (boolean) method.invoke(resourceController, inToken);
		assertThat(enable, is(true));
	}

	/**
	 * <p>共用DBでの問い合わせでアクセストークンが無効</p>
	 * isEnableAccessTokenメソッドのテスト
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @return void
	 */
	@Test
	public void 共用DBでの問い合わせでアクセストークンが無効() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method method = ResourceController.class.getDeclaredMethod("isEnableAccessToken", String.class);
		method.setAccessible(true);

		String inToken = "non_exist_token";
		Mockito.when(oAuthResourceConfig.isIntrospectionEnable()).thenReturn(false);
		Mockito.when(oAuthResourceService.getAccessToken(inToken)).thenReturn(null);

		boolean enable = (boolean) method.invoke(resourceController, inToken);
		assertThat(enable, is(false));
	}

	// 外部サーバーからの戻り値をMock生成する
	private RequestEntity<MultiValueMap<String, String>> buildMockRequestEntity(String inToken, String clientId,
			String clientSecret) {
		String encodeClientCredentials = EncodeClientCredentials.encode(clientId, clientSecret);

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
		formParams.add("token", inToken);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Basic " + encodeClientCredentials);
		httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(formParams, httpHeaders,
				HttpMethod.POST, URI.create(oAuthResourceConfig.getIntrospectionEndpoint()));

		return requestEntity;
	}
}
