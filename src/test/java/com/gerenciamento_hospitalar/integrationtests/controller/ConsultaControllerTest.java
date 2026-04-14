package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.integrationtests.pageContent.ConsultaPageContent;
import com.gerenciamento_hospitalar.mocks.ConsultaMock;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsultaControllerTest extends AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    private static ConsultaRequest request;
    private static ConsultaResponse response;
    private static RequestSpecification specification;
    private static TokenDTO tokenRecepcionista;

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
                .body(new CadastroUsuarioDTO("recepcionista", "recepcionista123", "recepcionista"))
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
    void agendarConsultaComConsultasConflitantes() throws JsonProcessingException {
        request = mockConsultaRequestConsultaConflitante();

        specification = new RequestSpecBuilder()
                .setBasePath("/consultas")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(409);
    }

    @Test
    @Order(3)
    void agendarConsultaComMedicoIndisponivel() throws JsonProcessingException {
        request = mockConsultaRequestMedicoIndisponivel();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(409);
    }




    @Test
    @Order(4)
    void agendarConsulta() throws JsonProcessingException {
        request = mockConsultaRequest();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, ConsultaResponse.class);

        assertThat(response.proposito()).isEqualTo("teste");
        assertThat(response.data()).isEqualTo(LocalDate.of(2030, 1, 1));
        assertThat(response.hora()).isEqualTo(LocalTime.of(16, 0));
        assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
    }



    @Test
    @Order(5)
    void atualizarConsulta() throws JsonProcessingException {
        request = mockConsultaRequestAtualizado();

        specification = new RequestSpecBuilder()
                .setBasePath("/consultas/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", response.id())
                .body(request)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, ConsultaResponse.class);

        assertThat(response.proposito()).isEqualTo("teste2");
        assertThat(response.data()).isEqualTo(LocalDate.of(2030, 1, 1));
        assertThat(response.hora()).isEqualTo(LocalTime.of(16, 0));
        assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
    }

    @Test
    @Order(6)
    void obterConsultaPeloId() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", response.id())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, ConsultaResponse.class);

        assertThat(response.proposito()).isEqualTo("teste2");
        assertThat(response.data()).isEqualTo(LocalDate.of(2030, 1, 1));
        assertThat(response.hora()).isEqualTo(LocalTime.of(16, 0));
        assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
    }

    @Test
    @Order(7)
    void obterConsultaComIdInexistente() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 90L)
                .when()
                .get()
                .then()
                .statusCode(404);
    }



    @Test
    @Order(8)
    void alterarStatusConsulta() {
        specification = new RequestSpecBuilder()
                .setBasePath("/consultas/{id}/status")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", response.id())
                .queryParams("status", StatusConsulta.CANCELADA)
                .when()
                .patch()
                .then()
                .statusCode(204);

    }

    @Test
    @Order(9)
    void deletarConsultaPeloId() {
        specification = new RequestSpecBuilder()
                .setBasePath("/consultas/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", response.id())
                .queryParams("status", StatusConsulta.CANCELADA)
                .when()
                .delete()
                .then()
                .statusCode(204);
    }

    @Test
    @Order(10)
    void deletarConsultaComIdInexistente() {
        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", 90L)
                .queryParams("status", StatusConsulta.CANCELADA)
                .when()
                .delete()
                .then()
                .statusCode(404);
    }



    @Test
    @Order(11)
    void listarConsultas() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/consultas")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .queryParams("pagina", 0, "tamanho", 5, "direction", "ASC")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ConsultaPageContent pageContent = objectMapper.readValue(content, ConsultaPageContent.class);

        ConsultaResponse consulta0 = pageContent.getContent().get(0);

        assertThat(consulta0.proposito()).isEqualTo("check-up");
        assertThat(consulta0.data()).isEqualTo(LocalDate.of(2026, 5, 12));
        assertThat(consulta0.hora()).isEqualTo(LocalTime.of(15, 0));
        assertThat(consulta0.status()).isEqualTo(StatusConsulta.AGENDADA);

        ConsultaResponse consulta1 = pageContent.getContent().get(1);

        assertThat(consulta1.proposito()).isEqualTo("check-up");
        assertThat(consulta1.data()).isEqualTo(LocalDate.of(2026, 5, 13));
        assertThat(consulta1.hora()).isEqualTo(LocalTime.of(13, 0));
        assertThat(consulta1.status()).isEqualTo(StatusConsulta.CANCELADA);


    }


    private ConsultaRequest mockConsultaRequest() {
        return new ConsultaRequest(1L, 1L, "teste", LocalDate.of(2030, 1, 1), LocalTime.of(16, 0));
    }

    private ConsultaRequest mockConsultaRequestAtualizado() {
        return new ConsultaRequest(1L, 1L, "teste2", LocalDate.of(2030, 1, 1), LocalTime.of(16, 0));
    }

    private ConsultaRequest mockConsultaRequestMedicoIndisponivel() {
        return new ConsultaRequest(1L, 1L, "teste", LocalDate.of(2026, 5, 12), LocalTime.of(16, 20));
    }

    private ConsultaRequest mockConsultaRequestConsultaConflitante() {
        return new ConsultaRequest(1L, 1L, "teste", LocalDate.of(2026, 5, 12), LocalTime.of(15, 0));
    }


}