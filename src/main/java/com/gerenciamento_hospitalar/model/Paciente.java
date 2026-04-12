package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "paciente")
@ToString(exclude = "consultas")
@Data
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "genero")
    private String genero;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "tipo_sanguineo")
    private String tipoSanguineo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
    private List<Consulta> consultas;
}
