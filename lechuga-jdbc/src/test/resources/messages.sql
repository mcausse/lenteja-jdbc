
drop table messages if exists;

create table messages (
	uuid varchar(50) primary key,
	direction varchar(5) not null,
	body varchar(5000),
	parent_uuid varchar(50)
);

alter table messages add foreign key (parent_uuid) references messages(uuid);
