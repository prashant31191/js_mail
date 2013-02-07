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
	
	public boolean correctMailBegin(String mailBegin) {
		if (!mailBegin.matches("([a-zA-Z][\\w\u002E\u005F\u002D]*)") || mailBegin.length() < 4)
			return false;
		return true;
	}
	
	public boolean correctMailAddress(String address) {
		if (!address.matches("(([a-zA-Z][\\w\u002E\u005F\u002D]*)@[\\w[.]]*\\.+([a-z]+))")
				|| address.length() < 6)
			return false;
		return true;
	}
	
	public boolean correctPassword(String password) {
		if (!password.matches("([\\w\u002C\u0025\u002A\u002E\u005F\u002D]*)") 
				|| password.length() < 8)
			return false;
		return true;
	}
	
	public boolean correctName(String name) {
		if (!name.matches("([a-zA-Zа-яА-Я]*)") || name.length() < 2)
			return false;
		return true;
	}
	
	public boolean correctPhone(String phone) {
		if (!phone.matches("([\u002B]?[\\d]*)") || phone.length() < 6)
			return false;
		return true;
	}
	
	public boolean correctFieldTo(String fieldTo) {
		String[] emails = fieldTo.trim().split(";");
		
		for (String email: emails) {
			if (!correctMailAddress(email.trim()))
				return false;
		}
		return true;
	}
}
