

drop sequence seq_person if exists;
create sequence seq_person start with 100;


drop table dog if exists;
drop table persons if exists;

create table dog (
	id_dog integer primary key generated by default as identity(start with 10),
	chip_num varchar(20) not null,
	name varchar(15) not null,
	age smallint not null
);
create table persons (
	guid varchar(32) primary key,
	first_name varchar(15) not null,
	sur_name varchar(15) not null,
	age smallint not null,
	genre varchar(10) not null
);

insert into dog (chip_num,name,age) values ('aaa', 'faria', 12);
insert into dog (chip_num,name,age) values ('bbb', 'din', 13);
insert into dog (chip_num,name,age) values ('bbb', 'chucho', 14);

insert into persons (guid,first_name,sur_name,age,genre) values ('12345','m','h',41,'MALE');
insert into persons (guid,first_name,sur_name,age,genre) values ('67890','a','v',45,'FEMALE');
