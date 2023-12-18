package com.joon.security.config.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.joon.security.config.auth.PrincipalDetails;
import com.joon.security.config.oauth.provider.FacebookUserInfo;
import com.joon.security.config.oauth.provider.GoogleUserInfo;
import com.joon.security.config.oauth.provider.KakaoUserInfo;
import com.joon.security.config.oauth.provider.NaverUserInfo;
import com.joon.security.config.oauth.provider.OAuth2UserInfo;
import com.joon.security.model.User;
import com.joon.security.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;

	// 구글로 부터 받은 userRequest 데이터에 대한 후처리 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

		// 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리) ->
		// AccessToken요청
		// userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("getAttributes : " + oAuth2User.getAttributes());

		OAuth2UserInfo oAuth2UserInfo = null;
		String snsType =userRequest.getClientRegistration().getRegistrationId(); 
		if (snsType.equals("google")) {
			System.out.println("구글 로그인 요청");
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		} else if (snsType.equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
		} else if (snsType.equals("naver")) {
			System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
		} else if(snsType.equals("kakao")) {
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		} else {
			System.out.println("우리는 구글과 페이스북과 네이버만 지원해요");
		}
		String provider = oAuth2UserInfo.getProvider();
		String providerId = oAuth2UserInfo.getProviderId();
		String userId = provider+"_"+providerId;
		String userPassword = new BCryptPasswordEncoder().encode("getInThere");
		String userName = oAuth2UserInfo.getName();
		String userEmail = oAuth2UserInfo.getEmail();
		String userRole = "ROLE_USER";

		User userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) {
			System.out.println("소셜 로그인 가입");
			userEntity = User.builder()
					.userId(userId)
					.userPassword(userPassword)
					.userName(userName)
					.userEmail(userEmail)
					.userRole(userRole)
					.provider(provider)
					.providerId(providerId).build();
			userRepository.save(userEntity);
		} else {

		}

		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}
