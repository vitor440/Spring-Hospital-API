package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.DepartamentoControllerDocs;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.service.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/departamentos")
@RequiredArgsConstructor

public class DepartamentoController implements DepartamentoControllerDocs {

    private final DepartamentoService service;

    @PostMapping
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartamentoResponse> addDepartamento(@RequestBody @Valid DepartamentoRequest request) {
        DepartamentoResponse response = service.addDepartamento(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartamentoResponse> atualizarDepartamento(@PathVariable("id") Long id,
                                                                      @RequestBody @Valid DepartamentoRequest request) {
        return ResponseEntity.ok(service.atualizarDepartamento(id, request));
    }

    @GetMapping("/{id}")
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<DepartamentoResponse> obterDepartamentoPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterDepartamentoPeloId(id));
    }

    @GetMapping
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Page<DepartamentoResponse>> listarDepartamentos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "localizacao", required = false) String localizacao,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(service.listarDepartamentos(nome, localizacao, pagina, tamanho, direction));
    }

    @DeleteMapping("/{id}")
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarDepartamentoPeloId(@PathVariable("id") Long id) {
        service.deletarDepartamentoPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/importar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DepartamentoResponse>> importarDados(@RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(service.importar(file));
    }
}
