package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationPasswordsMatchTest {

	private String password;
	private String repeatPassword;
	private boolean result;

	public ValidationPasswordsMatchTest(String password, String repeatPassword, boolean result) {
	    this.password = password;
	    this.repeatPassword = repeatPassword;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", "rk9sMds", false},
	            { "4fGHe8l", "rk9sMds", false},
	            { "pasS", "pass", false}, 
	            { "PaSs", "PaSs", true}, 
	            { "", "", true},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("Passwords: \"" + password + "\", \"" + repeatPassword + "\"", result, validation.passwordsMatch(password, repeatPassword));
	}
}
