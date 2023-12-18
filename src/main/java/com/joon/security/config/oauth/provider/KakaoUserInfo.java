package com.joon.security.config.oauth.provider;

import java.util.Map;

import org.json.simple.JSONObject;

public class KakaoUserInfo implements OAuth2UserInfo {

	private JSONObject attributes;
	private JSONObject account;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = new JSONObject(attributes);
		this.account = new JSONObject((Map) this.attributes.get("kakao_account"));
	}

	@Override
	public String getProviderId() {
		return attributes.get("id") + "";
	}

	@Override
	public String getProvider() {
		return "KAKAO";
	}

	@Override
	public String getEmail() {
		return (String) account.get("email");
	}

	@Override
	public String getName() {
		return (String) ((Map) account.get("profile")).get("nickname");
	}
}
