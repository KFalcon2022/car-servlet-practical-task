create table "user" (
    id 				                bigserial 		primary key,
    username   		                varchar(100)    not null unique,
    first_name 		                varchar(100)    not null,
    second_name		                varchar(100)    not null,
    password   		                varchar(100)    not null
);
