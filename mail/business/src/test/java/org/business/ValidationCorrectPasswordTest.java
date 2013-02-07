package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectPasswordTest {

	private String password;
	private boolean result;

	public ValidationCorrectPasswordTest(String password, boolean result) {
	    this.password = password;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "Man", false},
	            { "jerk", false},
	            { "vova85", false},
	            { "8vova5", false},
	            { "dfs-fsdfs", true},
	            { "dfs_dfse33", true},
	            { "_dfs", false},
	            { "-rtr8", false},
	            { "df%ereds", true},
	            { "ds*sffdsf", true},
	            { "wer_888", false},
	            { ",dfsd88I", true},
	            { ".,fgdfgdf,.", true},
	            { "Опвтвыдыа", false},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("password = \"" + password + "\"", result, validation.correctPassword(password));
	}
}
