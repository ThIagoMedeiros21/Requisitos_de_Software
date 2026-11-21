# Relatório de Auditoria — Etapa 3.0: Ambiente Docker (`PostgreSQL` + `ui-backend`), `pnpm dev` e Complemento de Governança

**Parecer Final:** **APROVADO**  
**Escopo Auditado:** Configuração Docker para execução de `tcghub/ui-backend` com banco relacional **PostgreSQL 17**, suporte a `pnpm dev` no frontend (`tcghub/ui-frontend/src/main/react`) e complemento de [tcghub/AGENTS.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/AGENTS.md) / [.agents.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents.md) / [README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/README.md) (sem código de negócio de `F001`).  
**Responsável pela Auditoria:** `Supervisor`

---

## 1. Resumo Executivo

Todas as entregas da preparação de ambiente e governança foram verificadas diretamente pelo `Supervisor` e **aprovadas**:

- **`tcghub/AGENTS.md` complementado sem orquestração privada**: incorpora todas as diretrizes técnicas, arquiteturais, de stack (`Java 25`, `Spring Boot 4.1.1`, `JPA/PostgreSQL`, `H2` em `@DataJpaTest`, `React 19.3.0 + Vite 8.3.0 + Tailwind CSS + TanStack Query 5.104.0 + React Router 8.5.0 + Zustand`, `Context Mapper 6.12.0`, `DBML 10.1.1`, `Cucumber 7.34.8`, `Mockito 5.23.0`, `@NullMarked`), Clean Architecture + 4 níveis DDD, Trava GoF completa, funcionalidades completas, Conventional Commits em inglês, governança de IA e comandos de execução (`docker compose` + `pnpm dev`), **sem** incluir orquestração de enxame ou links para `../.agents/skills/`.
- **Ambiente Docker (`PostgreSQL 17` + `ui-backend`) funcional e verificado ao vivo**: subida validada com `docker compose -f tcghub/compose.yaml`, ambos serviços (`db` e `backend`) `Healthy`, conexão JDBC real ao PostgreSQL via HikariCP (`PostgreSQLDialect`), endpoints HTTP `/v3/api-docs` (`200`) e `/backend/autenticacao` (`401` anônimo / `200` autenticado) respondendo corretamente e ambiente encerrado com `docker compose down`.
- **Frontend `pnpm dev` pronto**: `"dev": "vite"` adicionado em `package.json`, `pnpm test` (`11/11` passando) e `pnpm build` (`466` módulos) executados com sucesso pelo `Supervisor`, mantendo `node_modules/` e `dist/` ignorados no Git.
- **Restrição de escopo**: zero código de negócio da Etapa 3.1 (`F001`) criado; zero instalação prematura de Tailwind/Zustand antes da Etapa 3.1.

---

## 2. Auditoria de Governança e Documentação

| Artefato | Verificação Realizada | Status |
| :--- | :--- | :---: |
| [tcghub/AGENTS.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/AGENTS.md) | Contém visão geral e distribuição das 12 funcionalidades (Lucas, Ricardo, Thiago), stack completa atualizada (incluindo Vite/Tailwind/Zustand/PostgreSQL/Docker/`pnpm dev`), 4 níveis DDD, Clean Architecture, Trava GoF completa, Conventional Commits em inglês e registros de IA. Verificação via `grep` confirmou **zero** menções a enxame/orquestração multiagente ou `../.agents/skills/`. | Aprovado |
| [.agents.md](file:///Users/menezes/faculdade/Requisitos_de_Software/.agents.md) | Mantém orquestração exclusiva de Lucas e documenta execução padrão (`docker compose -f tcghub/compose.yaml up --build` e `pnpm dev`). | Aprovado |
| [README.md](file:///Users/menezes/faculdade/Requisitos_de_Software/README.md) | Guia rápido na raiz para subida Docker (`8080`/`5432`), `pnpm dev` (`4200` com proxy `/backend`) e suítes de testes. | Aprovado |
| [.gitignore](file:///Users/menezes/faculdade/Requisitos_de_Software/.gitignore) | Ignora `**/target/`, `.env`, `.env.*` (preservando `!.env.example`), `.DS_Store`, `*.log`, `.idea/`, `*.iml`. | Aprovado |

---

## 3. Auditoria de Infraestrutura Docker (`PostgreSQL` + `ui-backend`)

### 3.1 Arquivos Auditados
- [tcghub/infraestrutura/pom.xml](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/infraestrutura/pom.xml): dependência `org.postgresql:postgresql` (`<scope>runtime</scope>`), preservando `d5-framework-infraestrutura-teste` (`H2`) para `@DataJpaTest`.
- [tcghub/ui-backend/src/main/resources/application.properties](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/ui-backend/src/main/resources/application.properties): `spring.application.name=tcghub`.
- [tcghub/ui-backend/src/main/resources/application-docker.properties](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/ui-backend/src/main/resources/application-docker.properties): `jdbc:postgresql://db:5432/tcghub`, `org.postgresql.Driver`, `ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:update}`, `spring.jpa.open-in-view=false`, `server.port=8080`.
- [tcghub/Dockerfile](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/Dockerfile): multi-stage (`maven:3.9-eclipse-temurin-25` compilando `mvn -B -pl ui-backend -am -DskipTests package` + runtime `eclipse-temurin:25-jre-alpine` com `curl` e `SPRING_PROFILES_ACTIVE=docker`).
- [tcghub/.dockerignore](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/.dockerignore): ignora `**/target/`, `.git/`, `**/node_modules/`, `ui-frontend/src/main/react/dist/`, `.env`, `.agents/`, `docs/`.
- [tcghub/compose.yaml](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/compose.yaml) e [tcghub/.env.example](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/.env.example): `db` (`postgres:17-alpine`, volume `tcghub_pgdata`, healthcheck `pg_isready`) e `backend` (`depends_on: db: condition: service_healthy`, healthcheck `curl -fsS http://localhost:8080/v3/api-docs`), portas padrão `5432`/`8080` parametrizáveis via `POSTGRES_PORT` / `BACKEND_PORT`.

### 3.2 Resultados Reais Executados pelo `Supervisor`
> Nota operacional: durante a auditoria, portas `5432`/`8080` do host estavam ocupadas por outro projeto ativo (`kotoro-api`), portanto a validação ao vivo do `compose.yaml` utilizou `POSTGRES_PORT=15432 BACKEND_PORT=18080`, mantendo `5432`/`8080` como padrões em `compose.yaml` e `.env.example`.

1. `docker compose -f tcghub/compose.yaml config --quiet` → exit **`0`**.
2. `POSTGRES_PORT=15432 BACKEND_PORT=18080 docker compose -f tcghub/compose.yaml up -d --wait` → ambos os serviços saudáveis:
   - `tcghub-db-1`: `Up ... (healthy)` (`postgres:17-alpine`)
   - `tcghub-backend-1`: `Up ... (healthy)` (`tcghub-backend`)
3. Logs do container `backend` confirmando PostgreSQL ativo:
   - `HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection`
   - `Database driver: PostgreSQL JDBC Driver`
   - `Database dialect: PostgreSQLDialect`
   - `Started BackendAplicacao in 1.773 seconds`
4. Requisições HTTP reais contra o container `backend`:
   - `GET /v3/api-docs` → **`HTTP 200`**
   - `GET /backend/autenticacao` (sem autenticação) → **`HTTP/1.1 401`** com payload `{"mensagem":"Usuário não autenticado."}`
   - `GET /backend/autenticacao` (Basic Auth `u:u`) → **`HTTP/1.1 200`** com payload `{"usuario":"u"}`
5. `docker compose -f tcghub/compose.yaml down` → containers `tcghub-backend-1`, `tcghub-db-1` e rede `tcghub_default` removidos com sucesso.
6. `mvn -f tcghub/pom.xml test-compile -pl dominio-compartilhado,dominio-adm,dominio-comunidade,dominio-loja,aplicacao,infraestrutura,ui-backend` → **`BUILD SUCCESS`** (`0.691 s`).

---

## 4. Auditoria de Frontend (`pnpm dev`)

- [tcghub/ui-frontend/src/main/react/package.json](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/ui-frontend/src/main/react/package.json): contém `"dev": "vite"` em `scripts`, mantendo `"start"`, `"build"`, `"preview"`, `"test"` e `"test:watch"`.
- Execuções diretas em `tcghub/ui-frontend/src/main/react`:
  - `pnpm test` (`vitest run` v5.1.1): **3 arquivos / 11 testes passaram (11/11)** (`api.test.ts` 7/7, `ErroRota.test.tsx` 2/2, `App.test.tsx` 2/2).
  - `pnpm build` (`tsc -b && vite build` v8.3.8): **`466 modules transformed`**, **`built in 176ms`**.
- `git status -u --short`: `node_modules/` e `dist/` permanecem devidamente ignorados; nenhum código de `F001` ou dependência prematura de Tailwind/Zustand foi adicionada.

---

## 5. Próximo Passo

- Acionar `@repo-guardian` para consolidar os commits incrementais em inglês (Conventional Commits, sem `git push`).
