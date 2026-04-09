alter table medico drop column user_id;
alter table paciente drop column user_id;

alter table medico add column user_id bigint references usuario(id);
alter table paciente add column user_id bigint references usuario(id);