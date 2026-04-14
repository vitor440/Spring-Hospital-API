# 🏥 Spring-Hospital-API

[![Java 21](https://img.shields.io/badge/Java-21-orange?logo=java)](https://www.oracle.com/java/)
[![Spring Boot 3.5.11](https://img.shields.io/badge/Spring%20Boot-3.5.11-green?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL 16.13](https://img.shields.io/badge/PostgreSQL-16.13-blue?logo=postgresql)](https://www.postgresql.org/)
[![Apache Maven 3.9](https://img.shields.io/badge/Apache%20Maven-3.9-C71A36?logo=apache-maven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> **Sistema de Gestão Hospitalar** - Uma API REST robusta e segura desenvolvida com Java 21, Spring Boot 3.5.11 e PostgreSQL para gerenciamento completo de operações hospitalares, incluindo pacientes, médicos, departamentos, consultas e prescrições.

---

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Recursos Principais](#recursos-principais)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Segurança](#segurança)
- [Roadmap de Melhorias](#roadmap-de-melhorias)
- [Contribuição](#contribuição)
- [Autor](#autor)
- [Licença](#licença)

---

## 🎯 Visão Geral

Este projeto é uma API REST completa para gerenciamento hospitalar, desenvolvida seguindo os princípios de **clean code**, **SOLID** e **boas práticas de desenvolvimento**. Oferece funcionalidades avançadas de autenticação JWT, controle de acesso baseado em papéis (RBAC), versionamento de banco de dados com Flyway, importação de dados em múltiplos formatos, e testes de integração automatizados.

A aplicação foi projetada para atender a demandas do mercado profissional de 2026, com foco em escalabilidade, segurança e observabilidade.

---

## ✨ Recursos Principais

### 👥 Gerenciamento de Usuários
- ✅ Cadastro e autenticação de usuários
- ✅ Geração de JWT com access token e refresh token
- ✅ Sistema de papéis e permissões (Roles)
- ✅ Controle de acesso baseado em papéis (RBAC)

### 👨‍⚕️ Gerenciamento de Médicos
- ✅ Cadastro completo de médicos com especialidades
- ✅ Gerenciamento de disponibilidades (turnos)
- ✅ Listagem paginada com filtros avançados
- ✅ Importação em massa via CSV e XLSX
- ✅ Validação de CRM único

### 🏥 Gerenciamento de Departamentos
- ✅ CRUD completo de departamentos
- ✅ Listagem com filtros dinâmicos
- ✅ Importação em massa de dados
- ✅ Proteção contra deleção com referências

### 👤 Gerenciamento de Pacientes
- ✅ Cadastro e atualização de pacientes
- ✅ Validação de CPF único
- ✅ Listagem com filtros (nome, gênero, tipo sanguíneo)
- ✅ Importação em massa via CSV e XLSX

### 📅 Gerenciamento de Consultas
- ✅ Agendamento de consultas
- ✅ Validação de conflitos de horário
- ✅ Controle de status (AGENDADA, REALIZADA, CANCELADA, FALTANTE)
- ✅ Listagem paginada com filtros

### 💊 Prescrições e Medicamentos
- ✅ Registro de resultados de consulta
- ✅ Prescrição de medicamentos
- ✅ Documentação de diagnósticos e tratamentos
- ✅ Gerenciamento de datas de retorno

### 📊 Recursos Adicionais
- ✅ Paginação configurável em todas as listas
- ✅ Tratamento robusto de erros com mensagens claras
- ✅ Auditoria automática (data de criação)
- ✅ Validação de dados em múltiplas camadas
- ✅ Documentação OpenAPI/Swagger integrada

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de que você tem os seguintes requisitos instalados:

| Requisito | Versão | Download |
|-----------|--------|----------|
| **Java JDK** | 21+ | [Download](https://www.oracle.com/java/technologies/downloads/#java21) |
| **Apache Maven** | 3.9+ | [Download](https://maven.apache.org/download.cgi) |
| **PostgreSQL** | 16.13+ | [Download](https://www.postgresql.org/download/) |
| **Git** | Qualquer versão | [Download](https://git-scm.com/download) |

### Verificar Instalação

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar PostgreSQL
psql --version
```

---

## 🚀 Instalação

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/vitor440/Spring-Hospital-API.git
cd Spring-Hospital-API
```

### 2️⃣ Criar Banco de Dados PostgreSQL

```bash
# Acessar PostgreSQL
psql -U postgres

# Criar database
CREATE DATABASE gerenciamento_hospitalar;

# Criar usuário (opcional)
CREATE USER hospital_user WITH ENCRYPTED PASSWORD 'seu_password';
GRANT ALL PRIVILEGES ON DATABASE gerenciamento_hospitalar TO hospital_user;

# Sair
\q
```

### 3️⃣ Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (opcional):

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gerenciamento_hospitalar
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=sua_senha
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_SECURITY_JWT_SECRET_KEY=sua_chave_secreta_super_segura_com_muitos_caracteres
SPRING_SECURITY_JWT_EXPIRE_LENGTH=3600000
CORS_ORIGIN_PATTERNS=http://localhost:3000,http://localhost:8080
```

---

## ⚙️ Configuração

### Arquivo de Configuração Principal

Edite `src/main/resources/application.yml` ou `application.properties`:

```yaml
spring:
  application:
    name: gerenciamento-hospitalar
  
  datasource:
    url: jdbc:postgresql://localhost:5432/gerenciamento_hospitalar
    username: postgres
    password: sua_senha
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
  
  security:
    jwt:
      secret-key: ${SPRING_SECURITY_JWT_SECRET_KEY:sua_chave_padrao}
      expire-length: ${SPRING_SECURITY_JWT_EXPIRE_LENGTH:3600000}

cors:
  originPatterns: ${CORS_ORIGIN_PATTERNS:http://localhost:3000,http://localhost:8080}

server:
  port: 8080
  servlet:
    context-path: /api
```

### Flyway - Versionamento de Banco de Dados

As migrações SQL são localizadas em `src/main/resources/db/migration/`. O Flyway as executa automaticamente na inicialização.

Exemplo de arquivo de migração (`V1__Initial_Schema.sql`):
```sql
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    nome_completo VARCHAR(255),
    enabled BOOLEAN DEFAULT true
);
```

---

## ▶️ Executando a Aplicação

### Via Maven

```bash
# Build do projeto
mvn clean install

# Executar aplicação
mvn spring-boot:run

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Via JAR

```bash
# Build
mvn clean package -DskipTests

# Executar
java -jar target/gerenciamento-hospitalar-0.0.1-SNAPSHOT.jar
```

### Verificar se está Rodando

```bash
# Acessar a API
curl http://localhost:8080/api/swagger-ui.html

# Ou via navegador
http://localhost:8080/api/swagger-ui.html
```

---

## 🧪 Testes

### Executar Testes Unitários

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=ConsultaServiceTest

# Com coverage
mvn test jacoco:report
```

### Executar Testes de Integração

```bash
# Todos os testes de integração
mvn verify

# Testes específicos
mvn verify -Dtest=DepartamentoControllerTest
```

### Estrutura de Testes

```
src/test/
├── java/com/gerenciamento_hospitalar/
│   ├── unittests/
│   │   ├── service/      # Testes unitários de serviços
│   │   └── validator/    # Testes de validadores
│   └── integrationtests/
│       ├── controller/   # Testes de integração de controllers
│       └── AbstractIntegrationTest.java  # Base para testes com TestContainers
```

### Ferramentas Utilizadas

- **JUnit 5** - Framework de testes
- **Mockito** - Mocking de dependências
- **RestAssured** - Testes de API REST
- **TestContainers** - PostgreSQL containerizado para testes

---

## 📚 Documentação da API

A documentação interativa da API está disponível via **Swagger/OpenAPI**:

### Acessar Swagger UI

```
http://localhost:8080/api/swagger-ui.html
```

### Acessar JSON OpenAPI

```
http://localhost:8080/api/v3/api-docs
```

---

## 🔌 Endpoints

### 🔐 Autenticação (Authentication)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/auth/singin` | Obter token JWT | Público |
| `POST` | `/auth/refresh/{username}` | Renovar token JWT | Público |
| `POST` | `/auth/createUser` | Criar novo usuário | Público |
| `POST` | `/auth/{userId}/roles` | Adicionar role ao usuário | ADMIN |

**Exemplo - Signin:**
```bash
curl -X POST http://localhost:8080/api/auth/singin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "senha": "admin123",
    "nomeCompleto": "Administrador"
  }'
```

**Resposta:**
```json
{
  "username": "admin",
  "authenticated": true,
  "created": "2026-04-14T10:30:00Z",
  "expiration": "2026-04-14T11:30:00Z",
  "acessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc..."
}
```

---

### 👨‍⚕️ Médicos (Doctors)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/medicos` | Criar médico | ADMIN |
| `GET` | `/medicos/{id}` | Obter médico por ID | ADMIN, RECEPCIONISTA |
| `GET` | `/medicos` | Listar médicos (paginado) | ADMIN, RECEPCIONISTA |
| `GET` | `/medicos/me` | Obter dados do médico logado | MEDICO |
| `PUT` | `/medicos/{id}` | Atualizar médico | ADMIN |
| `DELETE` | `/medicos/{id}` | Deletar médico | ADMIN |
| `POST` | `/medicos/importar` | Importar médicos (CSV/XLSX) | ADMIN |
| `GET` | `/medicos/{id}/consultas` | Consultas do médico | ADMIN, RECEPCIONISTA |
| `GET` | `/medicos/me/consultas` | Consultas do médico logado | MEDICO |

---

### 🏥 Departamentos (Departments)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/departamentos` | Criar departamento | ADMIN |
| `GET` | `/departamentos/{id}` | Obter departamento | ADMIN, RECEPCIONISTA |
| `GET` | `/departamentos` | Listar departamentos (paginado) | ADMIN, RECEPCIONISTA |
| `PUT` | `/departamentos/{id}` | Atualizar departamento | ADMIN |
| `DELETE` | `/departamentos/{id}` | Deletar departamento | ADMIN |
| `POST` | `/departamentos/importar` | Importar departamentos (CSV/XLSX) | ADMIN |

---

### 👤 Pacientes (Patients)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/pacientes` | Criar paciente | RECEPCIONISTA |
| `GET` | `/pacientes/{id}` | Obter paciente | ADMIN, RECEPCIONISTA |
| `GET` | `/pacientes` | Listar pacientes (paginado) | ADMIN, RECEPCIONISTA |
| `GET` | `/pacientes/me` | Obter dados do paciente logado | PACIENTE |
| `PUT` | `/pacientes/{id}` | Atualizar paciente | RECEPCIONISTA |
| `DELETE` | `/pacientes/{id}` | Deletar paciente | RECEPCIONISTA |
| `POST` | `/pacientes/importar` | Importar pacientes (CSV/XLSX) | RECEPCIONISTA |

---

### 📅 Consultas (Appointments)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/consultas` | Agendar consulta | RECEPCIONISTA |
| `GET` | `/consultas/{id}` | Obter consulta | ADMIN, RECEPCIONISTA |
| `GET` | `/consultas` | Listar consultas (paginado) | ADMIN, RECEPCIONISTA |
| `PUT` | `/consultas/{id}` | Atualizar consulta | RECEPCIONISTA |
| `DELETE` | `/consultas/{id}` | Cancelar consulta | RECEPCIONISTA |
| `PATCH` | `/consultas/{id}/status` | Alterar status consulta | RECEPCIONISTA |

**Statuses disponíveis:** `AGENDADA`, `REALIZADA`, `CANCELADA`, `FALTANTE`

---

### 💊 Resultados e Prescrições

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/consultas/{id}/resultadoConsulta` | Gerar resultado | MEDICO |
| `PUT` | `/resultadoConsulta/{id}` | Atualizar resultado | MEDICO |
| `GET` | `/resultadoConsulta/{id}` | Obter resultado | MEDICO |
| `DELETE` | `/resultadoConsulta/{id}` | Deletar resultado | MEDICO |

---

### ⏰ Turnos/Disponibilidades

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| `POST` | `/medicos/{id}/turnos` | Adicionar turno | MEDICO |
| `PUT` | `/turnos/{id}` | Atualizar turno | MEDICO |
| `DELETE` | `/turnos/{id}` | Deletar turno | MEDICO |
| `GET` | `/medicos/{id}/turnos` | Listar turnos do médico | MEDICO |

---

## 🏗️ Arquitetura

### Arquitetura em Camadas

```
┌─────────────────────────────────────────────────────┐
│                   Controllers                       │ ← REST Endpoints
├─────────────────────────────────────────────────────┤
│              Service Layer                          │ ← Business Logic
├─────────────────────────────────────────────────────┤
│           Repository Layer (JPA)                    │ ← Data Access
├─────────────────────────────────────────────────────┤
│              Database (PostgreSQL)                  │ ← Persistence
└─────────────────────────────────────────────────────┘
```

### Padrões de Design Utilizados

- **Controller-Service-Repository** - Separação de responsabilidades
- **Factory Pattern** - Importadores de arquivo (CSV, XLSX)
- **DTO Pattern** - Separação entre API e modelos internos
- **Mapper Pattern** - MapStruct para conversão de entidades
- **Exception Handler** - GlobalExceptionHandler centralizado

---

## 🛠️ Tecnologias

### Backend

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **Java** | 21 | Linguagem de programação |
| **Spring Boot** | 3.5.11 | Framework web |
| **Spring Security** | 6.x | Autenticação e autorização |
| **Spring Data JPA** | 3.x | Acesso a dados |
| **PostgreSQL** | 16.13 | Banco de dados relacional |
| **Flyway** | Latest | Versionamento de schema |
| **Lombok** | 1.18.x | Redução de boilerplate |
| **MapStruct** | 1.6.3 | Mapping de DTOs |
| **JWT (Auth0)** | 4.5.1 | Tokens JWT |
| **SpringDoc OpenAPI** | 2.8.0 | Documentação Swagger |
| **Apache POI** | 5.5.1 | Manipulação de Excel |
| **Apache Commons CSV** | 1.14.1 | Manipulação de CSV |
| **Hypersistence Utils** | 3.15.2 | Suporte a arrays PostgreSQL |

### Testes

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **JUnit 5** | Latest | Framework de testes |
| **Mockito** | Latest | Mocking de dependências |
| **RestAssured** | Latest | Testes de API REST |
| **TestContainers** | 1.21.4 | PostgreSQL para testes |

### Build & Deploy

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| **Apache Maven** | 3.9+ | Build automation |
| **Maven Compiler** | Latest | Compilação Java |

---

## 🔒 Segurança

### Mecanismos de Segurança Implementados

#### 1. **Autenticação JWT**
- Access Token com expiração curta (1 hora padrão)
- Refresh Token com expiração longa (3 horas)
- Algoritmo HMAC256

```java
// Exemplo de geração de token
POST /auth/singin
{
  "username": "user",
  "senha": "password",
  "nomeCompleto": "Full Name"
}
```

#### 2. **Autorização RBAC**
Papéis disponíveis:
- `ADMIN` - Acesso total ao sistema
- `MEDICO` - Gerenciamento de consultas e prescrições
- `RECEPCIONISTA` - Gerenciamento de pacientes e agendamentos
- `PACIENTE` - Visualização de próprias consultas

```java
@PreAuthorize("hasRole('ADMIN')")
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
```

#### 3. **Password Encoding**
- Algoritmo PBKDF2WithHmacSHA256
- 185.000 iterações
- Sal seguro gerado automaticamente

#### 4. **CORS Configuração**
```yaml
cors:
  originPatterns: "http://localhost:3000,http://localhost:8080"
```

#### 5. **Validação de Entrada**
- Jakarta Validation
- Custom validators para regras de negócio
- Sanitização contra SQL Injection/XSS

#### 6. **Tratamento de Erros**
- GlobalExceptionHandler
- Mensagens de erro seguras
- Sem exposição de stack traces em produção

### Boas Práticas de Segurança

✅ **Implementadas:**
- JWT com expiration
- PBKDF2 password hashing
- CORS restrictivo
- HTTPS ready (Spring Security)
- Input validation


## 👨‍💻 Autor

**Vitor de Souza Oliveira**
- 📧 Email: vitor16.souzaoliver@gmail.com
- 🐙 GitHub: [@vitor440](https://github.com/vitor440)
- 💼 LinkedIn: [LinkedIn Profile](https://linkedin.com)

---


**Última atualização:** 14 de Abril de 2026
