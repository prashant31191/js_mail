package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class UserValidationEnterUserCheckTest {

	private String[] data;
	private boolean result;
	private boolean exception; 

	public UserValidationEnterUserCheckTest(String[] data, boolean result, boolean exception) {
	    this.data = data;
	    this.result = result;
	    this.exception = exception;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { new String[] {"averageJoe@mail.com", "einfs64lse"}, true, false}, 
	            { new String[] {"averageJoe@mail", "einfs64lse"}, false, false},		
	            { new String[] {"averageJoe@mail.com", ""}, false, false},
	            { new String[] {"", "einfs64lse"}, false, false},
	            { new String[] {"averageJoe@mail.com", "einfs64lse"}, true, false},
	            { new String[] {"averageJoe@mail.com"}, true, true},
	            { new String[] {"averageJoe@mail.com", "einfs64lse", "plain string"}, true, false},
	            });
	}
	    
	@Test
	public void test() {
	    UserValidation validation = new UserValidation();
	    if (exception) {
            try {
            	validation.regDataCheck(data);
            } catch (Exception e) {
                Assert.assertTrue(e instanceof Exception);
            }
        } else {
        	Assert.assertEquals("data = \"" + data.toString() + "\"", result, validation.enterUserCheck(data));
        }
	}
}
