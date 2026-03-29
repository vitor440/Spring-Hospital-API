alter table prescricao_medicamento add column nome_medicamento varchar(150);
alter table prescricao_medicamento add column tipo varchar(150);
alter table prescricao_medicamento add column descricao varchar(150);

alter table prescricao_medicamento drop column medicamento_id;