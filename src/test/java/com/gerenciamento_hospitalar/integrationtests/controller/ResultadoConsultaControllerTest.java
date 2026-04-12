package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.mocks.PrescricaoMock;
import com.gerenciamento_hospitalar.mocks.ResultadoConsultaMock;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ResultadoConsultaControllerTest extends AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    private static TokenDTO tokenMedico;
    private static RequestSpecification specification;
    private static ResultadoConsultaRequest request;
    private static ResultadoConsultaResponse response;

    @BeforeEach
    void setUp() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void singinTokenMedico() throws IOException {
        specification = new RequestSpecBuilder()
                .setBasePath("/auth/singin")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CadastroUsuarioDTO(null, "robson amorim", "medico123", "robson amorim"))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenMedico = objectMapper.readValue(content, TokenDTO.class);
        assertNotNull(tokenMedico.acessToken());
        assertNotNull(tokenMedico.refreshToken());

        assertEquals(tokenMedico.username(), "robson amorim");
        assertTrue(tokenMedico.authenticated());
    }

    @Test
    @Order(2)
    void gerarResultadoConsulta() throws JsonProcessingException {
        request = ResultadoConsultaMock.mockRequest(1);

        specification = new RequestSpecBuilder()
                .setBasePath("consultas/{id}/resultadoConsulta")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenMedico.acessToken())
                .body(request)
                .pathParam("id", 1L)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, ResultadoConsultaResponse.class);

        Assertions.assertThat(response.diagnostico()).isEqualTo("Diagnostico1");
        Assertions.assertThat(response.notas()).isEqualTo("Notas1");
        Assertions.assertThat(response.tratamento()).isEqualTo("Tratamento1");

    }

    @Test
    void atualizarResultadoConsulta() {
    }

    @Test
    void obterResultadoConsulta() {
    }

    @Test
    void deletarResultadoConsulta() {
    }
}