
drop table adv_stain_protocol if exists;
drop table messages if exists;

create table stain_protocol (
	id integer identity primary key,
	name varchar(50) not null
);
create table adv_stain_protocol (
	host varchar(50) not null,
	id_prot integer
);

alter table adv_stain_protocol add constraint pk_adv_stain_protocol primary key (host, id_prot);
alter table adv_stain_protocol add foreign key (id_prot) references stain_protocol(id);
