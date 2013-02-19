package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ActionValidationCheckFolderNameTest {

	private String folderName;
	private boolean result;

	public ActionValidationCheckFolderNameTest(String folderName, boolean result) {
	    this.folderName = folderName;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "1", true},
	            { "2.2", true},
	            { "Important letter", true},
	            { "44fsf3s%%%fes es f3 ", true},
	            { "djsfa vuhafuhfuhwu3hfsjfsdu3i34r7447 e4h74eiw4h ehghic7eh4 7ehih", false},
	            });
	}
	    
	@Test
	public void test() {
	    ActionValidation validation = new ActionValidation();
	    Assert.assertEquals("folderName = \"" + folderName + "\"", result, validation.checkFolderName(folderName));
	}
}