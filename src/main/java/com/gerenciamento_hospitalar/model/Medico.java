package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medico")
@Data
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crm")
    private String crm;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "especialidade")
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
}
