package com.jchs.payrollapp.onstartup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateSSSContributionTableEntries implements ApplicationRunner {

	@Autowired private JdbcTemplate jdbcTemplate;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!isCurrentValuesBackedUp()) {
			backupCurrentValues();
			deleteCurrentValues();
			insertUpdatedValues();
		}
	}

	private static final String CHECK_IF_CURRENT_VALUES_ARE_BACKED_UP_SQL = "SELECT count(*) > 0 FROM information_schema.tables WHERE table_schema = 'jchs_payroll' AND table_name = 'ssscontributiontableentry_dec2024'";
	
	private boolean isCurrentValuesBackedUp() {
		return jdbcTemplate.queryForObject(CHECK_IF_CURRENT_VALUES_ARE_BACKED_UP_SQL, Boolean.class);
	}

	private static final String BACKUP_SQL = "create table ssscontributiontableentry_dec2024 select * from ssscontributiontableentry";
	
	private void backupCurrentValues() {
		jdbcTemplate.update(BACKUP_SQL);
	}

	private static final String DELETE_CURRENT_VALUES_SQL = "delete from ssscontributiontableentry";
	
	private void deleteCurrentValues() {
		jdbcTemplate.update(DELETE_CURRENT_VALUES_SQL);
	}

	private void insertUpdatedValues() {
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (0, 5249, 250, 500, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (5250, 5749.99, 275, 550, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (5750, 6249.99, 300, 600, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (6250, 6749.99, 325, 650, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (6750, 7249.99, 350, 700, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (7250, 7749.99, 375, 750, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (7750, 8249.99, 400, 800, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (8250, 8749.99, 425, 850, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (8750, 9249.99, 450, 900, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (9250, 9749.99, 475, 950, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (9750, 10249.99, 500, 1000, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (10250, 10749.99, 525, 1050, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (10750, 11249.99, 550, 1100, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (11250, 11749.99, 575, 1150, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (11750, 12249.99, 600, 1200, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (12250, 12749.99, 625, 1250, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (12750, 13249.99, 650, 1300, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (13250, 13749.99, 675, 1350, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (13750, 14249.99, 700, 1400, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (14250, 14749.99, 725, 1450, 10, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (14750, 15249.99, 750, 1500, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (15250, 15749.99, 775, 1550, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (15750, 16249.99, 800, 1600, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (16250, 16749.99, 825, 1650, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (16750, 17249.99, 850, 1700, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (17250, 17749.99, 875, 1750, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (17750, 18249.99, 900, 1800, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (18250, 18749.99, 925, 1850, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (18750, 19249.99, 950, 1900, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (19250, 19749.99, 975, 1950, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (19750, 20249.99, 1000, 2000, 30, false, null, null)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (20250, 20749.99, 1000, 2000, 30, false, 25, 50)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (20750, 21249.99, 1000, 2000, 30, false, 50, 100)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (21250, 21749.99, 1000, 2000, 30, false, 75, 150)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (21750, 22249.99, 1000, 2000, 30, false, 100, 200)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (22250, 22749.99, 1000, 2000, 30, false, 125, 250)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (22750, 23249.99, 1000, 2000, 30, false, 150, 300)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (23250, 23749.99, 1000, 2000, 30, false, 175, 350)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (23750, 24249.99, 1000, 2000, 30, false, 200, 400)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (24250, 24749.99, 1000, 2000, 30, false, 225, 450)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (24750, 25249.99, 1000, 2000, 30, false, 250, 500)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (25250, 25749.99, 1000, 2000, 30, false, 275, 550)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (25750, 26249.99, 1000, 2000, 30, false, 300, 600)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (26250, 26749.99, 1000, 2000, 30, false, 325, 650)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (26750, 27249.99, 1000, 2000, 30, false, 350, 700)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (27250, 27749.99, 1000, 2000, 30, false, 375, 750)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (27750, 28249.99, 1000, 2000, 30, false, 400, 800)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (28250, 28749.99, 1000, 2000, 30, false, 425, 850)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (28750, 29249.99, 1000, 2000, 30, false, 450, 900)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (29250, 29749.99, 1000, 2000, 30, false, 475, 950)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (29750, 30249.99, 1000, 2000, 30, false, 500, 1000)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (30250, 30749.99, 1000, 2000, 30, false, 525, 1050)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (30750, 31249.99, 1000, 2000, 30, false, 550, 1100)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (31250, 31749.99, 1000, 2000, 30, false, 575, 1150)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (31750, 32249.99, 1000, 2000, 30, false, 600, 1200)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (32250, 32749.99, 1000, 2000, 30, false, 625, 1250)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (32750, 33249.99, 1000, 2000, 30, false, 650, 1300)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (33250, 33749.99, 1000, 2000, 30, false, 675, 1350)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (33750, 34249.99, 1000, 2000, 30, false, 700, 1400)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (34250, 34749.99, 1000, 2000, 30, false, 725, 1450)");
		jdbcTemplate.update("insert into ssscontributiontableentry (compensationFrom, compensationTo, employeeContribution, employerContribution, employeeCompensation, household, employeeProvidentFundContribution, employerProvidentFundContribution)"
				+ " values (34750, null, 1000, 2000, 30, false, 750, 1500)");
	}

}
