package com.ubs.careers_apac.string_accumulator;

/**
 * Exception to indicate the input has negative number
 * @author Lawrence
 *
 */
public class NegativeNumberException extends Exception {

	private static final long serialVersionUID = 3520495243232643223L;
	
	public NegativeNumberException(String negativeNumber) {
		super(String.format("Negative number not allowed [%s]", negativeNumber));
	}

}
