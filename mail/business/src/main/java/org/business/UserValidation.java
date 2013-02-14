package org.business;

/**
 * Class, containing methods for checking the data, sent by a user
 * 
 * @author Fomin
 * @version 1.0
 */
public class UserValidation {
	/**
	 * Checks if the registration data is correct
	 * 
	 * @param a data Array of Strings, containing an information for registration 
	 * @return a mask-array of booleans, containing correctness of the data 
	 */
	public boolean[] regDataCheck(String[] data) {
		boolean[] checkedData = new boolean[9];
		Validation valid = new Validation();
		
		checkedData[0] = true;
		checkedData[1] = valid.correctMailBegin(data[0]);
		checkedData[2] = valid.correctPassword(data[1]);
		checkedData[3] = valid.correctPassword(data[2]);
		checkedData[4] = valid.passwordsMatch(data[1], data[2]);
		checkedData[5] = valid.correctName(data[3]);
		checkedData[6] = valid.correctName(data[4]);
		checkedData[7] = valid.correctDate(data[5]);
		checkedData[8] = valid.correctPhone(data[6]);
		
		for (int i = 1; i < 9; i++) {
			if (checkedData[i] == false) {
				checkedData[0] = false;
				break;
			}
		}
		
		return checkedData;
	}
	
	/**
	 * Checks if the data for logging in is correct
	 * 
	 * @param data an array of Strings, containing email-address and password
	 * @return true, in case the data is correct, otherwise false
	 */
	public boolean enterUserCheck(String[] data) {
		Validation valid = new Validation();
		if (!valid.fieldIsNotClear(data[0]) || !valid.fieldIsNotClear(data[1])
				|| !valid.correctMailAddress(data[0]))
			return false;
		return true;
	}
}
