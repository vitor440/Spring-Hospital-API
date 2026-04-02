create table usuario(
    id bigint generated always as identity primary key,
    nome_completo varchar(250),
    username varchar(250) not null,
    senha varchar(300) not null,
    account_non_expired BOOLEAN not null,
    account_non_locked BOOLEAN not null,
    credentials_non_expired BOOLEAN not null,
    enabled BOOLEAN not null
);

create table roles(
    id bigint generated always as identity primary key,
    role varchar(50) not null
);

create table user_roles(
   usuario_id bigint not null references usuario(id),
   role_id bigint not null references roles(id)
);