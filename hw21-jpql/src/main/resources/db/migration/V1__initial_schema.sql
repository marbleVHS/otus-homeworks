-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_seq start with 1 increment by 1;
create sequence address_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;

create table address
(
    id   bigint primary key,
    street varchar(255)
);

create table client
(
    id   bigint primary key,
    name varchar(50),
    address_id bigint,
    CONSTRAINT fk_address
        FOREIGN KEY(address_id)
            REFERENCES address(id)
);

create table phone
(
    id   bigint primary key,
    number varchar(255),
    client_id bigint,
    CONSTRAINT fk_client
        FOREIGN KEY(client_id)
            REFERENCES client(id)
);
