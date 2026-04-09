package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.RoleDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.model.Role;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest extends AbstractIntegrationTest {


    @Autowired
    ObjectMapper objectMapper;

    static TokenDTO tokenDTO;

    private static CadastroUsuarioDTO usuarioDTO;
    private static CadastroUsuarioDTO usuarioSalvo;

    @BeforeAll
    static void setUpAll() {
        usuarioDTO = mockUsuarioDTO();
    }

    @BeforeEach
    void setUp() {

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Test
    @Order(1)
    void createUser() throws JsonProcessingException {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("/auth/createUser")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(usuarioDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        usuarioSalvo = objectMapper.readValue(content, CadastroUsuarioDTO.class);
        assertEquals(usuarioSalvo.username(), "admin");
    }

    @Test
    @Order(2)
    void singIn() throws JsonProcessingException {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("/auth/singin")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

        assertEquals(tokenDTO.username(), "admin");
        assertTrue(tokenDTO.authenticated());
    }

    @Test
    @Order(3)
    void refresh() throws JsonProcessingException {

        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("auth/refresh/{username}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .pathParams("username", tokenDTO.username())
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.refreshToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        TokenDTO refreshToken = objectMapper.readValue(content, TokenDTO.class);

        assertNotNull(refreshToken.acessToken());
        assertNotNull(refreshToken.refreshToken());

        assertTrue(refreshToken.authenticated());
    }

    @Test
    @Order(4)
    void criaRole() throws JsonProcessingException {
        Role role = new Role();
        role.setRole("ADMIN");

        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("auth/createRoles")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(role)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Role roleSalvo = objectMapper.readValue(content, Role.class);

        assertNotNull(roleSalvo.getId());
        assertEquals(roleSalvo.getRole(), "ADMIN");

    }

    @Test
    @Order(5)
    void adicionaRole() {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBasePath("auth/{userId}/roles")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("userId", usuarioSalvo.id())
                .body(new RoleDTO(List.of("ADMIN")))
                .when()
                .post()
                .then()
                .statusCode(204);


    }


    private static CadastroUsuarioDTO mockUsuarioDTO() {
        CadastroUsuarioDTO usuarioDTO1 = new CadastroUsuarioDTO(null, "admin", "admin123", "admin");
        return usuarioDTO1;
    }
}