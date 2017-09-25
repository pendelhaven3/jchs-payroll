package com.pj.hrapp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CompanyProfile {

	@Id
	private Long id;

	private String name;
	private String address;
	private String contactNumbers;
	private String mobileNumber;
	private String emailAddress;
	private String tin;
	private String sssNumber;
	private String pagibigNumber;
	private String philhealthNumber;

	public CompanyProfile() {
		// do nothing
	}
	
	public CompanyProfile(long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumbers() {
		return contactNumbers;
	}

	public void setContactNumbers(String contactNumbers) {
		this.contactNumbers = contactNumbers;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public String getSssNumber() {
		return sssNumber;
	}

	public void setSssNumber(String sssNumber) {
		this.sssNumber = sssNumber;
	}

	public String getPagibigNumber() {
		return pagibigNumber;
	}

	public void setPagibigNumber(String pagibigNumber) {
		this.pagibigNumber = pagibigNumber;
	}

	public String getPhilhealthNumber() {
		return philhealthNumber;
	}

	public void setPhilhealthNumber(String philhealthNumber) {
		this.philhealthNumber = philhealthNumber;
	}

}
