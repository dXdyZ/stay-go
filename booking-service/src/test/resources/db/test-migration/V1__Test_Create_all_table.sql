create table if not exists addresses(
    id BIGSERIAL primary key,
    country varchar,
    city varchar,
    street varchar,
    house_number varchar,
    postal_code integer
);

create table if not exists hotels(
    id BIGSERIAL primary key,
    name varchar,
    stars int,
    grade numeric(10, 2),
    description text,
    address_id BIGINT unique references addresses(id) on delete cascade
);

create table if not exists rooms(
    id BIGSERIAL primary key,
    number INTEGER not null,
    capacity INTEGER not null,
    price_per_day DOUBLE PRECISION not null,
    description TEXT,
    is_active boolean default true,
    bed_type varchar not null,
    room_type varchar not null,
    hotel_id bigint not null references hotels(id) on delete cascade,
    auto_approve boolean default false
);

create table if not exists bookings(
    id bigserial primary key,
    hotel_id bigint not null references hotels(id),
    room_id bigint not null references rooms(id),
    booking_status varchar not null,
    start_date date not null,
    end_date date not null,
    total_price double precision not null,
    create_date timestamp default current_timestamp,
    update_date timestamp default current_timestamp,
    username varchar not null
);