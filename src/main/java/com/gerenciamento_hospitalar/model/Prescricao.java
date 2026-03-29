package com.gerenciamento_hospitalar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "prescricao")
@Data
@ToString(exclude = {"medicamentos", "consulta"})
@EntityListeners(AuditingEntityListener.class)
public class Prescricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medico_id")
    private Long medicoId;

    @Column(name = "paciente_id")
    private Long pacienteId;

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @Column(name = "data_prescricao")
    @CreatedDate
    private LocalDate dataPrescricao;

    @Column(name = "comentarios")
    private String comentarios;

    @OneToMany(mappedBy = "prescricao", cascade = CascadeType.ALL)
    private List<PrescricaoMedicamento> medicamentos;

}
