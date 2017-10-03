package com.jchs.payrollapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.jchs.payrollapp.Constants;

/**
 * 
 * @author PJ Miranda
 *
 */
public class FormatterUtil {

	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
	private static DateTimeFormatter dateMonthFormatter = DateTimeFormatter.ofPattern(Constants.DATE_MONTH_FORMAT);
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return dateFormatter.format(DateUtil.toLocalDate(date));
	}

	public static String formatAmount(BigDecimal amount) {
		return new DecimalFormat(Constants.AMOUNT_FORMAT).format(amount);
	}

	public static String formatDateMonth(Date date) {
		if (date == null) {
			return "";
		}
		return dateMonthFormatter.format(DateUtil.toLocalDate(date));
	}
	
}