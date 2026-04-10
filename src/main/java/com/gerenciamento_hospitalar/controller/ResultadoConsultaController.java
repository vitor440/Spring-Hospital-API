package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.service.ResultadoConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ResultadoConsultaController {

    private final ResultadoConsultaService service;

    @PostMapping("consultas/{id}/resultado")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ResultadoConsultaResponse> gerarResultadoConsulta(@PathVariable("id") Long consultaId, @RequestBody ResultadoConsultaRequest request) {
        ResultadoConsultaResponse response = service.gerarResultadoDaConsulta(request, consultaId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/prontuarios/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ResultadoConsultaResponse> atualizarResultadoConsulta(@PathVariable("id") Long id, @RequestBody ResultadoConsultaRequest request) {

        return ResponseEntity.ok(service.atualizarResultadoDaConsulta2(id, request));
    }

    @GetMapping("/prontuarios/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'RECEPCIONISTA')")
    public ResponseEntity<ResultadoConsultaResponse> obterResultadoConsulta(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.obterResultadoPeloIdDaConsulta2(id));
    }

    @DeleteMapping("/prontuarios/{id}")
    @PreAuthorize("hasRole('MEDICO')")
    public ResponseEntity<ResultadoConsultaResponse> deletarResultadoConsulta(@PathVariable("id") Long id) {

        service.deletarResultadoDaConsulta2(id);
        return ResponseEntity.noContent().build();
    }
}
