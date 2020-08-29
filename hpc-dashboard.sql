create database results_dashboard;

use results_dashboard;

show tables;

select DISTINCT app_name from average_result;

select * from password_reset_token;

#drop table password_reset_token;

select DISTINCT LOWER(cpu_sku) from average_result where app_name="namd" ORDER BY cpu_sku ASC;

select DISTINCT bm_name from results where app_name="openfoam" ORDER BY nodes ASC;

select * from cpu_info;

#UPDATE cpu_info SET cpu_sku = LOWER(cpu_sku) where id in (26,27,28,29);

select cpu_sku,cores from average_result group by cpu_sku;

select DISTINCT cpu_sku from average_result where app_name="abaqus" and nodes=1  ;

show tables;

#drop table heat_map;

select * from heat_map;
select * from app_category;
select * from results;
select * from average_result;

select DISTINCT isv from heat_map where category="Computational Fluid Dynamics" and cpu_sku="Rome64" and run_type="freq_2933";	

select DISTINCT app_name from app_category where isv="ansys";

select * from heat_map where cpu_sku="Rome64" and run_type="baseline" and nodes=1 and category is not NULL;

select DISTINCT ISV from heat_map where cpu_sku="Rome64" and run_type="freq_2933" and nodes=1 and category="Computational Fluid Dynamics";

select DISTINCT app_name from heat_map where cpu_sku="Milan64" and run_type="freq_2933" and isv="Ansys";

select bm_name from heat_map where cpu_sku="Milan64" and run_type="freq_2933" and nodes=1 and category is not NULL;

select bm_name from heat_map where cpu_sku="Rome64" and run_type="freq_2933" and nodes=1 and category is not NULL;

select * from heat_map where cpu_sku in ("Milan64","Rome64") and run_type in ("baseline","freq_2933") and category is not NULL;

select precision_info from applications where app_name="openfoam";

select * from average_result where app_name="openfoam" and cpu_sku in ("7F52","7F72") and nodes=1;

select DISTINCT cpu from results where cpu_gen="rome" ORDER BY cpu ASC;


select * from benchmarks;

select *  from results;

select DISTINCT bios_ver from results ORDER BY os ASC;

select * from average_result;

SELECT * FROM results where bm_name="aba-e13" and cpu="Milan64_3200" and nodes=1 and run_type="baseline";

SELECT  * FROM  results WHERE app_name='cfx'and cpu="Milan64_3200" and run_type="freq_2933";

SELECT  DISTINCT (bm_name) FROM  results WHERE app_name='abaqus'and cpu="Rome64_3200" and run_type="freq_2933";

SELECT  DISTINCT LOWER(run_type) FROM  results WHERE app_name='cfx' ORDER BY run_type ASC;

select * from average_result where app_name= 'cfx' and cpu_sku like "Milan64%" and nodes =1 and run_type="freq_2933" ORDER BY bm_name;

select * from average_result where cpu_sku in ( "Milan64_3200", "Milan64_2933", "Rome64_2933") and run_type in("baseline","freq_2933");

select * from average_result where app_name= 'abaqus' and cpu_sku IN ("Milan64_3200", "Milan64_2933", "Rome64_2933") and run_type in("baseline","freq_2933") and nodes =1 ORDER BY bm_name,avg_result DESC;



select DISTINCT cpu_sku from average_result where run_type in("baseline","freq_2933");

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

delete from users_roles where user_id=126;
delete from user where id=126;


SELECT * from average_result where cpu_sku ="7F52" and app_name ="openfoam" ORDER BY bm_name;

#delete from my_user_details where id=1;
#ALTER TABLE benchmarks auto_increment=1;


drop table average_result;

drop table results;

drop table heat_map;




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
