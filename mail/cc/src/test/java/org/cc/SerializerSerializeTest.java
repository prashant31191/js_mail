package org.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.cc.Serializer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class SerializerSerializeTest {

	private Object object;
	private boolean result;
	private boolean exception;
	
	public SerializerSerializeTest(Object object, boolean result, boolean exception) {
	    this.object = object;
	    this.result = result;
	    this.exception = exception;
	}

	@Parameters
	public static Collection<Object[]> data() {
	    return Arrays.asList(new Object[][] { 
	            { "String", true, false},
	            { new ArrayList<String>(), true, false},
	            { new SerializerSerializeTest("String", true, false), false, true},
	            });
	}
	    
	@Test
	public void test() {
		byte[] byteArray;
		if (exception) {
			try {
				Serializer.serialize(object);
			} catch (Exception e) {
				Assert.assertTrue(e instanceof Exception);
			}
		} else {
			try {
				byteArray = Serializer.serialize(object);
				Assert.assertEquals("Object = \"" + object.toString() + "\"",
						result, Serializer.deserialize(byteArray)
								.equals(object));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}