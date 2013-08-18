for (var i=15; i<45; i++) {

function formatDate(val) {
	return val >= 10 ? val : ('0'+val)
}

console.info(
"insert into health_examination (id , apk_id , user_id , test_item , user_name , user_type , level , blood_pressure_low,blood_pressure_high,heart_rhythm , breath,body_temp,pulserate,heart_data,latitude,altitude,temp,humidity,pressure,charge_type,heart_features,algorithm_version,created_date,version) " +
"values (" + i + ", '"+ i + "', 3,'PHONE', '上传用户','VIP','danger',80,120,10,10,37.5,60,'',0.0,200,37.6,null,null,'YEAR',null,null,'2013-07-" + formatDate (i-14)+ " 01:00:00',1);" +
"insert into task (id , type ,examination_id , user_id , apk_id ,user_name , status,expert_id,operator_id,auto , created_date , completed_date,version)" +
"values (" + i + ",'examinationTask' , " + i + " , 3 , '14' , '上传用户' , 'pending',null,9,0,'2013-07-" + formatDate (i-14) +  " 01:00:00',null,1);"
)

}