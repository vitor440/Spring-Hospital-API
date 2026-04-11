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
import com.gerenciamento_hospitalar.integrationtests.dto.DepartamentoPageContent;
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
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartamentoControllerTest extends AbstractIntegrationTest {


    @Autowired
    ObjectMapper objectMapper;

    private static DepartamentoRequest request;
    private static DepartamentoResponse response;
    private static TokenDTO tokenAdmin;
    private static RequestSpecification specification;

    @BeforeAll
    static void setUpAll() {
        request = mockRequest();
        response = mockResponse();

    }

    @BeforeEach
    void setUp() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }


    @Test
    @Order(1)
    void getToken() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/auth/singin")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CadastroUsuarioDTO(null, "admin", "admin123", "admin"))
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        tokenAdmin = objectMapper.readValue(content, TokenDTO.class);
        assertNotNull(tokenAdmin.acessToken());
        assertNotNull(tokenAdmin.refreshToken());

        assertEquals(tokenAdmin.username(), "admin");
        assertTrue(tokenAdmin.authenticated());
    }

    @Test
    @Order(2)
    void addDepartamento() throws JsonProcessingException {

        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("/departamentos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

       var content =  given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
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
       assertNotNull(response.dataCriacao());
       assertTrue(response.id() > 0);

       assertEquals(response.nome(), "Cardiologia");
       assertEquals(response.localizacao(), "quinto andar");

    }

    @Test
    @Order(3)
    void atualizarDepartamento() throws JsonProcessingException {
        DepartamentoRequest requestAtualizado = new DepartamentoRequest("Cardiologia", "oitavo andar");

        specification = new RequestSpecBuilder()
                .setBasePath("/departamentos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", response.id())
                .body(requestAtualizado)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, DepartamentoResponse.class);

        assertNotNull(response.id());
        assertNotNull(response.dataCriacao());
        assertTrue(response.id() > 0);

        assertEquals(response.nome(), "Cardiologia");
        assertEquals(response.localizacao(), "oitavo andar");
    }

    @Test
    @Order(4)
    void obterDepartamentoPeloId() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/departamentos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", response.id())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, DepartamentoResponse.class);

        assertNotNull(response.id());
        assertNotNull(response.dataCriacao());
        assertTrue(response.id() > 0);

        assertEquals(response.nome(), "Cardiologia");
        assertEquals(response.localizacao(), "oitavo andar");
    }

    @Test
    @Order(5)
    void listarDepartamentos() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/departamentos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .queryParams("pagina", 0, "tamanho", 6, "direction", "ASC")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var page = objectMapper.readValue(content, DepartamentoPageContent.class);

        List<DepartamentoResponse> lista = page.getContent();

        DepartamentoResponse departamento0 = lista.get(0);

        assertEquals(departamento0.nome(), "cardiologia");
        assertEquals(departamento0.localizacao(), "quinto andar");

        DepartamentoResponse departamento3 = lista.get(2);

        assertEquals(departamento3.nome(), "hepatologia");
        assertEquals(departamento3.localizacao(), "terceiro andar");


    }

    @Test
    @Order(6)
    void deletarDepartamentoPeloId() {
        specification = new RequestSpecBuilder()
                .setBasePath("/departamentos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", response.id())
                .when()
                .delete()
                .then()
                .statusCode(204);
    }

    private static Departamento mockDepartamento() {
        Departamento departamento1 = new Departamento();
        departamento1.setNome("Cardiologia");
        departamento1.setLocalizacao("quinto andar");

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