package com.gerenciamento_hospitalar.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gerenciamento_hospitalar.config.TestConfig;
import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.dto.security.CadastroUsuarioDTO;
import com.gerenciamento_hospitalar.dto.security.TokenDTO;
import com.gerenciamento_hospitalar.integrationtests.AbstractIntegrationTest;
import com.gerenciamento_hospitalar.integrationtests.pageContent.ConsultaPageContent;
import com.gerenciamento_hospitalar.integrationtests.pageContent.MedicoPageContent;
import com.gerenciamento_hospitalar.mocks.MedicoMock;
import com.gerenciamento_hospitalar.model.DiaSemana;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_TIME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MedicoControllerTest extends AbstractIntegrationTest {

    @Autowired
    ObjectMapper objectMapper;

    private static TokenDTO tokenMedico;
    private static TokenDTO tokenAdmin;
    private static RequestSpecification specification;
    private static MedicoRequest request;
    private static MedicoResponse response;

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
    void singinTokenAdmin() throws IOException {
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
    @Order(3)
    void addMedicoComRoleMedico() throws IOException {
        request = MedicoMock.mockRequest(1);

        specification = new RequestSpecBuilder()
                .setBasePath("/medicos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenMedico.acessToken())
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(403);

    }

    @Test
    @Order(4)
    void addMedicoComRoleAdmin() throws IOException {

        specification = new RequestSpecBuilder()
                .setBasePath("/medicos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
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

        response = objectMapper.readValue(content, MedicoResponse.class);

        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isGreaterThan(0);

        assertThat(response.nome()).isEqualTo("Nome1");
        assertThat(response.crm()).isEqualTo("crm-1");
        assertThat(response.email()).isEqualTo("Email1");
        assertThat(response.telefone()).isEqualTo("Telefone1");
        assertThat(response.especialidade()).isEqualTo(Especialidade.CARDIOLOGISTA);

    }

    @Test
    @Order(5)
    void addMedicoComCrmRepetido() throws IOException {

        specification = new RequestSpecBuilder()
                .setBasePath("/medicos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(409);
    }



    @Test
    @Order(6)
    void atualizarMedico() throws JsonProcessingException {

        request = MedicoMock.mockRequest(2);
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", response.id())
                .body(request)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, MedicoResponse.class);


        assertThat(response.nome()).isEqualTo("Nome2");
        assertThat(response.crm()).isEqualTo("crm-2");
        assertThat(response.email()).isEqualTo("Email2");
        assertThat(response.telefone()).isEqualTo("Telefone2");
        assertThat(response.especialidade()).isEqualTo(Especialidade.NEUROLOGISTA);
    }

    @Test
    @Order(7)
    void obterMedicoPeloId() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
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

        response = objectMapper.readValue(content, MedicoResponse.class);

        assertThat(response.nome()).isEqualTo("Nome2");
        assertThat(response.crm()).isEqualTo("crm-2");
        assertThat(response.email()).isEqualTo("Email2");
        assertThat(response.telefone()).isEqualTo("Telefone2");
        assertThat(response.especialidade()).isEqualTo(Especialidade.NEUROLOGISTA);
    }

    @Test
    @Order(8)
    void obterMedicoComIdInexistente() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", 90L)
                .when()
                .get()
                .then()
                .statusCode(404);

    }



    @Test
    @Order(9)
    void deletarMedicoPeloId() {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", response.id())
                .when()
                .delete()
                .then()
                .statusCode(204);

    }

    @Test
    @Order(10)
    void deletarMedicoComIdInexistente() {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", 90L)
                .when()
                .delete()
                .then()
                .statusCode(404);

    }

    @Test
    @Order(11)
    void deletarMedicoComConsultasRegistradas() {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", 1L)
                .when()
                .delete()
                .then()
                .statusCode(409);

    }

    @Test
    @Order(12)
    void obterMedicoLogado() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/me")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenMedico.acessToken())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        response = objectMapper.readValue(content, MedicoResponse.class);


        assertThat(response.nome()).isEqualTo("dr robson amorim");
        assertThat(response.crm()).isEqualTo("CRM-AM 2010");
        assertThat(response.email()).isEqualTo("robson.amorim@email.com");
        assertThat(response.telefone()).isEqualTo("8244-0227");
        assertThat(response.especialidade()).isEqualTo(Especialidade.NEUROLOGISTA);
    }

    @Test
    @Order(13)
    void listarMedicos() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .queryParams("pagina", 0, "tamanho", 5, "direction", "ASC")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        MedicoPageContent medicoPageContent = objectMapper.readValue(content, MedicoPageContent.class);

        MedicoResponse medico0 = medicoPageContent.getContent().get(0);

        assertThat(medico0.nome()).isEqualTo("dr carlos cavalcante");
        assertThat(medico0.crm()).isEqualTo("CRM-RJ 9032");
        assertThat(medico0.email()).isEqualTo("carlos.cavalcante@email.com");
        assertThat(medico0.telefone()).isEqualTo("8820-3234");
        assertThat(medico0.especialidade()).isEqualTo(Especialidade.CARDIOLOGISTA);

        MedicoResponse medico1 = medicoPageContent.getContent().get(1);

        assertThat(medico1.nome()).isEqualTo("dr marcos araújo");
        assertThat(medico1.crm()).isEqualTo("CRM-SP 8932");
        assertThat(medico1.email()).isEqualTo("marcos@email.com");
        assertThat(medico1.telefone()).isEqualTo("9812-5943");
        assertThat(medico1.especialidade()).isEqualTo(Especialidade.ENDOCRINOLOGISTA);

        MedicoResponse medico2 = medicoPageContent.getContent().get(2);

        assertThat(medico2.nome()).isEqualTo("dr robson amorim");
        assertThat(medico2.crm()).isEqualTo("CRM-AM 2010");
        assertThat(medico2.email()).isEqualTo("robson.amorim@email.com");
        assertThat(medico2.telefone()).isEqualTo("8244-0227");
        assertThat(medico2.especialidade()).isEqualTo(Especialidade.NEUROLOGISTA);

    }

    @Test
    @Order(14)
    void obterConsultasPeloIdDoMedico() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/{id}/consultas")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenAdmin.acessToken())
                .pathParam("id", 1)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ConsultaPageContent consultaPageContent = objectMapper.readValue(content, ConsultaPageContent.class);

        ConsultaResponse consulta0 = consultaPageContent.getContent().get(0);

        assertThat(consulta0.medicoId()).isEqualTo(1L);
        assertThat(consulta0.pacienteId()).isEqualTo(1L);
        assertThat(consulta0.proposito()).isEqualTo("check-up");
        assertThat(consulta0.data()).isEqualTo(LocalDate.of(2026, 5, 12));
        assertThat(consulta0.hora()).isEqualTo(LocalTime.of(15, 0));
        assertThat(consulta0.status()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(consulta0.diaSemana()).isEqualTo(DiaSemana.TERCA);

        ConsultaResponse consulta1 = consultaPageContent.getContent().get(1);

        assertThat(consulta1.medicoId()).isEqualTo(1L);
        assertThat(consulta1.pacienteId()).isEqualTo(2L);
        assertThat(consulta1.proposito()).isEqualTo("check-up");
        assertThat(consulta1.data()).isEqualTo(LocalDate.of(2026, 5, 13));
        assertThat(consulta1.hora()).isEqualTo(LocalTime.of(13, 0));
        assertThat(consulta1.status()).isEqualTo(StatusConsulta.CANCELADA);
        assertThat(consulta1.diaSemana()).isEqualTo(DiaSemana.QUARTA);


    }

    @Test
    @Order(15)
    void obterConsultasMedicoLogado() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/me/consultas")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenMedico.acessToken())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ConsultaPageContent consultaPageContent = objectMapper.readValue(content, ConsultaPageContent.class);

        ConsultaResponse consulta0 = consultaPageContent.getContent().get(0);

        assertThat(consulta0.medicoId()).isEqualTo(1L);
        assertThat(consulta0.pacienteId()).isEqualTo(1L);
        assertThat(consulta0.proposito()).isEqualTo("check-up");
        assertThat(consulta0.data()).isEqualTo(LocalDate.of(2026, 5, 12));
        assertThat(consulta0.hora()).isEqualTo(LocalTime.of(15, 0));
        assertThat(consulta0.status()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(consulta0.diaSemana()).isEqualTo(DiaSemana.TERCA);

        ConsultaResponse consulta1 = consultaPageContent.getContent().get(1);

        assertThat(consulta1.medicoId()).isEqualTo(1L);
        assertThat(consulta1.pacienteId()).isEqualTo(2L);
        assertThat(consulta1.proposito()).isEqualTo("check-up");
        assertThat(consulta1.data()).isEqualTo(LocalDate.of(2026, 5, 13));
        assertThat(consulta1.hora()).isEqualTo(LocalTime.of(13, 0));
        assertThat(consulta1.status()).isEqualTo(StatusConsulta.CANCELADA);
        assertThat(consulta1.diaSemana()).isEqualTo(DiaSemana.QUARTA);
    }

    @Test
    @Order(16)
    void obterConsultasMedicoLogadoComFiltragemDeStatus() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/medicos/me/consultas")
                .setPort(TestConfig.SERVER_PORT)
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenMedico.acessToken())
                .queryParams("status", StatusConsulta.CANCELADA)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        ConsultaPageContent consultaPageContent = objectMapper.readValue(content, ConsultaPageContent.class);

        ConsultaResponse consulta0 = consultaPageContent.getContent().get(0);

        assertThat(consulta0.medicoId()).isEqualTo(1L);
        assertThat(consulta0.pacienteId()).isEqualTo(2L);
        assertThat(consulta0.proposito()).isEqualTo("check-up");
        assertThat(consulta0.data()).isEqualTo(LocalDate.of(2026, 5, 13));
        assertThat(consulta0.hora()).isEqualTo(LocalTime.of(13, 0));
        assertThat(consulta0.status()).isEqualTo(StatusConsulta.CANCELADA);
        assertThat(consulta0.diaSemana()).isEqualTo(DiaSemana.QUARTA);
    }
}