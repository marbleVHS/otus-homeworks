create table address
(
    id     bigserial not null primary key,
    street varchar(255)
);
create table client
(
    id         bigserial not null primary key,
    name       varchar(255),
    address_id bigint,
    constraint fk_address
        foreign key (address_id)
            references address (id)
);
create table phone
(
    id        bigserial not null primary key,
    number    varchar(255),
    client_id bigint,
    constraint fk_client
        foreign key (client_id)
            references client (id)
);