package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.ConsultaControllerDocs;
import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import com.gerenciamento_hospitalar.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController

@RequiredArgsConstructor
public class ConsultaController implements ConsultaControllerDocs {

    private final ConsultaService service;

    @PostMapping("/consultas")
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> agendarConsulta(@RequestBody @Valid ConsultaRequest request) {
        ConsultaResponse response = service.agendarConsulta(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/consultas/{id}")
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> atualizarConsulta(@PathVariable("id") Long id, @RequestBody @Valid ConsultaRequest request) {
        return ResponseEntity.ok(service.atualizarConsulta(id, request));
    }

    @GetMapping("/consultas/{id}")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> obterConsultaPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterConsultaPeloId(id));
    }

    @GetMapping("/consultas")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Page<ConsultaResponse>> listarConsultas(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {

        return ResponseEntity.ok(service.listarConsultas(pagina, tamanho, direction));
    }


    @DeleteMapping("/consultas/{id}")
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> deletarConsultaPeloId(@PathVariable("id") Long id) {
        service.deletarConsultaPeloId(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/consultas/{id}/status")
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<Void> AlterarStatusConsulta(@PathVariable("id") Long id, @RequestParam(value = "status") StatusConsulta status) {
        service.modificaStatusConsulta(id, status);
        return ResponseEntity.noContent().build();
    }

}
