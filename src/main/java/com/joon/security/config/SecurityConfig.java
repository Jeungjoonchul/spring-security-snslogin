package com.joon.security.config;

import org.springframework.beans.factory.annotation.Autowired;

//1. 코드받기(인증) 
//2. 엑세스토큰(권한) 
//3. 사용자프로필 정보를 가져오고 
//4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
//4-2. 추가 정보(주소, 등급 등...) 요청

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.joon.security.config.oauth.PrincipalOauth2UserService;

@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터 체인에 등록됨
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)//secured 어노테이션 활성화, preAuthorize, postAuthorize 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	//해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소!!
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.usernameParameter("userId")
			.passwordParameter("userPassword")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService); //구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 코드X, (엑세스토큰+사용자프로필정보)
	}
}