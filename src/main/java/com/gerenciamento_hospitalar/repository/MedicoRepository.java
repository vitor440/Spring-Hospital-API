package com.gerenciamento_hospitalar.repository;

import com.gerenciamento_hospitalar.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long>, JpaSpecificationExecutor<Medico> {

    Optional<Medico> findByCrm(String crm);

    boolean existsByDepartamento(Departamento departamento);

    Optional<Medico> findByUsuario(Usuario usuario);


}
