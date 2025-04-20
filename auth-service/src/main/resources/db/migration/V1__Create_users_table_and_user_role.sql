create table if not exists users (
    id BIGSERIAL primary key,
    username varchar(100) unique not null ,
    password varchar not null ,
    create_at timestamp
);

create table if not exists roles (
    id BIGSERIAL primary key,
    role_name role_name not null
);

create type role_name as enum ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MANAGER', 'ROLE_OWNER');

create table if not exists user_roles (
    user_id BIGSERIAL not null,
    role_id BIGSERIAL not null,
    primary key (user_id, role_id),
    constraint fk_user foreign key (user_id) references users(id) on delete cascade,
    constraint fk_role foreign key (role_id) references roles(id) on delete cascade
);


