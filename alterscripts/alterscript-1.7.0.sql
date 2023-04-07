insert into employeeloantype
(description)
select distinct description from employeeloan;

update employeeloan
set loanType_id = (select id from employeeloantype where description = employeeloan.description);
