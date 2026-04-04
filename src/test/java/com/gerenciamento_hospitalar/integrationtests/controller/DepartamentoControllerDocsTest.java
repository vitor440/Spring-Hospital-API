package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.integrationtests.AbstractionIntegrationTest;
import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DepartamentoControllerDocsTest extends AbstractionIntegrationTest {

    private DepartamentoRequest request;
    private DepartamentoResponse response;
    private RequestSpecification specification;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        request = new DepartamentoRequest("Cardiologia", "quinto andar");

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    void addDepartamento() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_UDEMY)
                .setBasePath("/departamentos")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                .when()
                    .post()
                .then()
                    .statusCode(201)
                .extract()
                    .body()
                        .asString();

        response = objectMapper.readValue(content, DepartamentoResponse.class);

        assertNotNull(response.id());
        assertNotNull(response.nome());
        assertNotNull(response.localizacao());
        assertNotNull(response.dataCriacao());

        assertTrue(response.id() > 0);

        assertEquals("Cardiologia", response.nome());
        assertEquals("quinto andar", response.localizacao());


    }

    @Test
    void addDepartamentoComCorsIvnvalido() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_PARAM_ORIGIN, TestConfig.ORIGIN_GITHUB)
                .setBasePath("/departamentos")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                .when()
                    .post()
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

    }



    @Test
    void atualizarDepartamento() {
    }

    @Test
    void deletarDepartamentoPeloId() {
    }

    @Test
    void obterDepartamentoPeloId() {
    }

    @Test
    void listarDepartamentos() {
    }
}