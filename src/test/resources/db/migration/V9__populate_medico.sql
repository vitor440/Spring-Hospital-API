alter table medico drop constraint chk_especialidade;

insert into medico (crm, nome, email, telefone, especialidade, departamento_id, user_id)
VALUES ('CRM-AM 2010', 'dr robson amorim', 'robson.amorim@email.com', '8244-0227', 'NEUROLOGISTA', 2, 3),
       ('CRM-RJ 9032', 'dr carlos cavalcante', 'carlos.cavalcante@email.com', '8820-3234', 'CARDIOLOGISTA', 1, 4),
       ('CRM-SP 8932', 'dr marcos araújo', 'marcos@email.com', '9812-5943', 'ENDOCRINOLOGISTA', 3, 5);


insert  into turno_atendimento_medico (dia_semana, hora_inicio, hora_fim, medico_id)
values ('SEGUNDA', '12:00', '16:00', 1),
('TERCA', '12:00', '16:00', 1),
('QUARTA', '12:00', '16:00', 1),
('QUINTA', '12:00', '16:00', 1),
('SEXTA', '12:00', '16:00', 1),
('SEGUNDA', '15:00', '18:00', 2),
('QUARTA', '15:00', '18:00', 2),
('SEXTA', '15:00', '18:00', 2),
('TERCA', '07:00', '13:00', 3),
('QUINTA', '07:00', '13:00', 3),
('SABADO', '07:00', '13:00', 3);