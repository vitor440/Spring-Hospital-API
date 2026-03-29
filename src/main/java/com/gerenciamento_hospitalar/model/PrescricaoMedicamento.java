package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "prescricao_medicamento")
@ToString(exclude = "medicamento")
@Data
public class PrescricaoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescricao_id")
    private Prescricao prescricao;

    @Column(name = "nome_medicamento")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "dosagem")
    private String dosagem;

    @Column(name = "frequencia")
    private String frequencia;

    @Column(name = "duracao")
    private String duracao;
}
