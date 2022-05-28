package com.jchs.payrollapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.service.PhilHealthService;
import com.jchs.payrollapp.util.NumberUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Controller
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PhilHealthContributionTableController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhilHealthContributionTableController.class);
    
	@Autowired
	private PhilHealthService philHealthService;
	
	@FXML private TextField floorField;
    @FXML private TextField ceilingField;
    @FXML private TextField multiplierField;
    @FXML private TextField householdMonthlyContributionField;
	
    private PhilHealthContributionTable philHealthContributionTable;
    
	@Override
	public void updateDisplay() {
		stageController.setTitle("PhilHealth Contribution Table");
		
		philHealthContributionTable = philHealthService.getContributionTable();
		
		floorField.setText(philHealthContributionTable.getFloor().toString());
        ceilingField.setText(philHealthContributionTable.getCeiling().toString());
        multiplierField.setText(philHealthContributionTable.getMultiplier().toString());
        householdMonthlyContributionField.setText(philHealthContributionTable.getHouseholdMonthlyContribution().toString());
        
        floorField.requestFocus();
	}

	@FXML
	public void doOnBack() {
		stageController.showMainMenuScreen();
	}

    @FXML
    public void save() {
        if (!validateFields()) {
            return;
        }
        
        philHealthContributionTable.setFloor(NumberUtil.toBigDecimal(floorField.getText()));
        philHealthContributionTable.setCeiling(NumberUtil.toBigDecimal(ceilingField.getText()));
        philHealthContributionTable.setMultiplier(NumberUtil.toBigDecimal(multiplierField.getText()));
        philHealthContributionTable.setHouseholdMonthlyContribution(NumberUtil.toBigDecimal(householdMonthlyContributionField.getText()));
        
        try {
            philHealthService.save(philHealthContributionTable);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ShowDialog.unexpectedError();
            return;
        }
        
        ShowDialog.info("Saved");
    }
    
    private boolean validateFields() {
        if (isFloorNotSpecified()) {
            ShowDialog.error("Floor must be specified");
            floorField.requestFocus();
            return false;
        }
        
        if (isFloorNotValid()) {
            ShowDialog.error("Floor must be a valid amount");
            floorField.requestFocus();
            return false;
        }

        if (isCeilingNotSpecified()) {
            ShowDialog.error("Ceiling must be specified");
            ceilingField.requestFocus();
            return false;
        }
        
        if (isCeilingNotValid()) {
            ShowDialog.error("Ceiling must be a valid amount");
            ceilingField.requestFocus();
            return false;
        }

        if (isMultiplierNotSpecified()) {
            ShowDialog.error("Multiplier must be specified");
            multiplierField.requestFocus();
            return false;
        }
        
        if (isMultiplierNotValid()) {
            ShowDialog.error("Multiplier must be a valid amount");
            multiplierField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isFloorNotSpecified() {
        return StringUtils.isEmpty(floorField.getText());
    }

    private boolean isFloorNotValid() {
        return !NumberUtil.isAmount(floorField.getText());
    }

    private boolean isCeilingNotSpecified() {
        return StringUtils.isEmpty(ceilingField.getText());
    }

    private boolean isCeilingNotValid() {
        return !NumberUtil.isAmount(ceilingField.getText());
    }

    private boolean isMultiplierNotSpecified() {
        return StringUtils.isEmpty(multiplierField.getText());
    }

    private boolean isMultiplierNotValid() {
        return !NumberUtil.isAmount(multiplierField.getText());
    }

}
