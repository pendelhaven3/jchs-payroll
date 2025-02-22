package com.jchs.payrollapp.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.EmployeeLoanPaymentRepository;
import com.jchs.payrollapp.dao.EmployeeRepository;
import com.jchs.payrollapp.dao.ReportDao;
import com.jchs.payrollapp.dao.SalaryDao;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.EmployeeLoanType;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.model.SSSContributionTable;
import com.jchs.payrollapp.model.report.BasicSalaryReport;
import com.jchs.payrollapp.model.report.BasicSalaryReportItem;
import com.jchs.payrollapp.model.report.LatesReport;
import com.jchs.payrollapp.model.report.PagIbigReport;
import com.jchs.payrollapp.model.report.PagIbigReportItem;
import com.jchs.payrollapp.model.report.PayrollReport;
import com.jchs.payrollapp.model.report.PayrollReportItem;
import com.jchs.payrollapp.model.report.PhilHealthReport;
import com.jchs.payrollapp.model.report.PhilHealthReportItem;
import com.jchs.payrollapp.model.report.SSSReport;
import com.jchs.payrollapp.model.report.SSSReportItem;
import com.jchs.payrollapp.service.PhilHealthService;
import com.jchs.payrollapp.service.ReportService;
import com.jchs.payrollapp.service.SSSService;

@Service
public class ReportServiceImpl implements ReportService {

    private static final List<String> SPECIAL_SSS_NUMBERS =
            Arrays.asList("34-2262850-4", "33-6187727-0");
    private static final List<String> SPECIAL_PHILHEALTH_NUMBERS =
            Arrays.asList("19-025105390-5", "10-50259190-3");
    private static final List<String> SPECIAL_PAGIBIG_NUMBERS =
            Arrays.asList("1212-2749-6831", "1060-0016-1403");    
    
	@Autowired private ReportDao reportDao;
	@Autowired private EmployeeRepository employeeRepository;
	@Autowired private SalaryDao salaryDao;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	@Autowired private SSSService sssService;
    @Autowired private PhilHealthService philHealthService;
	
	@Override
	public LatesReport generateLatesReport(Date from, Date to) {
		LatesReport report = new LatesReport();
		report.setItems(reportDao.getLatesReportItems(from, to));
		return report;
	}

	@Override
	public BasicSalaryReport generateBasicSalaryReport(Date from, Date to) {
		Calendar calFrom = DateUtils.toCalendar(from);
		YearMonth ymFrom = YearMonth.of(calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH) + 1);
		
		Calendar calTo = DateUtils.toCalendar(to);
		YearMonth ymTo = YearMonth.of(calTo.get(Calendar.YEAR), calTo.get(Calendar.MONTH) + 1);
		
		BasicSalaryReport report = new BasicSalaryReport();
		report.setYearMonthFrom(ymFrom);
		report.setYearMonthTo(ymTo);
		
		for (Employee employee : employeeRepository.findAllByResigned(false)) {
			BasicSalaryReportItem item = new BasicSalaryReportItem();
			item.setEmployeeName(employee.getFirstAndLastName());
			item.setTotalSalary(getEmployeeCompensationForPeriod(employee, ymFrom, ymTo));
			report.getItems().add(item);
		}
		
		return report;
	}

	private BigDecimal getEmployeeCompensationForPeriod(Employee employee, YearMonth from, YearMonth to) {
		BigDecimal totalCompensation = BigDecimal.ZERO;
		
		YearMonth ym = from;
		while (ym.compareTo(to) <= 0) {
			totalCompensation = totalCompensation.add(salaryDao.getEmployeeCompensationForMonthYear(employee, ym));
			ym = ym.plusMonths(1);
		}
		
		return totalCompensation;
	}

	@Override
	public List<EmployeeLoanPayment> generateEmployeeLoanPaymentsReport(Date from, Date to, EmployeeLoanType loanType) {
		if (loanType != null) {
			return employeeLoanPaymentRepository.findAllByPaymentDateBetweenAndLoanType(from, to, loanType);
		} else {
			return employeeLoanPaymentRepository.findAllByPaymentDateBetween(from, to);
		}
	}

	@Override
	public PhilHealthReport generatePhilHealthReport(YearMonth yearMonth) {
        PhilHealthReport report = new PhilHealthReport();
        report.setYearMonth(yearMonth);
        report.setItems(reportDao.getPhilHealthReportItems(yearMonth));
        
        PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
        
        report.getItems().stream()
            .filter(item -> SPECIAL_PHILHEALTH_NUMBERS.contains(item.getPhilHealthNumber()))
            .forEach(item -> {
                item.setDue(philHealthContributionTable.getEmployeeShare(item.getMonthlyPay(), false));
            });
        
        return report;
	}

	@Override
	public SSSReport generateSSSReport(YearMonth yearMonth) {
        SSSReport report = new SSSReport();
        report.setYearMonth(yearMonth);
        report.setItems(reportDao.getSSSReportItems(yearMonth));
        
        SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
        
		report.getItems().stream()
			.filter(item -> SPECIAL_SSS_NUMBERS.contains(item.getSssNumber()))
			.forEach(item -> {
				item.setEmployeeContribution(sssContributionTable.getEmployeeContribution(item.getMonthlyPay(), false));
				item.setEmployerContribution(sssContributionTable.getEmployerContribution(item.getMonthlyPay(), false));
				item.setEmployeeCompensation(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
				item.setEmployeeProvidentFundContribution(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
				item.setEmployerProvidentFundContribution(sssContributionTable.getEmployeeCompensation(item.getMonthlyPay(), false));
			});
        
        return report;
	}

	@Override
	public PagIbigReport generatePagIbigReport(YearMonth yearMonth) {
        PagIbigReport report = new PagIbigReport();
        report.setYearMonth(yearMonth);
        report.setItems(reportDao.getPagIbigReportItems(yearMonth));
        
        report.getItems().stream()
	        .filter(item -> SPECIAL_PAGIBIG_NUMBERS.contains(item.getPagIbigNumber()))
	        .forEach(item -> {
	            item.setEmployeeContribution(BigDecimal.valueOf(200L));
	            item.setEmployerContribution(BigDecimal.valueOf(200L));
	        });
    
        return report;
	}
	
	@Override
	public PayrollReport generatePayrollReport(YearMonth yearMonth, Employee employee) {
		SSSReport sssReport = generateSSSReport(yearMonth);
		PhilHealthReport philHealthReport = generatePhilHealthReport(yearMonth);
		PagIbigReport pagIbigReport = generatePagIbigReport(yearMonth);
		
		List<PayrollReportItem> payrollReportItems = new ArrayList<>();
		
		for (SSSReportItem item : sssReport.getItems()) {
			PayrollReportItem payrollReportItem = new PayrollReportItem();
			payrollReportItem.setEmployee(item.getEmployee());
			payrollReportItem.setSssReportItem(item);
			
			payrollReportItems.add(payrollReportItem);
		}
		
		Map<Long, PayrollReportItem> itemMapping =
				payrollReportItems.stream().collect(Collectors.toMap(item -> item.getEmployee().getId(), item -> item));
		
		for (PhilHealthReportItem item : philHealthReport.getItems()) {
			PayrollReportItem payrollReportItem = itemMapping.get(item.getEmployeeId());
			if (payrollReportItem != null) {
				payrollReportItem.setPhilHealthReportItem(item);
			}
		}
		
		for (PagIbigReportItem item : pagIbigReport.getItems()) {
			PayrollReportItem payrollReportItem = itemMapping.get(item.getEmployeeId());
			if (payrollReportItem != null) {
				payrollReportItem.setPagIbigReportItem(item);
			}
		}
		
		PayrollReport report = new PayrollReport();
		report.setYearMonth(yearMonth);
		
		if (employee != null) {
			report.setItems(payrollReportItems.stream()
					.filter(item -> item.getEmployee().equals(employee))
					.collect(Collectors.toList()));
		} else {
			report.setItems(payrollReportItems);
		}
		
		return report;
	}
	
}
