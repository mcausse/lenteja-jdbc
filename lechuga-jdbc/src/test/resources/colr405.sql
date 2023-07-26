


drop table colr405 if exists;

create table colr405 (
	guid varchar(50) not null,
	version smallint not null,
	value varchar(50) not null,
);

alter table colr405 add constraint pk_colr405id primary key (guid, version);

