create table car (
    id 				                bigserial 		primary key,
    number  		                varchar(20)	    not null,
    year    		                int             not null,
    color			                varchar(20)	    not null,
    actual_technical_inspection		boolean			not null
);
