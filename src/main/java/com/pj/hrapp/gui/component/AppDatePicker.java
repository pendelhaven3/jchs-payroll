package com.pj.hrapp.gui.component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.pj.hrapp.Constants;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public class AppDatePicker extends DatePicker {

	public AppDatePicker() {
		setPromptText(Constants.DATE_FORMAT.toLowerCase());
		
		setConverter(new StringConverter<LocalDate>() {

			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});
		
		focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				setValue(getConverter().fromString(getEditor().getText()));			
			}
		});
	}

}
