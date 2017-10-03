package com.jchs.payrollapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.dialog.AddPayslipLoanPaymentDialog;
import com.jchs.payrollapp.dialog.AutoGeneratePayslipContributionsDialog;
import com.jchs.payrollapp.dialog.EmployeeAttendanceDialog;
import com.jchs.payrollapp.dialog.PayslipAdjustmentDialog;
import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.PayslipBasicPayItem;
import com.jchs.payrollapp.model.PreviewPayslipItem;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.service.PayrollService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

@Controller
public class PayslipController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayslipController.class);
	
	private static final int OTHERS_TAB_INDEX = 4;
	
	@Autowired private PayrollService payrollService;
	@Autowired private EmployeeService employeeService;
	@Autowired private EmployeeLoanService employeeLoanService;
	@Autowired private EmployeeAttendanceDialog employeeAttendanceDialog;
	@Autowired private PayslipAdjustmentDialog payslipAdjustmentDialog;
	@Autowired private AddPayslipLoanPaymentDialog addPayslipLoanPaymentDialog;
    @Autowired private AutoGeneratePayslipContributionsDialog autoGenerateContributionsDialog;
	
	@FXML private Label payrollBatchNumberLabel;
	@FXML private Label employeeLabel;
	@FXML private Label periodCoveredFromLabel;
	@FXML private Label periodCoveredToLabel;
	@FXML private Label basicPayLabel;
	@FXML private Label adjustmentsLabel;
	@FXML private Label netPayLabel;
	@FXML private AppTableView<EmployeeAttendance> attendancesTable;
	@FXML private TableView<PayslipBasicPayItem> basicPayItemsTable;
	@FXML private AppTableView<EmployeeLoanPayment> loanPaymentsTable;
	@FXML private AppTableView<PayslipAdjustment> adjustmentsTable;
	@FXML private TableView<PreviewPayslipItem> previewPayslipTable;
	@FXML private TabPane tabPane;
	@FXML private Button deletePayslipButton;
	@FXML private HBox editPayslipButtonsHBox;
	@FXML private HBox editLoanPaymentButtonsHBox;
	@FXML private HBox editValeProductButtonsHBox;
	@FXML private HBox editPayslipAdjustmentButtonsHBox;

	@Parameter private Payslip payslip;

	@Override
	public void updateDisplay() {
		stageController.setTitle("Payslip");
		
		payslip = payrollService.getPayslip(payslip.getId());
		payrollBatchNumberLabel.setText(payslip.getPayroll().getBatchNumber().toString());
		employeeLabel.setText(payslip.getEmployee().toString());
		periodCoveredFromLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredFrom()));
		periodCoveredToLabel.setText(FormatterUtil.formatDate(payslip.getPeriodCoveredTo()));
		basicPayLabel.setText(FormatterUtil.formatAmount(payslip.getBasicPay()));
		adjustmentsLabel.setText(FormatterUtil.formatAmount(payslip.getTotalPayslipAdjustments()));
		netPayLabel.setText(FormatterUtil.formatAmount(payslip.getNetPay()));
		
		basicPayItemsTable.getItems().setAll(payslip.getBasicPayItems());
		
		attendancesTable.getItems().setAll(payslip.getAttendances());
		attendancesTable.setDoubleClickAction(() -> editSelectedAttendance());

		loanPaymentsTable.setItems(payslip.getLoanPayments());
		loanPaymentsTable.setDeleteKeyAction(() -> deleteLoanPayment());
		
		adjustmentsTable.getItems().setAll(payslip.getAdjustments());
		adjustmentsTable.setDoubleClickAction(() -> editSelectedAdjustment());
		adjustmentsTable.setDeleteKeyAction(() -> deletePayslipAdjustment());
		
		previewPayslipTable.getItems().setAll(payslip.getPreviewItems());
		
		if (payslip.getPayroll().isPosted()) {
			preventPayslipFromBeingEdited();
		}
	}

	private void preventPayslipFromBeingEdited() {
		deletePayslipButton.setDisable(true);
		editPayslipButtonsHBox.setDisable(true);
		editLoanPaymentButtonsHBox.setDisable(true);
		editValeProductButtonsHBox.setDisable(true);
		editPayslipAdjustmentButtonsHBox.setDisable(true);
		
		attendancesTable.setDoubleClickAction(null);
		loanPaymentsTable.setDeleteKeyAction(null);
		adjustmentsTable.setDoubleClickAction(null);
		adjustmentsTable.setDeleteKeyAction(null);
	}

	protected void editSelectedAttendance() {
		Map<String, Object> model = new HashMap<>();
		model.put("employeeAttendance", attendancesTable.getSelectionModel().getSelectedItem());
		
		employeeAttendanceDialog.showAndWait(model);
		
		updateDisplay();
	}

	protected void editSelectedAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslipAdjustment", adjustmentsTable.getSelectionModel().getSelectedItem());
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void doOnBack() {
		stageController.showPayrollScreen(payslip.getPayroll());
	}

	@FXML public void editPayslip() {
		stageController.showEditPayslipScreen(payslip);
	}

	@FXML public void addPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}
	
	@FXML public void editPayslipAdjustment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		payslipAdjustmentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deletePayslipAdjustment() {
		if (!isAdjustmentSelected()) {
			ShowDialog.error("No payment selected");
			return;
		}
		
		if (!ShowDialog.confirm("Delete payslip adjustment?")) {
			return;
		}
		
		try {
			payrollService.delete(adjustmentsTable.getSelectionModel().getSelectedItem());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip adjustment deleted");
		updateDisplay();
	}

	private boolean isAdjustmentSelected() {
		return adjustmentsTable.getSelectionModel().getSelectedItem() != null;
	}

	@FXML public void addLoanPayment() {
		Map<String, Object> model = new HashMap<>();
		model.put("payslip", payslip);
		
		addPayslipLoanPaymentDialog.showAndWait(model);
		
		updateDisplay();
	}

	@FXML public void deleteLoanPayment() {
		if (hasNoSelectedLoanPayment()) {
			ShowDialog.error("No loan payment selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete selected loan payment?")) {
			try {
				employeeLoanService.delete(getSelectedLoanPayment());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Loan payment deleted");
			updateDisplay();
		}
	}

	private boolean hasNoSelectedLoanPayment() {
		return getSelectedLoanPayment() == null;
	}

	private EmployeeLoanPayment getSelectedLoanPayment() {
		return loanPaymentsTable.getSelectedItem();
	}

	@FXML 
	public void deletePayslip() {
		if (!ShowDialog.confirm("Delete payslip?")) {
			return;
		}
		
		try {
			payrollService.delete(payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payslip deleted");
		stageController.showPayrollScreen(payslip.getPayroll());
	}

	@FXML 
	public void generateGovernmentContributions() {
	    Map<String, Object> model = new HashMap<>();
	    model.put("payslip", payslip);
	    model.put("contributionMonth", DateUtil.getNextContributionMonthString());
	    
	    autoGenerateContributionsDialog.setSuccess(false);
	    autoGenerateContributionsDialog.showAndWait(model);

	    if (autoGenerateContributionsDialog.isSuccess()) {
	        updateDisplay();
	        selectOtherAdjustmentsTab();
	    }
	}

    private void selectOtherAdjustmentsTab() {
		tabPane.getSelectionModel().select(OTHERS_TAB_INDEX);
	}

	@FXML 
	public void deleteEmployeeAttendance() {
		if (hasNoSelectedEmployeeAttendance()) {
			ShowDialog.error("No employee attendance selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete selected employee attendance?")) {
			try {
				employeeService.deleteEmployeeAttendance(getSelectedEmployeeAttendance());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Employee attendance deleted");
			updateDisplay();
		}
	}

	private boolean hasNoSelectedEmployeeAttendance() {
		return getSelectedEmployeeAttendance() == null;
	}

	private EmployeeAttendance getSelectedEmployeeAttendance() {
		return attendancesTable.getSelectedItem();
	}

}
