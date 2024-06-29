package com.microservices.user.dto.awsSns;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.microservices.user.Entity.UserEntity;
import com.microservices.user.model.UserRegistrationRequest;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = Employee.class)
public class UserDetails {

	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;

	public UserDetails(UserRegistrationRequest user){
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.emailId = user.getEmailAddress();
		this.mobileNumber = user.getMobileNumber();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	@Override
	public String toString() {
		return "UserDetails{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", emailId='" + emailId + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				'}';
	}
}
