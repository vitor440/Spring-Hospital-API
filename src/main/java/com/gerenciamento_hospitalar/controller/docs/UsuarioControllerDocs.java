package com.gerenciamento_hospitalar.controller.docs;

import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.RoleDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Usuario", description = "endpoint para cadastro de usuários, obtenção de tokens jwt e obtenção de novo token com refresh token")
public interface UsuarioControllerDocs {


    @Operation(summary = "Obter token", description = "obtém um token através da autenticação com usuário e senha.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TokenDTO.class))
            ),
            @ApiResponse(description = "Usuário não encontrado", responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    }
    )
    ResponseEntity<?> signin(@RequestBody CadastroUsuarioDTO usuarioDTO);



    @Operation(summary = "Refresh token", description = "Obtém um novo token através do refresh token.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TokenDTO.class))
            ),
            @ApiResponse(description = "Usuário não encontrado", responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    }
    )
    ResponseEntity<?> refresh(@PathVariable("username") String username,
                              @RequestHeader("Authorization") String refreshToken);




    @Operation(summary = "Cadastrar usuários", description = "Cadastra novo usuário no banco de dados.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TokenDTO.class))
            ),
            @ApiResponse(description = "Usuário não encontrado", responseCode = "403",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "Erro de validação", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    }
    )
    ResponseEntity<CadastroUsuarioDTO> createUser(@RequestBody CadastroUsuarioDTO usuarioDTO);

    ResponseEntity<Void> adicionaRole(@PathVariable("userId") Long userId, @RequestBody RoleDTO roleDTO);

    ResponseEntity<Role> criaRole(@RequestBody Role role);
}
