alter table salary add paySchedule varchar(20);
alter table salary add payType varchar(20);
update salary set paySchedule = 'WEEKLY', payType = 'PER_DAY';
alter table salary modify column paySchedule varchar(20) not null;
alter table salary modify column payType varchar(20) not null;
