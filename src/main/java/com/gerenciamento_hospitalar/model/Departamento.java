package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departamento")
@Data
@ToString(exclude = "medicos")

@EntityListeners(AuditingEntityListener.class)
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "localizacao")
    private String localizacao;

    @Column(name = "data_criacao")
    @CreatedDate
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "departamento", fetch = FetchType.LAZY)
    private List<Medico> medicos;

    public Departamento(Long id, String nome, String localizacao, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.dataCriacao = dataCriacao;
    }

    public Departamento() {

    }
}
