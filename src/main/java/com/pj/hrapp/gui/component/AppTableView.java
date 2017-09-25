package com.pj.hrapp.gui.component;

import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

public class AppTableView<T> extends TableView<T> {

	public T getSelectedItem() {
		return getSelectionModel().getSelectedItem();
	}
	
	public void setItems(List<T> items) {
		getItems().setAll(items);
	}
	
	public void setItemsThenFocus(List<T> items) {
		setItems(items);
		
		if (!items.isEmpty()) {
			getSelectionModel().select(0);
		}
		requestFocus();
	}
	
	public void setDoubleClickAndEnterKeyAction(CustomAction action) {
		setDoubleClickAction(action);
		setEnterKeyAction(action);
	}

	private void setEnterKeyAction(CustomAction action) {
		setOnKeyPressed(e -> {
			if (e.getCode().equals(KeyCode.ENTER)) {
				action.doAction();
			}
		});
	}

	public boolean hasSelectedItem() {
		return getSelectedItem() != null;
	}
	
	public void setDoubleClickAction(CustomAction action) {
		if (action != null) {
			setOnMouseClicked(new DoubleClickEventHandler() {
				
				@Override
				protected void onDoubleClick(MouseEvent event) {
					action.doAction();
				}
			});
		} else {
			setOnMouseClicked(null);
		}
	}

	public void setDeleteKeyAction(CustomAction action) {
		if (action != null) {
			setOnKeyPressed(e -> {
				if (e.getCode().equals(KeyCode.DELETE)) {
					action.doAction();
				}
			});
		} else {
			setOnKeyPressed(null);
		}
	}
	
}
