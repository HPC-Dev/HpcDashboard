create database results_dashboard;

use results_dashboard;

show tables;

select * from cpu_info;

select * from benchmarks;

select * from applications;

select * from results;
select * from user;
select * from role;
select * from users_roles;

select b.app_name,a.bm_name,a.bm_full_name,a.bm_dur,a.bm_metric,a.bm_size, a.bm_size_units,a.bm_units,a.est_runtime from benchmarks a JOIN applications b on a.app_name=b.app_id;

select b.app_name,a.bm_name,a.bm_full_name,a.bm_dur,a.bm_metric,a.bm_size, a.bm_size_units,a.bm_units,a.est_runtime from benchmarks a JOIN applications b on a.app_name=b.app_id;

select app_name from benchmarks;

#delete from users_roles where user_id=3;
#delete from role where id=4;
#delete from user where id=3;




#delete from my_user_details where id=1;
#ALTER TABLE my_user_details auto_increment=1;
#drop table applications;
#drop table results;

