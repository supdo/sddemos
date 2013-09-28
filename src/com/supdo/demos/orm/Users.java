package com.supdo.demos.orm;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

@DatabaseTable(tableName = "users")
public class Users {
	@DatabaseField(generatedId=true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private int age;
	@DatabaseField
	private String company;
	@DatabaseField
	private String PhoneNum;
	
	public Users() {
	}
	
	public Users(String name, int age, String company, String PhoneNum) {
		this.name = name;
		this.age = age;
		this.company = company;
		this.PhoneNum = PhoneNum;
	}

	public int getId(){
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPhoneNum() {
		return PhoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	
	
}
