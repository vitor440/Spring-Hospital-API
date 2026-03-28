package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.PrescricaoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.service.ResultadoConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/resultadoConsulta")
@RequiredArgsConstructor
public class ResultadoConsultaController {

    private final ResultadoConsultaService service;

//    @PostMapping
//    public ResponseEntity<ResultadoConsultaResponse> create(@RequestBody ResultadoConsultaRequest request) {
//        ResultadoConsultaResponse response = service.create(request);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(response.id())
//                .toUri();
//
//        return ResponseEntity.created(location).body(response);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ResultadoConsultaResponse> update(@PathVariable("id") Long id, @RequestBody ResultadoConsultaRequest request) {
//        return ResponseEntity.ok(service.(id, request));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoConsultaResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPrescricaoPeloId(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
