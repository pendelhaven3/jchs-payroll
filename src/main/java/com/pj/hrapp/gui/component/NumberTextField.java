package com.pj.hrapp.gui.component;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

	@Override
	public void replaceText(int start, int end, String text) {
		if (validate(text)) {
			super.replaceText(start, end, text.toUpperCase());
		}
	}

	@Override
	public void replaceSelection(String text) {
		if (validate(text)) {
			super.replaceSelection(text.toUpperCase());
		}
	}

	private boolean validate(String text) {
		return ("".equals(text) || text.matches("[0-9]"));
	}

}