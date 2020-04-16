create database results_dashboard;

use results_dashboard;

show tables;

select cores,cpu_sku from cpu_info;

select * from cpu_info;

select * from benchmarks;

select * from applications;

select * from results where bm_name="ls-3cars" and cpu="7F72" and nodes=1;

select * from results;

delete from results where job_id in ("66215","66216");

select * from average_result where bm_name="ls-3cars" and cpu_sku = "7F72" and nodes =1;

select * from average_result;

select * from user;
select * from role;
select * from users_roles;




select app_name from benchmarks;

#delete from users_roles where user_id=3;
#delete from role where id=4;
#delete from user where id=3;




#delete from my_user_details where id=1;
#ALTER TABLE my_user_details auto_increment=1;
#

drop table average_result;

drop table benchmarks;

drop table applications;
#
drop table results;

