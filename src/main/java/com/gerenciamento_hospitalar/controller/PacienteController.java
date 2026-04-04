package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.PacienteControllerDocs;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.service.PacienteService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController implements PacienteControllerDocs {

    private final PacienteService service;

    @PostMapping
    @Override
    public ResponseEntity<PacienteResponse> addPaciente(@RequestBody @Valid PacienteRequest request) {
        PacienteResponse response = service.addPaciente(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<PacienteResponse> atualizarPaciente(@PathVariable("id") Long id, @RequestBody @Valid PacienteRequest request) {
        return ResponseEntity.ok(service.atualizarPaciente(id, request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PacienteResponse> obterPacientePeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterPacientePeloId(id));
    }

    @GetMapping
    @Override
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
    @Override
    public ResponseEntity<Void> deletarPacientePeloId(@PathVariable("id") Long id) {
        service.deletarPaciente(id);

        return ResponseEntity.noContent().build();
    }
}
