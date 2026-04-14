package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.mocks.TurnoMock;
import com.gerenciamento_hospitalar.model.DiaSemana;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TurnoControllerTest extends AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    private static TurnoAtendimentoRequest request;
    private static TurnoAtendimentoResponse response;
    private static TokenDTO tokenRecepcionista;
    private static RequestSpecification specification;



    @BeforeEach
    void setUp() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void signin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/auth/signin")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CadastroUsuarioDTO( "recepcionista", "recepcionista123", "recepcionista"))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenRecepcionista = objectMapper.readValue(content, TokenDTO.class);
        assertNotNull(tokenRecepcionista.acessToken());
        assertNotNull(tokenRecepcionista.refreshToken());

        assertEquals(tokenRecepcionista.username(), "recepcionista");
        assertTrue(tokenRecepcionista.authenticated());
    }

    @Test
    @Order(2)
    void addTurnoMedico() throws JsonProcessingException {
        request = TurnoMock.mockTurnoRequest(1);

        specification = new RequestSpecBuilder()
                .setBasePath("medicos/{id}/turnos-atendimento")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 1L)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, TurnoAtendimentoResponse.class);

        Assertions.assertThat(response.diaSemana()).isEqualTo(DiaSemana.TERCA);
        Assertions.assertThat(response.horaInicio()).isEqualTo(LocalTime.of(1, 1));
        Assertions.assertThat(response.horaFim()).isEqualTo(LocalTime.of(1, 1));
    }

    @Test
    @Order(3)
    void addTurnoComMedicoInexistente() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 99L)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    @Test
    @Order(4)
    void addTurnoComSobreposicaoDeHorarios() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 1L)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(409);
    }


    @Test
    @Order(5)
    void atualizarTurnoMedico() throws JsonProcessingException {
        request = TurnoMock.mockTurnoRequest(2);

        specification = new RequestSpecBuilder()
                .setBasePath("turnos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 1L)
                .body(request)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, TurnoAtendimentoResponse.class);

        Assertions.assertThat(response.diaSemana()).isEqualTo(DiaSemana.SEGUNDA);
        Assertions.assertThat(response.horaInicio()).isEqualTo(LocalTime.of(2, 2));
        Assertions.assertThat(response.horaFim()).isEqualTo(LocalTime.of(2, 2));
    }

    @Test
    @Order(6)
    void atualizarTurnoInexistente() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 99L)
                .body(request)
                .when()
                .put()
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    void atualizarTurnoMedicoComSobreposicaoDeHorarios() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 1L)
                .body(request)
                .when()
                .put()
                .then()
                .statusCode(409);
    }


    @Test
    @Order(8)
    void obterTurnosPeloIdDoMedico() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("medicos/{id}/turnos-atendimento")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 1L)
                .body(request)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<TurnoAtendimentoResponse> responseList = objectMapper.readValue(content, LinkedList.class);

        TurnoAtendimentoResponse turno0 = responseList.get(0);

        Assertions.assertThat(turno0.diaSemana()).isEqualTo(DiaSemana.SEGUNDA);
        Assertions.assertThat(turno0.horaInicio()).isEqualTo(LocalTime.of(2, 2));
        Assertions.assertThat(turno0.horaFim()).isEqualTo(LocalTime.of(2, 2));

        TurnoAtendimentoResponse turno1 = responseList.get(1);

        Assertions.assertThat(turno1.diaSemana()).isEqualTo(DiaSemana.SEGUNDA);
        Assertions.assertThat(turno1.horaInicio()).isEqualTo(LocalTime.of(2, 2));
        Assertions.assertThat(turno1.horaFim()).isEqualTo(LocalTime.of(2, 2));

    }

    @Test
    void deletarTurnoPeloIdMedico() {
    }

    @Test
    void obterTurnosDeMedicoLogado() {
    }
}