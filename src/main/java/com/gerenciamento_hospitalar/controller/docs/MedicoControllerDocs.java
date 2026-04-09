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




    @Operation(summary = "adicionar turno de atendimento", description = "adiciona turno de atendimento para um médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "201",
                    content = @Content(schema = @Schema(implementation = TurnoAtendimentoResponse.class))
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "Conflito de turnos", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<TurnoAtendimentoResponse> addTurnoMedico(@PathVariable("id") Long id,
                                                            @RequestBody TurnoAtendimentoRequest request);



    @Operation(summary = "atualizar turno de atendimento", description = "atualiza turno de atendimento de um médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TurnoAtendimentoResponse.class))
            ),
            @ApiResponse(description = "médico/turno não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "Conflito de turnos", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),

    })
    ResponseEntity<TurnoAtendimentoResponse> atualizarTurnoMedico(@PathVariable("id") Long id,
                                                                  @PathVariable("turnoId") Long turnoId,
                                                                  @RequestBody TurnoAtendimentoRequest request);




    @Operation(summary = "listar turnos de atendimentos", description = "lista todas os turnos de atendimentos de um médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "200",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TurnoAtendimentoResponse.class)))
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            )
    })
    ResponseEntity<List<TurnoAtendimentoResponse>> listarTurnos(
            @PathVariable("id") Long id,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho
    );




    @Operation(summary = "deletar turno de atendimento", description = "deleta turno de atendimento de um médico.")
    @ApiResponses({
            @ApiResponse(description = "Sucesso", responseCode = "204",
                    content = @Content
            ),
            @ApiResponse(description = "médico não encontrado", responseCode = "404",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),
            @ApiResponse(description = "não é permitida a deleção de turnos com consultas agendadas.", responseCode = "409",
                    content = @Content(schema = @Schema(implementation = ErroResposta.class))
            ),

    })
    ResponseEntity<TurnoAtendimentoResponse> deletarTurnoPeloIdMedico(@PathVariable("id") Long id,
                                                                      @PathVariable("turnoId") Long turnoId);



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

    public ResponseEntity<List<ConsultaResponse>> obterConsultasAgendadas(@PathVariable("id") Long id);

    public ResponseEntity<List<MedicoResponse>> importarDados(@RequestParam("file") MultipartFile file);
}
