# Relatório de Auditoria — Etapa 3.1: Reestruturação do Frontend Standalone (`/frontend/`), Design System Stitch, Cadastro/Login Real e Contratos `optcgapi.com` / `animethemes.moe`

**Parecer Final:** **APROVADO**  
**Escopo Auditado:** Desacoplamento do frontend para `/frontend/` na raiz do repositório (porta padrão Vite `5173`), eliminação total de Bootstrap/Angular, adoção de organização feature-based padrão de mercado React, implementação visual 100% alinhada ao Design System **Nautical Blue & White Marketplace** e aos protótipos do **Stitch** (`projects/18247718655827442438`), **Cadastro de Conta (`/cadastro` e `POST /backend/autenticacao/cadastro`) e Login Real persistido no banco relacional (`USR_USUARIO_CONTA` + criação automática de `PerfilComunitario`)** e alinhamento dos contratos de cartas e aberturas com **`https://optcgapi.com`** e **`https://api.animethemes.moe`**.  
**Responsável pela Auditoria:** `Supervisor`

---

## 1. Resumo Executivo

Todas as entregas e travas de governança foram verificadas diretamente pelo `Supervisor` e **aprovadas**:

1. **Checklist 1 — Separação `/frontend/` vs. `tcghub/` e Ambiente Docker Ativo**:
   - `/frontend/` criado na raiz do repositório de forma independente do reator Maven (porta `5173` com proxy `/backend` → `http://localhost:18080`); `/tcghub/ui-frontend/` completamente removido.
   - `<module>ui-frontend</module>` removido de [tcghub/pom.xml](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/pom.xml), `COPY ui-frontend/pom.xml` removido de [tcghub/Dockerfile](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/Dockerfile), e `**/node_modules/` + `**/dist/` + `.env` ignorados em [.gitignore](file:///Users/menezes/faculdade/Requisitos_de_Software/.gitignore) e [tcghub/.dockerignore](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/.dockerignore).
   - Containers Docker `tcghub-db-1` (`postgres:17-alpine`, `:15432`) e `tcghub-backend-1` (`tcghub-backend`, `:18080`) mantidos em execução **`Up (healthy)`**, respondendo tanto diretamente em `http://localhost:18080/backend/autenticacao` quanto via proxy Vite em `http://localhost:5173/backend/autenticacao` (`HTTP/1.1 200 OK`).
2. **Checklist 2 — 100% React Moderno (Zero Angular / Zero Bootstrap)**:
   - Removidas as dependências `bootstrap` e `react-bootstrap` de [frontend/package.json](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/package.json); removidos `src/app/`, `src/d5/`, `src/estilos.css`, `src/configuracao-testes.ts` e `public/favicon.ico`.
   - `grep -riE "bootstrap|favicon\.ico"` em `frontend/` e `tcghub/` retornou **`0` ocorrências**.
   - Criado [frontend/public/favicon.svg](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/public/favicon.svg) náutico (`#0A2540` / `#0F4C81` / `#2563EB`) e atualizado [frontend/index.html](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/index.html) (`lang="pt-BR"`, fontes `Montserrat`, `Inter` e `Material Symbols Outlined`).
3. **Checklist 3 — Organização Padrão de Mercado React (`feature-based`)**:
   - Árvore estruturada em `frontend/src/{styles,lib,stores,components/{layout,ui},features/{auth,community/{profile,friends}},routes}` (`48` arquivos).
   - Rotas limpas em [frontend/src/routes/index.tsx](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/src/routes/index.tsx): `/login`, `/cadastro`, `/` → `/comunidade/perfil`, `/comunidade/perfil`, `/comunidade/perfil/:id`, `/comunidade/amigos`, `/comunidade/colecionadores` (com redirecionamentos `<Navigate>` das rotas D5 antigas).
   - `pnpm --dir frontend test` (`6` arquivos / **`20/20` testes passando**) e `pnpm --dir frontend build` (**`183` módulos transformados** em `96ms`, CSS final de `46.26 kB` contendo os tokens Stitch compilados pelo Tailwind CSS v4).
4. **Checklist 4 — Design System Stitch, Cadastro/Login Real (`USR_USUARIO_CONTA`), Contratos Externos e Governança**:
   - **Cadastro e Login Real em Banco Relacional**:
     - `UsuarioConta` e `IUsuarioContaRepositorio` em `dominio-compartilhado`; `UsuarioContaJpa` (`USR_USUARIO_CONTA`: `USR_ID`, `USR_EMAIL`, `USR_SENHA_HASH`, `USR_AUTORIZACAO`, `USR_CRIADO_EM`), `IUsuarioContaRepositorioJpa` e `UsuarioContaRepositorio` em `infraestrutura/comunidade/conta` (com `@NullMarked` e `UsuarioContaRepositorioTest` `@DataJpaTest`).
     - `SegurancaConfiguracao` autentica contra o banco relacional por `usuario` ou `email` (*case-insensitive*); `POST /backend/autenticacao/cadastro` valida `usuario`, `apelido` (`RN001`), `email` e `senha`, grava a conta com hash de senha e cria automaticamente o `PerfilComunitario` em `PCM_PERFIL_COMUNITARIO`.
     - Tela [RegisterPage.tsx](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/src/features/auth/pages/RegisterPage.tsx) (`/cadastro`) e [LoginPage.tsx](file:///Users/menezes/faculdade/Requisitos_de_Software/frontend/src/features/auth/pages/LoginPage.tsx) (`/login`) no padrão Stitch *Nautical Blue & White Marketplace*, com login automático após cadastro e acesso rápido de demonstração (`Lucas`, `Ricardo`, `Thiago`).
   - **Contratos Oficiais de APIs Externas**:
     - **One Piece TCG API (`https://optcgapi.com`)**: `CartaColecaoResumo`, `ColecaoCartaConsultaInfra`, `PerfilComunitarioControlador` e `frontend/src/features/community/profile/*` expõem e consomem `card_set_id`, `card_name`, `set_id`, `set_name`, `rarity`, `card_color`, `card_type`, `card_cost`, `card_power`, `sub_types`, `counter_amount`, `attribute`, `market_price`, `inventory_price`, `card_image_id` e `card_image` (`https://optcgapi.com/media/static/Card_Images/*.jpg`), com fallback SVG náutico `onError`.
     - **AnimeThemes API (`https://api.animethemes.moe`)**: documentada em [tcghub/docs/adm/funcionalidades/F001.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/docs/adm/funcionalidades/F001.md) e [tcghub/docs/comunidade/funcionalidades/F004.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/docs/comunidade/funcionalidades/F004.md).
   - **Trava GoF preservada**: **`0` ocorrências** de `Observer`, `AmizadeAdicionada` ou `AmizadeRemovida` no código de `frontend/` e `tcghub/` (apenas `Proxy` exclusivo de Lucas + `Template Method` transversal D5).
   - [tcghub/AGENTS.md](file:///Users/menezes/faculdade/Requisitos_de_Software/tcghub/AGENTS.md) permanece livre de qualquer menção a enxame multiagente ou `../.agents/skills/`.

---

## 2. Resultados Reais Executados pelo `Supervisor`

### 2.1 Testes Maven (`JAVA_HOME="/Applications/IntelliJ IDEA.app/Contents/jbr/Contents/Home"`)
1. `mvn -f tcghub/pom.xml test -pl dominio-comunidade -Dcucumber.features="src/test/resources/features/perfil-e-amigos.feature"`:
   - **`Tests run: 26, Failures: 0, Errors: 0, Skipped: 0`** (`BUILD SUCCESS`).
2. `mvn -f tcghub/pom.xml test -Dcucumber.execution.dry-run=true -pl dominio-adm,dominio-comunidade,dominio-loja`:
   - **`143/143`** (`19` em `dominio-adm`, `82` em `dominio-comunidade`, `42` em `dominio-loja`, `BUILD SUCCESS`).
3. `mvn -f tcghub/pom.xml test -pl dominio-compartilhado,aplicacao,infraestrutura,ui-backend`:
   - `infraestrutura` (`@DataJpaTest`): **`9/9`** (`PerfilComunitarioAplicacaoRepositorioTest` `2`, `PerfilComunitarioRepositorioTest` `4`, `AmizadeRepositorioTest` `1`, `AmizadeAplicacaoRepositorioTest` `1`, `UsuarioContaRepositorioTest` `1`).
   - `ui-backend` (`ComunidadeControladoresTest`): **`4/4`** (incluindo cadastro real `POST /backend/autenticacao/cadastro`, login por usuário e por e-mail, criação automática do perfil, rejeição de duplicidades e contrato `optcgapi.com`).
   - Reactor total: **`BUILD SUCCESS`** (`7.598 s`).

### 2.2 Testes e Build Frontend (`/frontend/`)
- `pnpm test` (`vitest run` v5.1.1):
  - `useProfileUiStore.test.ts` (`3/3`), `api.test.ts` (`7/7`), `ErrorFallback.test.tsx` (`2/2`), `LoginPage.test.tsx` (`3/3`, incluindo fluxo de cadastro `/cadastro`), `FriendsNetworkPage.test.tsx` (`2/2`), `ProfilePage.test.tsx` (`3/3`).
  - Total: **`Test Files 6 passed (6)` / `Tests 20 passed (20)`** (`1.27s`).
- `pnpm build` (`tsc -b && vite build` v8.3.8):
  - **`183 modules transformed`**, **`✓ built in 96ms`**.

### 2.3 Verificação HTTP ao Vivo (`Docker PostgreSQL 17` + `ui-backend :18080` + `Vite :5173`)
- `docker compose --env-file tcghub/.env -f tcghub/compose.yaml ps`:
  - `tcghub-db-1`: **`Up (healthy)`** (`0.0.0.0:15432->5432/tcp`)
  - `tcghub-backend-1`: **`Up (healthy)`** (`0.0.0.0:18080->8080/tcp`)
- `GET http://localhost:18080/backend/autenticacao` (Basic Auth por e-mail `lucas@tcghub.com:lucas`): **`HTTP/1.1 200`** retornando `{"usuario":"lucas", ...}`.
- `GET http://localhost:5173/backend/autenticacao` (via proxy Vite com `lucas:lucas`): **`HTTP/1.1 200 OK`**.
- `GET http://localhost:18080/backend/comunidade/perfil-comunitario/visualizacao` (`lucas:lucas`): **`HTTP/1.1 200`** retornando `card_set_id: "OP01-001"`, `card_name: "Roronoa Zoro"`, `set_name: "Romance Dawn"`, `card_type: "Leader"`, `card_power: "5000"`, `card_image: "https://optcgapi.com/media/static/Card_Images/OP01-001.jpg"`, etc.
- `POST http://localhost:18080/backend/autenticacao/cadastro` (usuário duplicado `lucas`): **`HTTP/1.1 409`** com `{"mensagem":"O usuário lucas já está cadastrado"}`.
- **Containers mantidos ativos (`up -d`)** para uso direto no navegador.

---

## 3. Próximo Passo

- Acionar `@repo-guardian` para consolidar as entregas em commits pequenos e incrementais em inglês no padrão Conventional Commits (sem commitar `tcghub/.env` e sem `git push`).
