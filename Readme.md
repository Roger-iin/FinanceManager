# Finance Manager — Backend

API REST para gerenciamento de finanças pessoais, desenvolvida com Spring Boot 3 e Java 21.

## Tecnologias

- **Java 21**
- **Spring Boot 3.5** — Web, Security, Data JPA, Validation
- **PostgreSQL** — banco de dados relacional
- **Flyway** — migrações de banco de dados
- **JWT (JJWT 0.12)** — autenticação stateless
- **Lombok** — redução de boilerplate

## Funcionalidades

- Autenticação com registro e login via JWT
- Gerenciamento de contas (corrente, poupança, PIX, cartão de crédito, débito)
- Categorias personalizadas de receitas e despesas
- Lançamento de transações com filtros por período, tipo e conta
- Controle de assinaturas recorrentes com cancelamento e reativação
- Dashboard com totais do período e breakdown de despesas por categoria

## Pré-requisitos

- Java 21+
- Maven 3.8+
- PostgreSQL 14+

## Configuração

### 1. Banco de dados

Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE finance_manager;
```

### 2. Variáveis de ambiente

Configure o arquivo `src/main/resources/application.yml` com suas credenciais:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_manager
    username: seu_usuario
    password: sua_senha

app:
  jwt:
    secret: "sua_chave_base64_aqui"
    expiration: 86400000

# Se necessário pode configurar outro ip 
config:
  local-ip: ${LOCAL_IP}
```

Para gerar uma chave JWT segura:

```bash
openssl rand -base64 64
```

### 3. Rodando o projeto

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

As migrações do Flyway são executadas automaticamente na inicialização.

## Endpoints

### Autenticação

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/register` | Cadastrar novo usuário |
| POST | `/api/auth/login` | Autenticar e obter token JWT |

### Contas

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/accounts` | Listar contas do usuário |
| GET | `/api/accounts/{id}` | Buscar conta por ID |
| POST | `/api/accounts` | Criar nova conta |
| PUT | `/api/accounts/{id}` | Atualizar conta |
| DELETE | `/api/accounts/{id}` | Excluir conta |

### Categorias

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/categories` | Listar categorias (sistema + usuário) |
| GET | `/api/categories?type=EXPENSE` | Filtrar por tipo |
| POST | `/api/categories` | Criar categoria |
| PUT | `/api/categories/{id}` | Atualizar categoria |
| DELETE | `/api/categories/{id}` | Excluir categoria |

### Transações

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/transactions` | Listar transações (filtros opcionais) |
| GET | `/api/transactions?startDate=2025-03-01&endDate=2025-03-31` | Filtrar por período |
| GET | `/api/transactions?type=EXPENSE` | Filtrar por tipo |
| GET | `/api/transactions?accountId={id}` | Filtrar por conta |
| GET | `/api/transactions/{id}` | Buscar transação por ID |
| POST | `/api/transactions` | Criar transação |
| PUT | `/api/transactions/{id}` | Atualizar transação |
| DELETE | `/api/transactions/{id}` | Excluir transação |

### Assinaturas

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/subscriptions` | Listar assinaturas ativas |
| GET | `/api/subscriptions?all=true` | Listar todas (ativas + canceladas) |
| GET | `/api/subscriptions/{id}` | Buscar assinatura por ID |
| POST | `/api/subscriptions` | Criar assinatura |
| PUT | `/api/subscriptions/{id}` | Atualizar assinatura |
| PATCH | `/api/subscriptions/{id}/cancel` | Cancelar assinatura |
| PATCH | `/api/subscriptions/{id}/reactivate` | Reativar assinatura |
| DELETE | `/api/subscriptions/{id}` | Excluir assinatura |

### Dashboard

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/dashboard` | Resumo do mês atual |
| GET | `/api/dashboard?startDate=2025-03-01&endDate=2025-03-31` | Resumo do período |

## Autenticação

Todos os endpoints (exceto `/api/auth/**`) requerem o token JWT no header:

```
Authorization: Bearer <token>
```

O token é retornado no corpo da resposta após o login ou registro.

## Respostas de Erro

Todos os erros seguem o formato padronizado pelo `GlobalExceptionHandler`:

```json
{
  "timestamp": "2025-03-15T10:30:00",
  "status": 400,
  "error": "Mensagem descritiva do erro"
}
```

Erros de validação incluem os campos com problema:

```json
{
  "timestamp": "2025-03-15T10:30:00",
  "status": 400,
  "error": "Dados inválidos",
  "fields": {
    "email": "E-mail inválido",
    "password": "Mínimo 8 caracteres"
  }
}
```