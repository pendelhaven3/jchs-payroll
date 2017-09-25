package com.pj.hrapp.gui.component;

import javafx.scene.control.TextField;

public class UppercaseTextField extends TextField {

	@Override
	public void replaceText(int start, int end, String text) {
		super.replaceText(start, end, text.toUpperCase());
	}

	@Override
	public void replaceSelection(String text) {
		super.replaceSelection(text.toUpperCase());
	}

}