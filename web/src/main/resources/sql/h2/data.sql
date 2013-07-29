insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (1,'13911111111' ,'13911111111','测试用户1','2e8ec7d18a6e108fce3af34044c661ffa56b084f',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址1',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);
insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (2,'13922222222' ,'13922222222','测试用户2','2e8ec7d18a6e108fce3af34044c661ffa56b084f',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址2',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);
insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (3,'13912345678' ,'13912345678','上传用户','2e8ec7d18a6e108fce3af34044c661ffa56b084f',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址2',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);
insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (4,'13412345678' ,'13412345678','测试用户3','2e8ec7d18a6e108fce3af34044c661ffa56b084f',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址2',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);
insert into user (id, mobile , username, name, password, gender, id_card , type ,birthday,address,stature,weight,city,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark ,is_free , version)
values (5,'13512345678' ,'13512345678','测试用户4','2e8ec7d18a6e108fce3af34044c661ffa56b084f',1 ,'430203198502011218', 'SIMPLE','1983-06-04 01:00:00','地址2',1.72,72,'','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','',1,1);

--employee start
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(1,'employee' ,'管理员','admin','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'admin' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(2,'chief' ,'主任','chief','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'chief' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(3,'chief' ,'主任1','chief1','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'chief' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(4,'chief' ,'主任2','chief2','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'chief' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(5,'expert' ,'专家','expert','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'expert' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(6,'expert' ,'专家1','expert1','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'expert' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(7,'expert' ,'专家2','expert2','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'expert' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(8,'operator' ,'接线员','operator','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'operator' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(9,'operator' ,'接线员1','operator1','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'operator' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
insert into employee (id ,type, name , username , password , roles , status,enabled,dismissed,gender,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(10,'operator' ,'接线员2','operator2','2e8ec7d18a6e108fce3af34044c661ffa56b084f', 'operator' ,'ONLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);

insert into expert_operator (expert_id, operator_id) values
(5, 8);


--employee end
insert into expert_operator(expert_id , operator_id) values (3 , 4);
insert into health_rule (id , name , code , rule_usage , can_reply , type , employee_id , group_id, unit , level ,min , max,created_date,remark , version)
values (1 , '心跳规则' ,          '3' , 'group' ,1, '11', null , null, '次' , 'success' , 0 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (2 , '心跳规则的无效区间' , '3' , 'filter' , 1, '11' , null ,1, '次' , 'outside' , -9999 , 0 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (3 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1, '次' , 'danger' , 0 , 60 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (4 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1,'次' , 'success' , 60 , 90 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (5 , '心跳规则的回复区间' , '3' , 'filter' , 1, '11' , null , 1, '次' , 'warning' , 90 , 100 , '2012-06-04 01:00:00' , '心跳规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
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

insert into health_rule (id , name , code , rule_usage , can_reply , type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (7 , '呼吸规则' , '5' , 'group' ,1, '11', null ,1, '次' , 'success' , 0 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (8 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 7, '次' , 'outside' , -9999 , 0 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (9 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 7,'次' , 'warning' , 0 , 40 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (10 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 7, '次' , 'success' , 40 , 90 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (11, '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 7, '次' , 'warning' , 90 , 100 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (12 , '呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , null , 7, '次' , 'outside' , 100 , 9999 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);

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

insert into health_rule (id , name , code , rule_usage , can_reply , type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (13 , '专家的自定义呼吸规则' ,          '5' , 'group' ,1, '11', 5 , null, '次' , 'success' , 0 , 100 , '2012-06-04 01:00:00' , '心跳数字的用于检测的区间' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (14 , '专家的自定义呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , 5 , 13, '次' , 'outside' , -9999 , 0 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (15 , '专家的自定义呼吸规则的回复区间' , '5' , 'filter' , 1, '11' ,5 , 13,'次' , 'warning' , 0 , 40 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (16 , '专家的自定义呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , 5 , 13, '次' , 'success' , 40 , 90 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id,  unit , level ,min , max,created_date,remark , version)
values (17, '专家的自定义呼吸规则的回复区间' , '5' , 'filter' , 1, '11' , 5 , 13, '次' , 'warning' , 90 , 100 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);
insert into health_rule (id , name , code , rule_usage , can_reply, type , employee_id ,group_id, unit , level ,min , max,created_date,remark , version)
values (18 , '专家的自定义呼吸规则的回复区间' , '5' , 'filter' , 1, '11' ,5 , 13, '次' , 'outside' , 100 , 9999 , '2012-06-04 01:00:00' , '呼吸规则的说明' , 1);

insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (11, '标题' , '呼吸异常的个性建议' , '呼吸异常' ,  14, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (12, '标题' ,  '需要检查呼吸的个性建议' ,'需要检查呼吸' , 15, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (13, '标题' ,  '呼吸正常的个性建议' ,'呼吸正常' , 16, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (14, '标题' ,  '需要检查呼吸的个性建议' , '需要检查呼吸' ,17, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);
insert into health_rule_reply (id , title , content , result , rule_id, created_date, last_updated, version)
values (15, '标题' , '呼吸异常的个性建议' ,'呼吸异常' ,  18, '2012-06-04 01:00:00', '2012-06-04 01:00:00' , 1);

insert into health_rule_user (rule_id, user_id) values
(13, 1);

-- health_examination start
insert into health_examination (id , apk_id ,  user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (1, '1' , 1,'PHONE', '测试用户1','VIP','success',80,120,60,60,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-01 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (2, '2' , 1,'PHONE', '测试用户1','VIP','outside',80,120,-100,80,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-02 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (3, '3' , 1,'PHONE', '测试用户1','VIP','outside',80,120,300,40,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-03 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (4, '4' , 1,'PHONE', '测试用户1','VIP','warning',80,120,77,90,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-04 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (5, '5' , 1,'PHONE', '测试用户1','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-05 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (6, '6' , 1,'PHONE', '测试用户1','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-06 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (7, '7' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-07 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (8, '8' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-08 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (9, '9' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-09 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (10, '10' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-10 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (11, '11' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-11 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (12, '12' , 2,'PHONE', '测试用户2','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-12 01:00:00',1);

insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (13, '13' , 4,'PHONE', '测试用户4','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-13 01:00:00',1);
insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (14, '14' , 5,'PHONE', '测试用户5','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-14 01:00:00',1);

-- health_examination end
-- task start
insert into task (id , type ,examination_id , user_id , apk_id , user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (1,'examinationTask' , 1 , 1 , '1' , '测试用户1' , 'pending',null,8,0,'2013-07-01 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id , user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (2,'examinationTask' , 2 , 1 , '2' , '测试用户1' , 'pending',null,8,0,'2013-07-02 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name ,  status,expert_id,operator_id,auto , created_date , completed_date,version)
values (3,'examinationTask' , 3 , 1 , '3' , '测试用户1' , 'pending',null,8,0,'2013-07-03 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (4,'examinationTask' , 4 , 1 , '4' , '测试用户1' , 'pending',null,8,0,'2013-07-04 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (5,'examinationTask' , 5 , 1 , '5' , '测试用户1' , 'pending',null,8,0,'2013-07-05 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (6,'examinationTask' , 6 , 1 , '6' , '测试用户1' , 'pending',null,8,0,'2013-07-06 01:00:00',null,1);

insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (7,'examinationTask' , 7 , 2 , '7' , '测试用户2'  , 'pending',null,9,0,'2013-07-07 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (8,'examinationTask' , 8 , 2 , '8' , '测试用户2', 'pending',null,9,0,'2013-07-08 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name ,  status,expert_id,operator_id,auto , created_date , completed_date,version)
values (9,'examinationTask' , 9 , 2 , '9' , '测试用户2' , 'pending',null,9,0,'2013-07-09 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (10,'examinationTask' , 10 , 2 , '10' , '测试用户2' , 'pending',null,9,0,'2013-07-10 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (11,'examinationTask' , 11 , 2 , '11' , '测试用户2' , 'pending',null,9,0,'2013-07-11 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (12,'examinationTask' , 12 , 2 , '12' , '测试用户2' , 'pending',null,9,0,'2013-07-12 01:00:00',null,1);

insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (13,'examinationTask' , 13 , 4 , '13' , '测试用户4' , 'pending',null,8,0,'2013-07-12 01:00:00',null,1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (14,'examinationTask' , 14 , 5 , '14' , '测试用户5' , 'pending',null,9,0,'2013-07-12 01:00:00',null,1);
-- task end
-- system_config start
insert into system_config (id , type , config_key , value ) values (1 , 'basic' , 'upload.rootPath' , 'c:/upload/');
insert into system_config (id , type , config_key , value ) values (2 , 'basic' , 'examination.reply.isAuto' , 'false');
-- system_config end


insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) 
values (15, '15' , 5,'PHONE', '测试用户5','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-14 01:00:00',1);
insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)
values (15,'examinationTask' , 14 , 5 , '14' , '测试用户5' , 'pending',null,9,0,'2013-07-12 01:00:00',null,1);
commit;