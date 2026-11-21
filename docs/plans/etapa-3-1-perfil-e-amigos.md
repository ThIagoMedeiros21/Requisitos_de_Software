# Plano Técnico de Implementação — Etapa 3.1: `F001 — Perfil e amigos`

> [!IMPORTANT]
> Este plano consolida a arquitetura, os contratos e o plano de testes e entregas da **Etapa 3.1** (**exclusivamente `comunidade / F001 — Perfil e amigos`** de Lucas Menezes), em conformidade com [.agents.md](../../.agents.md), [tcghub/AGENTS.md](../../tcghub/AGENTS.md), [F001.md](../../tcghub/docs/comunidade/funcionalidades/F001.md), [comunidade/projeto.cml](../../tcghub/docs/comunidade/projeto.cml), [comunidade/projeto.dbml](../../tcghub/docs/comunidade/projeto.dbml) e [prototipos/README.md](../prototipos/README.md).
> **Restrição desta etapa**: Nenhuma linha de código Java ou React deve ser implementada antes da validação e aprovação deste plano pelo `@supervisor` e do aval para execução.

---

## 1. Escopo e Matriz de Rastreabilidade Completa

A funcionalidade **F001 — Perfil comunitário e rede de amigos** entrega operações completas de escrita, validação e leitura para os agregados **`PerfilComunitario`** e **`Amizade`** no subdomínio `comunidade`, cobrindo integralmente as **6 Regras de Negócio (`RN001`–`RN006`)**, os **26 cenários BDD** de [perfil-e-amigos.feature](../../tcghub/dominio-comunidade/src/test/resources/features/perfil-e-amigos.feature) e os **7 fluxos de tela** (`P01`–`P05`, `P19`–`P20`).

| Regra de Negócio | Cenários BDD (`perfil-e-amigos.feature`) | Protótipos (`docs/prototipos/README.md`) | Agregados / Camadas Impactadas |
| :--- | :--- | :--- | :--- |
| **RN001 — Apelido único (case-insensitive) e obrigatório** | Edição de perfil, manter próprio apelido, rejeição de apelido duplicado (`ZoroTC` vs. `zorotc`), rejeição de apelido vazio | `P01`, `P02` | `Apelido`, `PerfilComunitario`, `PerfilComunitarioServico`, `IPerfilComunitarioRepositorio`, `IPerfilComunitarioRepositorioJpa`, `PerfilEdicaoModal` |
| **RN002 — Edição exclusiva pelo dono e Proxy de visibilidade** | Consulta próprio perfil, consulta perfil público de terceiro, perfil inexistente, cancelamento de edição preserva estado, rejeição de edição por terceiro | `P01`, `P02`, `P03` | `PerfilComunitarioVisibilidadeProxy`, `PerfilComunitarioControlador`, `PerfilVisualizacao`, `PerfilEdicaoModal` |
| **RN003 — Destaques de até 4 cartas favoritas** | Fixar carta da coleção, limite máximo de 4, sem duplicata, remoção libera espaço | `P03`, `P04`, `P05` | `PerfilComunitario`, `IColecaoCartaConsulta`, `PerfilComunitarioServico`, `PerfilComunitarioJpa`, `PerfilCartaFavoritaJpa`, `CartasFavoritasGrid`, `SelecaoCartaFavoritaModal` |
| **RN004 — Validade da solicitação de amizade** | Envio válido, bloqueio de auto-solicitação, usuário inexistente, solicitação duplicada, amizade já ativa | `P19` | `Amizade`, `AmizadeServico`, `IAmizadeRepositorio`, `AmizadeInclusao` |
| **RN005 — Autorização de aceite, recusa e cancelamento** | Aceitar estabelece amizade bilateral, bloqueio de aceite por terceiro, recusar encerra sem amizade, cancelar pelo solicitante, bloqueio de cancelamento por terceiro | `P20` | `Amizade`, `StatusAmizade`, `AmizadeServico`, `AmizadeControlador`, `AmizadePesquisa` |
| **RN006 — Remoção bilateral de amizade** | Remover amigo apaga vínculo dos dois lados, rejeição ao tentar remover quem não é amigo | `P20` | `AmizadeServico`, `IAmizadeRepositorio`, `AmizadeControlador`, `AmizadePesquisa` |

---

## 2. Arquitetura D5 e Trava Rígida de Padrões GoF

### 2.1 Padrão Exclusivo de Lucas Menezes: **Proxy** *(ZERO `Observer`)*
- **`IPerfilComunitarioVisibilidadeProxy` / `PerfilComunitarioVisibilidadeProxy`** (subdomínio `comunidade`, pacote `dominio.comunidade.perfil`):
  - **Subject / RealSubject**: `PerfilComunitarioServico` (estende `AgregadoServicoAbstrato`, que mantém as regras de negócio puras do agregado).
  - **Papel de Proxy de Proteção e Visibilidade**:
    1. **`consultarPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo)`**: verifica existência do alvo, compara `solicitante.equals(alvo)` e entrega visão completa ao proprietário (incluindo `cartasColecaoDisponiveis` obtidas via `IColecaoCartaConsulta` e controles de edição) ou visão pública filtrada ao visitante (omitindo ações/dados privados conforme flags de visibilidade).
    2. **`editarPerfil(UsuarioIdentificador solicitante, PerfilComunitario agregado)`**: intercepta a chamada antes do serviço de domínio e lança `NaoAutorizadoExcecao` se `!solicitante.equals(agregado.getUsuario())`.
    3. **`fixarCartaFavorita(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, CartaIdentificador carta)`** e **`removerCartaFavorita(...)`**: garantem que apenas o dono (`solicitante.equals(alvo)`) alcance `PerfilComunitarioServico`.
  - **Registro Spring D5**: como `AgregadoRegistrador` do D5 registra automaticamente apenas beans de `IAgregadoRepositorio`, `IAgregadoAplicacaoRepositorio`, `IAgregadoServico` e `IAgregadoAplicacaoServico` (e para evitar colisão de dois beans `IAgregadoServico` para `PerfilComunitario`), `PerfilComunitarioVisibilidadeProxy` permanece **Java puro** em `dominio-comunidade` (zero anotações Spring) e é exposto via `@Bean` em uma `@Configuration` dedicada no módulo `ui-backend`.

### 2.2 Padrão Transversal Compartilhado: **Template Method**
- Utilizado exclusivamente pelas superclasses base do framework **D5**:
  - `AgregadoServicoAbstrato` (`incluir`, `buscar`, `alterar`, `excluir` → ganchos `criarAutorizacoes`, `validarInclusao`, `validarInclusaoAlteracao`, `validarAlteracao`, `validarExclusao`);
  - `AgregadoRepositorioAbstrato` (conversões `paraJpa`, `paraAgregado`, `paraIdentificadorJpa`);
  - `AgregadoAplicacaoRepositorioAbstrato` (pesquisa paginada com `paraLike` e `paraPropriedadeOrdenacao`).

### 2.3 Isolamento de Subdomínios e `@NullMarked`
- `dominio-comunidade` depende apenas de `dominio-compartilhado` (`Autorizacao`, `UsuarioIdentificador`), sem ciclos com `adm` e sem acesso a `loja`.
- A verificação de posse na coleção para fixação de destaques (`RN003`) usa uma SPI leve `IColecaoCartaConsulta` (`usuarioPossuiCarta(UsuarioIdentificador, CartaIdentificador)` e `listarCartasColecao(UsuarioIdentificador)`), com implementação inicial desacoplada na camada `infraestrutura` (sem depender do agregado `ColecaoPessoal` até sua futura implementação ou integrando via consulta segura).
- **Todos** os novos pacotes Java em `src/main/java` devem conter `package-info.java` com `@NullMarked` (`org.jspecify.annotations.NullMarked`), respeitando o verificador NullAway `0.14.1` (`checking=MAIN`) e as regras de `programar-defensivamente/SKILL.md`.

---

## 3. Inventário de Arquivos e Contratos por Módulo

### 3.1 `tcghub/dominio-compartilhado`
- `com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador` (`extends ObjetoValorStringAbstrato`, `TAMANHO = 100`, compatível com `PCM_USR_ID` e `AMI_*_ID`).

### 3.2 `tcghub/dominio-comunidade`
- **Pacote `com.cesar.tgchub.tcghub.dominio.comunidade.perfil`** (`package-info.java` `@NullMarked`):
  - Objetos de valor: `PerfilComunitarioIdentificador` (`extends ObjetoValorStringAbstrato implements IIdentificador`), `Apelido` (`extends ObjetoValorStringAbstrato`, `TAMANHO = 50`, `Validate.notBlank`), `CartaIdentificador` (`extends ObjetoValorStringAbstrato`, `TAMANHO = 100`), `CartaColecaoResumo` (VO imutável para transporte da coleção elegível em `P05`), `PerfilComunitarioVisao` (VO retornado pelo Proxy diferenciando `ehProprietario`, `statusAmizade`, `amizadeId` e visibilidade).
  - Entidade raiz: `PerfilComunitario implements IAgregado<PerfilComunitarioIdentificador>` (`Optional<PerfilComunitarioIdentificador> identificador`, `UsuarioIdentificador usuario`, `Apelido apelido`, `Optional<String> cidade`, `Optional<String> descricao`, `Optional<String> foto`, `Optional<String> preferenciasPublicas`, `boolean decksFavoritosVisiveis`, `List<CartaIdentificador> cartasFavoritas` via `List.copyOf` com `MAX_CARTAS_FAVORITAS = 4` e métodos ricos `editarDados(...)`, `fixarCartaFavorita(CartaIdentificador)`, `removerCartaFavorita(CartaIdentificador)`, `alterarVisibilidadeDecksFavoritos(boolean)`).
  - Autorizações: `PerfilComunitarioAutorizacoes extends AgregadoAutorizacoes<PerfilComunitario>` (incluindo `fixarDestaque`, `removerDestaque`).
  - Serviço (RealSubject): `PerfilComunitarioServico extends AgregadoServicoAbstrato<PerfilComunitarioIdentificador, PerfilComunitario, PerfilComunitarioAutorizacoes>` com constantes públicas `IDENTIFICADOR_INEXISTENTE_MODELO`, `IDENTIFICADOR_EXISTENTE_MODELO`, `APELIDO_EXISTENTE_MODELO`, `LIMITE_DESTAQUES_MODELO`, `CARTA_DUPLICADA_DESTAQUE_MODELO`, `CARTA_NAO_POSSUIDA_MODELO`, `CARTA_DESTAQUE_INEXISTENTE_MODELO`.
  - Proxy GoF: `IPerfilComunitarioVisibilidadeProxy` e `PerfilComunitarioVisibilidadeProxy`.
  - SPI (`spi/` com `package-info.java` `@NullMarked`):
    - `IPerfilComunitarioRepositorio extends IAgregadoRepositorio<PerfilComunitarioIdentificador, PerfilComunitario>` (`existePorUsuario`, `buscarPorUsuario`, `existePorApelido(Apelido, Optional<PerfilComunitarioIdentificador>)`);
    - `IColecaoCartaConsulta` (`usuarioPossuiCarta`, `listarCartasDisponiveis`).
- **Pacote `com.cesar.tgchub.tcghub.dominio.comunidade.amizade`** (`package-info.java` `@NullMarked`):
  - VO/Enum: `AmizadeIdentificador` (`extends ObjetoValorStringAbstrato implements IIdentificador`), `StatusAmizade { PENDENTE, ACEITA }`.
  - Entidade raiz: `Amizade implements IAgregado<AmizadeIdentificador>` (`Optional<AmizadeIdentificador> identificador`, `UsuarioIdentificador solicitante`, `UsuarioIdentificador destinatario`, `StatusAmizade status`, `Instant criadoEm`, `Optional<Instant> atualizadoEm`; métodos `aceitar(Instant)`, `validarRecusaOuCancelamento`, `estaAceita()`).
  - Autorizações: `AmizadeAutorizacoes extends AgregadoAutorizacoes<Amizade>` (`aceitar`, `recusar`, `cancelar`, `remover`).
  - Serviço: `AmizadeServico extends AgregadoServicoAbstrato<AmizadeIdentificador, Amizade, AmizadeAutorizacoes>`: `enviarSolicitacao`, `aceitarSolicitacao`, `recusarSolicitacao`, `cancelarSolicitacao`, `removerAmizadeBilateral`.
  - SPI (`spi/` com `package-info.java` `@NullMarked`): `IAmizadeRepositorio extends IAgregadoRepositorio<AmizadeIdentificador, Amizade>` (`existeRelacaoEntre`, `buscarRelacaoEntre`, `listarAmizadesAtivas`).

### 3.3 `tcghub/aplicacao`
- **`com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil`** (+ `.spi`, com `@NullMarked`):
  - `PerfilComunitarioProjecao implements IProjecao<PerfilComunitario>` (construtor com VOs + construtor com tipos JPA para `select new`), `PerfilComunitarioOrdenacao implements IOrdenacao<PerfilComunitarioProjecao>`, `PerfilComunitarioConsulta extends Consulta<PerfilComunitarioOrdenacao>` (filtro por apelido/cidade para `P19`), `IPerfilComunitarioAplicacaoRepositorio`, `PerfilComunitarioAplicacaoServico`.
- **`com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade`** (+ `.spi`, com `@NullMarked`):
  - `AmizadeProjecao implements IProjecao<Amizade>`, `AmizadeOrdenacao`, `AmizadeConsulta extends Consulta<AmizadeOrdenacao>` (filtro por usuário e `StatusAmizade`), `IAmizadeAplicacaoRepositorio`, `AmizadeAplicacaoServico`.

### 3.4 `tcghub/infraestrutura`
- **`com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil`** (`@NullMarked`):
  - `PerfilComunitarioJpa implements IAgregadoJpa<PerfilComunitario>` (`@Table(name = "PCM_PERFIL_COMUNITARIO")`) com colunas `@NotNull`/`@Size` alinhadas ao DBML e `@OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true) @OrderBy("indice ASC")`.
  - `PerfilCartaFavoritaJpa` (`@Table(name = "PCF_PERFIL_CARTA_FAVORITA")`, `PCF_ID` autoincrementado, `@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "PCM_ID")`, `PCF_INDICE`, `PCF_CARTA_ID`, `uniqueConstraints` `(PCM_ID, PCF_INDICE)` e `(PCM_ID, PCF_CARTA_ID)`).
  - `IPerfilComunitarioRepositorioJpa extends JpaRepository<PerfilComunitarioJpa, Long>` com query `lower(p.apelido) = lower(:apelido) and (:identificadorIgnorado is null or p.identificador <> :identificadorIgnorado)` para unicidade case-insensitive (`RN001`) e queries com `escape '\\'`.
  - `PerfilComunitarioRepositorio extends AgregadoRepositorioAbstrato<...>` (seguindo `ClienteRepositorio`: `NumberFormatException` → `AgregadoInexistenteExcecao` e `existePorIdentificador` retornando `false` para não numérico).
  - `PerfilComunitarioAplicacaoRepositorio extends AgregadoAplicacaoRepositorioAbstrato<...>`.
  - `ColecaoCartaConsultaInfra` (implementação SPI `IColecaoCartaConsulta`, sem `@Repository`).
- **`com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade`** (`@NullMarked`):
  - `AmizadeJpa implements IAgregadoJpa<Amizade>` (`@Table(name = "AMI_AMIZADE")`, unique `(AMI_SOLICITANTE_ID, AMI_DESTINATARIO_ID)`).
  - `IAmizadeRepositorioJpa`, `AmizadeRepositorio`, `AmizadeAplicacaoRepositorio`.

### 3.5 `tcghub/ui-backend`
- **Configuração e Segurança**:
  - `ComunidadeConfiguracao` (`@Configuration` registrando `@Bean PerfilComunitarioVisibilidadeProxy`).
  - `SegurancaConfiguracao`: manter `"a"` (`ADMINISTRADOR`) e `"u"` (`USUARIO`), acrescentando usuários de desenvolvimento multiusuário (`"lucas"`, `"ricardo"`, `"thiago"` com papel `USUARIO`) para validar amizades e proteção via Proxy em execução web.
  - `AutenticacaoRecurso` e `AutenticacaoControlador`: expor `perfilComunitario` (`PerfilComunitarioAutorizacoes`) e `amizade` (`AmizadeAutorizacoes`) em `GET /backend/autenticacao`.
- **`PerfilComunitarioControlador`** (`@RequestMapping(Backend.PREFIXO + "comunidade/perfil-comunitario")`):
  - `GET /visualizacao` e `GET /{identificador}/visualizacao`: retorna DTO filtrado pelo `PerfilComunitarioVisibilidadeProxy` (entregando `cartasColecaoDisponiveis` ao proprietário quando `ehProprietario = true`; `404 { mensagem }` se `:id` inexistente).
  - `GET /{identificador}/alteracao`: dados editáveis do perfil (`403 { mensagem }` se não for o proprietário).
  - `POST /{identificador}/alterar`: `{ apelido, cidade, descricao, foto, preferenciasPublicas, decksFavoritosVisiveis }` (`400`/`409` apelido inválido/duplicado; `403` terceiro).
  - `POST /{identificador}/fixar-destaque` e `POST /{identificador}/remover-destaque`: `{ cartaId }`.
- **`AmizadeControlador`** (`@RequestMapping(Backend.PREFIXO + "comunidade/amizade")`):
  - `GET /pesquisa`: `{ amigos, recebidas, enviadas }` do usuário autenticado.
  - `GET /inclusao?termo=`: colecionadores com `statusRelacao` (`NENHUMA | PENDENTE_ENVIADA | PENDENTE_RECEBIDA | AMIGO`) e `amizadeId`.
  - `GET /{identificador}/visualizacao`: detalhe da amizade/solicitação (`404` se inexistente).
  - `POST /incluir`: `{ destinatarioId }` (`RN004`).
  - `POST /{identificador}/aceitar`, `POST /{identificador}/recusar`, `POST /{identificador}/cancelar` (`RN005`).
  - `POST /{identificador}/remover`: remoção bilateral (`RN006`, alinhado ao contrato D5 Frontend/BFF).
- Todas as exceções de domínio/autorização retornam JSON `{ "mensagem": "..." }` compatível com `mensagemErro(erro)` do D5 Frontend; nenhum `@Transactional` manual (POST já é transacional via `PostMappingTransactionAttributeSource`).

### 3.6 `tcghub/ui-frontend/src/main/react`
- **Infraestrutura Frontend (`React 19.3.0 + Vite 8.3.0 + Tailwind CSS + TanStack Query 5.104.0 + React Router 8.5.0 + Zustand`)**:
  - `package.json`: adicionar `tailwindcss`, `@tailwindcss/vite` e `zustand` preservando todas as dependências D5 existentes.
  - `vite.config.ts`: acrescentar plugin `tailwindcss()` mantendo `server.port: 4200`, proxy `/backend` → `http://localhost:8080` e configuração `test` (`jsdom`, `globals: true`, `setupFiles: ['src/configuracao-testes.ts']`).
  - `src/estilos.css` + `src/main.tsx`: importar `./estilos.css` (`@import "tailwindcss";`) **após** `bootstrap/dist/css/bootstrap.min.css` para coexistência segura com os componentes em `src/d5/bootstrap/*`.
  - `src/app/App.tsx`: acrescentar navegação comunitária no menu condicionada por `autorizacoesDe('perfilComunitario')?.buscar` e `autorizacoesDe('amizade')?.buscar`.
- **Store Zustand (`src/app/comunidade/perfil/perfilStore.ts`)**:
  - Armazena **exclusivamente estado efêmero de UI** (cache remoto permanece 100% no `clienteConsultas` do TanStack Query): `abaAtiva`, `subAbaAmizade`, `modalEdicaoAberto`, `modalSlotAberto`, `slotSelecionado`, `alvoRemocaoAmizade`, `filtroColecionadores`, `filtroColecaoSlot` e suas ações + `resetarEstadoUi`.
- **Consultas TanStack Query (`chamarApi` + `queryOptions` + `useSuspenseQuery` + `useMutation` + `invalidateQueries`)**:
  - `src/app/comunidade/perfil/perfilConsultas.ts` e `src/app/comunidade/amizade/amizadeConsultas.ts` (invalidando `['comunidade', 'perfil-comunitario']` e `['comunidade', 'amizade']`).
- **Rotas em `src/app/rotas.tsx` (dentro de `RotaProtegida`, com `lazy` e `ErroRota`)**:
  - `comunidade/perfil-comunitario/visualizacao` (`PerfilVisualizacao` + `carregarProprioPerfil`)
  - `comunidade/perfil-comunitario/:id/visualizacao` (`PerfilVisualizacao` + `carregarPerfilPorId`)
  - `comunidade/perfil-comunitario/:id/alteracao` (`PerfilEdicao` + `carregarPerfilAlteracao`)
  - `comunidade/amizade/pesquisa` (`AmizadePesquisa` + `carregarRedeAmizades`)
  - `comunidade/amizade/inclusao` (`AmizadeInclusao` + `carregarColecionadores`)
  - `comunidade/amizade/:id/visualizacao` (`AmizadeVisualizacao` + `carregarAmizadePorId`)
- **Componentes (`1` componente por arquivo `.tsx`)**:
  - `src/app/comunidade/perfil/BarraNavegacaoComunidade.tsx`
  - `src/app/comunidade/perfil/PerfilVisualizacao.tsx` (`P01`, `P03`, `P04`)
  - `src/app/comunidade/perfil/PerfilCabecalhoCard.tsx`
  - `src/app/comunidade/perfil/CartasFavoritasGrid.tsx` (`P03`, `P04`)
  - `src/app/comunidade/perfil/SelecaoCartaFavoritaModal.tsx` (`P05`)
  - `src/app/comunidade/perfil/PerfilEdicaoModal.tsx` (`P02` com `React Hook Form`, contador `X / 200` e `Cancelar` sem mutação)
  - `src/app/comunidade/perfil/PerfilEdicao.tsx` (rota dedicada `:id/alteracao`)
  - `src/app/comunidade/amizade/AmizadePesquisa.tsx` (`P20` — abas `Meus Amigos`, `Recebidas`, `Enviadas` + modal bilateral)
  - `src/app/comunidade/amizade/AmizadeInclusao.tsx` (`P19` — buscar colecionadores e solicitar amizade)
  - `src/app/comunidade/amizade/AmizadeVisualizacao.tsx`

---

## 4. Plano de Testes e Verificação Objetiva

1. **Testes BDD (`dominio-comunidade`)**:
   - Implementar os 59 passos em [PerfilEAmigosStepDefinitions.java](../../tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/perfil/PerfilEAmigosStepDefinitions.java) usando `PerfilComunitarioServico`, `PerfilComunitarioVisibilidadeProxy`, `AmizadeServico` e mocks Mockito de `ISegurancaServico`, `IPerfilComunitarioRepositorio`, `IAmizadeRepositorio` e `IColecaoCartaConsulta`.
   - Critério de aceite: `mvn test -pl dominio-comunidade` com os **26 cenários de `perfil-e-amigos.feature` passando (`0` falhas/erros/pendentes)** e demais features da Onda 1 sem regressão.
2. **Testes de Infraestrutura JPA (`infraestrutura`)**:
   - `@DataJpaTest` em `PerfilComunitarioRepositorioTest`, `PerfilComunitarioAplicacaoRepositorioTest`, `AmizadeRepositorioTest` e `AmizadeAplicacaoRepositorioTest` (incluindo unicidade case-insensitive de `PCM_APELIDO`, ordem `PCF_INDICE`, queries `escape '\\'` e remoção bilateral).
3. **Testes Frontend (`Vitest` + Testing Library + `createMemoryRouter`)**:
   - `perfilStore.test.ts`, `PerfilVisualizacao.test.tsx`, `PerfilEdicaoModal.test.tsx`, `AmizadeInclusao.test.tsx`, `AmizadePesquisa.test.tsx` e manutenção de `App.test.tsx` (`npm test` e `npm run build` em `tcghub/ui-frontend/src/main/react`).
4. **Compilação Global Maven**:
   - `mvn test-compile` em todos os módulos Java + `mvn test -pl dominio-comunidade,infraestrutura,ui-backend`.

---

## 5. Estratégia de Commits Incrementais (Conventional Commits em Inglês)

Durante a execução da implementação (Etapa 3.1), o `@repo-guardian` consolidará cada fatia validada em commits pequenos e atômicos (sem `git push` automático):

1. `feat(comunidade): add domain model, SPIs, and visibility proxy for PerfilComunitario and Amizade` (`dominio-compartilhado`, `dominio-comunidade/src/main/java`)
2. `test(bdd): implement Cucumber StepDefinitions for F001 Perfil e amigos` (`PerfilEAmigosStepDefinitions.java`)
3. `feat(infraestrutura): add JPA entities and repositories for PerfilComunitario and Amizade` (`infraestrutura`)
4. `feat(aplicacao): add CQRS projections, queries, and application services for F001` (`aplicacao`)
5. `feat(ui-backend): add REST BFF controllers, proxy bean wiring, and security config for F001` (`ui-backend`)
6. `feat(ui-frontend): configure Tailwind and Zustand and implement F001 views and tests` (`ui-frontend`)
7. `docs(reports): add Stage 3.1 implementation audit report and AI interaction log` (`docs/reports/`, `docs/ai-interactions/`)

---

## 6. Evolução Arquitetural — Frontend Standalone (`/frontend`) e Design System Stitch

Para desacoplar integralmente a camada web do reator Maven (`tcghub/`) e alinhar a interface ao Design System oficial do projeto (**Nautical Blue & White Marketplace**, extraído do Stitch `projects/18247718655827442438`), a arquitetura frontend evolui de `tcghub/ui-frontend` para o projeto standalone **`/frontend/`** na raiz do repositório, **sem Bootstrap e sem Angular**:

- **Localização e Build**: `/frontend/` (React `19.3.0`, Vite `8.3.0`, Tailwind CSS v4 via `@tailwindcss/vite`, TanStack Query `5.104.0`, React Router `8.5.0`, Zustand, React Hook Form `7.90.0`, Vitest + Testing Library), executado com `pnpm dev` na porta `5173` com proxy `/backend` → `http://localhost:8080`.
- **Estrutura Feature-Based em `/frontend/src/`**:
  - `styles/` (`tokens.css` com `:root` + `@theme` Tailwind v4 e `globals.css`)
  - `lib/` (`api.ts` com `chamarApi`, `queryClient.ts`, utilitários de autenticação/erros)
  - `stores/` (stores Zustand para estado efêmero de UI)
  - `components/layout/` (`AppShell`, navegação, notificações, `ErroRota`, `RotaProtegida`)
  - `features/auth/` e `features/community/` (componentes, hooks/queries, tipos e testes por funcionalidade)
  - `routes/` (roteamento com `lazy` e `loader`)
- **Rotas de Mercado (Frontend)** integradas aos endpoints BFF existentes em `ui-backend` (`/backend/autenticacao`, `/backend/autenticacao/cadastro`, `/backend/comunidade/perfil-comunitario/...`, `/backend/comunidade/amizade/...`):
  - `/login` (autenticação por usuário ou e-mail persistida em `USR_USUARIO_CONTA`)
  - `/cadastro` (`RegisterPage.tsx` → `POST /backend/autenticacao/cadastro`, persistindo `UsuarioContaJpa` em `USR_USUARIO_CONTA` e criando automaticamente o `PerfilComunitario` em `PCM_PERFIL_COMUNITARIO`)
  - `/comunidade/perfil`
  - `/comunidade/perfil/:id`
  - `/comunidade/amigos`
  - `/comunidade/colecionadores`
- **Contratos Oficiais de Integração Externa**:
  - **One Piece TCG API (`https://optcgapi.com`)** para cartas e decks (`card_set_id`, `card_name`, `set_id`, `set_name`, `rarity`, `card_color`, `card_type`, `card_cost`, `card_power`, `sub_types`, `counter_amount`, `attribute`, `market_price`, `inventory_price`, `card_image_id`, `card_image`).
  - **AnimeThemes API (`https://api.animethemes.moe`)** para aberturas de One Piece (`slug`, `sequence`, `song.title`, `song.artists[].name`, `animethemeentries[].episodes`, `animethemeentries[].videos[].link`, `images[].link`).

