package com.jchs.payrollapp.service.impl;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jchs.payrollapp.dao.EmployeeAttendanceDao;
import com.jchs.payrollapp.dao.EmployeeLoanPaymentRepository;
import com.jchs.payrollapp.dao.PayrollDao;
import com.jchs.payrollapp.dao.PayslipAdjustmentDao;
import com.jchs.payrollapp.dao.PayslipDao;
import com.jchs.payrollapp.dao.SalaryDao;
import com.jchs.payrollapp.model.Attendance;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.EmployeeLoanPayment;
import com.jchs.payrollapp.model.PayType;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.PayslipAdjustment;
import com.jchs.payrollapp.model.PayslipAdjustmentType;
import com.jchs.payrollapp.model.PhilHealthContributionTable;
import com.jchs.payrollapp.model.SSSContributionTable;
import com.jchs.payrollapp.model.Salary;
import com.jchs.payrollapp.model.search.EmployeeAttendanceSearchCriteria;
import com.jchs.payrollapp.model.search.PayslipAdjustmentSearchCriteria;
import com.jchs.payrollapp.model.search.PayslipSearchCriteria;
import com.jchs.payrollapp.model.search.SalarySearchCriteria;
import com.jchs.payrollapp.service.EmployeeLoanService;
import com.jchs.payrollapp.service.PayrollService;
import com.jchs.payrollapp.service.PhilHealthService;
import com.jchs.payrollapp.service.SSSService;
import com.jchs.payrollapp.service.SystemService;
import com.jchs.payrollapp.util.ApplicationUtil;
import com.jchs.payrollapp.util.DateUtil;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired private PayrollDao payrollDao;
	@Autowired private PayslipDao payslipDao;
	@Autowired private SalaryDao salaryDao;
	@Autowired private PayslipAdjustmentDao payslipAdjustmentDao;
	@Autowired private EmployeeAttendanceDao employeeAttendanceDao;
	@Autowired private SSSService sssService;
	@Autowired private PhilHealthService philHealthService;
	@Autowired private EmployeeLoanPaymentRepository employeeLoanPaymentRepository;
	@Autowired private SystemService systemService;
	@Autowired private EmployeeLoanService employeeLoanService;
	
	@Override
	public List<Payroll> getAllPayroll() {
		return payrollDao.getAll();
	}

	@Transactional
	@Override
	public void save(Payroll payroll) {
		payrollDao.save(payroll);
	}

	@Override
	public Payroll getPayroll(long id) {
		Payroll payroll = payrollDao.get(id);
		payroll.setPayslips(payslipDao.findAllByPayroll(payroll));
		
		for (int i = 0; i < payroll.getPayslips().size(); i++) {
			payroll.getPayslips().set(i, getPayslip(payroll.getPayslips().get(i).getId()));
		}
		
		return payroll;
	}

	@Override
	public Payroll findPayrollByBatchNumber(long batchNumber) {
		return payrollDao.findByBatchNumber(batchNumber);
	}

	@Transactional
	@Override
	public void delete(Payroll payroll) {
		payrollDao.delete(payroll);
	}

	private void generateEmployeeAttendance(Payslip payslip) {
		for (Date date : payslip.getPeriodCovered().toDateList()) {
			if (shouldGenerateEmployeeAttendance(payslip.getEmployee(), date)) {
				EmployeeAttendance attendance = new EmployeeAttendance();
				attendance.setEmployee(payslip.getEmployee());
				attendance.setDate(date);
				attendance.setAttendance(Attendance.WHOLE_DAY);
				employeeAttendanceDao.save(attendance);
			}
		}
	}

	private boolean shouldGenerateEmployeeAttendance(Employee employee, Date date) {
	    if (!isEmployeeAttendanceNotYetGenerated(employee, date)) {
	        return false;
	    }
	    
	    return DateUtil.isSunday(date) ? ApplicationUtil.isSundayAWorkingDay() : true;
	}

	private boolean isEmployeeAttendanceNotYetGenerated(Employee employee, Date date) {
		return employeeAttendanceDao.findByEmployeeAndDate(employee, date) == null;
	}
	
	private void addSSSPagibigPhilHealthContributionAdjustments(Payslip payslip, String contributionMonth) {
		BigDecimal sssContribution = null;
		BigDecimal philHealthContribution = null;
		SSSContributionTable sssContributionTable = sssService.getSSSContributionTable();
		PhilHealthContributionTable philHealthContributionTable = philHealthService.getContributionTable();
		
		switch (payslip.getEmployee().getPaySchedule()) {
		case WEEKLY:
			BigDecimal employeeCompensation = getEmployeeCompensationForMonthYear(
					payslip.getEmployee(), DateUtil.toYearMonth(contributionMonth));
			sssContribution = sssContributionTable.getEmployeeContribution(employeeCompensation);
			philHealthContribution = philHealthContributionTable.getEmployeeShare(employeeCompensation);
			break;
		case SEMIMONTHLY: {
			BigDecimal referenceCompensation = null;
			if (payslip.getEmployee().getPayType() == PayType.PER_DAY) {
				referenceCompensation = getEmployeeCompensationForMonthYear(
						payslip.getEmployee(), DateUtil.toYearMonth(contributionMonth));
			} else {
				referenceCompensation = salaryDao.findByEmployeeAndEffectiveDate(
				        payslip.getEmployee(), DateUtil.toDate(DateUtil.toYearMonth(contributionMonth)))
				                .getRate().multiply(BigDecimal.valueOf(2L));
			}
			
			sssContribution = sssContributionTable.getEmployeeContribution(referenceCompensation);
			philHealthContribution = philHealthContributionTable.getEmployeeShare(referenceCompensation);
			break;
		}
		}
		
		BigDecimal pagibigContribution = systemService.getPagibigContributionValue();
		
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.SSS);
		adjustment.setDescription("SSS");
		adjustment.setAmount(sssContribution.negate());
		adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);

		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PHILHEALTH);
		adjustment.setDescription("PhilHealth");
		adjustment.setAmount(philHealthContribution.negate());
        adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);
		
		adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.PAGIBIG);
		adjustment.setDescription("Pag-IBIG");
		adjustment.setAmount(pagibigContribution.negate());
        adjustment.setContributionMonth(contributionMonth);
		payslipAdjustmentDao.save(adjustment);
	}

	@Override
	public Payslip getPayslip(long id) {
		Payslip payslip = payslipDao.get(id);
		if (payslip != null) {
			payslip.setEffectiveSalaries(findEffectiveSalaries(payslip));
			payslip.setAttendances(findAllEmployeeAttendances(payslip));
			payslip.setLoanPayments(employeeLoanPaymentRepository.findAllByPayslip(payslip));
			payslip.setAdjustments(payslipAdjustmentDao.findAllByPayslip(payslip));
		}
		return payslip;
	}

	private List<EmployeeAttendance> findAllEmployeeAttendances(Payslip payslip) {
		EmployeeAttendanceSearchCriteria criteria = new EmployeeAttendanceSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setDateTo(payslip.getPeriodCoveredTo());
		
		return employeeAttendanceDao.search(criteria);
	}

	private List<Salary> findEffectiveSalaries(Payslip payslip) {
		SalarySearchCriteria criteria = new SalarySearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setEffectiveDateFrom(payslip.getPeriodCoveredFrom());
		criteria.setEffectiveDateTo(payslip.getPeriodCoveredTo());
		
		return salaryDao.search(criteria);
	}

	@Transactional
	@Override
	public void save(Payslip payslip) {
		boolean isNew = payslip.isNew();
		payslipDao.save(payslip);
		if (isNew) {
			generateEmployeeAttendance(payslip);
			if (hasNegativeBalanceInPreviousPayslip(payslip)) {
				generateAdjustmentForNegativeBalance(payslip);
			}
		}
	}

	private void generateAdjustmentForNegativeBalance(Payslip payslip) {
		PayslipAdjustment adjustment = new PayslipAdjustment();
		adjustment.setPayslip(payslip);
		adjustment.setType(PayslipAdjustmentType.OTHERS);
		adjustment.setDescription("balance");
		adjustment.setAmount(findPreviousPayslip(payslip).getNetPay());
		payslipAdjustmentDao.save(adjustment);
	}

	private boolean hasNegativeBalanceInPreviousPayslip(Payslip payslip) {
		Payslip previousPayslip = findPreviousPayslip(payslip);
		if (previousPayslip != null) {
			return previousPayslip.hasNegativeBalance();
		} else {
			return false;
		}
	}

	private Payslip findPreviousPayslip(Payslip payslip) {
		PayslipSearchCriteria criteria = new PayslipSearchCriteria();
		criteria.setEmployee(payslip.getEmployee());
		criteria.setPayDateLessThan(payslip.getPayroll().getPayDate());
		
		List<Payslip> result = payslipDao.search(criteria);
		if (!result.isEmpty()) {
			return getPayslip(result.get(0).getId());
		} else {
			return null;
		}
	}

	@Transactional
	@Override
	public void save(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.save(payslipAdjustment);
	}

	@Transactional
	@Override
	public void delete(PayslipAdjustment payslipAdjustment) {
		payslipAdjustmentDao.delete(payslipAdjustment);
	}

	private BigDecimal getEmployeeCompensationForMonthYear(Employee employee, YearMonth yearMonth) {
		return salaryDao.getEmployeeCompensationForMonthYear(employee, yearMonth);
	}

	@Transactional
	@Override
	public void delete(Payslip payslip) {
		payslipDao.delete(payslip);
	}

	@Override
	public Payslip findAnyPayslipByEmployee(Employee employee) {
		return payslipDao.findAnyPayslipByEmployee(employee);
	}

	@Transactional
	@Override
	public void postPayroll(Payroll payroll) {
		payroll.setPosted(true);
		payrollDao.save(payroll);
		
		markEmployeeLoansWithLastPaymentInPayrollAsPaid(payroll);
	}

	private void markEmployeeLoansWithLastPaymentInPayrollAsPaid(Payroll payroll) {
		for (Payslip payslip : payroll.getPayslips()) {
			for (EmployeeLoanPayment payment : payslip.getLoanPayments()) {
				if (payment.isLast()) {
					employeeLoanService.markAsPaid(payment.getLoan());
				}
			}
		}
	}

    @Transactional
    @Override
    public void regenerateGovernmentContributions(Payslip payslip, String contributionMonth) {
        deleteGovernmentContributions(payslip);
        addSSSPagibigPhilHealthContributionAdjustments(payslip, contributionMonth);
    }

	private void deleteGovernmentContributions(Payslip payslip) {
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.SSS);
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.PHILHEALTH);
		payslipAdjustmentDao.deleteByPayslipAndType(payslip, PayslipAdjustmentType.PAGIBIG);
	}

	@Transactional
	@Override
	public void regenerateGovernmentContributions(Payroll payroll, String contributionMonth) {
		for (Payslip payslip : payroll.getPayslips()) {
			regenerateGovernmentContributions(payslip, contributionMonth);
		}
	}

	@Override
	public long getNextBatchNumber() {
		return payrollDao.getLatestBatchNumber();
	}

    @Override
    public List<PayslipAdjustment> searchPayslipAdjustment(PayslipAdjustmentSearchCriteria criteria) {
        return payslipAdjustmentDao.search(criteria);
    }

}
