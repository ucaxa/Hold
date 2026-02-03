package com.hold.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

	private static final Pattern ONLY_DIGITS = Pattern.compile("\\D");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isBlank()) {
			return true;
		}
		String digits = ONLY_DIGITS.matcher(value).replaceAll("");
		if (digits.length() != 11) {
			return false;
		}
		if (digits.matches("(\\d)\\1{10}")) {
			return false;
		}
		int d1 = calcDigit(digits.substring(0, 9), new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2 });
		int d2 = calcDigit(digits.substring(0, 9) + d1, new int[] { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 });
		return d1 == Character.getNumericValue(digits.charAt(9))
				&& d2 == Character.getNumericValue(digits.charAt(10));
	}

	private static int calcDigit(String base, int[] weights) {
		int sum = 0;
		for (int i = 0; i < base.length(); i++) {
			sum += Character.getNumericValue(base.charAt(i)) * weights[i];
		}
		int rem = sum % 11;
		return rem < 2 ? 0 : 11 - rem;
	}
}
