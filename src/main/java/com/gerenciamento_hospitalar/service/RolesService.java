package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Role;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository repository;

    public Role createRole(Role role) {
        if(repository.findByRole(role.getRole()).isPresent()) {
            throw new RegistroDuplicadoException("Já existe uma role com este nome!");
        }

        return repository.save(role);
    }
}
