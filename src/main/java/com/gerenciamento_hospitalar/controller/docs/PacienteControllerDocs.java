package com.gerenciamento_hospitalar.controller.docs;

import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface PacienteControllerDocs {


    @Operation(summary = "Adicionar paciente", description = "Adicionar novo paciente.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))
            ),
            @ApiResponse(description = "unprocessable entity", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "cpf duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest request);

    @Operation(summary = "atualizar paciente", description = "atualizar dados do paciente.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))
            ),
            @ApiResponse(description = "unprocessable entity", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "cpf duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<PacienteResponse> atualizarPaciente(@PathVariable("id") Long id, @RequestBody @Valid PacienteRequest request);

    @Operation(summary = "obter paciente", description = "obtém paciente pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PacienteResponse.class))
            ),
            @ApiResponse(description = "Paciente não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
    })
    ResponseEntity<PacienteResponse> obterPacientePeloId(@PathVariable("id") Long id);

    @Operation(summary = "listar pacientes", description = "pesquisa paciente pelo nome, gênero e tipo sanguinero.")
    ResponseEntity<Page<PacienteResponse>> listarPacientes(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "tipo-sanguineo", required = false) String tipoSanguineo,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    );

    @Operation(summary = "deletar paciente", description = "deleta um paciente pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "deleção não permitida: proibido excluir pacientes com consultas registradas no sistema", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "Paciente não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
    })
    ResponseEntity<Void> deletarPacientePeloId(@PathVariable("id") Long id);
}
