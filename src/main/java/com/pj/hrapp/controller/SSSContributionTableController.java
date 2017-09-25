package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.model.SSSContributionTable;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.service.SSSService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SSSContributionTableController extends AbstractController {

	@Autowired private SSSService sssService;
	
	@FXML private AppTableView<SSSContributionTableEntry> entriesTable;
	@FXML private Label tableCompleteLabel;
	
	@Override
	public void updateDisplay() {
		SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
		
		tableCompleteLabel.setText(String.valueOf(sssContributionTable.isComplete()));
		
		entriesTable.setItems(FXCollections.observableList(sssContributionTable.getEntries()));
		entriesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedEntry();
			}
		});
	}

	protected void editSelectedEntry() {
		stageController.showEditSSSContributionTableEntryScreen(
				entriesTable.getSelectedItem());
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addEntry() {
		stageController.addSSSContributionTableEntryScreen();
	}

}
