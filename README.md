# TCGHub — Requisitos de Software

Plataforma web de comunidade, catálogo e comércio de cardgames desenvolvida com **Java 25**, **Spring Boot 4.1.1**, **JPA/PostgreSQL** e frontend standalone em **React 19.3.0**, **Vite**, **Tailwind CSS v4** (Design System *Nautical Blue & White Marketplace*), **TanStack Query**, **React Router** e **Zustand**.

## Estrutura do Repositório
- `tcghub/` — Monorepo Maven do backend e domínio (`dominio-compartilhado`, `dominio-adm`, `dominio-comunidade`, `dominio-loja`, `aplicacao`, `infraestrutura`, `ui-backend`)
- `frontend/` — Aplicação web standalone em React + Vite + Tailwind CSS v4 (desacoplada do reator Maven)
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
Em `frontend/` (na raiz do repositório):

```bash
cd frontend
pnpm install
pnpm dev
```

- Executa o servidor de desenvolvimento Vite na porta **`5173`** (`http://localhost:5173`), encaminhando automaticamente chamadas `/backend` para `http://localhost:8080` via proxy configurado em `frontend/vite.config.ts`.

### 3. Testes Automatizados
- **Suítes BDD / Cucumber (H2 / Mockito)** em `tcghub/`:
  ```bash
  mvn test -pl dominio-adm,dominio-comunidade,dominio-loja
  ```
- **Testes e build do Frontend** em `frontend/`:
  ```bash
  pnpm test
  pnpm build
  ```
- **Alternativa backend em memória (sem Docker)** em `tcghub/`:
  ```bash
  mvn spring-boot:test-run -pl ui-backend
  ```
