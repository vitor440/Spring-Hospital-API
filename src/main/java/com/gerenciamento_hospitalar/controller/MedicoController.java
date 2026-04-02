package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.DisponibilidadeMedicoRequest;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.DisponibilidadeMedicoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.service.DisponibilidadeMedicoService;
import com.gerenciamento_hospitalar.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService service;
    private final DisponibilidadeMedicoService disponibilidadeMedicoService;

    @PostMapping
    public ResponseEntity<MedicoResponse> addMedico(@RequestBody @Valid MedicoRequest request) {
        MedicoResponse response = service.addMedico(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> atualizarMedico(@PathVariable("id") Long id, @RequestBody @Valid MedicoRequest request) {

        return ResponseEntity.ok(service.atualizarMedico(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> obterMedicoPeloId(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.obterMedicoPeloId(id));
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponse>> listarMedicos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "especialidade", required = false)Especialidade especialidade,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
            ) {

        return ResponseEntity.ok(service.ListarMedicos(nome, especialidade, pagina, tamanho, direction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedicoPeloId(@PathVariable("id") Long id) {

        service.deletarMedicoPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disponibilidades")
    public ResponseEntity<DisponibilidadeMedicoResponse> addDisponibilidade(@PathVariable("id") Long id,
                                                                            @RequestBody DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedicoResponse response = disponibilidadeMedicoService.addDisponibilidadeMedico(id, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}/disponibilidades")
    public ResponseEntity<List<DisponibilidadeMedicoResponse>> listarDisponibilidades(
            @PathVariable("id") Long id,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho
    ) {

        return ResponseEntity.ok(disponibilidadeMedicoService.listarDisponibilidades(id, pagina, tamanho));
    }

    @PutMapping("/{id}/disponibilidades/{disponibilidadeId}")
    public ResponseEntity<DisponibilidadeMedicoResponse> obterDisponibilidadePeloId(@PathVariable("id") Long id,
                                                                                    @PathVariable("disponibilidadeId") Long disponibilidadeId,
                                                                                    @RequestBody DisponibilidadeMedicoRequest request) {
        return ResponseEntity.ok(disponibilidadeMedicoService.atualizarDisponibilidadeMedico(id, disponibilidadeId, request));
    }

    @DeleteMapping("/{id}/disponibilidades/{disponibilidadeId}")
    public ResponseEntity<DisponibilidadeMedicoResponse> obterDisponibilidadePeloId(@PathVariable("id") Long id,
                                                                                    @PathVariable("disponibilidadeId") Long disponibilidadeId) {
        disponibilidadeMedicoService.deletarPeloIdMedico(id, disponibilidadeId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<ConsultaResponse>> obterConsultas(@PathVariable("id") Long id) {

        return ResponseEntity.ok(service.obterConsultas(id));
    }
}
