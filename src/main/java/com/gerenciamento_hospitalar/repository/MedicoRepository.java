package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCrm(String crm);

    boolean existsByDepartamento(Departamento departamento);

    Optional<Medico> findByUsuario(Usuario usuario);
}
