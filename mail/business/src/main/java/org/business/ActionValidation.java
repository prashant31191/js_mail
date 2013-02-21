package org.business;

/**
 * Class, containing methods for checking information sent by authorized user
 * 
 * @author Fomin
 * @version 1.0
 */
public class ActionValidation {
	/**
	 * Checks if the message is correct, namely fields To, About and Message
	 * text
	 * 
	 * @param data
	 *            array of Strings, containing required information
	 * @return mask-array of booleans, containing correctness of the data
	 */
	public boolean[] checkMessage(String[] data) {
		boolean[] checkedData = new boolean[4];
		Validation valid = new Validation();
		/* Заполняем маску */
		checkedData[0] = true;
		checkedData[1] = valid.correctFieldTo(data[0]);
		checkedData[2] = valid.correctAbout(data[1]);
		checkedData[3] = valid.correctMessText(data[2]);
		 /*Если хотя бы одно значение некорректно, выставляем первый "бит" в false*/
		for (int i = 1; i < 4; i++) {
			if (checkedData[i] == false) {
				checkedData[0] = false;
				break;
			}
		}
		return checkedData;
	}

	/**
	 * Checks if the name of folder for creation is correct
	 * 
	 * @param data
	 *            name of folder
	 * @return true, in case the name is correct, otherwise false
	 */
	public boolean checkFolderName(String data) {
		Validation valid = new Validation();

		return valid.correctFolder(data);
	}
}
