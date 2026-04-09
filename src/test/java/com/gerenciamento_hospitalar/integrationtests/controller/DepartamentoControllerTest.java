package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.model.Departamento;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartamentoControllerTest extends AbstractIntegrationTest {


    @Autowired
    ObjectMapper objectMapper;

    private static DepartamentoRequest request;
    private static DepartamentoResponse response;
    private static Departamento departamento;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUpAll() {
        departamento = mockDepartamento();
        request = mockRequest();
        response = mockResponse();

    }

    @BeforeEach
    void setUp() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void createUserAndGetToken() throws JsonProcessingException {
        CadastroUsuarioDTO usuarioDTO = new CadastroUsuarioDTO(null, "admin", "admin123", "admin");
        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("/auth/createUser")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .basePath("/auth/createUser")
                .port(TestConfig.SERVER_PORT)
                .body(usuarioDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .basePath("/auth/singin")
                    .port(TestConfig.SERVER_PORT)
                    .body(usuarioDTO)
                    .when()
                    .post()
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .asString();

        tokenDTO = objectMapper.readValue(content, TokenDTO.class);
        assertNotNull(tokenDTO.acessToken());
        assertNotNull(tokenDTO.refreshToken());
    }

    @Test
    @Order(2)
    void setRoleAdmin() {

    }

    @Test
    @Order(3)
    void addDepartamento() throws JsonProcessingException {

        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("/departamentos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

       var content =  given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.acessToken())
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

    }

    @Test
    void atualizarDepartamento() {
    }

    @Test
    void obterDepartamentoPeloId() {
    }

    @Test
    void listarDepartamentos() {
    }

    @Test
    void deletarDepartamentoPeloId() {
    }

    @Test
    void importarDados() {
    }


    private static Departamento mockDepartamento() {
        Departamento departamento1 = new Departamento();
        departamento1.setNome("Cardiologia");
        departamento1.setLocalizacao("quinto andar");

        return departamento1;
    }

    private static Departamento mockDepartamentoSalvo() {
        Departamento departamento1 = mockDepartamento();
        departamento1.setId(1L);
        departamento1.setDataCriacao(LocalDateTime.of(2026, 4, 4, 14, 7, 30));

        return departamento1;
    }


    private static DepartamentoRequest mockRequest() {
        DepartamentoRequest request1 = new DepartamentoRequest("Cardiologia", "quinto andar");
        return request1;
    }

    private static DepartamentoResponse mockResponse() {
        DepartamentoResponse response1 = new DepartamentoResponse(1L, "Cardiologia", "quinto andar",
                LocalDateTime.of(2026, 4, 4, 14, 7, 30));

        return response1;
    }
}