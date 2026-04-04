package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.controller.docs.ConsultaControllerDocs;
import com.gerenciamento_hospitalar.dto.ErroResposta;
import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import com.gerenciamento_hospitalar.service.ConsultaService;
import com.gerenciamento_hospitalar.service.ResultadoConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class ConsultaController implements ConsultaControllerDocs {

    private final ConsultaService service;
    private final ResultadoConsultaService resultadoConsultaService;

    @PostMapping
    @Override
    public ResponseEntity<ConsultaResponse> agendarConsulta(@RequestBody @Valid ConsultaRequest request) {
        ConsultaResponse response = service.agendarConsulta(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ConsultaResponse> atualizarConsulta(@PathVariable("id") Long id, @RequestBody @Valid ConsultaRequest request) {
        return ResponseEntity.ok(service.atualizarConsulta(id, request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ConsultaResponse> obterConsultaPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterConsultaPeloId(id));
    }

    @GetMapping
    @Override
    public ResponseEntity<Page<ConsultaResponse>> listarConsultas(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "6") int tamanho,
            @RequestParam(value = "direction", defaultValue = "desc") String direction
    ) {

        return ResponseEntity.ok(service.listarConsultas(pagina, tamanho, direction));
    }


    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ConsultaResponse> deletarConsultaPeloId(@PathVariable("id") Long id) {
        service.deletarConsultaPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resultado")
    @Override
    public ResponseEntity<ResultadoConsultaResponse> create(@PathVariable("id") Long consultaId, @RequestBody ResultadoConsultaRequest request) {
        ResultadoConsultaResponse response = resultadoConsultaService.create(request, consultaId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}/resultado")
    @Override
    public ResponseEntity<ResultadoConsultaResponse> update(@PathVariable("id") Long consultaId, @RequestBody ResultadoConsultaRequest request) {

        return ResponseEntity.ok(resultadoConsultaService.update(consultaId, request));
    }

    @GetMapping("/{id}/resultado")
    @Override
    public ResponseEntity<ResultadoConsultaResponse> obterResultado(@PathVariable("id") Long consultaId) {

        return ResponseEntity.ok(resultadoConsultaService.getByConsultaId(consultaId));
    }

    @DeleteMapping("/{id}/resultado")
    @Override
    public ResponseEntity<ResultadoConsultaResponse> delete(@PathVariable("id") Long consultaId) {

        resultadoConsultaService.delete(consultaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{status}")
    @Override
    public ResponseEntity<Void> modificaStatusConsulta(@PathVariable("id") Long id,
                                                       @PathVariable("status") StatusConsulta status) {
        service.modificaStatusConsulta(id, status);
        return ResponseEntity.noContent().build();
    }
}
