alter table salary add paySchedule varchar(20);
alter table salary add payType varchar(20);
update salary set paySchedule = 'WEEKLY', payType = 'PER_DAY';
alter table salary modify column paySchedule varchar(20) not null;
alter table salary modify column payType varchar(20) not null;

insert into philhealthcontributiontable (id, floor, ceiling, multiplier, householdMonthlyContribution) values (1, 10000, 40000, 2.75, 0);
insert into systemparameter (id, name, value) values (1, 'PAGIBIG_CONTRIBUTION_VALUE', 100);
