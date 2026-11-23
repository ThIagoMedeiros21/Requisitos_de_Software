# Plano Técnico de Implementação — Etapa 3.4: `F003 — Avaliação de Cartas`

> [!IMPORTANT]
> Este plano consolida a arquitetura, a rastreabilidade, os contratos REST/`optcgapi.com` e a estratégia de testes e entregas da **Etapa 3.4** (**última funcionalidade de Lucas Menezes: `comunidade / F003 — Avaliação de cartas`**), em estrita conformidade com [.agents.md](../../.agents.md), [tcghub/AGENTS.md](../../tcghub/AGENTS.md), [F003.md](../../tcghub/docs/comunidade/funcionalidades/F003.md), [comunidade/projeto.cml](../../tcghub/docs/comunidade/projeto.cml), [comunidade/projeto.dbml](../../tcghub/docs/comunidade/projeto.dbml), [avaliacao-de-cartas.feature](../../tcghub/dominio-comunidade/src/test/resources/features/avaliacao-de-cartas.feature) e [prototipos/README.md](../prototipos/README.md).
> **Restrição desta etapa**: Nenhuma linha de código Java ou React deve ser criada antes da validação e aprovação deste plano pelo `@supervisor` (Etapa 2).

---

## 1. Escopo e Matriz de Rastreabilidade Rigorosa (100%)

A funcionalidade **F003 — Avaliação de cartas** entrega operações completas de escrita, validação, invalidação por reembolso e leitura para os agregados **`CompraVerificada`** (projeção local desacoplada de compras elegíveis) e **`AvaliacaoProduto`** no subdomínio `comunidade`, mediada pelo padrão GoF **`Proxy`** (`AvaliacaoElegibilidadeProxy`).

Cobertura total: **6 Regras de Negócio (`RN001`–`RN006`)**, **16 cenários BDD** (31 steps em [AvaliacaoDeCartasStepDefinitions.java](../../tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/avaliacao/AvaliacaoDeCartasStepDefinitions.java)), **2 tabelas (`CVE_COMPRA_VERIFICADA` e `AVA_AVALIACAO_PRODUTO`)** e **5 fluxos de tela** (`P10`–`P14`). Com a entrega da Etapa 3.4, Lucas Menezes conclui **100% de suas 4 funcionalidades** (**92/92 cenários BDD reais**: `26 + 15 + 19 + 16 + 16`).

| Regra de Negócio (`F003.md`) | Cenários BDD (`avaliacao-de-cartas.feature` — 16 cenários) | Modelo CML (`comunidade/projeto.cml`) | Modelo DBML (`comunidade/projeto.dbml`) | Protótipos / UI |
| :--- | :--- | :--- | :--- | :--- |
| **RN001 — Elegibilidade restrita a compradores com pedido pago e não reembolsado** | 1. Registrar avaliação por comprador com pedido pago e não reembolsado<br>2. Registrar avaliação sem comentário opcional<br>3. Registrar avaliação com nota mínima válida 1<br>4. Rejeitar avaliação sem ter comprado a carta<br>5. Rejeitar avaliação com pedido ainda não pago<br>6. Rejeitar avaliação quando único pedido foi reembolsado<br>7. Permitir avaliação quando há pedido reembolsado e outro pago não reembolsado | `CompraVerificada`, `CompraVerificadaRepository.existeCompraElegivel`, `AvaliacaoElegibilidadeProxy.registrarOuAtualizarAvaliacao` | `CVE_COMPRA_VERIFICADA` (`CVE_PAGA = true`, `CVE_REEMBOLSADA = false`, unique `(CVE_USR_ID, CVE_PEDIDO_ID, CVE_PRODUTO_ID)`) | `P10`, `P11`, `P13` |
| **RN002 — Uma avaliação por usuário e produto (atualização idempotente + autorização do autor)** | 10. Reavaliar uma carta atualiza a nota existente sem duplicar a contagem<br>11. Cancelar edição da avaliação preserva a nota e o comentário anteriores<br>16. Não é possível atualizar a avaliação de outro usuário | `AvaliacaoProdutoRepository.existePorUsuarioEProduto/buscarPorUsuarioEProduto`, `AvaliacaoElegibilidadeProxy` (`solicitante.equals(autor)`), `AvaliacaoProduto.atualizar(...)` | Índice único `(AVA_USR_ID, AVA_PRODUTO_ID)` em `AVA_AVALIACAO_PRODUTO` | `P10`, `P13`, `P14` (botão `Cancelar` preserva estado) |
| **RN003 — Faixa válida de nota (1 a 5) e comentário opcional** | 1. Nota 5 com comentário<br>2. Nota 4 sem comentário<br>3. Nota mínima válida 1<br>8. Rejeitar nota menor que 1 (`0`)<br>9. Rejeitar nota maior que 5 (`6`) | `NotaAvaliacao` (`1 <= valor <= 5`), `AvaliacaoProduto.comentario nullable` | `AVA_NOTA int [not null]`, `AVA_COMENTARIO varchar(1000)` | `P13`, `P14` (seletor 1..5 estrelas + comentário opcional) |
| **RN004 — Média e contagem válidas exibidas no catálogo** | 1. Média 5.0 e 1 válida<br>3. Média 1.0 e 1 válida<br>10. Média atualizada sem duplicar contagem<br>12. Consultar média (`4.0`) e contagem (`2`) no catálogo<br>13. Consultar carta sem avaliações (`0.0` e `0`) | `ResumoAvaliacoesProduto` (`media`, `quantidadeValida`, distribuição `1..5`), `AvaliacaoProdutoServico.consultarResumo`, `AvaliacaoProdutoRepository.listarValidasPorProduto` | Consulta filtrada por `AVA_PRODUTO_ID` e `AVA_VALIDA = true` | `P11`, `P12` (`/comunidade/avaliacoes` e detalhe da carta) |
| **RN005 — Invalidação automática por reembolso e recálculo da média (SEM `Observer`)** | 14. Reembolso do único pedido válido invalida a avaliação e recalcula a média<br>15. Reembolso de apenas um pedido mantém a avaliação válida se houver outra compra elegível | **Colaboração síncrona direta** `CompraVerificadaServico.registrarReembolso` → `AvaliacaoProdutoServico.invalidarPorReembolso` (verifica `existeCompraElegivel`; marca `valida = false` se não houver outra compra elegível) | Atualização `CVE_REEMBOLSADA = true` em `CVE_COMPRA_VERIFICADA` e `AVA_VALIDA = false` em `AVA_AVALIACAO_PRODUTO` | `P12`, `P14` + painel demonstrativo de pedido (pago/reembolsado) |
| **RN006 — Selo `"compra verificada"`** | 1, 2, 7, 12, 15. Toda avaliação válida registrada e exibida publicamente carrega o selo `"compra verificada"` | `AvaliacaoProduto.compraVerificada` (constante `SELO_COMPRA_VERIFICADA = "compra verificada"`) | `AVA_COMPRA_VERIFICADA boolean [not null]` | `P12`, `P13`, `P14` (badge `✓ Compra Verificada`) |

---

## 2. Trava Rígida de Padrões GoF, Isolamento e Clean Architecture D5

### 2.1 Padrão Exclusivo de Lucas Menezes: **Proxy** *(ZERO `Observer` — atenção especial à `RN005`)*
- **`IAvaliacaoElegibilidadeProxy` / `AvaliacaoElegibilidadeProxy`** (pacote `com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto`):
  - **RealSubject**: `AvaliacaoProdutoServico` (`extends AgregadoServicoAbstrato`).
  - **Colaborador de Elegibilidade**: `ICompraVerificadaRepositorio`.
  - **Papel de Protection Proxy**:
    1. `registrarOuAtualizarAvaliacao(UsuarioIdentificador solicitante, UsuarioIdentificador autor, ProdutoIdentificador produto, NotaAvaliacao nota, Optional<String> comentario)`:
       - **Guarda 1 (Autorização — `RN002`)**: valida `solicitante.equals(autor)`; caso contrário lança `NaoAutorizadoExcecao` (impedindo alteração direta da avaliação de outro usuário, cenário 16).
       - **Guarda 2 (Elegibilidade — `RN001`)**: consulta `compraVerificadaRepositorio.existeCompraElegivel(autor, produto)` (exige ao menos um registro com `paga == true && reembolsada == false`); caso contrário lança `IllegalStateException` com mensagem clara (`"O usuário %s não possui compra paga e não reembolsada da carta %s"`), bloqueando cenários 4, 5 e 6 antes de alcançar `AvaliacaoProdutoServico`.
       - **Delegação**: invoca `avaliacaoProdutoServico.avaliar(autor, produto, nota, comentario)`, que persiste com `compraVerificada = true` e `valida = true` (`RN006`).
    2. `verificarElegibilidade(UsuarioIdentificador usuario, ProdutoIdentificador produto)`: expõe ao BFF/UI se o usuário autenticado pode avaliar a carta (`podeAvaliar: boolean`, motivo quando inelegível).
  - **Registro Spring D5**: `AvaliacaoElegibilidadeProxy` permanece **Java puro** em `dominio-comunidade` (zero anotações Spring) e é registrado via `@Bean` em `ComunidadeConfiguracao` (`ui-backend`), evitando conflito com `AgregadoRegistrador`.

### 2.2 Invalidação por Reembolso (`RN005`) sem `Observer`
- **PROIBIDO** usar `Observer`, `ApplicationEventPublisher`, `@EventListener` ou qualquer mecanismo publish/subscribe (`Observer` é exclusivo de Ricardo Freitas).
- A sincronização da `RN005` é implementada por **colaboração síncrona direta**:
  - `CompraVerificadaServico.registrarReembolso(usuario, pedido, produto)` atualiza `CompraVerificada` (`reembolsada = true`, `atualizadoEm = Instant.now()`).
  - Em seguida, `AvaliacaoProdutoServico.invalidarPorReembolso(usuario, pedido, produto)` (invocado diretamente pelo caso de uso/controlador ou orquestração síncrona da transação POST do D5) consulta `ICompraVerificadaRepositorio.existeCompraElegivel(usuario, produto)`:
    - Se **`false`** (nenhuma outra compra paga e não reembolsada existe) e o usuário possui `AvaliacaoProduto` para o produto → chama `avaliacao.invalidar()` (`valida = false`) e grava via `IAvaliacaoProdutoRepositorio.alterar(...)`.
    - Se **`true`** (existe outro pedido pago e não reembolsado) → preserva `valida = true` e `compraVerificada = true` (cenário 15).
  - O recálculo da média e da contagem é sempre consistente porque `consultarResumo(produto)` agrega dinamicamente sobre `listarValidasPorProduto(produto)` (`AVA_VALIDA = true`).

### 2.3 Padrão Transversal Compartilhado: **Template Method** e Desacoplamento
- `Template Method` mantido exclusivamente nas superclasses D5 (`AgregadoServicoAbstrato`, `AgregadoRepositorioAbstrato`, `AgregadoAplicacaoRepositorioAbstrato`).
- **Zero acoplamento a `dominio-loja`**: `dominio-comunidade/pom.xml` **não** depende de `dominio-loja`, e nenhuma tabela/entidade JPA faz FK com `loja`. `CompraVerificada` atua como projeção local desacoplada dentro de `comunidade`, usando `ProdutoIdentificador` e `PedidoIdentificador`.

---

## 3. Contrato Oficial `https://optcgapi.com` e Cálculo de Resumo

- **Reaproveitamento de Metadados de Cartas**:
  - O módulo de avaliações reutiliza `IColecaoCartaConsulta` / catálogo curado compatível com **`https://optcgapi.com`** (`OP05-119` Monkey.D.Luffy Manga, `OP01-001` Roronoa Zoro, `OP01-016` Nami, `OP01-006` Otama, `OP01-025` Roronoa Zoro SR, `OP01-047` Trafalgar Law, `OP01-120` Shanks, `OP02-013` Portgas.D.Ace, `OP03-123` Charlotte Katakuri), expondo os campos canônicos `card_set_id`, `card_name`, `set_id`, `set_name`, `rarity`, `card_color`, `card_type`, `card_cost`, `card_power`, `market_price`, `inventory_price`, `card_image_id` e `card_image` (com fallback SVG náutico offline no frontend).
- **Cálculo de Resumo (`ResumoAvaliacoesProduto`)**:
  - `quantidadeValida`: total de avaliações com `valida == true`.
  - `media`: média aritmética em `BigDecimal` com escala `1` (`RoundingMode.HALF_UP`, ex.: `5.0`, `4.0`, `1.0`); retorna `0.0` quando `quantidadeValida == 0` (`RN004`, cenário 13).
  - `distribuicao`: mapa/contadores exatos para notas `5`, `4`, `3`, `2` e `1` (para o histograma `P12`).

---

## 4. Inventário Completo por Módulo

> Todos os novos pacotes `src/main/java` devem declarar `package-info.java` com `@NullMarked` (verificado por ErrorProne + NullAway `0.14.1`) e aplicar programação defensiva (`Objects.requireNonNull`, `Validate.*`, `Optional` no domínio, `@Nullable` apenas em JPA/JPQL/DTOs REST). Zero anotações Spring no domínio e zero `@Transactional` manual em controladores REST.

### 4.1 `tcghub/dominio-comunidade`
- **Pacote `com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada`** (+ `.spi`, `@NullMarked`):
  - VOs:
    - `CompraVerificadaIdentificador extends ObjetoValorStringAbstrato implements IIdentificador`
    - `ProdutoIdentificador extends ObjetoValorStringAbstrato` (`TAMANHO = 100`, `Validate.notBlank`)
    - `PedidoIdentificador extends ObjetoValorStringAbstrato` (`TAMANHO = 100`, `Validate.notBlank`)
  - Entidade raiz: `CompraVerificada implements IAgregado<CompraVerificadaIdentificador>` (`Optional<CompraVerificadaIdentificador> identificador`, `UsuarioIdentificador usuario`, `ProdutoIdentificador produto`, `PedidoIdentificador pedido`, `boolean paga`, `boolean reembolsada`, `Instant atualizadoEm`; métodos `isElegivel()` (`paga && !reembolsada`), `marcarComoPaga(Instant)`, `marcarComoReembolsada(Instant)`).
  - Autorizações: `CompraVerificadaAutorizacoes extends AgregadoAutorizacoes<CompraVerificada>`.
  - SPI `spi/ICompraVerificadaRepositorio extends IAgregadoRepositorio<CompraVerificadaIdentificador, CompraVerificada>`: `existeCompraElegivel(UsuarioIdentificador, ProdutoIdentificador)`, `buscarPorUsuarioEPedidoEProduto(UsuarioIdentificador, PedidoIdentificador, ProdutoIdentificador)`, `existePorUsuarioEPedidoEProduto(...)`, `listarPorUsuarioEProduto(UsuarioIdentificador, ProdutoIdentificador)`, `listarPorUsuario(UsuarioIdentificador)`.
  - Serviço: `CompraVerificadaServico extends AgregadoServicoAbstrato<CompraVerificadaIdentificador, CompraVerificada, CompraVerificadaAutorizacoes>`: `registrarCompraPaga(usuario, pedido, produto)`, `registrarCompraPendente(usuario, pedido, produto)`, `registrarReembolso(usuario, pedido, produto)`.
- **Pacote `com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto`** (+ `.spi`, `@NullMarked`):
  - VOs:
    - `AvaliacaoProdutoIdentificador extends ObjetoValorStringAbstrato implements IIdentificador`
    - `NotaAvaliacao implements IObjetoValor` (`int valor`, `MINIMO = 1`, `MAXIMO = 5`; `Validate.isTrue(valor >= 1 && valor <= 5, "A nota deve estar entre 1 e 5")`)
    - `ResumoAvaliacoesProduto` (`ProdutoIdentificador produto`, `BigDecimal media`, `int quantidadeValida`, `Map<Integer, Integer> distribuicaoNotas`)
  - Entidade raiz: `AvaliacaoProduto implements IAgregado<AvaliacaoProdutoIdentificador>` (`Optional<AvaliacaoProdutoIdentificador> identificador`, `UsuarioIdentificador usuario`, `ProdutoIdentificador produto`, `NotaAvaliacao nota`, `Optional<String> comentario`, `boolean compraVerificada`, `boolean valida`, `Instant avaliadoEm`; constante `SELO_COMPRA_VERIFICADA = "compra verificada"`; métodos `atualizar(NotaAvaliacao, Optional<String>, Instant)` — revalidando e mantendo `compraVerificada = true` — e `invalidar()`).
  - Autorizações: `AvaliacaoProdutoAutorizacoes extends AgregadoAutorizacoes<AvaliacaoProduto>` (`avaliar`).
  - SPI `spi/IAvaliacaoProdutoRepositorio extends IAgregadoRepositorio<AvaliacaoProdutoIdentificador, AvaliacaoProduto>`: `existePorUsuarioEProduto(UsuarioIdentificador, ProdutoIdentificador)`, `buscarPorUsuarioEProduto(UsuarioIdentificador, ProdutoIdentificador)`, `listarValidasPorProduto(ProdutoIdentificador)`.
  - Serviço (RealSubject): `AvaliacaoProdutoServico extends AgregadoServicoAbstrato<AvaliacaoProdutoIdentificador, AvaliacaoProduto, AvaliacaoProdutoAutorizacoes>`:
    - Constantes públicas: `IDENTIFICADOR_INEXISTENTE_MODELO`, `IDENTIFICADOR_EXISTENTE_MODELO`, `COMPRA_NAO_ELEGIVEL_MODELO`, `AUTORIZACAO_PROPRIA_OBRIGATORIA`.
    - `avaliar(usuario, produto, nota, comentario)` — atualiza se já existe `(usuario, produto)` (`RN002`) ou inclui nova com `compraVerificada = true` e `valida = true` (`RN006`).
    - `invalidarPorReembolso(usuario, pedido, produto)` — registra reembolso em `CompraVerificadaServico` (se ainda não marcado) e, se `!compraVerificadaRepositorio.existeCompraElegivel(usuario, produto)` e existir avaliação, marca `valida = false` (`RN005`).
    - `consultarResumo(produto)` — calcula média aritmética (1 casa decimal, `0.0` se vazio), contagem e distribuição sobre `listarValidasPorProduto(produto)` (`RN004`).
    - `listarAvaliacoesValidas(produto)` — retorna somente avaliações com `valida == true`.
  - **Proxy GoF**: `IAvaliacaoElegibilidadeProxy` + `AvaliacaoElegibilidadeProxy` (Java puro, sem Spring).

### 4.2 `tcghub/aplicacao`
- `com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada` (+ `.spi`, `@NullMarked`): `CompraVerificadaProjecao`, `CompraVerificadaOrdenacao`, `CompraVerificadaConsulta`, `ICompraVerificadaAplicacaoRepositorio`, `CompraVerificadaAplicacaoServico`.
- `com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto` (+ `.spi`, `@NullMarked`): `AvaliacaoProdutoProjecao`, `AvaliacaoProdutoOrdenacao` (`AVALIADO_EM`, `NOTA`), `AvaliacaoProdutoConsulta` (filtros opcionais `produto`, `usuario`, `apenasValidas`), `IAvaliacaoProdutoAplicacaoRepositorio`, `AvaliacaoProdutoAplicacaoServico`.

### 4.3 `tcghub/infraestrutura`
- `com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada` (`@NullMarked`):
  - `CompraVerificadaJpa implements IAgregadoJpa<CompraVerificada>` (`@Entity @Table(name = "CVE_COMPRA_VERIFICADA", uniqueConstraints = @UniqueConstraint(columnNames = { "CVE_USR_ID", "CVE_PEDIDO_ID", "CVE_PRODUTO_ID" }))`): `CVE_ID` identity, `CVE_USR_ID`, `CVE_PRODUTO_ID`, `CVE_PEDIDO_ID`, `CVE_PAGA`, `CVE_REEMBOLSADA`, `CVE_ATUALIZADO_EM`.
  - `ICompraVerificadaRepositorioJpa extends JpaRepository<CompraVerificadaJpa, Long>`, `CompraVerificadaRepositorio`, `CompraVerificadaAplicacaoRepositorio`.
- `com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto` (`@NullMarked`):
  - `AvaliacaoProdutoJpa implements IAgregadoJpa<AvaliacaoProduto>` (`@Entity @Table(name = "AVA_AVALIACAO_PRODUTO", uniqueConstraints = @UniqueConstraint(columnNames = { "AVA_USR_ID", "AVA_PRODUTO_ID" }))`): `AVA_ID` identity, `AVA_USR_ID`, `AVA_PRODUTO_ID`, `AVA_NOTA`, `AVA_COMENTARIO` (`@Nullable`), `AVA_COMPRA_VERIFICADA`, `AVA_VALIDA`, `AVA_AVALIADO_EM`.
  - `IAvaliacaoProdutoRepositorioJpa extends JpaRepository<AvaliacaoProdutoJpa, Long>`, `AvaliacaoProdutoRepositorio`, `AvaliacaoProdutoAplicacaoRepositorio`.

### 4.4 `tcghub/ui-backend`
- **Wiring & Seed (`ComunidadeConfiguracao` + `AutenticacaoRecurso` + `AutenticacaoControlador`)**:
  - Registrar `@Bean IAvaliacaoElegibilidadeProxy avaliacaoElegibilidadeProxy(AvaliacaoProdutoServico servico, ICompraVerificadaRepositorio compraRepositorio)`.
  - Expor `avaliacaoProduto` (`AvaliacaoProdutoAutorizacoes`) em `AutenticacaoRecurso` / `GET /backend/autenticacao`.
  - Seed idempotente em `ComunidadeConfiguracao`:
    - Compras verificadas (`CVE_COMPRA_VERIFICADA`) para `lucas` (`PED-100` pago para `OP05-119`, `PED-101` pago para `OP01-001`, `PED-102` pago para `OP01-016`), `ricardo` (`PED-200` pago para `OP05-119`, `PED-201` pendente para `OP01-025`) e `thiago` (`PED-300` reembolsado para `OP05-119`).
    - Avaliações válidas iniciais (`AVA_AVALIACAO_PRODUTO`) para `OP05-119` (`lucas` nota `5` e `ricardo` nota `4` → média `4.5`, `2` avaliações válidas com selo `"compra verificada"`) e `OP01-001` (`lucas` nota `5`), mantendo cartas sem avaliação (ex.: `OP01-006`) para demonstrar média `0.0` e `0` avaliações.
- **`AvaliacaoProdutoControlador`** (`@RestController @RequestMapping(Backend.PREFIXO + "comunidade/avaliacao-produto")`):
  - `GET /catalogo?termo=` — lista cartas do catálogo (`optcgapi.com`) enriquecidas com `{ media, quantidadeValida, distribuicao, podeAvaliar, minhaAvaliacao, comprasDoUsuario }` para o usuário autenticado.
  - `GET /{produtoId}/pesquisa` — detalhe completo de avaliações da carta (`carta` com metadados `optcgapi.com`, `resumo` com `media`, `quantidadeValida`, `distribuicao`, `podeAvaliar`, `minhaAvaliacao`, `comprasDoUsuario`, e lista `avaliacoes` contendo apenas registros válidos com apelido do autor, `selo: "compra verificada"`, nota, comentário e data).
  - `POST /{produtoId}/avaliar` — body `{ nota, comentario? }` (e opcional `autorUsuario` para validar bloqueio de edição de terceiro via Proxy) → executa `avaliacaoElegibilidadeProxy.registrarOuAtualizarAvaliacao(...)` (`RN001`–`RN003`, `RN006`; retorna `400` nota fora `1..5`, `403` terceiro, `409`/`400` sem compra elegível).
  - `POST /compras/registrar` — body `{ pedidoId, produtoId, paga }` → registra compra verificada paga/pendente na projeção local do usuário autenticado (apoio demonstrativo e de testes integrados para `CompraVerificada`).
  - `POST /compras/{pedidoId}/reembolsar` — body `{ produtoId }` → executa fluxo síncrono `CompraVerificadaServico.registrarReembolso` + `AvaliacaoProdutoServico.invalidarPorReembolso` (sem `Observer`), recalculando imediatamente a média da carta (`RN005`).

### 4.5 `/frontend/` (Standalone — React 19 + Vite + Tailwind CSS v4 Stitch + TanStack Query + Zustand)
- **Store Zustand (`frontend/src/stores/reviewsUiStore.ts`)** — somente estado efêmero de UI:
  - `termoBusca`, `filtroElegibilidade` (`'todas' | 'elegiveis' | 'avaliadas'`), `produtoModalAberto: string | null`, `modalAvaliacaoAberto: boolean`, `notaHover: number | null`, `painelPedidosAberto: boolean` e ações + `resetarEstadoUi()`.
- **Feature `frontend/src/features/community/reviews/`**:
  - `types/reviews.types.ts` (tipos alinhados ao BFF e metadados `optcgapi.com`).
  - `api/reviewsApi.ts` e `api/reviewsQueries.ts` (`chamarApi`, `queryOptions`, `ensureQueryData`, `useSuspenseQuery`, `useMutation` invalidando `['comunidade', 'avaliacao-produto']`).
  - `pages/CardReviewsCatalogPage.tsx` (rota `/comunidade/avaliacoes`): vitrine de cartas com imagens `optcgapi.com` (fallback SVG náutico), média + estrelas + contagem válida (`0.0` se sem avaliações), filtro (`Todas`, `Elegíveis para Avaliar`, `Minhas Avaliações`), busca por nome/código e acesso rápido a detalhes ou modal `Avaliar` / `Editar Minha Avaliação`.
  - `pages/CardReviewDetailPage.tsx` (rota `/comunidade/avaliacoes/:produtoId`): ficha da carta (`P11`) + bloco `Avaliações da Comunidade` (`P12`: card resumo média + histograma `5★..1★` + lista com badge **`✓ Compra Verificada`**) + botão `Avaliar Carta` / `Editar Minha Avaliação` + painel colapsável de compras verificadas do usuário (permitindo simular/demonstrar pagamento ou reembolso e observar `RN005` ao vivo).
  - `components/ReviewFormModal.tsx` (`P13`/`P14` com `React Hook Form`): seletor interativo `1..5` estrelas com legenda dinâmica, comentário opcional, alerta quando usuário não possui compra paga e não reembolsada (`AvaliacaoElegibilidadeProxy`), botão `Cancelar` descartando rascunho sem enviar mutação (`RN002`) e `Salvar Avaliação` / `Atualizar Avaliação`.
  - `components/StarRating.tsx` e `components/RatingDistributionBars.tsx`.
- **Rotas e Navegação (`frontend/src/routes/index.tsx`, `CommunitySubNav.tsx`)**:
  - Adicionar rotas lazy `/comunidade/avaliacoes` e `/comunidade/avaliacoes/:produtoId` (com redirect legado `/comunidade/avaliacao-produto/pesquisa` → `/comunidade/avaliacoes`).
  - Adicionar aba `Avaliações` (`/comunidade/avaliacoes`) em `CommunitySubNav.tsx`.

---

## 5. Plano de Testes e Critérios de Aceite

1. **Testes BDD (`dominio-comunidade`)**:
   - Automatizar todos os **31 steps** em `AvaliacaoDeCartasStepDefinitions.java` exercitando `CompraVerificadaServico`, `AvaliacaoProdutoServico`, `AvaliacaoElegibilidadeProxy`, `AvaliacaoProduto`, `NotaAvaliacao` e `CompraVerificada` com mocks Mockito em `ISegurancaServico`, `ICompraVerificadaRepositorio` e `IAvaliacaoProdutoRepositorio`.
   - Critério de aceite:
     - `avaliacao-de-cartas.feature`: **`16/16` cenários passando** (`0` falhas/erros/skipped, `0` `PendingException`).
     - Regressão completa de Lucas: `perfil-e-amigos.feature` (`26/26`), `decks-favoritos.feature` (`15/15`), `abertura.feature` (`19/19`), `abertura-preferida.feature` (`16/16`) → **92/92 cenários reais de Lucas 100% verdes!**
     - Dry-run global (`dominio-adm,dominio-comunidade,dominio-loja`): **`143/143`** (`BUILD SUCCESS`).
2. **Testes JPA (`infraestrutura`)**:
   - `@DataJpaTest` em `CompraVerificadaRepositorioTest`, `AvaliacaoProdutoRepositorioTest` e `AvaliacaoProdutoAplicacaoRepositorioTest` (unicidade `(CVE_USR_ID, CVE_PEDIDO_ID, CVE_PRODUTO_ID)`, unicidade `(AVA_USR_ID, AVA_PRODUTO_ID)`, `existeCompraElegivel`, `listarValidasPorProduto`, atualização e invalidação).
3. **Testes REST BFF (`ui-backend`)**:
   - `AvaliacaoProdutoControladorTest` cobrindo listagem com resumo, avaliação válida com selo `"compra verificada"`, reavaliação sem duplicar contagem, rejeição sem compra/pendente/reembolsada, rejeição de nota fora `1..5`, rejeição de terceiro (`403`) e reembolso invalidando avaliação + recalculando média (`RN005`).
4. **Testes Frontend (`Vitest` + Testing Library em `/frontend/`)**:
   - `reviewsUiStore.test.ts`, `CardReviewsCatalogPage.test.tsx`, `CardReviewDetailPage.test.tsx`, `ReviewFormModal.test.tsx` (incluindo botão `Cancelar` preservando estado, selo `Compra Verificada`, nota `1..5` e recálculo após reembolso); `pnpm test` 100% verde e `pnpm build` limpo.
5. **Verificação Docker Vivo**:
   - Rebuild e subida `docker compose --env-file .env -f compose.yaml up -d --build --wait` em `tcghub/` (mantendo containers ativos `:15432` e `:18080`) e verificação HTTP ao vivo em `/backend/comunidade/avaliacao-produto/catalogo` e `/backend/comunidade/avaliacao-produto/OP05-119/pesquisa`.

---

## 6. Estratégia de Commits Incrementais (Conventional Commits em Inglês)

Sem `git push` e sem incluir `tcghub/.env` ou diretórios de build:
1. *(Planejamento)* `docs(plan): add Stage 3.4 technical plan, ubiquitous language, and prototypes for F003`
2. *(Auditoria Planejamento)* `docs(reports): add Stage 3.4 planning audit report and AI interaction log`
3. *(Implementação)*:
   - `feat(comunidade): implement F003 CompraVerificada, AvaliacaoProduto, and Eligibility Proxy`
   - `test(bdd): automate F003 Avaliacao de Cartas Cucumber StepDefinitions`
   - `feat(aplicacao): add CQRS projections and queries for F003`
   - `feat(infraestrutura): add JPA persistence for CVE_COMPRA_VERIFICADA and AVA_AVALIACAO_PRODUTO`
   - `feat(ui-backend): add AvaliacaoProdutoControlador REST endpoints and seed data`
   - `feat(frontend): implement F003 card reviews catalog, detail view, and rating modal`
   - `docs(reports): add Stage 3.4 implementation audit report and AI interaction log`
