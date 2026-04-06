package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.model.Role;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository repository;

    public Role createRole(Role role) {
        return repository.save(role);
    }
}
