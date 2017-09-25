package com.pj.hrapp.gui.component;

import java.util.List;
import java.util.stream.Collectors;

import com.pj.hrapp.model.util.TableItem;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

public class SelectableTableView<T> extends TableView<TableItem<T>> {

	private static final double CHECKBOX_COLUMN_WIDTH = 50.0d;
	private static final String CHECKBOX_COLUMN_STYLE_CLASS = "center";
	
	public SelectableTableView() {
		setEditable(true);
		addCheckBoxColumn();
	}

	private void addCheckBoxColumn() {
		TableColumn<TableItem<T>, Boolean> column = new TableColumn<>();
		column.setCellFactory(param -> new CheckBoxTableCell<>());
		column.setCellValueFactory(param -> param.getValue().selectedProperty());
		column.getStyleClass().add(CHECKBOX_COLUMN_STYLE_CLASS);
		column.setMinWidth(CHECKBOX_COLUMN_WIDTH);
		column.setMaxWidth(CHECKBOX_COLUMN_WIDTH);
		getColumns().add(column);
	}
	
	public List<T> getSelectedItems() {
		return getItems().stream()
				.filter(item -> item.isSelected())
				.map(item -> item.getItem())
				.collect(Collectors.toList());
	}

	public List<T> getAllItems() {
		return getItems().stream()
				.map(item -> item.getItem())
				.collect(Collectors.toList());
	}

	public boolean hasSelected() {
		return getItems().stream().anyMatch(item -> item.isSelected());
	}

	public boolean hasNoItems() {
		return getItems().isEmpty();
	}
	
	public void setItems(List<T> items) {
		getItems().clear();
		
		for (T item : items) {
			TableItem<T> tableItem = new TableItem<>(item);
			getItems().add(tableItem);
		}
	}
	
}
