package com.pj.hrapp.dialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Attendance;
import com.pj.hrapp.model.EmployeeAttendance;
import com.pj.hrapp.service.EmployeeService;
import com.pj.hrapp.util.FormatterUtil;

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
