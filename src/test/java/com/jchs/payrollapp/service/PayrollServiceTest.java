package com.jchs.payrollapp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.jchs.payrollapp.dao.EmployeeAttendanceDao;
import com.jchs.payrollapp.dao.PayslipDao;
import com.jchs.payrollapp.model.Attendance;
import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.EmployeeAttendance;
import com.jchs.payrollapp.model.Payroll;
import com.jchs.payrollapp.model.Payslip;
import com.jchs.payrollapp.model.util.DateInterval;
import com.jchs.payrollapp.service.PayrollService;
import com.jchs.payrollapp.service.impl.PayrollServiceImpl;
import com.jchs.payrollapp.util.ApplicationUtil;
import com.jchs.payrollapp.util.DateUtil;

@RunWith(MockitoJUnitRunner.class)
public class PayrollServiceTest {

    @Mock private PayslipDao payslipDao;
    @Mock private EmployeeAttendanceDao employeeAttendanceDao;
    
    @Captor private ArgumentCaptor<EmployeeAttendance> employeeAttendanceCaptor;
    
    private PayrollService payrollService;
    
    @Before
    public void setUp() {
        payrollService = new PayrollServiceImpl();
        
        ReflectionTestUtils.setField(payrollService, "payslipDao", payslipDao);
        ReflectionTestUtils.setField(payrollService, "employeeAttendanceDao", employeeAttendanceDao);
    }
    
    @Test
    public void savePayslip_generateAttendance_sundayAsWorkingDay() {
        Employee employee = new Employee();
        
        Payroll payroll = new Payroll();
        payroll.setPayDate(DateUtil.toDate("09/07/2017"));
        
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPayroll(payroll);
        payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("09/01/2017"), DateUtil.toDate("09/07/2017")));
        
        ApplicationUtil applicationUtil = new ApplicationUtil();
        ReflectionTestUtils.setField(applicationUtil, "sundayAsWorkingDay", true);
        ReflectionTestUtils.setField(ApplicationUtil.class, "instance", applicationUtil);
        
        payrollService.save(payslip);
        
        verify(payslipDao).save(payslip);
        verify(employeeAttendanceDao, times(7)).save(employeeAttendanceCaptor.capture());
        
        List<EmployeeAttendance> attendances = employeeAttendanceCaptor.getAllValues();
        verifyEmployeeAttendance(attendances.get(0), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/01/2017"));
        verifyEmployeeAttendance(attendances.get(1), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/02/2017"));
        verifyEmployeeAttendance(attendances.get(2), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/03/2017"));
        verifyEmployeeAttendance(attendances.get(3), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/04/2017"));
        verifyEmployeeAttendance(attendances.get(4), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/05/2017"));
        verifyEmployeeAttendance(attendances.get(5), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/06/2017"));
        verifyEmployeeAttendance(attendances.get(6), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/07/2017"));
    }

    private static void verifyEmployeeAttendance(EmployeeAttendance employeeAttendance, Employee employee, Attendance attendance, Date date) {
        assertSame(employee, employeeAttendance.getEmployee());
        assertEquals(attendance, employeeAttendance.getAttendance());
        assertEquals(date, employeeAttendance.getDate());
    }
    
    @Test
    public void savePayslip_generateAttendance_sundayIsNotAWorkingDay() {
        Employee employee = new Employee();
        
        Payroll payroll = new Payroll();
        payroll.setPayDate(DateUtil.toDate("09/07/2017"));
        
        Payslip payslip = new Payslip();
        payslip.setEmployee(employee);
        payslip.setPayroll(payroll);
        payslip.setPeriodCovered(new DateInterval(DateUtil.toDate("09/01/2017"), DateUtil.toDate("09/07/2017")));
        
        ApplicationUtil applicationUtil = new ApplicationUtil();
        ReflectionTestUtils.setField(applicationUtil, "sundayAsWorkingDay", false);
        ReflectionTestUtils.setField(ApplicationUtil.class, "instance", applicationUtil);
        
        payrollService.save(payslip);
        
        verify(payslipDao).save(payslip);
        verify(employeeAttendanceDao, times(6)).save(employeeAttendanceCaptor.capture());
        
        List<EmployeeAttendance> attendances = employeeAttendanceCaptor.getAllValues();
        verifyEmployeeAttendance(attendances.get(0), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/01/2017"));
        verifyEmployeeAttendance(attendances.get(1), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/02/2017"));
        verifyEmployeeAttendance(attendances.get(2), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/04/2017"));
        verifyEmployeeAttendance(attendances.get(3), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/05/2017"));
        verifyEmployeeAttendance(attendances.get(4), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/06/2017"));
        verifyEmployeeAttendance(attendances.get(5), employee, Attendance.WHOLE_DAY, DateUtil.toDate("09/07/2017"));
    }
    
}
