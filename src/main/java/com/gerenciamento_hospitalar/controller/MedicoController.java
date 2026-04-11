package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.MedicoControllerDocs;
import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.service.TurnoAtendimentoService;
import com.gerenciamento_hospitalar.service.MedicoService;
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

    @GetMapping("/me")
    @PreAuthorize("hasRole('MEDICO')")
    @Override
    public ResponseEntity<MedicoResponse> obterMedicoLogado() {
        return ResponseEntity.ok(service.obterMedicoLogado());
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


    @PostMapping("/importar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MedicoResponse>> importarDados(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(service.importar(file));
    }
}
