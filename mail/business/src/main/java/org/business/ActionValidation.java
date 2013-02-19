package org.business;

public class ActionValidation {
	public boolean[] checkMessage(String[] data) {
		boolean[] checkedData = new boolean[4];
		Validation valid = new Validation();
		
		checkedData[0] = true;
		checkedData[1] = valid.correctFieldTo(data[0]);
		checkedData[2] = valid.correctAbout(data[1]);
		checkedData[3] = valid.correctMessText(data[2]);

		for (int i = 1; i < 4; i++) {
			if (checkedData[i] == false) {
				checkedData[0] = false;
				break;
			}
		}
		return checkedData;
	}
	
	public boolean checkFolderName(String data) {
		Validation valid = new Validation();
		
		return valid.correctFolder(data);
	}
}
