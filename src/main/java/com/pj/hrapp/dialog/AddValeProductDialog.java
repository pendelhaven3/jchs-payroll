package com.pj.hrapp.dialog;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.hrapp.Parameter;
import com.pj.hrapp.gui.component.SelectableTableView;
import com.pj.hrapp.gui.component.ShowDialog;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.model.util.TableItem;
import com.pj.hrapp.service.ValeProductService;

import javafx.fxml.FXML;

@Component
public class AddValeProductDialog extends AbstractDialog {

	private static final Logger logger = LoggerFactory.getLogger(AddValeProductDialog.class);
	
	@Autowired private ValeProductService valeProductService;
	
	@FXML private SelectableTableView<ValeProduct> valeProductsTable;
	
	@Parameter private Payslip payslip;
	
	@Override
	public void updateDisplay() {
		setTitle(getDialogTitle());
		
		valeProductsTable.getItems().clear();
		for (ValeProduct valeProduct : findUnpaidValeProductsByEmployeeAndNotInPayslip()) {
			valeProductsTable.getItems().add(new TableItem<ValeProduct>(valeProduct));
		}
	}
	
	private List<ValeProduct> findUnpaidValeProductsByEmployeeAndNotInPayslip() {
		return valeProductService.findUnpaidValeProductsByEmployee(payslip.getEmployee())
				.stream()
				.filter((valeProduct) -> {
					for (ValeProduct vp : payslip.getValeProducts()) {
						if (vp.getSalesInvoiceNumber().equals(valeProduct.getSalesInvoiceNumber())) {
							return false;
						}
					}
					return true;
				})
				.collect(Collectors.toList());
	}
	
	@Override
	protected String getDialogTitle() {
		return "Add Vale Products to Payslip";
	}

	@Override
	protected String getSceneName() {
		return "addValeProductDialog";
	}

	@FXML
	public void addAll() {
		addValeProductsToPayslip(valeProductsTable.getAllItems());
	}
	
	@FXML
	public void addSelected() {
		if (hasNoValeProductsSelected()) {
			ShowDialog.error("No vale products selected");
		} else {
			addValeProductsToPayslip(valeProductsTable.getSelectedItems());
		}
	}

	private boolean hasNoValeProductsSelected() {
		return !valeProductsTable.hasSelected();
	}

	private void addValeProductsToPayslip(List<ValeProduct> valeProducts) {
		if (!ShowDialog.confirm("Add vale products?")) {
			return;
		}
		
		try {
			valeProductService.addValeProductsToPayslip(valeProducts, payslip);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ShowDialog.unexpectedError();
			return;
		}
		
		ShowDialog.info("Vale Products saved");
		hide();
	}
	
}