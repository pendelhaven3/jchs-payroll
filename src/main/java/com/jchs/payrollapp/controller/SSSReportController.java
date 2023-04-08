package com.jchs.payrollapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jchs.payrollapp.gui.component.AppTableView;
import com.jchs.payrollapp.gui.component.ShowDialog;
import com.jchs.payrollapp.model.report.SSSReport;
import com.jchs.payrollapp.model.report.SSSReportItem;
import com.jchs.payrollapp.report.excel.SSSReportExcelGenerator;
import com.jchs.payrollapp.service.ReportService;
import com.jchs.payrollapp.service.SystemService;
import com.jchs.payrollapp.util.DateUtil;
import com.jchs.payrollapp.util.ExcelUtil;
import com.jchs.payrollapp.util.FormatterUtil;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SSSReportController extends AbstractController {

    @Autowired private ReportService reportService;
    @Autowired private SystemService systemService;
    
    @FXML private ComboBox<Month> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private AppTableView<SSSReportItem> itemsTable;
    @FXML private Label totalMonthlyPayField;
    @FXML private Label totalEmployeeContributionField;
    @FXML private Label totalEmployerContributionField;
    @FXML private Label totalContributionField;
    @FXML private Label totalEmployeeCompensationField;
    @FXML private Label totalEmployeeProvidentFundContributionField;
    @FXML private Label totalEmployerProvidentContributionField;
    @FXML private Label totalProvidentFundContributionField;
    
    @Override
    public void updateDisplay() {
        stageController.setTitle("SSS Report");
        monthComboBox.getItems().setAll(Month.values());
        yearComboBox.getItems().setAll(DateUtil.getYearDropdownValues());
        yearComboBox.setValue(Calendar.getInstance().get(Calendar.YEAR));
    }

    @FXML
    public void doOnBack() {
        stageController.back();
    }

    @FXML
    public void generateReport() {
        if (isCriteriaNotSpecified()) {
            ShowDialog.error("Month and Year must be specified");
            return;
        }
        
        SSSReport report = reportService.generateSSSReport(getYearMonthCriteria());
        itemsTable.setItemsThenFocus(report.getItems());
        if (report.isEmpty()) {
            ShowDialog.error("No records found");
        }
        
        totalMonthlyPayField.setText(FormatterUtil.formatAmount(report.getTotalMonthlyPay()));
        totalEmployeeContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployeeContribution()));
        totalEmployerContributionField.setText(FormatterUtil.formatAmount(report.getTotalEmployerContribution()));
        totalContributionField.setText(FormatterUtil.formatAmount(report.getTotalContribution()));
        totalEmployeeCompensationField.setText(FormatterUtil.formatAmount(report.getTotalEmployeeCompensation()));
    }

    private boolean isCriteriaNotSpecified() {
        return monthComboBox.getValue() == null || yearComboBox.getValue() == null;
    }

    private YearMonth getYearMonthCriteria() {
        int month = monthComboBox.getValue().getValue();
        int year = yearComboBox.getValue();
        return YearMonth.of(year, month);
    }
    
    @FXML 
    public void generateExcelReport() {
        if (isCriteriaNotSpecified()) {
            ShowDialog.error("Month and Year must be specified");
            return;
        }
        
        FileChooser fileChooser = ExcelUtil.getSaveExcelFileChooser(getExcelFilename());
        File file = fileChooser.showSaveDialog(stageController.getStage());
        if (file == null) {
            return;
        }
        
        try (
            Workbook workbook = new SSSReportExcelGenerator().generate(reportService.generateSSSReport(getYearMonthCriteria()),
                    systemService.getCompanyProfile());
            FileOutputStream out = new FileOutputStream(file);
        ) {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            ShowDialog.unexpectedError();
            return;
        }
        
        ExcelUtil.openExcelFile(file);
    }

    private String getExcelFilename() {
        YearMonth yearMonth = getYearMonthCriteria();
        return MessageFormat.format("sss_report_{0}_{1}.xlsx", 
                String.valueOf(yearMonth.getYear()), yearMonth.getMonth().getValue());
    }
    
}
