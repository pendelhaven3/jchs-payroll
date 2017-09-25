package com.pj.hrapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.DoubleClickEventHandler;
import com.pj.hrapp.gui.component.EnterKeyEventHandler;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.service.PayrollService;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayrollListController extends AbstractController {

	@Autowired private PayrollService payrollService;
	
	@FXML private TableView<Payroll> payrollsTable;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Payroll List");
		
		payrollsTable.getItems().setAll(payrollService.getAllPayroll());
		if (!payrollsTable.getItems().isEmpty()) {
			payrollsTable.getSelectionModel().select(0);
			payrollsTable.requestFocus();
		}
		
		payrollsTable.setOnMouseClicked(new DoubleClickEventHandler() {
			
			@Override
			protected void onDoubleClick(MouseEvent event) {
				openSelectedPayroll();
			}
		});
		
		payrollsTable.setOnKeyPressed(new EnterKeyEventHandler() {
			
			@Override
			protected void onEnterKey() {
				openSelectedPayroll();
			}
		});
	}

	private void openSelectedPayroll() {
		if (!payrollsTable.getSelectionModel().isEmpty()) {
			stageController.showPayrollScreen(
					payrollsTable.getSelectionModel().getSelectedItem());
		}
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML public void addPayroll() {
		stageController.showAddPayrollScreen();
	}
	
}
