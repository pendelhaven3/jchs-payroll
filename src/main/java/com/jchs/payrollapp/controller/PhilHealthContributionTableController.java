package com.jchs.payrollapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.DoubleClickEventHandler;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.model.PhilHealthContributionTableEntry;
import com.jchs.payrollapp.service.PhilHealthService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhilHealthContributionTableController extends AbstractController {

	@Autowired private PhilHealthService philHealthService;
	
	@FXML private TableView<PhilHealthContributionTableEntry> entriesTable;
	@FXML private Label tableCompleteLabel;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("PhilHealth Contribution Table");
		
		PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
		
		tableCompleteLabel.setText(String.valueOf(philHealthContributionTable.isComplete()));
		
		entriesTable.setItems(FXCollections.observableList(philHealthContributionTable.getEntries()));
		entriesTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				editSelectedEntry();
			}
		});
	}

	protected void editSelectedEntry() {
		stageController.showEditPhilHealthContributionTableEntryScreen(
				entriesTable.getSelectionModel().getSelectedItem());
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addEntry() {
		stageController.addPhilHealthContributionTableEntryScreen();
	}

}
