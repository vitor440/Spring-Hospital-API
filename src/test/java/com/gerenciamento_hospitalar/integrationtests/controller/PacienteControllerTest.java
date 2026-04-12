package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.integrationtests.pageContent.PacientePageContent;
import com.gerenciamento_hospitalar.mocks.PacienteMock;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacienteControllerTest extends AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    private static PacienteRequest request;
    private static PacienteResponse response;
    private static TokenDTO tokenRecepcionista;
    private static RequestSpecification specification;



    @BeforeEach
    void setUp() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void singin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/auth/singin")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CadastroUsuarioDTO(null, "recepcionista", "recepcionista123", "recepcionista"))
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
    void addPaciente() throws JsonProcessingException {
        request = PacienteMock.mockPacienteRequest(1);
        specification = new RequestSpecBuilder()
                .setBasePath("/pacientes")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content =  given(specification)
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

        response = objectMapper.readValue(content, PacienteResponse.class);

        assertNotNull(response.id());
        assertTrue(response.id() > 0);

        assertEquals(response.nome(), "paciente1");
        assertEquals(response.cpf(), "449.815.530-05");
        assertEquals(response.endereco(), "endereço1");
        assertEquals(response.genero(), "masculino");
        assertEquals(response.telefone(), "Telefone1");
        assertEquals(response.tipoSanguineo(), "O-");
    }

    @Test
    @Order(3)
    void atualizarPaciente() throws JsonProcessingException {
        request = PacienteMock.mockPacienteRequest(2);
        specification = new RequestSpecBuilder()
                .setBasePath("/pacientes/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content =  given(specification)
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

        response = objectMapper.readValue(content, PacienteResponse.class);

        assertNotNull(response.id());
        assertTrue(response.id() > 0);

        assertEquals(response.nome(), "paciente2");
        assertEquals(response.cpf(), "449.815.530-05");
        assertEquals(response.endereco(), "endereço2");
        assertEquals(response.genero(), "feminino");
        assertEquals(response.telefone(), "Telefone2");
        assertEquals(response.tipoSanguineo(), "O+");
    }

    @Test
    @Order(4)
    void obterPacientePeloId() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/pacientes/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content =  given(specification)
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

        response = objectMapper.readValue(content, PacienteResponse.class);

        assertNotNull(response.id());
        assertTrue(response.id() > 0);

        assertEquals(response.nome(), "paciente2");
        assertEquals(response.cpf(), "449.815.530-05");
        assertEquals(response.endereco(), "endereço2");
        assertEquals(response.genero(), "feminino");
        assertEquals(response.telefone(), "Telefone2");
        assertEquals(response.tipoSanguineo(), "O+");
    }

    @Test
    @Order(6)
    void deletarPacientePeloId() {
        specification = new RequestSpecBuilder()
                .setBasePath("/pacientes/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content =  given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenRecepcionista.acessToken())
                .pathParam("id", response.id())
                .when()
                .delete()
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void listarPacientes() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/pacientes")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content =  given(specification)
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

        PacientePageContent pageContent = objectMapper.readValue(content, PacientePageContent.class);

        List<PacienteResponse> lista = pageContent.getContent();

        PacienteResponse paciente0 = lista.get(0);

        assertEquals(paciente0.cpf(), "014.553.780-38");
        assertEquals(paciente0.nome(), "ana carolina dos santos");
        assertEquals(paciente0.genero(), "feminino");

        PacienteResponse paciente1 = lista.get(1);

        assertEquals(paciente1.cpf(), "455.705.400-59");
        assertEquals(paciente1.nome(), "lucas pereira de souza");
        assertEquals(paciente1.genero(), "masculino");



    }

}