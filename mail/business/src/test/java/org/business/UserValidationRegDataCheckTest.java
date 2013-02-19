package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class UserValidationRegDataCheckTest {

	private String[] data;
	private boolean[] result;
	private boolean exception; 

	public UserValidationRegDataCheckTest(String[] data, boolean[] result, boolean exception) {
	    this.data = data;
	    this.result = result;
	    this.exception = exception;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
	            		"Павел", "Морозов", "12.11.1999", "+85453232"}, 
	            		new boolean[] {true, true, true, true, true, true, true, true, true}, 
	            		false},
	            		
	            { new String[] {"aver564*", "einfs64lse", "einfs64lse",
	            		"Павел", "Морозов", "12.11.1999", "+85453232"}, 
		            	new boolean[] {false, false, true, true, true, true, true, true, true},
		            	false},
	            		
		        { new String[] {"averageJoe", "einfs", "einfs64lse",
		        		"Павел", "Морозов", "12.11.1999", "+85453232"}, 
		            	new boolean[] {false, true, false, true, false, true, true, true, true},
		            	false},
	            		
		        { new String[] {"averageJoe", "einfs64lse", "", "Павел",
		        		"Морозов", "12.11.1999", "+85453232"}, 
		            	new boolean[] {false, true, true, false, false, true, true, true, true},
		            	false},
	            		
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
		        		"7", "Морозов", "12.11.1999", "+85453232"}, 
		            	new boolean[] {false, true, true, true, true, false, true, true, true},
		            	false},
	            		
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse", 
		        		"Павел", "12", "12.11.1999", "+85453232"}, 
		            	new boolean[] {false, true, true, true, true, true, false, true, true},
		            	false},
	            		
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
		        		"Павел", "Морозов", "29.02.2011", "+85453232"}, 
		            	new boolean[] {false, true, true, true, true, true, true, false, true},
		            	false},	
		            	
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
		        		"Павел", "Морозов", "12.11.1999", "+"}, 
		            	new boolean[] {false, true, true, true, true, true, true, true, false},
		            	false}, 
		            	
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
		        		"Павел", "Морозов", "+85453232"}, 
		            	new boolean[] {true, true, true, true, true, true, true, true, true},
		            	true},
		            	
		        { new String[] {"averageJoe", "einfs64lse", "einfs64lse",
		        		"Павел", "Морозов", "12.11.1999", "+85453232", "plain string"}, 
		            	new boolean[] {true, true, true, true, true, true, true, true, true},
		            	false},
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
        	Assert.assertEquals("data = \"" + data.toString() + "\"", true, Arrays.equals(result, validation.regDataCheck(data)));
        }
	}
}
