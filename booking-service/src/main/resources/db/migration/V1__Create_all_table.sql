create table if not exists addresses(
    id BIGINT primary key,
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
    bed_type bed_type not null,
    room_type room_type not null,
    hotel_id bigint not null references hotels(id) on delete cascade,
    auto_approve boolean default false
);

create table if not exists bookings(
    id bigserial primary key,
    hotel_id bigint not null references hotels(id),
    room_id bigint not null references rooms(id),
    booking_status booking_status not null,
    start_date date not null,
    end_date date not null,
    total_price double precision not null,
    create_date timestamp default current_timestamp,
    update_date timestamp default current_timestamp,
    username varchar not null
);

create type bed_type as enum ('SINGLE', 'DOUBLE', 'QUEEN_SIZE', 'KING_SIZE', 'TWO_TIRE');
create type room_type as enum ('STANDARD', 'LUX', 'BUSINESS', 'PRESIDENT');
create type booking_status as enum ('PENDING', 'CONFIRMED', 'CANCELLED', 'EXPIRED');

















