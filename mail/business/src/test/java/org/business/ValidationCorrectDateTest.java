package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectDateTest {

	private String date;
	private boolean result;

	public ValidationCorrectDateTest(String date, boolean result) {
	    this.date = date;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "1", false},
	            { "2.2", false},
	            { "02.02.12", true},
	            { "02.02.1989", true},
	            { "12.03.", false},
	            { "dfs", false},
	            { "29.02.2011", false},
	            { "29.02.2012", true},
	            { "31.7.2012", true},
	            { "32.07.2012", false},
	            { "31.6.2013", false},
	            { "31.06.2013", false}
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("date = \"" + date + "\"", result, validation.correctDate(date));
	}
}
