package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectMessTextTest {

	private String text;
	private boolean result;

	public ValidationCorrectMessTextTest(String text, boolean result) {
	    this.text = text;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "1", true},
	            { "2.2", true},
	            { "Important letter", true},
	            { "44fsf3sfes es f3 ", true},
	            { "djsfa vuhafuhfuhwu3hfsjfsdu3i34r7447 e4h74eiw4h ehghic7eh4 7ehih", true},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("text = \"" + text + "\"", result, validation.correctMessText(text));
	}
}
