package com.example.imonitor.entity;

public class Account {
	private int accountid;
	private String accountname;
	private String password;
	private String email;
	public Account(int accountid, String accountname, String email,
			String password) {
		this.accountid = accountid;
		this.accountname = accountname;
		this.email = email;
		this.password = password;
	}
	public Account() {
		
	}
	public int getAccountid() {
		return accountid;
	}
	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
