package com.gerenciamento_hospitalar.controller.docs;

import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Consultas", description = "gerenciamento de consultas.")
public interface ConsultaControllerDocs {


    @Operation(summary = "agendar consulta", description = "agenda uma nova consulta.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = ConsultaResponse.class))
            ),
            @ApiResponse(description = "médico/paciente não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "consulta conflitante com outra ou médico indisponível", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )

    })
    ResponseEntity<ConsultaResponse> agendarConsulta(@RequestBody @Valid ConsultaRequest request);




    @Operation(summary = "atualizar consulta", description = "atualiza uma consulta existente.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ConsultaResponse.class))
            ),
            @ApiResponse(description = "consulta/médico/paciente não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "consulta conflitante com outra ou médico indisponível", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )

    })
    ResponseEntity<ConsultaResponse> atualizarConsulta(@PathVariable("id") Long id, @RequestBody @Valid ConsultaRequest request);




    @Operation(summary = "obter consulta pelo id", description = "obtém uma consulta pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ConsultaResponse.class))
            ),
            @ApiResponse(description = "consulta não encontrada", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<ConsultaResponse> obterConsultaPeloId(@PathVariable("id") Long id);




    @Operation(summary = "listar consultas", description = "lista todas as consultas registradas no banco.")
    ResponseEntity<Page<ConsultaResponse>> listarConsultas(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    );




    @Operation(summary = "deletar consulta pelo id", description = "deleta uma consulta pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "consulta não encontrada", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<ConsultaResponse> deletarConsultaPeloId(@PathVariable("id") Long id);





    @Operation(summary = "alterar status da consulta", description = "altera o status de uma consulta para REALIZADA.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "consulta não encontrada", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<Void> AlterarStatusConsultaParaRealizada(@PathVariable("id") Long id);




    @Operation(summary = "alterar status da consulta", description = "altera o status de uma consulta para CANCELADA.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "consulta não encontrada", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<Void> AlterarStatusConsultaParaCancelada(@PathVariable("id") Long id);




    @Operation(summary = "alterar status da consulta", description = "altera o status de uma consulta para FALTANTE.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "consulta não encontrada", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<Void> AlterarStatusConsultaParaFaltante(@PathVariable("id") Long id);

}
