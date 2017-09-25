package com.pj.hrapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.AppTableView;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.model.PaySchedule;
import com.pj.hrapp.model.search.EmployeeAttendanceSearchCriteria;
import com.pj.hrapp.model.util.DateInterval;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.DateUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeAttendanceListController extends AbstractController {

	private static final int ATTENDANCES_TAB_INDEX = 0;
	private static final int ATTENDANCE_BY_EMPLOYEE_TAB_INDEX = 1;

	@Autowired private EmployeeService employeeService;
	
	@FXML private AppTableView<EmployeeAttendanceSummary> attendancesTable;
	@FXML private AppTableView<EmployeeAttendance> attendancesByEmployeeTable;
	@FXML private DatePicker dateFromDatePicker;
	@FXML private DatePicker dateToDatePicker;
	@FXML private ComboBox<PaySchedule> payScheduleComboBox;
	@FXML private TabPane tabPane;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Employee Attendance List");
		
		attendancesTable.getItems().setAll(getAllEmployeeAttendanceSummariesForCurrentMonth());
		attendancesTable.setDoubleClickAction(() -> {
			Employee selected = attendancesTable.getSelectedItem().getEmployee();
			showAttendancesByEmployee(selected);
		});
		
		DateInterval currentMonthInterval = getCurrentYearMonthInterval();
		dateFromDatePicker.setValue(DateUtil.toLocalDate(currentMonthInterval.getDateFrom()));
		dateToDatePicker.setValue(DateUtil.toLocalDate(currentMonthInterval.getDateTo()));
		
		payScheduleComboBox.setItems(FXCollections.observableArrayList(PaySchedule.values()));
		
		attendancesByEmployeeTable.setDoubleClickAction(() -> {
			updateEmployeeAttendance(attendancesByEmployeeTable.getSelectedItem());
		});
	}

	private List<EmployeeAttendanceSummary> getAllEmployeeAttendanceSummariesForCurrentMonth() {
		return toAttendanceSummaries(getAllEmployeeAttendancesForCurrentMonth());
	}

	private List<EmployeeAttendance> getAllEmployeeAttendancesForCurrentMonth() {
		DateInterval currentYearMonthInterval = getCurrentYearMonthInterval();
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setDateFrom(currentYearMonthInterval.getDateFrom());
		criteria.setDateTo(currentYearMonthInterval.getDateTo());
		
		return employeeService.searchEmployeeAttendances(criteria);
	}
	
	private List<EmployeeAttendanceSummary> toAttendanceSummaries(List<EmployeeAttendance> attendances) {
		Map<Employee, Double> attendanceSummaryMap = new HashMap<>();
		for (EmployeeAttendance attendance : attendances) {
			Employee employee = attendance.getEmployee();
			Double value = attendance.getValue();
			if (!attendanceSummaryMap.containsKey(employee)) {
				attendanceSummaryMap.put(employee, value);
			} else {
				attendanceSummaryMap.put(employee, attendanceSummaryMap.get(employee) + value);
			}
		}
		
		List<EmployeeAttendanceSummary> attendanceSummaries = new ArrayList<>();
		for (Employee employee : attendanceSummaryMap.keySet()) {
			EmployeeAttendanceSummary attendanceSummary = new EmployeeAttendanceSummary();
			attendanceSummary.setEmployee(employee);
			attendanceSummary.setNumberOfDaysWorked(attendanceSummaryMap.get(employee));
			attendanceSummaries.add(attendanceSummary);
		}
		
		Collections.sort(attendanceSummaries, (o1, o2) -> o1.getEmployee().compareTo(o2.getEmployee()));
		
		return attendanceSummaries;
	}
	
	private DateInterval getCurrentYearMonthInterval() {
		return DateUtil.generateMonthYearInterval(DateUtil.getYearMonth(new Date()));
	}

	@FXML public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	public class EmployeeAttendanceSummary {
		
		private Employee employee;
		private double numberOfDaysWorked;
		
		public Employee getEmployee() {
			return employee;
		}
		
		public void setEmployee(Employee employee) {
			this.employee = employee;
		}
		
		public double getNumberOfDaysWorked() {
			return numberOfDaysWorked;
		}
		
		public void setNumberOfDaysWorked(double numberOfDaysWorked) {
			this.numberOfDaysWorked = numberOfDaysWorked;
		}
		
	}

	@FXML 
	public void searchEmployeeAttendances() {
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setDateFrom(DateUtil.toDate(dateFromDatePicker.getValue()));
		criteria.setDateTo(DateUtil.toDate(dateToDatePicker.getValue()));
		criteria.setPaySchedule(payScheduleComboBox.getValue());
		
		attendancesTable.getItems()
				.setAll(toAttendanceSummaries(employeeService.searchEmployeeAttendances(criteria)));
		tabPane.getSelectionModel().select(ATTENDANCES_TAB_INDEX);
	}
	
	@FXML
	public void addEmployeeAttendance() {
		stageController.showAddEmployeeAttendanceScreen();
	}

	private void updateEmployeeAttendance(EmployeeAttendance employeeAttendance) {
		stageController.showEditEmployeeAttendanceScreen(employeeAttendance);
	}
	
	private void showAttendancesByEmployee(Employee employee) {
		attendancesByEmployeeTable.getItems().setAll(getAllEmployeeAttendancesForCurrentMonth(employee));
		tabPane.getTabs().get(ATTENDANCE_BY_EMPLOYEE_TAB_INDEX).setText(employee.getFullName());
		tabPane.getSelectionModel().select(ATTENDANCE_BY_EMPLOYEE_TAB_INDEX);
	}
	
	private List<EmployeeAttendance> getAllEmployeeAttendancesForCurrentMonth(Employee employee) {
		DateInterval currentYearMonthInterval = getCurrentYearMonthInterval();
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setEmployee(employee);
		criteria.setDateFrom(currentYearMonthInterval.getDateFrom());
		criteria.setDateTo(currentYearMonthInterval.getDateTo());
		
		return employeeService.searchEmployeeAttendances(criteria);
	}
	
}
