create table if not exists photos (
    id BIGSERIAL primary key,
    file_name varchar not null,
    file_size BIGINT,
    mime_type varchar,
    upload_date timestamp default CURRENT_TIMESTAMP,
    is_main boolean,
    hotel_id BIGINT NOT NULL REFERENCES hotels(id) ON DELETE CASCADE
)