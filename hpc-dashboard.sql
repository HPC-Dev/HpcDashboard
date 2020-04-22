create database results_dashboard;

use results_dashboard;

show tables;

select cores,cpu_sku from cpu_info;

select * from cpu_info;

select * from benchmarks;

select * from applications;

select * from results;

select * from results where bm_name="ls-3cars" and cpu="7F72" and nodes=1;

select * from results where app_name='fluent' and bm_name='fluent-sed4';

select DISTINCT bm_name from results where app_name='fluent' ORDER BY nodes ASC;

select * from average_result;

select * from average_result where app_name="lsdyna" and cpu_sku = "7F52" and nodes =1;

select DISTINCT app_name from average_result;


select DISTINCT app_name from results ORDER BY app_name ASC;

select DISTINCT bm_name from results ORDER BY bm_name ASC;

select DISTINCT nodes from results ORDER BY nodes ASC;

select DISTINCT cpu from results ORDER BY cpu ASC;





select * from user;
select * from role;	
select * from users_roles;




select app_name from benchmarks;

#delete from users_roles where user_id=3;
#delete from role where id=4;
#delete from user where id=3;




#delete from my_user_details where id=1;
#ALTER TABLE benchmarks auto_increment=1;


drop table average_result;

drop table benchmarks;

drop table applications;
#
drop table results;

drop table cpu_info;

