package com.afluenta;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public final class DoubleUtil {
	
	public static Double getDouble(String number) {
		try {
			return new DecimalFormat("###,###.##", new DecimalFormatSymbols(Locale.GERMANY)).parse(number)
					.doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
			return Double.NaN;
		}
	}

	public static double lFindDoubleDelimitedBy(String source, String right, int left) {

		int index = source.indexOf(right);

		if (index == -1) {
			return Double.NaN;
		}

		String number = source.substring(index - left, index);
		return getDouble(number);
	}

	/**
	 * Find a double in source delimited on the right by right and, from there going left, by left
	 */
	public static double rFindDoubleDelimitedBy(String source, String left, String right) {

		int index = source.indexOf(right);
		
		if (index == -1) {
			return Double.NaN;
		}
		
		String newSource = source.substring(0, index);

		int index2 = newSource.lastIndexOf(left);
		
		if (index2 == -1) {
			return Double.NaN;
		}
		
		String number = newSource.substring(index2 + left.length());
		return getDouble(number);
	}
	
	public static double findDoubleDelimitedBy(String source, String left, String right) {

		int index = source.indexOf(left);
		
		if (index == -1) {
			return Double.NaN;
		}
		
		String newSource = source.substring(index + left.length(), source.length()-1);
		int index2 = newSource.indexOf(right);
		
		if (index2 == -1) {
			return Double.NaN;
		}
		
		String number = newSource.substring(0, index2);
		return getDouble(number);
	}
}
