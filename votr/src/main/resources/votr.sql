
--drop sequence seq_votrs if exists;
--create sequence seq_votrs start with 100;
--
--drop sequence seq_users if exists;
--create sequence seq_users start with 200;

--drop sequence seq_options if exists;
--create sequence seq_options start with 300;

--drop sequence seq_messages if exists;
--create sequence seq_messages start with 400;

drop table messages if exists;
drop table users if exists;
drop table options if exists;
drop table votrs if exists;

create table votrs (
	uuid varchar(50) primary key,
	name varchar(100) not null,
	descr varchar(500) not null
--	creat_date timestamp not null,
--	creat_user_hash varchar(50) not null
);

create table users (
	uuid varchar(50) primary key,
	email varchar(100) not null,
	alias varchar(100),
	votr_uuid varchar(50) not null
	
--	voted_option_num_order integer,
--	voted_option_date timestamp
);

create table options (
	uuid varchar(50) primary key,
	name varchar(100) not null,
	descr varchar(500) not null,
	votr_uuid varchar(50) not null
);
--ALTER TABLE options ADD PRIMARY KEY(votr_id,norder);

create table messages (
	uuid varchar(50) primary key,
	
	votr_uuid varchar(50) not null,
	user_uuid varchar(50),

	posted varchar(30) not null,
	message varchar(1024) not null
);



alter table users add foreign key (votr_uuid) references votrs(uuid);
--alter table users add foreign key (votr_id,voted_option_num_order) references options(votr_id,norder);
alter table options add foreign key (votr_uuid) references votrs(uuid);

alter table messages add foreign key (votr_uuid) references votrs(uuid);
alter table messages add foreign key (user_uuid) references users(uuid);





