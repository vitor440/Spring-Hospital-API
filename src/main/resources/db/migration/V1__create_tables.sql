create table departamento(
     id bigint generated always as identity primary key,
     nome varchar(50) not null,
     localizacao varchar(50) not null,
     data_criacao timestamp
);

create table medico(
   id bigint generated always as identity primary key,
   crm varchar(300) unique not null,
   nome varchar(150) not null,
   email varchar(200) not null,
   telefone varchar(100) not null,
   especialidade varchar(80) not null,
   departamento_id bigint not null references departamento(id),
   constraint chk_especialidade check(especialidade in ('CARDIOLOGISTA', 'NEUROLOGISTA', 'HEPATOLOGISTA', 'ENDOCRINOLOGISTA', 'DERMATOLOGISTA','OFTALMOLOGISTA', 'PSICOLOGO', 'PSIQUIATRA'))
);



create table paciente(
     id bigint generated always as identity primary key,
     cpf varchar(100) unique not null,
     nome varchar(100) not null,
     genero varchar(30) not null,
     endereco varchar(150) not null,
     email varchar(200),
     telefone varchar(100) not null,
     tipo_sanguineo varchar(30) not null,
     data_nascimento date not null
);

create table consulta(
     id bigint generated always as identity primary key,
     medico_id bigint not null references medico(id),
     paciente_id bigint not null references paciente(id),
     proposito varchar(500),
     data date not null,
     hora time not null,
     status varchar(30) not null,
     constraint chk_status check(status in ('AGENDADA','REALIZADA','CANCELADA','FALTANTE'))
);


create table prescricao(
   id bigint generated always as identity primary key,
   consulta_id bigint not null references consulta(id) not null,
   data_prescricao date not null,
   comentarios varchar(500)
);

create table medicamento(
    id bigint generated always as identity primary key,
    nome varchar(100) not null,
    marca varchar(80),
    tipo varchar(30) not null,
    descricao varchar(500) not null,
    estoque int not null
);

create table resultado_consulta(
   id bigint generated always as identity primary key,
   consulta_id bigint references consulta(id) unique not null,
   diagnostico varchar(300),
   tratamento varchar(300),
   data_retorno date
);

create table prescricao_medicamento(
       id bigint generated always as identity primary key,
       prescricao_id bigint references prescricao(id) not null,
       medicamento_id bigint references medicamento(id) not null,
       dosagem varchar(50) not null,
       frequencia varchar(100) not null,
       duracao varchar(50) not null
);