drop table if exists user;

create table user (
    id bigint auto_increment,
    mobile varchar(20) not null unique,
    id_card varchar(20),
    username varchar(20),
    email varchar(64) unique,
    name varchar(20) not null,
    password varchar(100) not null,
    roles varchar(100),
    retake_code varchar(10),
    retake_date datetime,
    retake_count int,
    salt varchar(100),
    last_login_date datetime,
    token_date datetime,
    gender int,
    type varchar(10),
    birthday datetime,
    address varchar(255),
    stature double,
    weight  double,
    city     varchar(20),
    em_contact1 varchar(20),
    em_contact1_tel varchar(20),
    em_contact2 varchar(20),
    em_contact2_tel varchar(20),
    bad_habits varchar(30),
    anamnesis varchar(30),
    created_date datetime,
    last_updated datetime,
    remark varchar(255),
    title varchar(20),
    is_free int,
    version int,
    primary key (id)
);

drop table if exists employee;

create table employee (
    id bigint auto_increment,
    type varchar(20),
    name varchar(20),
    username varchar(20) not null unique,
    password varchar(100),
    roles varchar(100),
    status varchar(10) not null,
    enabled int not null,
    dismissed int not null,
    gender int not null,
    expire datetime,
    birthday datetime,
    id_card varchar(20),
    mobile varchar(11),
    title varchar(20),
    company varchar(100),
    created_date datetime,
    last_updated datetime,
    last_live_date datetime,
    email varchar(64),
    salt varchar(100),
    last_login_date datetime,
    token_date datetime,
    version int,
    primary key(id)
);

drop table if exists expert_operator;

create table expert_operator (
    expert_id bigint not null ,
    operator_id bigint not null
);

drop table if exists health_rule;

create table health_rule (
    id bigint auto_increment,
    name varchar(20),
    code varchar(20),
    rule_usage varchar(10),
    can_reply tinyint,
    type varchar(30),
    employee_id bigint,
    group_id bigint,
    unit varchar(10),
    level varchar(10),
    min float,
    max float,
    created_date datetime,
    last_updated datetime,
    remark varchar(255),
    version int,
    primary key(id)
);

drop table if exists health_rule_reply;

create table health_rule_reply (
    id bigint auto_increment,
    title varchar(100) not null,
    content varchar(1000),
    result varchar(20),
    rule_id bigint ,
    created_date datetime,
    last_updated datetime,
    version int,
    primary key(id)
);

drop table if exists health_rule_user;

create table health_rule_user (rule_id bigint , user_id bigint);

drop table if exists health_examination;

create table health_examination (
    id bigint auto_increment,
    user_id bigint,
    user_name varchar(30),
    test_item varchar(30) not null,
    user_type varchar(30),
    is_test int,
    level varchar(30),
    apk_id varchar(40),
    blood_pressure_low int,
    blood_pressure_high int,
    heart_rhythm int,
    blood_sugar float,
    medicine varchar(64),
    blood_oxygen int,
    breath int,
    body_temp float,
    pulserate int,
    has_data_error int,
    heart_data varchar(200),
    latitude double,
    altitude int,
    temp float,
    humidity int,
    pressure int ,
    charge_type varchar(30),
    heart_features varchar(30),
    algorithm_version float,
    created_date datetime,
    version int,
    primary key(id)
);

drop table if exists health_reply;

create table health_reply(
    id bigint auto_increment,
    type varchar(30),
    reason varchar(100),
    result varchar(100),
    content varchar(1000),
    level varchar(30),
    employee_id bigint,
    examination_id bigint,
    created_date datetime,
    last_updated datetime,
    version int,
    primary key(id)
);

drop table if exists task;

create table task(
    id bigint auto_increment,
    status varchar(30),
    type varchar(30),
    apk_id varchar(40),
    user_id bigint,
    user_name varchar(30),
    expert_id bigint,
    operator_id bigint,
    examination_id bigint,
    auto int,
    created_date datetime,
    completed_date datetime,
    version int,
    primary key(id)
);

drop table if exists system_config;

create table system_config (
    id int auto_increment,
    type varchar(10),
    apk_id varchar(40),
    config_key varchar(30),
    value varchar(200),
    primary key(id)
);

drop table if exists card;

create table card (
    id int auto_increment,
    encoded_serial varchar(200) unique,
    serial varchar(100) unique,
    encoded_password varchar(100),
    days int,
    created_date datetime,
    created_batch int,
    expire_date datetime,
    actived_date datetime,
    user_id bigint,
    user_name varchar(20),
    charged_date datetime,
    charge_type varchar(10),
    employee_id bigint,
    employee_name varchar(20),
    used_count int ,
    max_used int,
    primary key(id)
);

drop  table  if exists apk;
create table apk (
    id bigint auto_increment,
    version varchar(20) not null unique,
    released int,
    enabled int,
    primary key(id)
);