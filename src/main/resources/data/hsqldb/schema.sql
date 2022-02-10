
--Car Table
drop table if exists car;
create table car (
	id bigint NOT NULL AUTO_INCREMENT,
	type varchar(20) NOT NULL,
	primary key (id));



--Reservation Table Which References A Car With its Foreign Key
drop table if exists reservation;
create table reservation (
	id bigint NOT NULL AUTO_INCREMENT,
	carid bigint NOT NULL,
	starttime datetime NOT NULL,
	endtime datetime NOT NULL,
	primary key (id),
	foreign key (carid) references car(id));