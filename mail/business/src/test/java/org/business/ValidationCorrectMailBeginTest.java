package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectMailBeginTest {

	private String mailBegin;
	private boolean result;

	public ValidationCorrectMailBeginTest(String mailBegin, boolean result) {
	    this.mailBegin = mailBegin;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "Man", false},
	            { "jerk", true},
	            { "vova85", true},
	            { "8vova5", false},
	            { "dfs-f", true},
	            { "dfs_dfs", true},
	            { "_dfs", false},
	            { "-rtr8", false},
	            { "df%ere", false},
	            { "ds*sffd", false},
	            { "wer_888", true},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("mailBegin = \"" + mailBegin + "\"", result, validation.correctMailBegin(mailBegin));
	}
}
