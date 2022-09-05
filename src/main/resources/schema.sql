create table if not exists users ( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, username varchar(100), email varchar(100), password varchar(100) );

create table if not exists accounts ( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, balance decimal, description varchar, user_id int );

create table if not exists operations ( id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, description varchar(200), value decimal, type varchar, created timestamp, account_id int, user_id int );