
drop table outbox_table if exists;

create table outbox_table (
	uuid varchar(50) primary key,
	created varchar(50) not null,

	type varchar(50) not null,
	payload varchar(5000)
);


