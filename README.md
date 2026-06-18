# Solvo — API de Pagamentos Simplificada

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-async-black)
![Redis](https://img.shields.io/badge/Redis-cache-red)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

> API de pagamentos entre usuários, com dois tipos de conta (Consumidor e Lojista), validação de saldo, autorização externa, notificação assíncrona e autenticação via JWT.

## Sobre o projeto

O Solvo simula uma plataforma de pagamentos onde usuários podem manter saldo em carteira e transferir dinheiro entre si. Toda transferência passa por validação de saldo e por um serviço externo de autorização antes de ser efetivada, e o destinatário é notificado de forma assíncrona quando recebe um pagamento.

## Funcionalidades

- Cadastro de usuários do tipo Consumidor (pode enviar e receber) ou Lojista (só recebe)
- Autenticação e autorização via Spring Security + JWT
- Transferência entre usuários com validação de saldo e bloqueio para lojistas tentando enviar
- Integração com serviço externo de autorização (mock) antes de efetivar a transferência
- Notificação assíncrona ao recebedor via Kafka
- Cache de leitura e controle de idempotência de transferências via Redis
- Circuit breaker e retry na chamada ao serviço externo, via Resilience4j
- Documentação interativa da API via Swagger UI
- Testes unitários (JUnit 5 + Mockito) e de integração com banco e Kafka reais (Testcontainers)
- Pipeline de CI no GitHub Actions

## Arquitetura

O projeto segue uma separação em camadas inspirada em Clean Architecture:

```
domain/          → entidades de negócio e regras puras (Usuario, Conta, Carteira, Transacao)
application/     → casos de uso (RealizarTransferencia, CriarUsuario, ConsultarHistorico)
infrastructure/  → repositórios JPA, cliente do serviço de autorização, producer/consumer Kafka, Redis
api/             → controllers REST, DTOs, tratamento de exceções, configuração de segurança
```

Fluxo simplificado de uma transferência:

```
Cliente → POST /transferencias
        → valida saldo e tipo de conta
        → consulta serviço externo de autorização (com retry/circuit breaker)
        → autorizado? debita pagador, credita recebedor, persiste transação
        → publica evento no Kafka
        → consumer processa e simula notificação ao recebedor
```

## Stack tecnológica

| Categoria             | Tecnologia                       |
| --------------------- | -------------------------------- |
| Linguagem / Framework | Java 25, Spring Boot             |
| Segurança             | Spring Security, JWT             |
| Banco de dados        | PostgreSQL                       |
| Migrations            | Flyway                           |
| Cache                 | Redis                            |
| Mensageria            | Apache Kafka                     |
| Mapeamento            | MapStruct                        |
| Produtividade         | Lombok                           |
| Resiliência           | Resilience4j                     |
| Documentação de API   | springdoc-openapi (Swagger)      |
| Testes                | JUnit 5, Mockito, Testcontainers |
| Containerização       | Docker, Docker Compose           |
| CI/CD                 | GitHub Actions                   |

## Como rodar localmente

Pré-requisitos: Docker e Docker Compose instalados, JDK 25
e Maven (ou usar o wrapper `./mvnw` incluso no projeto).

```bash
# 1. Clone o repositório
git clone https://github.com/davi0416/solvo.git
cd solvo

# 2. Suba a infraestrutura (Postgres, Redis, Kafka)
docker-compose up -d

# 3. Rode a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080` e a documentação interativa em `http://localhost:8080/swagger-ui.html`.

## Endpoints principais

| Método | Endpoint                    | Descrição                                        |
| ------ | --------------------------- | ------------------------------------------------ |
| POST   | `/usuarios`                 | Cadastra um novo usuário (Consumidor ou Lojista) |
| POST   | `/auth/login`               | Autentica e retorna um token JWT                 |
| POST   | `/transferencias`           | Realiza uma transferência entre dois usuários    |
| GET    | `/usuarios/{id}`            | Detalha um usuário e o saldo da carteira         |
| GET    | `/usuarios/{id}/transacoes` | Lista o histórico de transações do usuário       |

A lista completa e atualizada de endpoints, com exemplos de payload, está disponível no Swagger UI.

## Testes

```bash
./mvnw test
```

Os testes de integração usam Testcontainers e exigem o Docker em execução, já que sobem instâncias reais de PostgreSQL e Kafka durante a suíte.

## CI/CD

O repositório possui um pipeline configurado em GitHub Actions (`.github/workflows`) que executa build e testes automaticamente a cada push.

## Roteiro de desenvolvimento

O projeto foi construído em fases incrementais, da regra de negócio core até a resiliência e observabilidade. O detalhamento completo de cada fase está documentado em `docs/`.

## Autor

**Davi Asafe dos Santos Kling**
Estudante de Engenharia de Software (ênfase em IA) — Infnet
[LinkedIn](https://linkedin.com/in/) · [GitHub](https://github.com/davi0416) · [Portfólio](https://davi0416.github.io)

## Licença

Este projeto está sob a licença MIT — veja o arquivo [LICENSE](LICENSE) para mais detalhes.
