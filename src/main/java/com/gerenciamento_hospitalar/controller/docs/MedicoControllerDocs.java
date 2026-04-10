package com.gerenciamento_hospitalar.controller.docs;

import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.model.Especialidade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Médicos", description = "gerenciamento de médicos")
public interface MedicoControllerDocs {


    @Operation(summary = "Adicionar médico", description = "Adiciona novo médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = MedicoResponse.class))
            ),
            @ApiResponse(description = "departamento não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "unprocessable entity", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "crm duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<MedicoResponse> addMedico(@RequestBody @Valid MedicoRequest request);




    @Operation(summary = "atualizar médico", description = "atualiza dados do médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = MedicoResponse.class))
            ),
            @ApiResponse(description = "departamento/médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "unprocessable entity", responseCode = "422",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "crm duplicado", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<MedicoResponse> atualizarMedico(@PathVariable("id") Long id, @RequestBody @Valid MedicoRequest request);




    @Operation(summary = "obter médico", description = "obtém médico pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = MedicoResponse.class))
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<MedicoResponse> obterMedicoPeloId(@PathVariable("id") Long id);


    @GetMapping("/me")
    @PreAuthorize("hasRole('MEDICO')")
    ResponseEntity<MedicoResponse> obterMedicoLogado();

    @Operation(summary = "listar médicos", description = "pesquisa médicos pelo nome e especialidade.")
    ResponseEntity<Page<MedicoResponse>> listarMedicos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "especialidade", required = false) Especialidade especialidade,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    );




    @Operation(summary = "deletar médico", description = "deleta médico pelo id.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "deleção não permitida: médico possui consultas registradas no banco.", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<Void> deletarMedicoPeloId(@PathVariable("id") Long id);




    @Operation(summary = "obter consultas", description = "lista todas as consultas de um médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ConsultaResponse.class)))
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<List<ConsultaResponse>> obterConsultas(@PathVariable("id") Long id);

    @GetMapping("/me/consultas")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    ResponseEntity<List<ConsultaResponse>> obterConsultasDoMedicoLogado();

    @GetMapping("/me/consultasAgendadas")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    ResponseEntity<List<ConsultaResponse>> obterConsultasAgendadasDoMedicoLogado();

    public ResponseEntity<List<ConsultaResponse>> obterConsultasAgendadas(@PathVariable("id") Long id);

    public ResponseEntity<List<MedicoResponse>> importarDados(@RequestParam("file") MultipartFile file);
}
