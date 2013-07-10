insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (1,'13911111111' ,'测试用户1','测试用户1','691b14d79bf0fa2215f155235df5e670b64394cc',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址1',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);
insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (2,'13922222222' ,'测试用户2','测试用户2','691b14d79bf0fa2215f155235df5e670b64394cc',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址2',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);


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
insert into expert_operator(expert_id , operator_id) values (3 , 4);
insert into health_rule (id , name , code , usage , can_reply , type , employee_id , group_id, unit , level ,min , max,created_date,remark , version)
values (1 , '心跳规则' , '3' , 'group' ,1, '11', null , null, '次' , 'success' , 0 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (2 , '心跳规则的无效区间' , '3' , 'filter' , 1, '11' , null ,1, '次' , 'outside' , -9999 , 0 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (3 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1, '次' , 'danger' , 0 , 60 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (4 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1,'次' , 'success' , 60 , 90 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (5 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1, '次' , 'warning' , 90 , 100 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (6 , '心跳规则的无效区间' , '3' , 'filter' , 1, '11' , null ,  1 ,'次' , 'outside' , 100 , 9999 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);

insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (1, '标题' ,  '心跳异常的建议' , '心跳异常' ,2, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (2, '标题' , '需要检查的建议' ,'需要检查' ,  3, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (3, '标题' , '心跳正常的建议' , '心跳正常' , 4, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (4, '标题' , '需要检查的建议' , '需要检查' , 5, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (5, '标题' , '心跳异常的建议' , '心跳异常' , 6, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);

insert into health_rule (id , name , code , usage , can_reply , type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (7 , '呼吸规则' , '5' , 'group' ,1, '11', null ,1, '次' , 'success' , 0 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (8 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 1, '次' , 'outside' , -9999 , 0 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (9 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 1,'次' , 'warning' , 0 , 40 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (10 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null ,1, '次' , 'success' , 40 , 90 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (11, '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 1, '次' , 'warning' , 90 , 100 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (12 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 1, '次' , 'outside' , 100 , 9999 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);

insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (6, '标题' , '呼吸异常的建议' , '呼吸异常' ,  8, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (7, '标题' ,  '需要检查呼吸的建议' ,'需要检查呼吸' , 9, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (8, '标题' ,  '呼吸正常的建议' ,'呼吸正常' , 10, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (9, '标题' ,  '需要检查呼吸的建议' , '需要检查呼吸' ,11, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (10, '标题' , '呼吸异常的建议' ,'呼吸异常' ,  12, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);


-- health_examination start
insert into health_examination (id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (1,1,'PHONE', '测试用户1','VIP','success',80,120,60,60,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
insert into health_examination (id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (2,1,'PHONE', '测试用户1','VIP','outside',80,120,-100,80,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
insert into health_examination (id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (3,2,'PHONE', '测试用户2','VIP','outside',80,120,300,40,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
insert into health_examination (id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (4,2,'PHONE', '测试用户2','VIP','warning',80,120,77,90,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
insert into health_examination (id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (5,2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);

-- health_examination end
-- task start
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (1,'examinationTask' , 1 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (2,'examinationTask' , 2 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (3,'examinationTask' , 3 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (4,'examinationTask' , 4 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
insert into task (id , type ,examination_id , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (5,'examinationTask' , 5 ,'pending',null,4,0,'2013-07-04 01:00:00',null,1);
-- task end
commit;