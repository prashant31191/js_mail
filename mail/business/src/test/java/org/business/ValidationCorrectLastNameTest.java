package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ValidationCorrectLastNameTest {

	private String name;
	private boolean result;

	public ValidationCorrectLastNameTest(String name, boolean result) {
	    this.name = name;
	    this.result = result;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "", false},
	            { "Man", true},
	            { "jerk", true},
	            { "vova85", false},
	            { "8vova5", false},
	            { "dfs-fsdfs", false},
	            { "dfs_dfse33", false},
	            { "_dfs", false},
	            { "-rtr8", false},
	            { "df%ereds", false},
	            { "Johns", true},
	            { "Сидоров", true},
	            { "Ли", true},
	            });
	}
	    
	@Test
	public void test() {
	    Validation validation = new Validation();
	    Assert.assertEquals("last name = \"" + name + "\"", result, validation.correctLastName(name));
	}
}
