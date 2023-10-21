
drop table event_errors if exists;
drop table events_queue if exists;

create table events_queue (
	uuid varchar(50) primary key,
	created varchar(50) not null,
	status varchar(20) not null,
	status_changed varchar(50),

	type varchar(50) not null,
	payload varchar(5000)
);

create table event_errors (
	uuid varchar(50) primary key,
	event_uuid varchar(50) not null,
	consumer_ref varchar(100) not null,
	error_stack_trace varchar(5000)
);

