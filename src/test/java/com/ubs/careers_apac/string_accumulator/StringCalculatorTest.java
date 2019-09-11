package com.ubs.careers_apac.string_accumulator;

import org.junit.Test;

import org.junit.Assert;

public class StringCalculatorTest {

	@Test
	public void testEmpty() {
		try {
			Assert.assertEquals(0, StringCalculator.add(""));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testSingleNum() {
		try {
			Assert.assertEquals(1, StringCalculator.add("1"));
			Assert.assertEquals(123, StringCalculator.add("123"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testTwoNumWithComma() {
		try {
			Assert.assertEquals(3, StringCalculator.add("1,2"));
			Assert.assertEquals(35, StringCalculator.add("12,23"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testUnknownAmountOfNumber() {
		int num = (int) (Math.random()*1000);
		int sum = 0;
		StringBuilder strBuilder = new StringBuilder();
		String inputStr = "";
		if(num > 0) {
			strBuilder.append(0);
			for(int i=1; i<num; i++) {
				sum += i;
				strBuilder.append(',');
				strBuilder.append(i);
			}
		}
		inputStr = strBuilder.toString();
	
		try {
			Assert.assertEquals(sum, StringCalculator.add(inputStr));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testNewLineAndCommaDelimiter() {
		try {
			Assert.assertEquals(6, StringCalculator.add("1\n2,3"));
			Assert.assertEquals(79, StringCalculator.add("12,34\n33"));
			Assert.assertEquals(102, StringCalculator.add("12,34\n33,12\n11"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testCustomDelimiter() {
		try {
			Assert.assertEquals(3, StringCalculator.add("//;\n1;2"));
			Assert.assertEquals(3, StringCalculator.add("//.\n1.2"));
			Assert.assertEquals(3, StringCalculator.add("//,\n1,2"));
			Assert.assertEquals(3, StringCalculator.add("//$\n1$2"));
			Assert.assertEquals(3, StringCalculator.add("//!\n1!2"));
			Assert.assertEquals(3, StringCalculator.add("//\\\n1\\2"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testIgnoreEqualLargeNumber() {
		try {
			Assert.assertEquals(1001, StringCalculator.add("//;\n2;999"));
			Assert.assertEquals(2, StringCalculator.add("//;\n2;1000"));
			Assert.assertEquals(2, StringCalculator.add("//;\n2;1001"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testAnyLengthDelimiter() {
		try {
			Assert.assertEquals(6, StringCalculator.add("//***\n1***2***3"));
			Assert.assertEquals(6, StringCalculator.add("//abcd\n1abcd2abcd3"));
			Assert.assertEquals(6, StringCalculator.add("//*,.$\n1*,.$2*,.$3"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testMultipleDeilimiter() {
		try {
			Assert.assertEquals(6, StringCalculator.add("//*|,\n1*2,3"));
			Assert.assertEquals(10, StringCalculator.add("//a|b|c\n1a2b3c4"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}

	
	@Test
	public void testMultipleAnyLengthDeilimiter() {
		try {
			Assert.assertEquals(6, StringCalculator.add("//***|,,,\n1***2,,,3"));
			Assert.assertEquals(10, StringCalculator.add("//*?.,|abcd|ef\n1*?.,2abcd3ef4"));
		} catch (NegativeNumberException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test(expected = NegativeNumberException.class )
	public void testRejectSingleNegativeNubmer() throws NegativeNumberException {
		StringCalculator.add("//***|,,,\n1***-2,,,3");
	}

	@Test(expected = NegativeNumberException.class)
	public void testRejectMulitpleNegativeNubmer() throws NegativeNumberException {
		StringCalculator.add("//***|,,,\n-1***-2,,,3");
	}
}
