# TCGHub — Requisitos de Software

Plataforma web de comunidade, catálogo e comércio de cardgames desenvolvida com **Java 25**, **Spring Boot 4.1.1**, **JPA/PostgreSQL**, **React 19.3.0**, **Vite**, **TanStack Query**, **React Router** e **Framework D5**.

## Estrutura do Repositório
- `tcghub/` — Monorepo Maven (`dominio-compartilhado`, `dominio-adm`, `dominio-comunidade`, `dominio-loja`, `aplicacao`, `infraestrutura`, `ui-backend`, `ui-frontend`)
- `docs/` — Artefatos de DDD Preliminar, Context Mapper, protótipos, planos, relatórios de auditoria e registros de interação com IA
- `.agents.md` e `.agents/skills/` — Orquestração e skills especializadas dos agentes

## Execução do Ambiente Local

### 1. Backend (`ui-backend`) + Banco de Dados (`PostgreSQL`) via Docker
Na raiz do repositório (`Requisitos_de_Software/`):

```bash
docker compose -f tcghub/compose.yaml up --build
```

- Sobe o banco relacional **PostgreSQL** (`5432`) e o backend Spring Boot (`8080`) com o profile `docker`.
- Endpoints disponíveis em `http://localhost:8080` (ex.: `http://localhost:8080/v3/api-docs` e `http://localhost:8080/backend/autenticacao`).

### 2. Frontend (`React` + `Vite`) via `pnpm dev`
Em `tcghub/ui-frontend/src/main/react`:

```bash
cd tcghub/ui-frontend/src/main/react
pnpm install
pnpm dev
```

- Executa o servidor de desenvolvimento Vite na porta **`4200`** (`http://localhost:4200`), encaminhando automaticamente chamadas `/backend` para `http://localhost:8080` via proxy configurado em `vite.config.ts`.

### 3. Testes Automatizados
- **Suítes BDD / Cucumber (H2 / Mockito)** em `tcghub/`:
  ```bash
  mvn test -pl dominio-adm,dominio-comunidade,dominio-loja
  ```
- **Testes e build do Frontend** em `tcghub/ui-frontend/src/main/react`:
  ```bash
  pnpm test
  pnpm build
  ```
- **Alternativa D5 em memória (sem Docker)** em `tcghub/`:
  ```bash
  mvn spring-boot:test-run -pl ui-backend
  ```
