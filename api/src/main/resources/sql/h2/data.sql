insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (1,'13910230012' ,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','����',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);

--employee start
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(1,'employee' ,'管理员','admin','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'admin' ,'INLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(2,'chief' ,'主任','chief','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'chief' ,'INLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(3,'expert' ,'专家','expert','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'expert' ,'INLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(4,'operator' ,'接线员','operator','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'operator' ,'INLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
--employee end
--insert into expert_operator(expert_id , operator_id) values (3 , 4);
insert into health_rule (id , name , code , usage , can_reply , type , user_id , unit , level ,min , max,created_date,remark , version)
values (1 , '心跳规则' , '1' , 'group' ,1, '11', null , '次' , 'success' , 80 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);

insert into health_rule (id , name , code , usage , can_reply, type , user_id , unit , level ,min , max,created_date,remark , version)
values (2 , '心跳规则的回复区间' , '1' , 'filter' , 1, '11' , null , '次' , 'danger' , 0 , 40 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , user_id , unit , level ,min , max,created_date,remark , version)
values (3 , '心跳规则的回复区间' , '1' , 'filter' , 1, '11' , null , '次' , 'warning' , 41 , 60 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , user_id , unit , level ,min , max,created_date,remark , version)
values (4 , '心跳规则的回复区间' , '1' , 'filter' , 1, '11' , null , '次' , 'success' , 61 , 90 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , user_id , unit , level ,min , max,created_date,remark , version)
values (5 , '心跳规则的回复区间' , '1' , 'filter' , 1, '11' , null , '次' , 'warning' , 91 , 100 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , user_id , unit , level ,min , max,created_date,remark , version)
values (6 , '心跳规则的回复区间' , '1' , 'filter' , 1, '11' , null , '次' , 'danger' , 101 , 200 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
-- health_examination start
insert into health_examination (id , user_id , test_item , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (1,null,'PHONE','VIP','danger',80,120,60,60,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
-- health_examination end
-- task start
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (1,'examinationTask' , 1 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
-- task end
commit;