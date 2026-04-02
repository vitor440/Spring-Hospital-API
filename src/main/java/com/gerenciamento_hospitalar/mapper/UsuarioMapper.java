package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(CadastroUsuarioDTO usuarioDTO);

    CadastroUsuarioDTO toDTO(Usuario usuario);
}
