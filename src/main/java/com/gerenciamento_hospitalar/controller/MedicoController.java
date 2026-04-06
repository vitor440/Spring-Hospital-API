package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.MedicoControllerDocs;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.service.TurnoAtendimentoService;
import com.gerenciamento_hospitalar.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController implements MedicoControllerDocs {

    private final MedicoService service;
    private final TurnoAtendimentoService turnoAtendimentoService;

    @PostMapping
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> addMedico(@RequestBody @Valid MedicoRequest request) {
        MedicoResponse response = service.addMedico(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicoResponse> atualizarMedico(@PathVariable("id") Long id, @RequestBody @Valid MedicoRequest request) {

        return ResponseEntity.ok(service.atualizarMedico(id, request));
    }

    @GetMapping("/{id}")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<MedicoResponse> obterMedicoPeloId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.obterMedicoPeloId(id));
    }

    @GetMapping
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Page<MedicoResponse>> listarMedicos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "especialidade", required = false) Especialidade especialidade,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {

        return ResponseEntity.ok(service.ListarMedicos(nome, especialidade, pagina, tamanho, direction));
    }

    @DeleteMapping("/{id}")
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarMedicoPeloId(@PathVariable("id") Long id) {

        service.deletarMedicoPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/turnos-atendimento")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> addTurnoMedico(@PathVariable("id") Long id,
                                                                   @RequestBody TurnoAtendimentoRequest request) {
        TurnoAtendimentoResponse response = turnoAtendimentoService.addDisponibilidadeMedico(id, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}/turnos-atendimento/{turnoId}")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> atualizarTurnoMedico(@PathVariable("id") Long id,
                                                                         @PathVariable("turnoId") Long turnoId,
                                                                         @RequestBody TurnoAtendimentoRequest request) {
        return ResponseEntity.ok(turnoAtendimentoService.atualizarDisponibilidadeMedico(id, turnoId, request));
    }

    @GetMapping("/{id}/turnos-atendimento")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<TurnoAtendimentoResponse>> listarTurnos(
            @PathVariable("id") Long id,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho
    ) {

        return ResponseEntity.ok(turnoAtendimentoService.listarDisponibilidades(id, pagina, tamanho));
    }

    @DeleteMapping("/{id}/turnos-atendimento/{turnoId}")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> deletarTurnoPeloIdMedico(@PathVariable("id") Long id,
                                                                             @PathVariable("turnoId") Long turnoId) {
        turnoAtendimentoService.deletarPeloIdMedico(id, turnoId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/consultas")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'MEDICO')")
    public ResponseEntity<List<ConsultaResponse>> obterConsultas(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterConsultas(id));
    }

    @PostMapping("/importar")
    public ResponseEntity<List<MedicoResponse>> importarDados(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(service.importar(file));
    }
}
