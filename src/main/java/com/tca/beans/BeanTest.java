package com.tca.beans;

public class BeanTest {
	
	public BeanTest() {}
	
	public BeanTest(String username, String password) {
		this.username = username;
		this.password = password;
		System.err.println(this);
	}

	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "BeanTest [username=" + username + ", password=" + password + "]";
	}
	
}
