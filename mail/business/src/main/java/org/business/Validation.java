package org.business;

public class Validation {
	public boolean passwordsMatch(String password, String repeatPassword) {
		if (password.equals(repeatPassword))
			return true;
		return false;
	}
	
	public boolean fieldIsNotClear(String field) {
		if (field.equals("")) 
			return false;
		return true;
	}
	
	public boolean correctMailAddress(String address) {
		return true;
	}
	
	public boolean correctPassword(String password) {
		return true;
	}
	
	public boolean correctFirstName(String name) {
		return true;
	}
	
	public boolean correctLastName(String name) {
		return true;
	}
	
	public boolean correctPhone(String phone) {
		return true;
	}
	
	public boolean correctFieldTo(String fieldTo) {
		return true;
	}
}
