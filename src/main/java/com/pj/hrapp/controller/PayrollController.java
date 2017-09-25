package com.pj.hrapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.dialog.AddPayslipDialog;
import com.pj.hrapp.dialog.AutoGeneratePayrollContributionsDialog;
import com.pj.hrapp.exception.ConnectToMagicException;
import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.service.PayrollService;
import com.pj.hrapp.util.DateUtil;
import com.pj.hrapp.util.ExcelUtil;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.PayrollToBdoExcelGenerator;
import com.pj.hrapp.util.PayrollToExcelGenerator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PayrollController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);
	
	@Autowired private PayrollService payrollService;
	@Autowired private PayrollToExcelGenerator excelGenerator;
	@Autowired private PayrollToBdoExcelGenerator bdoExcelGenerator;
	@Autowired private AddPayslipDialog addPayslipDialog;
	@Autowired private AutoGeneratePayrollContributionsDialog autoGeneratePayrollContributionsDialog;
	
	@FXML private Label batchNumberLabel;
	@FXML private Label payDateLabel;
	@FXML private Label payScheduleLabel;
	@FXML private Label postedLabel;
	@FXML private Label periodCoveredFromLabel;
	@FXML private Label periodCoveredToLabel;
	@FXML private AppTableView<Payslip> payslipsTable;
	@FXML private Button deletePayrollButton;
	@FXML private HBox editPayrollButtonsHBox;
	@FXML private HBox editPayslipButtonsHBox;
	
	@Parameter private Payroll payroll;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Payroll");
		
		payroll = payrollService.getPayroll(payroll.getId());
		
		batchNumberLabel.setText(payroll.getBatchNumber().toString());
		payDateLabel.setText(FormatterUtil.formatDate(payroll.getPayDate()));
		payScheduleLabel.setText(payroll.getPaySchedule().toString());
		postedLabel.setText(payroll.isPosted() ? "Yes" : "No");
		periodCoveredFromLabel.setText(FormatterUtil.formatDate(payroll.getPeriodCoveredFrom()));
		periodCoveredToLabel.setText(FormatterUtil.formatDate(payroll.getPeriodCoveredTo()));
		
		payslipsTable.getItems().setAll(payroll.getPayslips());
		payslipsTable.setDoubleClickAction(() -> openSelectedPayslip());
		payslipsTable.setDeleteKeyAction(() -> deletePayslip());
		
		if (payroll.isPosted()) {
			preventPayrollFromBeingEdited();
		}
	}

	private void preventPayrollFromBeingEdited() {
		deletePayrollButton.setDisable(true);
		editPayrollButtonsHBox.setDisable(true);
		editPayslipButtonsHBox.setDisable(true);
	}

	protected void openSelectedPayslip() {
		stageController.showPayslipScreen(payslipsTable.getSelectionModel().getSelectedItem());
	}

	@FXML 
	public void doOnBack() {
		stageController.showPayrollListScreen();
	}

	@FXML 
	public void updatePayroll() {
		stageController.showUpdatePayrollScreen(payroll);
	}

	@FXML 
	public void deletePayroll() {
		if (ShowDialog.confirm("Delete payroll?")) {
			try {
				payrollService.delete(payroll);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
			}
			
			ShowDialog.info("Payroll deleted");
			stageController.showPayrollListScreen();
		}
	}

	@FXML 
	public void generateExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel files", "*.xlsx"));
        fileChooser.setInitialFileName(getExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			Workbook workbook = excelGenerator.generate(payroll);
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
			ExcelUtil.openExcelFile(file);
		}
	}

	private String getExcelFilename() {
		return new StringBuilder()
				.append("PAYSLIP as of ")
				.append(new SimpleDateFormat("MM-dd").format(payroll.getPayDate()))
				.append(".xlsx")
				.toString();
	}

	@FXML 
	public void deletePayslip() {
		if (!isPayslipSelected()) {
			ShowDialog.error("No payslip selected");
			return;
		}
		
		if (ShowDialog.confirm("Delete selected payslip?")) {
			try {
				payrollService.delete(getSelectedPayslip());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				ShowDialog.unexpectedError();
				return;
			}
			
			ShowDialog.info("Payslip deleted");
			updateDisplay();
		}
	}

	private boolean isPayslipSelected() {
		return getSelectedPayslip() != null;
	}

	private Payslip getSelectedPayslip() {
		return payslipsTable.getSelectionModel().getSelectedItem();
	}

	@FXML
	public void addPayslip() {
		Map<String, Object> model = new HashMap<>();
		model.put("payroll", payroll);
		
		addPayslipDialog.showAndWait(model);
		
		Payslip payslip = addPayslipDialog.getPayslip();
		if (payslip != null && payslip.getId() != null) {
			stageController.showPayslipScreen(payslip);
		}
	}

	@FXML 
	public void postPayroll() {
		if (!ShowDialog.confirm("Post payroll?")) {
			return;
		}
		
		try {
			payrollService.postPayroll(payroll);
		} catch (ConnectToMagicException e) {
			ShowDialog.error("Cannot connect to MAGIC");
			return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Payroll posted");
		updateDisplay();
	}

	@FXML 
	public void generateBdoExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "Desktop").toFile());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel Macro-Enabled Workbook", "*.xlsm"));
        fileChooser.setInitialFileName(getBdoExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
        	return;
        }
		
		try (
			Workbook workbook = bdoExcelGenerator.generate(payroll);
			FileOutputStream out = new FileOutputStream(file);
		) {
			workbook.write(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		if (ShowDialog.confirm("Excel file generated.\nDo you wish to open the file?")) {
			ExcelUtil.openExcelFile(file);
		}
	}

	private String getBdoExcelFilename() {
		return new StringBuilder()
				.append("BDO EPCI Regular Payroll ")
				.append(new SimpleDateFormat("MM-dd").format(payroll.getPayDate()))
				.append(".xlsm")
				.toString();
	}

	@FXML 
	public void regenerateAllGovernmentContributions() {
        Map<String, Object> model = new HashMap<>();
        model.put("payroll", payroll);
        model.put("contributionMonth", DateUtil.getNextContributionMonthString());
        
        autoGeneratePayrollContributionsDialog.setSuccess(false);
        autoGeneratePayrollContributionsDialog.showAndWait(model);

        if (autoGeneratePayrollContributionsDialog.isSuccess()) {
            updateDisplay();
        }
	    /*
		if (!ShowDialog.confirm("Generate SSS/PhilHealth/Pag-IBIG contributions for all payslips?")) {
			return;
		}
		
		try {
			payrollService.regenerateAllGovernmentContributions(payroll);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("SSS/PhilHealth/Pag-IBIG contributions generated");
		updateDisplay();
		*/
	}
	
}
