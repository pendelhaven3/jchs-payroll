package com.pj.hrapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.service.SystemService;
import com.pj.hrapp.util.FormatterUtil;
import com.pj.hrapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EditPagibigContributionValueController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(EditPagibigContributionValueController.class);
	
	@Autowired private SystemService systemService;
	
	@FXML private TextField valueField;
	
	@Override
	public void updateDisplay() {
		valueField.setText(FormatterUtil.formatAmount(systemService.getPagibigContributionValue()));
		valueField.requestFocus();
	}

	@FXML 
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

	@FXML 
	public void savePagibigContributionValue() {
		if (!validateFields()) {
			return;
		}
		
		try {
			systemService.updatePagibigContributionValue(NumberUtil.toBigDecimal(valueField.getText()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Saved");
		updateDisplay();
	}

	private boolean validateFields() {
		if (isValueNotSpecified()) {
			ShowDialog.error("Pag-IBIG Contribution Value must be specified");
			valueField.requestFocus();
			return false;
		}
		
		if (isValueNotValid()) {
			ShowDialog.error("Pag-IBIG Contribution Value must be a valid amount");
			valueField.requestFocus();
			return false;
		}
		
		return true;
	}

	private boolean isValueNotSpecified() {
		return StringUtils.isEmpty(valueField.getText());
	}

	private boolean isValueNotValid() {
		return !NumberUtil.isAmount(valueField.getText());
	}
	
}
