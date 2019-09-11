package com.ubs.careers_apac.string_accumulator;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * String Calculator
 * 
 * @since 2019-09-12
 * @author Lawrence
 *
 */
public class StringCalculator {
	
	private static Logger logger = LoggerFactory.getLogger(StringCalculator.class);
	/**
	 * Internal implementation class
	 * 
	 * @author Lawrence
	 *
	 */
	private static class StringCalculatorInternal {
		/**
		 * Default Delimiter '\n' and ','
		 */
		private static final String DEFAULT_DELIMITER = "\n|,";
		
		private static final String CUSTOM_DELIMITER_START_TAG = "//";

		private static final String CUSTOM_DELIMITER_END_TAG = "\n";
		
		/**
		 * Max number to ignore
		 */
		private static final int MAX_NUMBER_TO_ADD = 1000;

		/**
		 * input String
		 */
		private String inputStr = "";
		
		/**
		 * Internal class for StringCalculator
		 * @param inputStr
		 */
		private StringCalculatorInternal(String inputStr) {
			this.inputStr = inputStr;
		}
		
		/**
		 * Add the input
		 * @return the calculation result
		 * @throws NegativeNumberException if any negative number detected
		 */
		private int add() throws NegativeNumberException {
			if(inputStr.length() == 0) {
				return 0;
			} else {
				String[] numberStrArray = splitNumber();
				String negativeNum = Arrays.stream(numberStrArray).map(Integer::parseInt).filter(num-> num <0).map(String::valueOf)
				        .collect(Collectors.joining(","));
				
				if(negativeNum.length() > 0) {
					logger.info("Negative number detected [{}]", negativeNum);
					throw new NegativeNumberException(negativeNum);
				} else {
					return Arrays.stream(numberStrArray).map(Integer::parseInt).filter(num-> num <MAX_NUMBER_TO_ADD).
							mapToInt(Integer::intValue).sum();
				}
			}
		}

		/**
		 * Split the number
		 * @return String[] of the number
		 */
		private String[] splitNumber() {
			String[] numStrArray;
			
			if(hasCustomDelimiter()) {
				String rawDelimiter = inputStr.substring(CUSTOM_DELIMITER_START_TAG.length(), inputStr.indexOf(CUSTOM_DELIMITER_END_TAG));
				String delimiter = Arrays.stream(rawDelimiter.split("\\|")).map(d->Pattern.quote(d)).collect(Collectors.joining("|"));
				numStrArray = (inputStr.substring(inputStr.indexOf(CUSTOM_DELIMITER_END_TAG)+1)).split(delimiter);
			} else {
				numStrArray = inputStr.split(DEFAULT_DELIMITER);
			}
			
			if(logger.isDebugEnabled()) {
				logger.debug("numStrArray {}", Arrays.stream(numStrArray).collect(Collectors.joining(" ")));
			}
			return numStrArray;
		}
		
		/**
		 * @return true if it has Custom delimiter "\\.....\n"
		 */
		private boolean hasCustomDelimiter() {
			return (inputStr.startsWith(CUSTOM_DELIMITER_START_TAG) && inputStr.contains(CUSTOM_DELIMITER_END_TAG));
		}
			
	}
	
	/**
	 * Add the integer contains in the string
	 * 
	 * @param inputStr may contain 0, 1, 2, .... unknown amount of number
	 * 
	 * Default delimiter is ',' and new line '\n' 
	 * --- “1\n2,3” return 6
	 * 
	 * To change a delimiter, the beginning of the string will contain a separate line that
	 * looks like this: “//<delimiter>\n<numbers…>”, for example 
	 * --- “//;\n1;2” should return 3 where the delimiter is ‘;’.
	 * 
	 * Numbers bigger than 1000 should be ignored
	 * --- adding 2 + 1001 return 2
	 * 
	 * Delimiters can be of any length
	 * --- “//***\n1***2***3” return 6.
	 * 
	 * Allow multiple delimiters ( multiple delimiters with length longer than one character) 
	 * like this: “//delim1|delim2\n” (with a “|” separating delimiters),
	 * --- “//*|%\n1*2%3” return 6
	 * 
	 * @return the result of calculation
	 * 
	 * @throws NegativeNumberException when Calling add with a negative number
	 * with the message “negatives not allowed” - and the negative that was passed
	 * 
	 */
	public static int add(String inputStr) throws NegativeNumberException{
		StringCalculatorInternal strCal = new StringCalculatorInternal(inputStr);
		return strCal.add();
	}
	
}
