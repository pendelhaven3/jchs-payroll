package com.pj.hrapp.gui.component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.text.WordUtils;

import com.pj.hrapp.util.FormatterUtil;

import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class AppPropertyValueFactory <T>
		implements Callback<CellDataFeatures<T, String>, ObservableValue<String>> {

	private static final String GET_PREFIX = "get";
	private static final String IS_PREFIX = "is";
	
	private String property;

	public AppPropertyValueFactory(@NamedArg("property") String property) {
		this.property = property;
	}

	@Override
	public ObservableValue<String> call(CellDataFeatures<T, String> param) {
		Object item = param.getValue();
		StringTokenizer tokenizer = new StringTokenizer(property, ".");
		while (tokenizer.hasMoreTokens()) {
			String propertyName = tokenizer.nextToken();
			boolean safeNavigation = false;
			
			if (propertyName.endsWith("?")) {
				propertyName = propertyName.substring(0, propertyName.length() - 1);
				safeNavigation = true;
			}
			
			try {
				Method method = findGetterMethod(item, propertyName);
				item = method.invoke(item);
			} catch (Exception e) {
				throw new RuntimeException("Error getting property " + property, e);
			}
			
			if (safeNavigation && item == null) {
				break;
			}
		}
		
		String value = null;
		if (item != null) {
			if (item instanceof String) {
				value = (String)item;
			} else if (item instanceof Date) {
				value = FormatterUtil.formatDate((Date)item);
			} else if (item instanceof BigDecimal) {
				value = FormatterUtil.formatAmount((BigDecimal)item);
			} else if (item instanceof Boolean) {
				value = (Boolean)item ? "Yes" : "No";
			} else {
				value = item.toString();
			}
		} else {
			value = "";
		}
		
		return new ReadOnlyStringWrapper(value);
	}

	private Method findGetterMethod(Object item, String propertyName) throws NoSuchMethodException {
		try {
			return item.getClass().getMethod(GET_PREFIX + WordUtils.capitalize(propertyName));
		} catch (NoSuchMethodException e) {
			return item.getClass().getMethod(IS_PREFIX + WordUtils.capitalize(propertyName));
		}
	}

}
