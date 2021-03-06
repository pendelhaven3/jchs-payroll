package com.jchs.payrollapp.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jchs.payrollapp.Parameter;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.Attendance;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.service.EmployeeService;
import com.jchs.payrollapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

@Component
public class EmployeeAttendanceDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeAttendanceDialog.class);
	
	@Autowired private EmployeeService employeeService;
	
	@FXML private Label dateLabel;
	@FXML private ComboBox<Attendance> attendanceComboBox;
	
	@Parameter private EmployeeAttendance employeeAttendance;
	
	@Override
	protected String getDialogTitle() {
		return "Edit Employee Attendance";
	}

	@Override
	protected void updateDisplay() {
		attendanceComboBox.getItems().setAll(Attendance.values());
		
		employeeAttendance = employeeService.getEmployeeAttendance(employeeAttendance.getId());
		dateLabel.setText(FormatterUtil.formatDate(employeeAttendance.getDate()));
		attendanceComboBox.setValue(employeeAttendance.getAttendance());
	}

	@Override
	protected String getSceneName() {
		return "employeeAttendanceDialog";
	}

	@FXML 
	public void saveEmployeeAttendance() {
		employeeAttendance.setAttendance(attendanceComboBox.getValue());
		
		try {
			employeeService.save(employeeAttendance);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Employee attendance saved");
		hide();
	}

}
