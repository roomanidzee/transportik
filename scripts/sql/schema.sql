CREATE TABLE users(
    id SERIAL PRIMARY KEY ,
    username VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE profile(
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    surname VARCHAR(100),
    name VARCHAR(100),
    patronymic VARCHAR(100),
    phone VARCHAR(25),
    CONSTRAINT profile_user_fk FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE EXTENSION postgis;

CREATE TABLE trip(
    id SERIAL PRIMARY KEY,
    source POINT,
    target POINT
);

CREATE TABLE transport_info(
    id SERIAL PRIMARY KEY,
    name TEXT,
    length BIGINT,
    tonnage BIGINT,
    created_time TIMESTAMP,
    type VARCHAR(50)
);

CREATE TABLE trip_to_profile(
    id SERIAL PRIMARY KEY,
    trip_id INTEGER,
    profile_id INTEGER,
    CONSTRAINT profile_fk FOREIGN KEY(profile_id) REFERENCES profile(id),
    CONSTRAINT trip_fk FOREIGN KEY(trip_id) REFERENCES trip(id)
);

CREATE TABLE transport_position(
    id SERIAL PRIMARY KEY,
    transport_id INTEGER,
    coordinate POINT,
    record_date TIMESTAMP,
    CONSTRAINT transport_pos_fk FOREIGN KEY(transport_id) REFERENCES transport_info(id)
);

CREATE TABLE trip_info(
    id SERIAL PRIMARY KEY,
    trip_id INTEGER,
    transport_id INTEGER,
    cost BIGINT,
    CONSTRAINT trip_info_fk FOREIGN KEY(trip_id) REFERENCES trip(id),
    CONSTRAINT transport_info_fk FOREIGN KEY(transport_id) REFERENCES transport_info(id)
);