insert into user (id, mobile , username, name, password, gender, type ,birthday,address,stature,weight,fn_place,city,tel,em_contact1,em_contact1_tel,em_contact2,em_contact2_tel , bad_habits , anamnesis , created_date , last_updated , remark,mobile_num ,is_free , version) values (1,'13910230012' ,'admin','Admin','691b14d79bf0fa2215f155235df5e670b64394cc',1 , 'SIMPLE','1983-06-04 01:00:00','����',1.72,72,'','','','','','','','','','2012-06-04 01:00:00','2012-06-04 01:00:00','','',1,1);

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


commit;