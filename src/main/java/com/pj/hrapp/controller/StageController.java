package com.pj.hrapp.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.ControllerFactory;
import com.pj.hrapp.NavigationHistory;
import com.pj.hrapp.NavigationHistoryItem;
import com.pj.hrapp.Parameter;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.EmployeeLoan;
import com.pj.hrapp.model.Payroll;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.PhilHealthContributionTableEntry;
import com.pj.hrapp.model.SSSContributionTableEntry;
import com.pj.hrapp.model.Salary;
import com.pj.hrapp.model.search.EmployeeSearchCriteria;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageController {

	private static final double WIDTH = 1200d;
	private static final double HEIGHT = 640d;
	
	@Autowired private ControllerFactory controllerFactory;
	
	private Stage stage;
	private NavigationHistory history = new NavigationHistory();
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void loadSceneFromFXML(String file) {
		loadSceneFromFXML(file, null);
	}
	
	private void loadSceneFromFXML(String file, Map<String, Object> model) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(controllerFactory);
		
		Parent root = null;
		try {
			root = fxmlLoader.load(getClass().getResourceAsStream("/fxml/" + file + ".fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if (stage.getScene() != null) {
			stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
		} else {
			stage.setScene(new Scene(root, WIDTH, HEIGHT));
		}
		
		stage.getScene().getStylesheets().add("css/application.css");
		
		if (fxmlLoader.getController() instanceof AbstractController) {
			AbstractController controller = (AbstractController)fxmlLoader.getController();
			if (model != null && !model.isEmpty()) {
				mapParameters(controller, model);
			}
			controller.updateDisplay();
		}
		
		saveHistory(file, model);
	}

	private void mapParameters(AbstractController controller, Map<String, Object> model) {
		Class<? extends AbstractController> clazz = controller.getClass();
		for (String key : model.keySet()) {
			try {
				Field field = clazz.getDeclaredField(key);
				if (field != null && field.getAnnotation(Parameter.class) != null) {
					field.setAccessible(true);
					field.set(controller, model.get(key));
				}
			} catch (Exception e) {
				System.out.println("Error setting parameter " + key);
			}
		}
	}

	private void saveHistory(String sceneName, Map<String, Object> model) {
		history.add(new NavigationHistoryItem(sceneName, model));
	}
	
	public void clearLastHistory() {
		history.clearLastItem();
	}
	
	public void back() {
		NavigationHistoryItem previousScreen = history.getPreviousScreen();
		loadSceneFromFXML(previousScreen.getSceneName(), previousScreen.getModel());
	}
	
	public void setTitle(String title) {
		stage.setTitle("HR App - " + title);
	}

	public void showMainMenuScreen() {
		loadSceneFromFXML("mainMenu");
	}

	public void showEmployeeListScreen() {
		loadSceneFromFXML("employeeList");
	}

	public void showAddEmployeeScreen() {
		loadSceneFromFXML("employee");
	}

	public void showUpdateEmployeeScreen(Employee employee, EmployeeSearchCriteria searchCriteria) {
		history.addToLastItemModel("searchCriteria", searchCriteria);
		loadSceneFromFXML("employee", Collections.singletonMap("employee", employee));
	}

	public void showSalaryListScreen() {
		loadSceneFromFXML("salaryList");
	}

	public void showAddSalaryScreen() {
		loadSceneFromFXML("salary");
	}

	public void showUpdateSalaryScreen(Salary salary) {
		loadSceneFromFXML("salary", Collections.singletonMap("salary", salary));
	}

	public void showPayrollListScreen() {
		loadSceneFromFXML("payrollList");
	}

	public void showAddPayrollScreen() {
		loadSceneFromFXML("addEditPayroll");
	}

	public void showPayrollScreen(Payroll payroll) {
		loadSceneFromFXML("payroll", Collections.singletonMap("payroll", payroll));
	}

	public void showUpdatePayrollScreen(Payroll payroll) {
		loadSceneFromFXML("addEditPayroll", Collections.singletonMap("payroll", payroll));
	}

	public void showPayslipScreen(Payslip payslip) {
		loadSceneFromFXML("payslip", Collections.singletonMap("payslip", payslip));
	}

	public void showEditPayslipScreen(Payslip payslip) {
		loadSceneFromFXML("editPayslip", Collections.singletonMap("payslip", payslip));
	}

	public Stage getStage() {
		return stage;
	}

	public void showSSSContributionTableScreen() {
		loadSceneFromFXML("sssContributionTable");
	}

	public void addSSSContributionTableEntryScreen() {
		loadSceneFromFXML("sssContributionTableEntry");
	}

	public void showEditSSSContributionTableEntryScreen(SSSContributionTableEntry entry) {
		loadSceneFromFXML("sssContributionTableEntry", 
				Collections.singletonMap("entry", entry));
	}

	public void showPhilHealthContributionTableScreen() {
		loadSceneFromFXML("philHealthContributionTable");
	}

	public void addPhilHealthContributionTableEntryScreen() {
		loadSceneFromFXML("philHealthContributionTableEntry");
	}

	public void showEditPhilHealthContributionTableEntryScreen(PhilHealthContributionTableEntry entry) {
		loadSceneFromFXML("philHealthContributionTableEntry", 
				Collections.singletonMap("entry", entry));
	}

	public void showEmployeeLoanListScreen() {
		loadSceneFromFXML("employeeLoanList");
	}

	public void showAddEmployeeLoanScreen() {
		loadSceneFromFXML("addEditEmployeeLoan");
	}

	public void showUpdateEmployeeLoanScreen(EmployeeLoan loan) {
		loadSceneFromFXML("addEditEmployeeLoan", Collections.singletonMap("loan", loan));
	}

	public void showEmployeeLoanScreen(EmployeeLoan loan) {
		loadSceneFromFXML("employeeLoan", Collections.singletonMap("loan", loan));
	}

	public void showEmployeeAttendanceListScreen() {
		loadSceneFromFXML("employeeAttendanceList");
	}

	public void showEditPagibigContributionValueScreen() {
		loadSceneFromFXML("editPagibigContributionValue");
	}

	public void showReportListScreen() {
		loadSceneFromFXML("reportList");
	}

	public void showSSSPhilHealthReportScreen() {
		loadSceneFromFXML("sssPhilHealthReport");
	}

	public void updateSalaryListScreenHistoryItemModel(Map<String, Object> model) {
		history.updateHistoryItemModel("salaryList", model);
	}

	public void showLatesReportScreen() {
		loadSceneFromFXML("latesReport");
	}

	public void showAddEmployeeAttendanceScreen() {
		loadSceneFromFXML("employeeAttendance");
	}

	public void showEditEmployeeAttendanceScreen(EmployeeAttendance employeeAttendance) {
		loadSceneFromFXML("employeeAttendance", Collections.singletonMap("employeeAttendance", employeeAttendance));
	}

	public void showCompanyProfileScreen() {
		loadSceneFromFXML("companyProfile");
	}

	public void showBasicSalaryReportScreen() {
		loadSceneFromFXML("basicSalaryReport");
	}

	public void addCurrentScreenParameter(String name, Object value) {
		history.addToLastItemModel(name, value);
	}

	public void showEmployeeLoanPaymentsReportScreen() {
		loadSceneFromFXML("employeeLoanPaymentsReport");
	}

}