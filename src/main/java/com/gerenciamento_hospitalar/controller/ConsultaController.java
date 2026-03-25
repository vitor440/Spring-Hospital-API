package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    @PostMapping
    public ResponseEntity<ConsultaResponse> agendarConsulta(@RequestBody @Valid ConsultaRequest request) {
        ConsultaResponse response = service.agendarConsulta(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponse> atualizarConsulta(@PathVariable("id") Long id, @RequestBody @Valid ConsultaRequest request) {

        return ResponseEntity.ok(service.atualizarConsulta(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> obterConsultaPeloId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.obterConsultaPeloId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ConsultaResponse>> obterConsultaPeloId(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {

        return ResponseEntity.ok(service.listarConsultas(pagina, tamanho, direction));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ConsultaResponse> deletarConsultaPeloId(@PathVariable("id") Long id) {

        service.deletarConsultaPeloId(id);

        return ResponseEntity.noContent().build();
    }
}
