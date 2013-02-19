package org.business;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class ActionValidationCheckMessageTest {

	private String[] data;
	private boolean[] result;
	private boolean exception; 

	public ActionValidationCheckMessageTest(String[] data, boolean[] result, boolean exception) {
	    this.data = data;
	    this.result = result;
	    this.exception = exception;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { new String[] {"averageJoe@mail.js", "about", "hi, everyone"}, 
	            		new boolean[] {true, true, true, true}, false},
	            		
	            { new String[] {"averageJoe", "about", "hi, everyone"}, 
	            		new boolean[] {false, false, true, true}, false},
	            		
	            { new String[] {"averageJoe@mail.js", "", "hi, everyone"}, 
	            		new boolean[] {false, true, false, true}, false},
	            		
	            { new String[] {"averageJoe@mail.js", "about", ""}, 
	            		new boolean[] {false, true, true, false}, false},
	            		
	            { new String[] {"@mail.js", "", ""}, 
	            		new boolean[] {false, false, false, false}, false},
	            		
	            { new String[] {"@mail.js", ""}, 
	            		new boolean[] {false, false, false, false}, true},
	            		
	            { new String[] {"averageJoe@mail.js", "about", "hi, everyone", "plain string"}, 
		            	new boolean[] {true, true, true, true}, false},		
	            });
	}
	    
	@Test
	public void test() {
	    ActionValidation validation = new ActionValidation();
	    if (exception) {
            try {
            	validation.checkMessage(data);
            } catch (Exception e) {
                Assert.assertTrue(e instanceof Exception);
            }
        } else {
        	Assert.assertEquals("data = \"" + data.toString() + "\"", true, Arrays.equals(result, validation.checkMessage(data)));
        }
	}
}
