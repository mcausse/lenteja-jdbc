
drop table events_queue if exists;

create table events_queue (
	uuid varchar(50) primary key,
	created varchar(50) not null,
	status varchar(20) not null,
	error_message varchar(5000),
	status_changed varchar(50),
	type varchar(50) not null,
	payload varchar(5000)
);
