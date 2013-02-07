package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectMailAddressTest {

	private String address;
	private boolean result;

	public ValidationCorrectMailAddressTest(String address, boolean result) {
	    this.address = address;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "Man", true},
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
	    Assert.assertEquals("address = \"" + address + "\"", result, validation.correctMailAddress(address));
	}
}
