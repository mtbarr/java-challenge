# API de Livros

Base URL: `/v1/books`

---

## Sumário

- [Criar livro](#criar-livro)
- [Buscar livro por ID](#buscar-livro-por-id)
- [Listar livros](#listar-livros)
- [Atualizar livro](#atualizar-livro)
- [Excluir livro](#excluir-livro)
- [Códigos de resposta](#códigos-de-resposta)
- [Erros](#erros)

---

## Criar livro

`POST /v1/books`

Cria um novo livro no sistema.

### Request Body

```json
{
  "title": "O Senhor dos Anéis",
  "author": "J.R.R. Tolkien",
  "publicationYear": 1954
}
```

| Campo             | Tipo    | Obrigatório | Validação                          |
|-------------------|---------|-------------|------------------------------------|
| `title`           | String  | Sim         | Não pode ser vazio                 |
| `author`          | String  | Sim         | Não pode ser vazio                 |
| `publicationYear` | Integer | Sim         | Deve ser positivo, entre 1000–2100 |

### Response `201 Created`

```json
{
  "id": 1,
  "title": "O Senhor dos Anéis",
  "author": "J.R.R. Tolkien",
  "publicationYear": 1954
}
```

---

## Buscar livro por ID

`GET /v1/books/{id}`

Retorna os dados de um livro específico pelo seu identificador.

### Path Parameters

| Parâmetro | Tipo | Descrição               |
|-----------|------|-------------------------|
| `id`      | Long | Identificador do livro  |

### Response `200 OK`

```json
{
  "id": 1,
  "title": "O Senhor dos Anéis",
  "author": "J.R.R. Tolkien",
  "publicationYear": 1954
}
```

### Response `404 Not Found`

```json
{
  "status": 404,
  "message": "Book not found with id: 1"
}
```

---

## Listar livros

`GET /v1/books`

Retorna uma lista paginada de todos os livros cadastrados.

### Query Parameters

| Parâmetro | Tipo    | Obrigatório | Padrão | Descrição                       |
|-----------|---------|-------------|--------|---------------------------------|
| `page`    | Integer | Não         | `0`    | Número da página (base zero)    |
| `size`    | Integer | Não         | `15`   | Quantidade de itens por página  |

### Exemplo de requisição

```
GET /v1/books?page=0&size=15
```

### Response `200 OK`

```json
{
  "content": [
    {
      "id": 1,
      "title": "O Senhor dos Anéis",
      "author": "J.R.R. Tolkien",
      "publicationYear": 1954
    },
    {
      "id": 2,
      "title": "Clean Code",
      "author": "Robert C. Martin",
      "publicationYear": 2008
    }
  ],
  "page": {
    "size": 15,
    "number": 0,
    "totalElements": 2,
    "totalPages": 1
  }
}
```

---

## Atualizar livro

`PUT /v1/books/{id}`

Atualiza todos os dados de um livro existente. Todos os campos são obrigatórios.

### Path Parameters

| Parâmetro | Tipo | Descrição               |
|-----------|------|-------------------------|
| `id`      | Long | Identificador do livro  |

### Request Body

```json
{
  "title": "O Senhor dos Anéis: A Sociedade do Anel",
  "author": "J.R.R. Tolkien",
  "publicationYear": 1954
}
```

| Campo             | Tipo    | Obrigatório | Validação                          |
|-------------------|---------|-------------|------------------------------------|
| `title`           | String  | Sim         | Não pode ser vazio                 |
| `author`          | String  | Sim         | Não pode ser vazio                 |
| `publicationYear` | Integer | Sim         | Deve ser positivo, entre 1000–2100 |

### Response `200 OK`

```json
{
  "id": 1,
  "title": "O Senhor dos Anéis: A Sociedade do Anel",
  "author": "J.R.R. Tolkien",
  "publicationYear": 1954
}
```

### Response `404 Not Found`

```json
{
  "status": 404,
  "message": "Book not found with id: 1"
}
```

---

## Excluir livro

`DELETE /v1/books/{id}`

Exclui permanentemente um livro pelo seu identificador.

### Path Parameters

| Parâmetro | Tipo | Descrição               |
|-----------|------|-------------------------|
| `id`      | Long | Identificador do livro  |

### Response `204 No Content`

Sem corpo de resposta.

### Response `404 Not Found`

```json
{
  "status": 404,
  "message": "Book not found with id: 1"
}
```

---

## Códigos de resposta

| Código | Descrição                                          |
|--------|----------------------------------------------------|
| `200`  | Requisição processada com sucesso                  |
| `201`  | Recurso criado com sucesso                         |
| `204`  | Requisição processada com sucesso, sem conteúdo    |
| `400`  | Dados da requisição inválidos                      |
| `404`  | Recurso não encontrado                             |

---

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
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "title": "Título é obrigatório",
    "publicationYear": "Ano de publicação é obrigatório"
  }
}
```