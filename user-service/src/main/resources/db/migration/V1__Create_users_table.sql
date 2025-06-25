create table if not exists users (
    id bigserial primary key,
    email varchar(100) unique not null,
    phone_number varchar(20),
    username varchar(100) unique not null,
    hotel_id BIGINT
);

