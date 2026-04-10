package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.PacienteControllerDocs;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.w3c.dom.stylesheets.LinkStyle;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController implements PacienteControllerDocs {

    private final PacienteService service;

    @PostMapping
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
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
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> atualizarPaciente(@PathVariable("id") Long id, @RequestBody @Valid PacienteRequest request) {
        return ResponseEntity.ok(service.atualizarPaciente(id, request));
    }

    @GetMapping("/{id}")
    @Override
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> obterPacientePeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterPacientePeloId(id));
    }

    @GetMapping("/me")
    @Override
    @PreAuthorize("hasAnyRole('PACIENTE')")
    public ResponseEntity<PacienteResponse> obterPacienteLogado() {
        return ResponseEntity.ok(service.obterPacienteLogado());
    }



    @GetMapping
    @Override
    @PreAuthorize("hasRole('RECEPCIONISTA')")
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
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<Void> deletarPacientePeloId(@PathVariable("id") Long id) {
        service.deletarPaciente(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/consultas")
    @Override
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> historicoConsultas(Long id) {
        return ResponseEntity.ok(service.historicoConsultas(id));
    }

    @GetMapping("/{id}/consultasAgendadas")
    @Override
    @PreAuthorize("hasAnyRole('RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> listarConsultasAgendadas(Long id) {
        return ResponseEntity.ok(service.listarConsultasAgendadas(id));
    }

    @GetMapping("/me/consultas")
    @Override
    @PreAuthorize("hasAnyRole('PACIENTE')")
    public ResponseEntity<List<ConsultaResponse>> historicoConsultasPacienteLogado() {
        return ResponseEntity.ok(service.historicoConsultasPacienteLogado());
    }

    @GetMapping("/me/consultasAgendadas")
    @Override
    @PreAuthorize("hasAnyRole('PACIENTE')")
    public ResponseEntity<List<ConsultaResponse>> listarConsultasAgendadasPacienteLogado() {
        return ResponseEntity.ok(service.listarConsultasAgendadasDoPacienteLogado());
    }

    @PostMapping("/importar")
    @PreAuthorize("hasRole('RECEPCIONISTA')")
    public ResponseEntity<List<PacienteResponse>> importar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.importar(file));
    }
}
