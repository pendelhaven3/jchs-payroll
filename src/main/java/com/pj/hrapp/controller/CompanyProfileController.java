package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.CompanyProfile;
import com.pj.hrapp.service.SystemService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompanyProfileController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(CompanyProfileController.class);
	
	@Autowired private SystemService systemService;
	
	@FXML private TextField nameField;
	@FXML private TextField addressField;
	@FXML private TextField contactNumbersField;
	@FXML private TextField mobileNumberField;
	@FXML private TextField emailAddressField;
	@FXML private TextField tinField;
	@FXML private TextField sssNumberField;
	@FXML private TextField pagibigNumberField;
	@FXML private TextField philhealthNumberField;
	
	private CompanyProfile companyProfile;
	
	@Override
	public void updateDisplay() {
		stageController.setTitle("Update Company Profile");
		
		companyProfile = systemService.getCompanyProfile();
		
		nameField.setText(companyProfile.getName());
		addressField.setText(companyProfile.getAddress());
		contactNumbersField.setText(companyProfile.getContactNumbers());
		mobileNumberField.setText(companyProfile.getMobileNumber());
		emailAddressField.setText(companyProfile.getEmailAddress());
		tinField.setText(companyProfile.getTin());
		sssNumberField.setText(companyProfile.getSssNumber());
		pagibigNumberField.setText(companyProfile.getPagibigNumber());
		philhealthNumberField.setText(companyProfile.getPhilhealthNumber());
		
		nameField.requestFocus();
		nameField.positionCaret(nameField.getText().length());
	}

	@FXML 
	public void doOnBack() {
		stageController.back();
	}

	@FXML 
	public void saveCompanyProfile() {
		companyProfile.setName(nameField.getText());
		companyProfile.setAddress(addressField.getText());
		companyProfile.setContactNumbers(contactNumbersField.getText());
		companyProfile.setMobileNumber(mobileNumberField.getText());
		companyProfile.setEmailAddress(emailAddressField.getText());
		companyProfile.setTin(tinField.getText());
		companyProfile.setSssNumber(sssNumberField.getText());
		companyProfile.setPagibigNumber(pagibigNumberField.getText());
		companyProfile.setPhilhealthNumber(philhealthNumberField.getText());
		
		try {
			systemService.save(companyProfile);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		ShowDialog.info("Company Profile saved");
		stageController.showCompanyProfileScreen();
		stageController.clearLastHistory();
	}

}
