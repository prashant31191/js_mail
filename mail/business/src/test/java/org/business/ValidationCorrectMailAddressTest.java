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
	            { "Man@ya.ru", true},
	            { "jerk.er-23@com.com", true},
	            { "vova85@com.com.ru", true},
	            { "8vova5@rutr.rt", false},
	            { "dfs-f@c", false},
	            { "dfs_dfs@@cr.com", false},
	            { "_dfs@com.com", false},
	            { "-rtr8@er.er", false},
	            { "df%ere@mail.ru", false},
	            { "ds*sffd@tr.tr", false},
	            { "wer_888.com.er@com", false},
	            { "wer_888.com.er.@com", false},
	            { "war__--..343.dfd@com.ru", true},
	            { "wer_888.com.ds@com.ru.", false},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("address = \"" + address + "\"", result, validation.correctMailAddress(address));
	}
}
