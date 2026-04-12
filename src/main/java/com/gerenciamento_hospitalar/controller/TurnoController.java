package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.service.TurnoAtendimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoAtendimentoService service;

    @PostMapping("medicos/{id}/turnos-atendimento")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> addTurnoMedico(@PathVariable("id") Long id,
                                                                   @RequestBody TurnoAtendimentoRequest request) {

        TurnoAtendimentoResponse response = service.addDisponibilidadeMedico(id, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("turnos/{id}")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> atualizarTurnoMedico(@PathVariable("id") Long id,
                                                                         @RequestBody TurnoAtendimentoRequest request) {
        return ResponseEntity.ok(service.atualizarDisponibilidadeMedico(id, request));
    }

    @GetMapping("medicos/{id}/turnos-atendimento")
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    public ResponseEntity<List<TurnoAtendimentoResponse>> obterTurnosDeMedicoPeloId(
            @PathVariable("id") Long id,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho
    ) {

        return ResponseEntity.ok(service.obterTurnosDeMedicoPeloId(id, pagina, tamanho));
    }

    @DeleteMapping("/turnos/{id}")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<TurnoAtendimentoResponse> deletarTurnoPeloIdMedico(@PathVariable("id") Long id) {
        service.deletarTurnoPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicos/me/turnos")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<List<TurnoAtendimentoResponse>> obterTurnosDeMedicoLogado() {
        return ResponseEntity.ok(service.obterTurnosDeMedicoLogado());
    }
}
