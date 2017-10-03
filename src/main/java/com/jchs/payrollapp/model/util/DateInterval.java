package com.jchs.payrollapp.model.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DateUtils;

import com.jchs.payrollapp.util.DateUtil;

/**
 * Represents a period between two dates
 * 
 * @author PJ Miranda
 *
 */
public class DateInterval {

	private Date dateFrom;
	private Date dateTo;

	public DateInterval(Date dateFrom, Date dateTo) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}
	
	public DateInterval overlap(DateInterval interval) {
		if (dateFrom == null) {
			if (interval.getDateFrom() == null) {
				return new DateInterval(null, DateUtil.min(dateTo, interval.getDateTo()));
			} else if (interval.getDateTo() == null) {
				if (interval.getDateFrom().compareTo(dateTo) <= 0) {
					return new DateInterval(interval.getDateFrom(), dateTo);
				} else {
					return null;
				}
			} else if (dateTo.compareTo(interval.getDateTo()) >= 0) {
				return interval;
			} else if (dateTo.compareTo(interval.getDateFrom()) < 0) {
				return null;
			} else {
				return new DateInterval(interval.getDateFrom(), dateTo);
			}
		} else if (dateTo == null) {
			if (interval.getDateTo() == null) {
				return new DateInterval(DateUtil.max(dateFrom, interval.getDateFrom()), null);
			} else if (interval.getDateFrom() == null) {
				if (interval.getDateTo().compareTo(dateFrom) >= 0) {
					return new DateInterval(dateFrom, interval.getDateTo());
				} else {
					return null;
				}
			} else if (dateFrom.compareTo(interval.getDateFrom()) <= 0) {
				return interval;
			} else if (dateFrom.compareTo(interval.getDateTo()) > 0) {
				return null;
			} else {
				return new DateInterval(dateFrom, interval.getDateTo());
			}
		} else if (interval.getDateFrom() == null || interval.getDateTo() == null) {
			return interval.overlap(this);
		}
		
		List<Date> overlapDates = new ArrayList<>(CollectionUtils.intersection(
				toDateList(), interval.toDateList()));
		if (!overlapDates.isEmpty()) {
			Collections.sort(overlapDates);
			return new DateInterval(overlapDates.get(0), overlapDates.get(overlapDates.size() - 1));
		} else {
			return null;
		}
	}
	
	public List<Date> toDateList() {
		List<Date> dates = new ArrayList<>();
		Calendar calendar = DateUtils.toCalendar(dateFrom);
		
		while (calendar.getTime().compareTo(dateTo) <= 0) {
			dates.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		
		return dates;
	}
	
	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(dateFrom)
				.append(dateTo)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateInterval other = (DateInterval) obj;
		return new EqualsBuilder()
				.append(dateFrom, other.getDateFrom())
				.append(dateTo, other.getDateTo())
				.isEquals();
	}

	public boolean contains(Date date) {
		if (dateFrom == null && date.compareTo(dateTo) <= 0) {
			return true;
		} else if (dateTo == null && date.compareTo(dateFrom) >= 0) {
			return true;
		} else {
			return dateFrom.compareTo(date) <= 0 && date.compareTo(dateTo) <= 0;
		}
	}

}
