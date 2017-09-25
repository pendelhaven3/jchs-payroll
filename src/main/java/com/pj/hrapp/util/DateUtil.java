package com.pj.hrapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.pj.hrapp.Constants;
import com.pj.hrapp.model.util.DateInterval;

public class DateUtil {

	private static ResourceBundle appProperties = ResourceBundle.getBundle("application");
	
	public static Date toDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static Date toDate(String dateString) {
		try {
			return new SimpleDateFormat(Constants.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static DateInterval generateMonthYearInterval(YearMonth yearMonth) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.YEAR, yearMonth.getYear());
		calendar.set(Calendar.MONTH, yearMonth.getMonthValue() - 1);
		calendar.set(Calendar.DATE, 1);
		Date startDate = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		Date endDate = DateUtils.truncate(calendar.getTime(), Calendar.DATE);
		
		return new DateInterval(startDate, endDate);
	}
	
	public static YearMonth getYearMonth(Date date) {
		return YearMonth.from(DateUtil.toLocalDate(date));
	}

	public static boolean isSunday(Date date) {
		return DateUtils.toCalendar(date).get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	public static Date max(Date date1, Date date2) {
		return date1.compareTo(date2) >= 0 ? date1 : date2;
	}

	public static Date min(Date date1, Date date2) {
		return date1.compareTo(date2) <= 0 ? date1 : date2;
	}
	
	public static List<Integer> getYearDropdownValues() {
		List<Integer> entries = new ArrayList<>();
		int startYear = Integer.parseInt(appProperties.getString("yearDropdown.startEntry"));
		int numberOfEntries = Integer.parseInt(appProperties.getString("yearDropdown.numberOfEntries"));
		for (int i = 0; i < numberOfEntries; i++) {
			entries.add(startYear + i);
		}
		return entries;
	}
	
	public static int getNumberOfWorkingDaysInFirstHalf(YearMonth yearMonth) {
		int numberOfWorkingDays = 0;
		for (int i = 1; i <= 15; i++) {
			if (yearMonth.atDay(i).getDayOfWeek() != DayOfWeek.SUNDAY) {
				numberOfWorkingDays++;
			}
		}
		return numberOfWorkingDays;
	}

	public static int getNumberOfWorkingDaysInSecondHalf(YearMonth yearMonth) {
		int numberOfWorkingDays = 0;
		for (int i = 16; i <= yearMonth.lengthOfMonth(); i++) {
			if (yearMonth.atDay(i).getDayOfWeek() != DayOfWeek.SUNDAY) {
				numberOfWorkingDays++;
			}
		}
		return numberOfWorkingDays;
	}
	
	public static Date toDate(YearMonth yearMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, yearMonth.getMonthValue() - 1);
		cal.set(Calendar.YEAR, yearMonth.getYear());
		return cal.getTime();
	}
	
    public static String getNextContributionMonthString() {
        return toString(YearMonth.now());
    }
	
    public static YearMonth toYearMonth(String value) {
        return YearMonth.of(Integer.parseInt(value.substring(2)), Integer.parseInt(value.substring(0, 2)));
    }

    public static String toString(YearMonth yearMonth) {
        return StringUtils.leftPad(String.valueOf(yearMonth.getMonthValue()), 2, "0") + String.valueOf(yearMonth.getYear());
    }
    
}