package com.pj.hrapp.gui.component;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class EnterKeyEventHandler implements EventHandler<KeyEvent> {

	@Override
	public void handle(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			onEnterKey();
		}
	}

	protected abstract void onEnterKey();
	
}
