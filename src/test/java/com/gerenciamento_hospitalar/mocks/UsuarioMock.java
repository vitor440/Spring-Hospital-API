package com.gerenciamento_hospitalar.mocks;

import com.gerenciamento_hospitalar.model.Usuario;

public class UsuarioMock {

    public static Usuario mockUsuario(int i) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setSenha("admin123");
        usuario.setNomeCompleto("admin");
        usuario.setAccountNonLocked(true);
        usuario.setAccountNonExpired(true);
        usuario.setCredentialsNonExpired(true);
        usuario.setEnabled(true);

        return usuario;
    }
}
