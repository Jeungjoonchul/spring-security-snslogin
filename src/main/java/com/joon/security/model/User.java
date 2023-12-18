package com.joon.security.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ORM - Object Relation Mapping

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_no")
	private int userNo;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_password")
	private String userPassword;
	
	@Column(name="user_email")
	private String userEmail;
	
	@Column(name="user_role")
	private String userRole; //ROLE_USER, ROLE_ADMIN
	
	@Column(name="createDate")
	@CreationTimestamp
	private Timestamp createDate;
	
	// OAuth를 위해 구성한 추가 필드 2개
	@Column(name="provider")
	private String provider;
	
	@Column(name="providerId")
	private String providerId;
}