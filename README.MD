# java-challenge

![Java 25](https://img.shields.io/badge/Java-25-informational)
![Spring Boot 3.5](https://img.shields.io/badge/Spring%20Boot-3.5-success)
![Gradle 9](https://img.shields.io/badge/Gradle-9-7f52ff)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![License MIT](https://img.shields.io/badge/License-MIT-black)

## Descrição

API REST para cadastro e gerenciamento de livros com suporte a paginação, validação e persistência em memória para entrega rápida de um backend sólido.

## Funcionalidades

- Listar livros com paginação (`page`, `size`).
- Buscar livro por ID.
- Criar livro com validação de dados.
- Atualizar livro existente.
- Excluir livro por ID.

## Stack e Decisões Técnicas

- **Java 25** — uso da versão mais recente para aproveitar recursos de performance: [JEP 491](https://openjdk.org/jeps/491) (Virtual Threads sem pinning) e [JEP 450](https://openjdk.org/jeps/450) (Compact Object Headers com menor uso de memória).
- **Spring Boot 3.5** — estabilidade e suporte moderno para APIs REST.
- **Gradle 9** — build rápido, configuração declarativa e compatibilidade com cache.
- **Spring Data JPA + H2** — persistência leve para protótipo com SQL acessível e testes rápidos.
- **MapStruct** — mapeamento de DTOs com código gerado, evitando boilerplate.
- **Bean Validation (Jakarta)** — validação consistente de entrada no nível do modelo.
- **SpringDoc OpenAPI 2.8.x** — documentação automática das rotas e contratos.
- **JUnit 5 + Mockito** — testes unitários e mocks para camada de serviço/controller.
- **Docker com multi-stage build e Layered JAR** — imagem enxuta e build otimizado.
- **GitHub Actions** — pipeline com etapa de testes obrigatória antes do build e push da imagem Docker.

## Documentação da API

| Método | Endpoint | Descrição | Status de sucesso |
|--------|----------|-----------|-------------------|
| GET | `/v1/books` | Lista paginada | `200` |
| GET | `/v1/books/{id}` | Busca por ID | `200` |
| POST | `/v1/books` | Cria livro | `201` |
| PUT | `/v1/books/{id}` | Atualiza livro | `200` |
| DELETE | `/v1/books/{id}` | Remove livro | `204` |

## Como Rodar

### Pré-requisitos

- [Java 25](https://openjdk.org/projects/jdk/25/) — necessário apenas para rodar via Gradle
- [Docker](https://www.docker.com/) — necessário para rodar via Docker Compose

### Instalação

```bash
git clone https://github.com/seu-usuario/java-challenge.git
cd java-challenge
```

### Via Docker Compose

```bash
docker compose up -d
```

### Via Gradle

```bash
./gradlew bootRun
```

### Logs

```bash
docker compose logs -f
```

### Parar

```bash
docker compose down
```

## Rodando os Testes

```bash
./gradlew test
```

## Códigos de Resposta

| Código | Descrição |
|--------|-----------|
| `200` | Requisição processada com sucesso |
| `201` | Recurso criado com sucesso |
| `204` | Requisição processada com sucesso, sem conteúdo |
| `400` | Dados da requisição inválidos |
| `404` | Recurso não encontrado |

## Erros

Todas as respostas de erro seguem o formato:

```json
{
  "status": 400,
  "message": "Descrição do erro"
}
```

Erros de validação (`400 Bad Request`) retornam os campos que falharam:

```json
{
  "timestamp": "2026-04-24T00:11:42.019+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/v1/books"
}
```