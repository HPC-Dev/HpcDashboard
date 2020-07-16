create database results_dashboard;

use results_dashboard;

select DISTINCT app_name from average_result;


select DISTINCT LOWER(cpu_sku) from average_result where app_name="namd" ORDER BY cpu_sku ASC;

select DISTINCT bm_name from results where app_name="openfoam" ORDER BY nodes ASC;

select * from cpu_info;

#UPDATE cpu_info SET cpu_sku = LOWER(cpu_sku) where id in (26,27,28,29);

select cpu_sku,cores from average_result group by cpu_sku;

select DISTINCT bm_name from average_result where app_name="openfoam" and cpu_sku="n1-highcpu-96" ORDER BY nodes ASC;

show tables;

select DISTINCT a.cpu_sku, b.cores from average_result a, cpu_info b where a.cpu_sku=b.cpu_sku;

select * from results;

select precision_info from applications where app_name="openfoam";

select * from average_result where app_name="openfoam" and cpu_sku in ("7F52","7F72") and nodes=1;

select DISTINCT cpu from results where cpu_gen="rome" ORDER BY cpu ASC;


select * from benchmarks;

select *  from results;

select DISTINCT bios_ver from results ORDER BY os ASC;

select * from average_result;

SELECT  * FROM    results WHERE   time_stamp between '2020-01-11 00:00:00' AND  '2020-06-16 00:00:00';	



select DISTINCT app_name from average_result;

select job_id, result from results where bm_name="cfx_pump" and cpu="7F72" and nodes=1;


select avg_result from average_result where bm_name="cfx_pump" and cpu_sku="7F72" and nodes=1;



select * from average_result where app_name="abaqus" and cpu_sku="6248";


select DISTINCT app_name from average_result where cpu_sku="7F32" ORDER BY app_name ASC;

select * from average_result where app_name="openfoam"  and cpu_sku in( "7F52", "7F32", "7F72") and nodes =1 ORDER BY avg_result DESC;


select * from average_result where app_name="openfoam" and cpu_sku IN ("7F52", "7F32", "7F72") and nodes =1 ORDER BY bm_name,avg_result DESC;

select * from average_result where app_name="namd" and cpu_sku IN ("7F52", "7F32", "7F72") and nodes =1 ORDER BY bm_name,avg_result;

select * from average_result where app_name="namd" and cpu_sku IN ("n1-standard-96", "n2d-standard-224", "n2d-highcpu-224","n1-highcpu-96") and nodes =1 ORDER BY bm_name;

#update applications set precision_info="Elapsed Time" where app_id="openfoam_v1906";

select DISTINCT app_name from average_result;


select DISTINCT app_name from results ORDER BY app_name ASC;

select DISTINCT bm_name from results ORDER BY bm_name ASC;

select DISTINCT nodes from results ORDER BY nodes ASC;

select DISTINCT cpu from results ORDER BY cpu ASC;


select * from user;
select * from role;
select * from users_roles;

update role set name="ROLE_TEAM" where id=11;


#insert into users_roles (user_id,role_id) values (17,7),(18,7),(19,7);


#update users_roles set role_id=2 where user_id=5 and role_id=6;

#update applications set comp_flags="-march=znver2 -O3" where app_id="openfoam_v1906";


select app_name from benchmarks;

#delete from users_roles where user_id=5 and role_id=7;
delete from user where id=5;


SELECT * from average_result where cpu_sku ="7F52" and app_name ="openfoam" ORDER BY bm_name;

#delete from my_user_details where id=1;
#ALTER TABLE benchmarks auto_increment=1;


drop table average_result;

drop table results;



#drop table benchmarks;

#drop table applications;

#drop table cpu_info;

ALTER TABLE results
ADD COLUMN inserted_at 
  TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
  ON UPDATE CURRENT_TIMESTAMP;


-- Indexes

show indexes from results_dashboard.results;

CREATE INDEX cpu_index ON results_dashboard.results (cpu);
