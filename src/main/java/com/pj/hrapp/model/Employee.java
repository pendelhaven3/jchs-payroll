package com.pj.hrapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Employee implements Comparable<Employee> {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private Long employeeNumber;

	private String lastName;
	private String firstName;
	private String middleName;
	private String nickname;
	private Date birthday;
	private String address;
	private String contactNumber;
	private String sssNumber;
	private String philHealthNumber;
	private String pagibigNumber;
	private String tin;
	private Date hireDate;
	
	@Enumerated(EnumType.STRING)
	private PaySchedule paySchedule;
	
	@Enumerated(EnumType.STRING)
	private PayType payType;

	@Column(columnDefinition = "date")
	private Date resignDate;
	
	@Column(columnDefinition = "boolean default false")
	private boolean resigned;
	
	@Column(length = 4000)
	private String remarks;
	
	public Employee() {
		// default constructor
	}
	
	public Employee(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Long employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
	}

	public String getSssNumber() {
		return sssNumber;
	}

	public void setSssNumber(String sssNumber) {
		this.sssNumber = sssNumber;
	}

	@Override
	public String toString() {
		return getFullName();
	}

	public String getFirstAndLastName() {
		return new StringBuilder()
				.append(firstName)
				.append(" ")
				.append(lastName)
				.toString();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPhilHealthNumber() {
		return philHealthNumber;
	}

	public void setPhilHealthNumber(String philHealthNumber) {
		this.philHealthNumber = philHealthNumber;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getTin() {
		return tin;
	}

	public void setTin(String tin) {
		this.tin = tin;
	}

	public PaySchedule getPaySchedule() {
		return paySchedule;
	}

	public void setPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
	}
	
	public String getFullName() {
		return new StringBuilder().append(lastName).append(", ").append(firstName).toString();
	}

	public static Employee withId(long id) {
		return new Employee(id);
	}

	public Employee withPaySchedule(PaySchedule paySchedule) {
		this.paySchedule = paySchedule;
		return this;
	}

	@Override
	public int compareTo(Employee o) {
		return getFullName().compareTo(o.getFullName());
	}

	public Date getResignDate() {
		return resignDate;
	}

	public void setResignDate(Date resignDate) {
		this.resignDate = resignDate;
	}

	public boolean isResigned() {
		return resigned;
	}

	public void setResigned(boolean resigned) {
		this.resigned = resigned;
	}

	public String getPagibigNumber() {
		return pagibigNumber;
	}

	public void setPagibigNumber(String pagibigNumber) {
		this.pagibigNumber = pagibigNumber;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
