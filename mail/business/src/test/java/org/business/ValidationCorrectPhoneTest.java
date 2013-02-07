package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectPhoneTest {

	private String phone;
	private boolean result;

	public ValidationCorrectPhoneTest(String phone, boolean result) {
	    this.phone = phone;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "+797543", true},
	            { "++43453", false},
	            { "vova85", false},
	            { "8945343", true},
	            { "+(35354345)", false},
	            { "8-(8454)-453", true},
	            { "+258934(34)-4", true},
	            { "-45648)353)", false},
	            { "34534()435", false},
	            { "3453(34)-(7654)334", true},
	            { "34533*435", false},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("phone number = \"" + phone + "\"", result, validation.correctPhone(phone));
	}
}
