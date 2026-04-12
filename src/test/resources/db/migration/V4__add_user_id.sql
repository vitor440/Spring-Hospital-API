alter table medico add column user_id bigint references usuario(id);
alter table paciente add column user_id bigint references usuario(id);