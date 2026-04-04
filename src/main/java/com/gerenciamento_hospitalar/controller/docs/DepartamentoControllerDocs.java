package com.gerenciamento_hospitalar.controller.docs;

import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface DepartamentoControllerDocs {

    @Operation(summary = "adicionar departamento", description = "Adiciona novo departamento.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = DepartamentoResponse.class))
            ),
            @ApiResponse(description = "erro de validação", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "nome duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<DepartamentoResponse> addDepartamento(@RequestBody @Valid DepartamentoRequest request);

    @Operation(summary = "atualizar departamento", description = "atualiza departamento pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = DepartamentoResponse.class))
            ),
            @ApiResponse(description = "erro de validação", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "nome duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "departamento não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<DepartamentoResponse> atualizarDepartamento(@PathVariable("id") Long id,
                                                               @RequestBody @Valid DepartamentoRequest request);

    @Operation(summary = "obter departamento", description = "obtém departamento pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = DepartamentoResponse.class))
            ),
            @ApiResponse(description = "departamento não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<DepartamentoResponse> obterDepartamentoPeloId(@PathVariable("id") Long id);

    @Operation(summary = "listar departamento", description = "pesquisa departamento pelo nome e localização.")
    ResponseEntity<Page<DepartamentoResponse>> listarDepartamentos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "localizacao", required = false) String localizacao,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    );

    @Operation(summary = "deletar departamento", description = "deleta departamento pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "departamento não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "deleção não permitida: proibido excluir departamentos com médicos associados.",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<Void> deletarDepartamentoPeloId(@PathVariable("id") Long id);
}
