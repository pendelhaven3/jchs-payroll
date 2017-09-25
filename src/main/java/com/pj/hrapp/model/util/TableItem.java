package com.pj.hrapp.model.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TableItem<T> {
	
	private T item;
	private BooleanProperty selected = new SimpleBooleanProperty();

	public TableItem(T item) {
		this.item = item;
	}
	
	public T getItem() {
		return item;
	}
	
	public boolean isSelected() {
		return selected.get();
	}

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}

	 public BooleanProperty selectedProperty() {  
         return selected;  
     }
	 
}
