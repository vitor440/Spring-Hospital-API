package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UsuarioRepository repository;

    public Usuario getUsuarioLogado() {
        var context = SecurityContextHolder.getContext();

        if(context == null || context.getAuthentication() == null) {
            throw new RuntimeException("usuário não autenticado");
        }

        var principal = context.getAuthentication().getPrincipal();
        if (principal instanceof Usuario usuario) {
            return usuario;
        }

        throw new RuntimeException("Usuário não autenticado");
    }

    public void validaUsuarioMedico(Medico medico) {

        Usuario usuario = getUsuarioLogado();

        boolean temRoleMedico = usuario.getRoles().contains("MEDICO");

        if(temRoleMedico && !usuario.getId().equals(medico.getUsuario().getId())) {
            throw new AcessoNegadoException("acesso negado.");
        }
    }

    public void validaUsuarioPaciente(Paciente paciente) {

        Usuario usuario = getUsuarioLogado();

        boolean temRoleMedico = usuario.getRoles().contains("PACIENTE");

        if(temRoleMedico && !usuario.getId().equals(paciente.getUsuario().getId())) {
            throw new AcessoNegadoException("acesso negado.");
        }
    }


}
