package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService service;

    @PostMapping
    public ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest request) {
        PacienteResponse response = service.addPaciente(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizarPaciente(@PathVariable("id") Long id, @RequestBody @Valid PacienteRequest request) {
        return ResponseEntity.ok(service.atualizarPaciente(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> obterPacientePeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterPacientePeloId(id));
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponse>> listarPacientes(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "tipo-sanguineo", required = false) String tipoSanguineo,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(service.listarPacientes(nome, genero, tipoSanguineo, pagina, tamanho, direction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPacientePeloId(@PathVariable("id") Long id) {
        service.deletarPaciente(id);

        return ResponseEntity.noContent().build();
    }
}
