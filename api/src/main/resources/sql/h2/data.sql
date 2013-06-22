insert into user (id, code , username, name, password, sex, type ,birthday,address,stature,weight,fn_place,city,tel,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark,mobile_num ,is_free , version) values (1,'13910230012' ,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc',1 , 'SIMPLE','1983-06-04 01:00:00','测试',1.72,72,'','','','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','','',1,1);

--employee start
insert into employee (id ,type, name , username , password ,status,enable,dismissed,sex,expire,birthday,id_card , mobile ,created_date,last_updated,version) values 
(1,'employee' ,'admin','admin','691b14d79bf0fa2215f155235df5e670b64394cc','INLINE',1,0,1,null,'1983-06-04 01:00:00','430203198602031218','13028339212','2012-06-04 01:00:00','2012-06-04 01:00:00',1);
--employee end
commit;
