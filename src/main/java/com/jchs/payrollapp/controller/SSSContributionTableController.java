package com.jchs.payrollapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.DoubleClickEventHandler;
import com.jchs.payrollapp.model.SSSContributionTable;
import com.jchs.payrollapp.model.SSSContributionTableEntry;
import com.jchs.payrollapp.service.SSSService;

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
        stageController.setTitle("SSS Contribution Table");
	    
		SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
		
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
