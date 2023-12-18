package com.joon.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joon.security.model.User;

//CRUD 사용가능(상속 받아서)
//@Repository라는 어노테이션이 없어도 IoC됨, 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
	
	//findBy=규칙 -> Username 문법
	// select * from user where username = 1?
	public User findByUserId(String user_id); //Jpa name 함수
	
}
