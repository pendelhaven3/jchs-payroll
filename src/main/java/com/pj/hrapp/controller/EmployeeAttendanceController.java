package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.AppDatePicker;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.DateUtil;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmployeeAttendanceController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeAttendanceController.class);
	
	@Autowired private EmployeeService employeeService;
	
	@Parameter private EmployeeAttendance employeeAttendance;

	@FXML ComboBox<Employee> employeeComboBox;
	@FXML AppDatePicker attendanceDateDatePicker;
	@FXML ComboBox<Attendance> attendanceComboBox;
	@FXML Button deleteButton;
	
	@Override
	public void updateDisplay() {
		setTitle();
		employeeComboBox.getItems().setAll(employeeService.getAllActiveEmployees());
		attendanceComboBox.getItems().setAll(Attendance.values());
		
		boolean isUpdate = (employeeAttendance != null);
		if (isUpdate) {
			employeeAttendance = employeeService.getEmployeeAttendance(employeeAttendance.getId());
			employeeComboBox.setValue(employeeAttendance.getEmployee());
			attendanceDateDatePicker.setValue(DateUtil.toLocalDate(employeeAttendance.getDate()));
			attendanceComboBox.setValue(employeeAttendance.getAttendance());
			deleteButton.setDisable(false);
		}
		
		employeeComboBox.setDisable(isUpdate);
		attendanceDateDatePicker.setDisable(isUpdate);
	}

	private void setTitle() {
		if (employeeAttendance != null) {
			stageController.setTitle("Update Employee Attendance");
		} else {
			stageController.setTitle("Add Employee Attendance");
		}
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void saveEmployeeAttendance() {
		if (!validateFields()) {
			return;
		}
		
		if (employeeAttendance == null) {
			employeeAttendance = new EmployeeAttendance();
			employeeAttendance.setEmployee(employeeComboBox.getValue());
			employeeAttendance.setDate(DateUtil.toDate(attendanceDateDatePicker.getValue()));
		}
		employeeAttendance.setAttendance(attendanceComboBox.getValue());
		try {
			employeeService.save(employeeAttendance);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		ShowDialog.info("Employee Attendance saved");
		stageController.showEmployeeAttendanceListScreen();
	}

	private boolean validateFields() {
		if (isEmployeeNotSpecified()) {
			ShowDialog.error("Employee must be specified");
			employeeComboBox.requestFocus();
			return false;
		}
		
		if (isAttendanceDateNotSpecified()) {
			ShowDialog.error("Attendance Date must be specified");
			attendanceDateDatePicker.requestFocus();
			return false;
		}
		
		if (isAttendanceNotSpecified()) {
			ShowDialog.error("Attendance must be specified");
			attendanceComboBox.requestFocus();
			return false;
		}
		
		if (isEmployeeAttendanceAlreadyExisting()) {
			ShowDialog.error("Employee already has an attendance record for the specified date");
			attendanceDateDatePicker.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isEmployeeAttendanceAlreadyExisting() {
		return employeeAttendance == null &&
				employeeService.findEmployeeAttendanceByEmployeeAndDate(
						employeeComboBox.getValue(), DateUtil.toDate(attendanceDateDatePicker.getValue())) != null;
	}

	private boolean isEmployeeNotSpecified() {
		return employeeComboBox.getValue() == null;
	}

	private boolean isAttendanceDateNotSpecified() {
		return attendanceDateDatePicker.getValue() == null;
	}

	private boolean isAttendanceNotSpecified() {
		return attendanceComboBox.getValue() == null;
	}

	@FXML 
	public void deleteEmployeeAttendance() {
		if (!ShowDialog.confirm("Delete employee attendance?")) {
			return;
		}
		
		try {
			employeeService.deleteEmployeeAttendance(employeeAttendance);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
		}
		ShowDialog.info("Employee Attendance deleted");
		stageController.showEmployeeAttendanceListScreen();
	}
	
}
