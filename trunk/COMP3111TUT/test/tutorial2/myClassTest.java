package tutorial2;

import static org.junit.Assert.*;

import org.junit.Test;

public class myClassTest {

	@Test
	public void testMultiply() {
		myClass testerClass = new myClass();
		
		assertEquals("Result",50,testerClass.multiply(5, 10));
	}
	@Test
	public void testMultiply_null() {
		myClass testerClass = new myClass();
		
		assertEquals("Result",0,testerClass.multiply(5, 0));
	}
	@Test
	public void testSum() {
		myClass testerClass = new myClass();
		
		assertEquals("Result",15,testerClass.sum(5, 10));
	}

}
