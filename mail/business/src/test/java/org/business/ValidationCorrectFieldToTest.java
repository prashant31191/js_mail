package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectFieldToTest {

	private String fieldTo;
	private boolean result;

	public ValidationCorrectFieldToTest(String fieldTo, boolean result) {
	    this.fieldTo = fieldTo;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "sdf@efe.ru; sdf@453.ru", true},
	            { "sdf@efe.ru, sdf@453.ru", false},
	            { "sdf@efe.ru; sdf@453.ru; sdfd@dsf.rfr", true},
	            { "sdf@efe.ru; sdf@453.ru; sdfd@dsf.rfr;", true},
	            { "sdf@efe.ru", true},
	            { "djskdfgkdsjhfgjgkjsbg", false},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("fieldTo = \"" + fieldTo + "\"", result, validation.correctFieldTo(fieldTo));
	}
}
