package com.gerenciamento_hospitalar.repository.specs;

import com.gerenciamento_hospitalar.model.Paciente;
import org.springframework.data.jpa.domain.Specification;

public class PacienteSpecs {

    public static Specification<Paciente> likeNome(String nome) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("nome")), "%" + nome.toUpperCase() + "%");
    }

    public static Specification<Paciente> likeTipoSanguineo(String tipoSanguineo) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("tipoSanguineo")),  tipoSanguineo.toUpperCase() );
    }

    public static Specification<Paciente> likeGenero(String genero) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("genero")), genero.toUpperCase());
    }
}
