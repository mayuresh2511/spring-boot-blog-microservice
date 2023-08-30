package com.microservices.user.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable{

	private Long Id;
	private String deparmentName;
	private String deparmentAddress;
	private String departmentCode;
	
	
	public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


	public String getDeparmentName() {
		return deparmentName;
	}


	public void setDeparmentName(String deparmentName) {
		this.deparmentName = deparmentName;
	}


	public String getDeparmentAddress() {
		return deparmentAddress;
	}


	public void setDeparmentAddress(String deparmentAddress) {
		this.deparmentAddress = deparmentAddress;
	}


	public String getDepartmentCode() {
		return departmentCode;
	}


	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}


	@Override
	public String toString() {
		return "Department [Id=" + Id + ", deparmentName=" + deparmentName + ", deparmentAddress=" + deparmentAddress
				+ ", departmentCode=" + departmentCode + "]";
	}
	
	
}
