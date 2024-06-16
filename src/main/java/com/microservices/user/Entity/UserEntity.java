package com.microservices.user.Entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	@Column(unique = true, nullable = false)
	private String userName;
	private String userPassword;
	private String userFirstName;
	private String userLastName;
	@Column(unique = true, nullable = false)
	private String userEmailId;
	@Column(unique = true, nullable = false)
	private String userMobileNumber;
	private String userRole;
	private String subscriptionCategory;
	private boolean isEmailVerified;
	private Integer emailVerificationOtp;
	private boolean isMobileVerified;
	private Integer mobileVerificationOtp;
}
