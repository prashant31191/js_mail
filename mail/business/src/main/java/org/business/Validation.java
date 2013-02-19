package org.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Validation class
 * 
 * @author Fomin
 * @version 1.0
 */
public class Validation {
	/**
	 * Checks if the passwords match
	 * 
	 * @param password
	 *            Password
	 * @param repeatPassword
	 *            Password duplicate
	 * @return true, in case the passwords match, otherwise false
	 */
	public boolean passwordsMatch(String password, String repeatPassword) {
		if (password.equals(repeatPassword))
			return true;
		return false;
	}

	/**
	 * Checks if the field is not clear
	 * 
	 * @param field
	 * @return true, in case the field is not clear, otherwise false
	 */
	public boolean fieldIsNotClear(String field) {
		if (field.equals(""))
			return false;
		return true;
	}

	/**
	 * Checks if the prefix of mail-address is correct, i.e begins with Latin
	 * letter, contains letters or ".", "-", "_", and consists of more than 3
	 * characters
	 * 
	 * @param mailBegin
	 * @return true, in case the prefix is correct, otherwise false
	 */
	public boolean correctMailBegin(String mailBegin) {
		if (!mailBegin.matches("([a-zA-Z][\\w\u002E\u005F\u002D]*)")
				|| mailBegin.length() < 4 || mailBegin.length() > 20)
			return false;
		return true;
	}

	/**
	 * Checks if the mail-address is correct, i.e. begins with Latin letter,
	 * contains letters or ".", "-", "_" before @, matches ...@??.??.?? and
	 * consists of more than 5 characters
	 * 
	 * @param address
	 * @return true, in case the address is correct, otherwise false
	 */
	public boolean correctMailAddress(String address) {
		if (!address
				.matches("(([a-zA-Z][\\w\u002E\u005F\u002D]*)@[\\w[.]]*\\.+([a-z]+))")
				|| address.length() < 6 || address.length() > 29)
			return false;
		return true;
	}

	/**
	 * Checks if the password is correct, i.e. contains only letters, ".", ",",
	 * "-", "_", "%", "*" and consists of more than 7 characters
	 * 
	 * @param password
	 * @return true, in case the password is correct, otherwise false
	 */
	public boolean correctPassword(String password) {
		if (!password.matches("([\\w\u002C\u0025\u002A\u002E\u005F\u002D]*)")
				|| password.length() < 8)
			return false;
		return true;
	}

	/**
	 * Checks if the first name or last name is correct, i.e. contains only
	 * letters and consists of more than 1 character
	 * 
	 * @param name
	 * @return true, in case the name is correct, otherwise false
	 */
	public boolean correctName(String name) {
		if (!name.matches("([a-zA-Zа-яА-Я]*)") || name.length() < 2
				|| name.length() > 29)
			return false;
		return true;
	}

	/**
	 * Checks if the phone number is correct, i.e. contains only numbers or "+"
	 * at the beginning and consists of more than 5 letters
	 * 
	 * @param phone
	 * @return true, in case the number is correct, otherwise false
	 */
	public boolean correctPhone(String phone) {
		if (!phone.matches("([\u002B]?[\\d]*)") || phone.length() < 6
				|| phone.length() > 19)
			return false;
		return true;
	}

	/**
	 * Checks if the line is correct, i.e. contains only correct addresses
	 * 
	 * @param fieldTo
	 *            Line of semicolon-separated email addresses
	 * @return true, in case the line is correct, otherwise false
	 */
	public boolean correctFieldTo(String to) {
		if (to.equals(""))
			return false;
		String[] emails = to.trim().split(";");

		for (String email : emails) {
			if (!correctMailAddress(email.trim()))
				return false;
		}
		return true;
	}

	/**
	 * Checks if the date is correct i.e. matches dd.mm.yyyy
	 * 
	 * @param date
	 * @return true, in case the date is correct, otherwise false
	 */
	public boolean correctDate(String date) {
		if (date.length() < 8)
			return false;
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		format.setLenient(false);
		try {
			format.parse(date.trim());
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	/**
	 * Checks if the field About is correct i.e. is not more then 49 characters
	 * and not less than 1
	 * 
	 * @param about
	 * @return true, in case the field is correct, otherwise false
	 */
	public boolean correctAbout(String about) {
		if (about.length() < 1 || about.length() > 49)
			return false;
		return true;
	}

	/**
	 * Checks if the text of some message is correct, i.e. not empty
	 * 
	 * @param text
	 * @return true, in case the text is correct, otherwise false
	 */
	public boolean correctMessText(String text) {
		if (text.length() < 1)
			return false;
		return true;
	}

	/**
	 * Checks if the name of folder is correct, i.e. is not empty and not more
	 * than 29 characters
	 * 
	 * @param folderName
	 * @return true, in case the folder name is correct, otherwise false
	 */
	public boolean correctFolder(String folderName) {
		if (folderName.length() < 1 || folderName.length() > 29)
			return false;
		return true;
	}
}
