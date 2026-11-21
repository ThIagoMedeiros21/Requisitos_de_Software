# Plano Técnico de Implementação — Etapa 3.2: `F002 — Decks Favoritos`

> [!IMPORTANT]
> Este plano consolida a arquitetura, a rastreabilidade, os contratos de API e a estratégia de testes e entregas da **Etapa 3.2** (**exclusivamente `comunidade / F002 — Decks favoritos`** de Lucas Menezes), em conformidade com [.agents.md](../../.agents.md), [tcghub/AGENTS.md](../../tcghub/AGENTS.md), [F002.md](../../tcghub/docs/comunidade/funcionalidades/F002.md), [comunidade/projeto.cml](../../tcghub/docs/comunidade/projeto.cml), [comunidade/projeto.dbml](../../tcghub/docs/comunidade/projeto.dbml), [decks-favoritos.feature](../../tcghub/dominio-comunidade/src/test/resources/features/decks-favoritos.feature) e [prototipos/README.md](../prototipos/README.md).
> **Restrição desta etapa**: Nenhuma linha de código Java ou React deve ser implementada antes da validação e aprovação deste plano pelo `@supervisor` (Etapa 2).

---

## 1. Escopo e Matriz de Rastreabilidade Rigorosa

A funcionalidade **F002 — Decks favoritos** entrega operações completas de escrita, validação e leitura para o agregado **`DeckFavorito`** no subdomínio `comunidade`, integrado à configuração de visibilidade de **`PerfilComunitario`** (`PCM_DECKS_FAVORITOS_VISIVEIS`) e mediado pelo padrão GoF **`Proxy`** (`CatalogoDeckProxy` e `PerfilComunitarioVisibilidadeProxy`).

Cobertura total: **5 Regras de Negócio (`RN001`–`RN005`)**, **15 cenários BDD** (44 steps em [DecksFavoritosStepDefinitions.java](../../tcghub/dominio-comunidade/src/test/java/com/cesar/tgchub/tcghub/dominio/comunidade/deckfavorito/DecksFavoritosStepDefinitions.java)), **1 tabela dedicada (`DFA_DECK_FAVORITO`) + 1 coluna de visibilidade (`PCM_DECKS_FAVORITOS_VISIVEIS`)** e **4 fluxos de tela** (`P06`–`P09`, telas Stitch `a778927205374bb68481d5bb1f1dc11d` e `d26c9d5f978f422fa021153ec9cfe885`).

| Regra de Negócio (`F002.md`) | Cenários BDD (`decks-favoritos.feature` — 15 cenários) | Modelo CML (`comunidade/projeto.cml`) | Modelo DBML (`comunidade/projeto.dbml`) | Protótipos / Telas Stitch |
| :--- | :--- | :--- | :--- | :--- |
| **RN001 — Limite máximo de 4 decks favoritos** | 1. Adicionar um deck favorito ao perfil<br>3. Adicionar até o limite máximo de 4 decks favoritos<br>4. Não é possível adicionar um quinto deck favorito<br>8. Remover um deck favorito libera vaga para outro deck<br>9. Não é possível remover um deck que não está nos favoritos | `DeckFavorito`, `DeckFavoritoServico.adicionar/remover`, `DeckFavoritoRepository.contarPorUsuario` (`MAX_DECKS_FAVORITOS = 4`) | `DFA_DECK_FAVORITO` (`DFA_ID`, `DFA_USR_ID`, `DFA_DECK_ID`, `DFA_ORDEM`, `DFA_ADICIONADO_EM`) | `P06`, `P07`, `P08` (`a778927205374bb68481d5bb1f1dc11d`) |
| **RN002 — Ausência de duplicatas e elegibilidade de deck ativo** | 2. Pesquisar decks disponíveis para adicionar aos favoritos<br>5. Não é possível adicionar o mesmo deck duas vezes aos favoritos<br>6. Não é possível adicionar um deck inativo aos favoritos<br>7. Não é possível adicionar um deck inexistente aos favoritos<br>12. Deck favorito que se torna inativo não fica disponível para novas escolhas | `DeckFavoritoRepository.existePorUsuarioEDeck`, `CatalogoDeckProxy.pesquisarDecksAtivos/verificarDeckAtivo/obterComposicaoDeck`, `ComposicaoDeck.ativo` | Índice único `(DFA_USR_ID, DFA_DECK_ID)` em `DFA_DECK_FAVORITO` | `P07` (badge *Inativo no Catálogo*), `P08` (modal/drawer de busca) |
| **RN003 — Composição via interface do Catálogo mediada por Proxy** | 10. Visualizar composição do deck favorito obtida via interface do Catálogo<br>11. Tratar indisponibilidade da interface do Catálogo ao expandir composição de deck favorito | `CatalogoDeckProxy`, `ComposicaoDeck`, `ItemComposicaoDeck`, `DeckIdentificador`, `CartaIdentificador` + SPI `ICatalogoDeckConsulta` | **Zero FK/acesso ao banco de `loja`** (`DFA_DECK_ID` é identificador lógico) | `P09` (`d26c9d5f978f422fa021153ec9cfe885` — modal expandido + cópia Bandai) |
| **RN004 — Ocultação e reexibição no perfil público** | 13. Ocultar os decks favoritos no perfil público sem perder a lista cadastrada<br>14. Reexibir os decks favoritos no perfil público | `PerfilComunitario.decksFavoritosVisiveis`, `PerfilComunitarioServico.alterarVisibilidadeDecksFavoritos`, `DeckFavoritoServico.listarVisiveisNoPerfil`, `PerfilComunitarioVisibilidadeProxy` | `PCM_PERFIL_COMUNITARIO.PCM_DECKS_FAVORITOS_VISIVEIS` | `P06`, `P07` (toggle *Exibir no Perfil Público*) |
| **RN005 — Autorização exclusiva do proprietário** | 15. Não é possível gerenciar decks favoritos de outro usuário | `DeckFavoritoAutorizacoes`, `DeckFavoritoServico` / `PerfilComunitarioVisibilidadeProxy` validando `solicitante.equals(alvo)` | Isolamento por `DFA_USR_ID` do usuário autenticado | `P06`, `P07`, `P08` (ações restritas ao dono) |

---

## 2. Linguagem Onipresente — Decks Favoritos (`comunidade / F002`)

| Termo | Classificação DDD | Definição Canônica no Domínio |
| :--- | :--- | :--- |
| **Deck Favorito** (`DeckFavorito`) | Agregado / Entidade Raiz | Vínculo entre um usuário (`UsuarioIdentificador`) e um deck oficial do Catálogo (`DeckIdentificador`), preservando ordem de exibição (`1..4`) e data de inclusão (`adicionadoEm`). |
| **Identificador do Deck** (`DeckIdentificador`) | Objeto de Valor | Código canônico de um deck no Catálogo (ex.: `ST01`, `ST10`, `D1`), utilizado para desacoplar `comunidade` da persistência externa. |
| **Composição do Deck** (`ComposicaoDeck`) | Objeto de Valor | Estrutura detalhada de um deck retornada via `CatalogoDeckProxy`: identificador, nome, líder (nome + código de carta), cor/tema, status (`ativo`), valor estimado de mercado e lista de `ItemComposicaoDeck`. |
| **Item da Composição** (`ItemComposicaoDeck`) | Objeto de Valor | Entrada agrupada de uma carta no deck contendo `CartaIdentificador`, nome da carta, tipo (`Leader`, `Character`, `Event`, `Stage`), custo, raridade, cor, imagem (`card_image`), quantidade (`1..4` cópias) e preço unitário (`BigDecimal`). |
| **Formato Oficial Bandai** | Regra de Domínio / UX | Padrão competitivo de One Piece TCG composto por **1 Líder + 50 cartas** no deck principal, respeitando o teto de **até 4 cópias** por código de carta (`4xOP01-016` na exportação de decklist). |
| **Deck Ativo** | Estado Externo | Deck elegível para pesquisa e inclusão nos favoritos; somente decks ativos podem ser adicionados por novos usuários (`RN002`). |
| **Deck Descontinuado / Inativo** | Estado Externo | Deck que foi inativado no Catálogo após já ter sido favoritado: permanece visível e removível na lista do usuário que já o possuía (com badge informativo), mas é rejeitado para novas inclusões (`RN002`). |
| **Visibilidade Pública de Decks** | Preferência de Privacidade | Flag booleana (`PCM_DECKS_FAVORITOS_VISIVEIS`) controlada pelo proprietário; quando `false`, oculta a vitrine de decks favoritos para visitantes sem remover os registros salvos (`RN004`). |
| **Proxy do Catálogo de Decks** (`CatalogoDeckProxy`) | Padrão GoF (`Proxy`) | Intermediário exclusivo de Lucas sobre a SPI `ICatalogoDeckConsulta` que media pesquisas, verificação de status ativo e expansão de composição, garantindo ausência de acesso ao banco de `loja` e tratamento explícito de indisponibilidade (`RN003`). |
| **Indisponibilidade do Catálogo** (`CatalogoIndisponivelExcecao`) | Exceção de Domínio | Falha controlada lançada pelo `CatalogoDeckProxy` quando o provedor do Catálogo não responde ou está temporariamente fora do ar, traduzida em `HTTP 503` com mensagem amigável no frontend. |

---

## 3. Arquitetura D5 e Trava Rígida de Padrões GoF

### 3.1 Padrão Exclusivo de Lucas Menezes: **Proxy** *(ZERO `Observer`)*
1. **`ICatalogoDeckProxy` / `CatalogoDeckProxy`** (pacote `com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito`):
   - **Subject / SPI**: `ICatalogoDeckConsulta` (pacote `.spi`, implementada em `infraestrutura` por `CatalogoDeckConsultaInfra` sem qualquer dependência de `dominio-loja` nem acesso a tabelas de `loja`).
   - **Responsabilidades do Proxy (Virtual / Proteção / ACL)**:
     - `pesquisarDecksAtivos(String termo)`: delega à SPI filtrando estritamente decks com `ativo == true`, normalizando busca por nome, líder, cor ou código (`st_id`), e encapsulando falhas em `CatalogoIndisponivelExcecao`.
     - `verificarDeckAtivo(DeckIdentificador deck)`: valida existência (lançando `AgregadoInexistenteExcecao` se inexistente) e status ativo (lançando `IllegalStateException` se inativo) antes de permitir inclusão em `DeckFavoritoServico`.
     - `obterComposicaoDeck(DeckIdentificador deck)`: consulta a composição completa (líder + cartas + preços unitários e total estimado), aplica cache em memória com invalidação segura/isolada por chamada e captura falhas da SPI convertendo-as em `CatalogoIndisponivelExcecao` (`RN003`).
     - `obterResumoDeck(DeckIdentificador deck)`: retorna resumo (incluindo indicador `ativo: false` para decks que se tornaram inativos após favoritados) para renderização da vitrine `P07`.
   - **Registro Spring D5**: permanece **Java puro** em `dominio-comunidade` (zero anotações Spring) e é registrado via `@Bean` em `ComunidadeConfiguracao` (`ui-backend`), enquanto `CatalogoDeckConsultaInfra` é registrado via `@Bean` em `ComunidadeInfraConfiguracao` (`infraestrutura`), evitando conflito com `AgregadoRegistrador`.
2. **`PerfilComunitarioVisibilidadeProxy`** (já existente da Etapa 3.1):
   - Colabora com `DeckFavoritoServico.listarVisiveisNoPerfil(solicitante, alvo)` verificando `solicitante.equals(alvo)` vs. `perfil.isDecksFavoritosVisiveis()` (`RN004` e `RN005`):
     - **Proprietário**: visualiza todos os seus decks favoritos + flag `decksFavoritosVisiveis` + controles de adicionar/remover/ocultar/reexibir.
     - **Visitante**: quando `decksFavoritosVisiveis == false`, recebe lista vazia e `visivelNoPerfilPublico = false`, sem expor dados dos decks nem controles de mutação.

### 3.2 Padrão Transversal Compartilhado: **Template Method**
- Mantido exclusivamente nas superclasses base do framework **D5**:
  - `AgregadoServicoAbstrato` (`DeckFavoritoServico` implementa os hooks `criarAutorizacoes`, `validarInclusao`, `validarInclusaoAlteracao`, `validarExclusao`);
  - `AgregadoRepositorioAbstrato` (`DeckFavoritoRepositorio` implementa `paraJpa`, `paraAgregado`, `paraIdentificadorJpa`);
  - `AgregadoAplicacaoRepositorioAbstrato` (`DeckFavoritoAplicacaoRepositorio` implementa `pesquisar`, `paraPropriedadeOrdenacao`).

### 3.3 Restrição de Ausência de `Observer`
- É **estritamente proibido** criar eventos/listeners no padrão `Observer` em `F002` (`Observer` é exclusivo de Ricardo Freitas).

---

## 4. Contrato Oficial `https://optcgapi.com` e Regras Bandai

O `CatalogoDeckConsultaInfra` (em `infraestrutura`) adota o contrato oficial de decks e cartas da **One Piece TCG API (`https://optcgapi.com`)**, utilizando catálogo curado compatível offline/testes e estrutura pronta para integração HTTP com os endpoints públicos:

- **Endpoints oficiais de referência**:
  - `GET https://optcgapi.com/api/allDecks/` — listagem de starter decks (`st_id`, `deck_name`, `leader`, `color`, `status`).
  - `GET https://optcgapi.com/api/decks/{st_id}/` — cartas da composição do deck.
- **Campos mapeados por carta em `ItemComposicaoDeck` / JSON BFF**:
  - `card_set_id` (ex.: `OP01-016`, `ST01-001`), `card_name`, `set_id`, `set_name`, `rarity`, `card_color`, `card_type` (`Leader`, `Character`, `Event`, `Stage`), `card_cost`, `card_power`, `sub_types`, `counter_amount`, `attribute`, `market_price` (`BigDecimal`), `inventory_price` (`BigDecimal`), `card_image_id`, `card_image` (`https://optcgapi.com/media/static/Card_Images/{id}.jpg`), além de `quantidade` (`1..4`) e `subtotal` (`quantidade * market_price`).
- **Metadados de conformidade Bandai em `ComposicaoDeck`**:
  - `lider`: entrada de `card_type == "Leader"` (quantidade `1`).
  - `totalCartasMainDeck`: soma das quantidades de `Character`, `Event` e `Stage` (padrão oficial `50` cartas; total exibido `50 + 1`).
  - `maxCopiasPorCarta`: validação de até `4` cópias por `card_set_id` (`conformidadeBandai: boolean`).
  - `custoEstimadoMercado`: soma exata em `BigDecimal` de `quantidade * market_price`.
  - **Formato de exportação/cópia Bandai** (no frontend): linhas ordenadas iniciando pelo líder (`1xST01-001`) seguidas pelas cartas (`4xOP01-016`, `4xOP01-006`, ...), copiadas via `navigator.clipboard.writeText(...)` com feedback em notificação.

---

## 5. Inventário Exato de Arquivos e Contratos por Módulo

> Todos os novos pacotes Java em `src/main/java` devem declarar `package-info.java` com `@org.jspecify.annotations.NullMarked` (verificado por ErrorProne + NullAway `0.14.1`), seguindo programação defensiva (`Objects.requireNonNull`, `Validate.*`, `Optional` no domínio, `@Nullable` apenas em JPA/JPQL/DTOs REST).

### 5.1 `tcghub/dominio-comunidade`
- **Pacote `com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito`** (`package-info.java` `@NullMarked`):
  - VOs:
    - `DeckFavoritoIdentificador extends ObjetoValorStringAbstrato implements IIdentificador`
    - `DeckIdentificador extends ObjetoValorStringAbstrato` (`TAMANHO = 100`, `Validate.notBlank`)
    - `ItemComposicaoDeck` (`CartaIdentificador carta`, `String nomeCarta`, `String setId`, `String setName`, `String rarity`, `String cardColor`, `String cardType`, `Optional<String> cardCost`, `Optional<String> cardPower`, `Optional<String> subTypes`, `Optional<String> counterAmount`, `Optional<String> attribute`, `int quantidade` com `1 <= quantidade <= 4`, `BigDecimal precoUnitario`, `BigDecimal inventoryPrice`, `String cardImageId`, `String cardImage`; método `calcularSubtotal()`) — *nota*: usa `BigDecimal` no Java em vez de `double` do CML para precisão monetária.
    - `ComposicaoDeck` (`DeckIdentificador deck`, `String nomeDeck`, `String lider`, `String cor`, `boolean ativo`, `List<ItemComposicaoDeck> itens` via `List.copyOf`; métodos `calcularCustoEstimadoMercado()`, `contarCartasPorTipo(String)`, `isConformidadeBandai()`).
  - Exceção: `CatalogoIndisponivelExcecao extends RuntimeException` (mensagem padrão `"O serviço de Catálogo está temporariamente indisponível"`).
  - Entidade raiz: `DeckFavorito implements IAgregado<DeckFavoritoIdentificador>` (`Optional<DeckFavoritoIdentificador> identificador`, `UsuarioIdentificador usuario`, `DeckIdentificador deck`, `int ordem`, `Instant adicionadoEm`; constante `MAX_DECKS_FAVORITOS = 4`).
  - Autorizações: `DeckFavoritoAutorizacoes extends AgregadoAutorizacoes<DeckFavorito>` (`ocultar`, `reexibir`, `visualizarComposicao`).
  - Proxy GoF:
    - `ICatalogoDeckProxy`
    - `CatalogoDeckProxy implements ICatalogoDeckProxy` (Java puro, delegando para `ICatalogoDeckConsulta`).
  - Serviço: `DeckFavoritoServico extends AgregadoServicoAbstrato<DeckFavoritoIdentificador, DeckFavorito, DeckFavoritoAutorizacoes>`:
    - Constantes públicas de mensagem: `IDENTIFICADOR_INEXISTENTE_MODELO`, `IDENTIFICADOR_EXISTENTE_MODELO`, `LIMITE_DECKS_MODELO`, `DECK_DUPLICADO_MODELO`, `DECK_INATIVO_MODELO`, `DECK_INEXISTENTE_MODELO`, `DECK_NAO_FAVORITADO_MODELO`, `OPERACAO_NAO_AUTORIZADA`.
    - Métodos de negócio:
      - `adicionar(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, DeckIdentificador deck)` — valida `solicitante.equals(alvo)` (`RN005`), limite `< 4` (`RN001`), sem duplicata (`RN002`), deck existe e está ativo via `ICatalogoDeckProxy` (`RN002`/`RN003`).
      - `remover(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, DeckIdentificador deck)` — valida `solicitante.equals(alvo)` (`RN005`), existência nos favoritos (`RN001`), remove e reordena se aplicável.
      - `ocultarNoPerfilPublico(UsuarioIdentificador solicitante, UsuarioIdentificador alvo)` / `reexibirNoPerfilPublico(UsuarioIdentificador solicitante, UsuarioIdentificador alvo)` — delegam para `PerfilComunitarioServico.alterarVisibilidadeDecksFavoritos(...)` após validar dono (`RN004`/`RN005`).
      - `listarVisiveisNoPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo)` — se `solicitante.equals(alvo)` retorna lista completa; se visitante, consulta `perfil.isDecksFavoritosVisiveis()` retornando lista vazia quando oculto (`RN004`).
      - `pesquisarDecksDisponiveis(String termo)` — delega a `ICatalogoDeckProxy.pesquisarDecksAtivos(termo)`.
      - `obterComposicaoExpandida(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, DeckIdentificador deck)` — valida visibilidade/posse e delega a `ICatalogoDeckProxy.obterComposicaoDeck(deck)` (`RN003`).
  - SPI (`spi/` com `package-info.java` `@NullMarked`):
    - `IDeckFavoritoRepositorio extends IAgregadoRepositorio<DeckFavoritoIdentificador, DeckFavorito>`: `contarPorUsuario`, `existePorUsuarioEDeck`, `buscarPorUsuarioEDeck`, `listarPorUsuario`.
    - `ICatalogoDeckConsulta`: `pesquisarAtivos(String termo)`, `buscarPorIdentificador(DeckIdentificador deck)` (`Optional<ComposicaoDeck>`).

### 5.2 `tcghub/aplicacao`
- **Pacote `com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito`** (+ `.spi`, `@NullMarked`):
  - `DeckFavoritoProjecao implements IProjecao<DeckFavorito>` (construtor VOs + construtor tipos JPA `Long, String, String, int, Instant` para `select new`).
  - `DeckFavoritoOrdenacao implements IOrdenacao<DeckFavoritoProjecao>` (`ORDEM`, `ADICIONADO_EM`).
  - `DeckFavoritoConsulta extends Consulta<DeckFavoritoOrdenacao>` (filtro opcional `usuario`).
  - `spi/IDeckFavoritoAplicacaoRepositorio extends IAgregadoAplicacaoRepositorio<DeckFavoritoProjecao, DeckFavoritoOrdenacao, DeckFavoritoConsulta>`.
  - `DeckFavoritoAplicacaoServico extends AgregadoAplicacaoServicoAbstrato<DeckFavoritoIdentificador, DeckFavorito, DeckFavoritoAutorizacoes, DeckFavoritoProjecao, DeckFavoritoOrdenacao, DeckFavoritoConsulta>`.

### 5.3 `tcghub/infraestrutura`
- **Pacote `com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito`** (`@NullMarked`):
  - `DeckFavoritoJpa implements IAgregadoJpa<DeckFavorito>` (`@Entity @Table(name = "DFA_DECK_FAVORITO", uniqueConstraints = @UniqueConstraint(columnNames = { "DFA_USR_ID", "DFA_DECK_ID" }))`):
    - `DFA_ID` (`Long`, `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)`), `DFA_USR_ID` (`@NotNull @Size(max = UsuarioIdentificador.TAMANHO)`), `DFA_DECK_ID` (`@NotNull @Size(max = DeckIdentificador.TAMANHO)`), `DFA_ORDEM` (`int`), `DFA_ADICIONADO_EM` (`Instant`).
  - `IDeckFavoritoRepositorioJpa extends JpaRepository<DeckFavoritoJpa, Long>` (com `@Query` `countByUsuario`, `existsByUsuarioAndDeck`, `findByUsuarioAndDeck`, `findByUsuarioOrderByOrdemAsc`, e pesquisa paginada `select new ...DeckFavoritoProjecao(...)`).
  - `DeckFavoritoRepositorio extends AgregadoRepositorioAbstrato<DeckFavoritoIdentificador, DeckFavorito, Long, DeckFavoritoJpa>` (tratando `NumberFormatException` → `AgregadoInexistenteExcecao` e `existePorIdentificador` retornando `false` para não numérico).
  - `DeckFavoritoAplicacaoRepositorio extends AgregadoAplicacaoRepositorioAbstrato<...>`.
  - `CatalogoDeckConsultaInfra implements ICatalogoDeckConsulta`: fornece starter decks e decks competitivos One Piece TCG alinhados ao contrato `https://optcgapi.com` (ex.: `ST01` Straw Hat Crew, `ST10` The Three Captains, `ST04` Animal Kingdom Pirates, `OP01-ZORO` Red Zoro Rush, `OP01-CROC` Blue Crocodile Control, `ST09-INATIVO` Deck Descontinuado para validar `RN002`), com imagens oficiais `https://optcgapi.com/media/static/Card_Images/*.jpg`, registrado via `@Bean` em `ComunidadeInfraConfiguracao`.

### 5.4 `tcghub/ui-backend`
- **Wiring & Segurança**:
  - `ComunidadeConfiguracao`: registrar `@Bean ICatalogoDeckProxy catalogoDeckProxy(ICatalogoDeckConsulta consulta)` retornando `new CatalogoDeckProxy(consulta)` e fazer seed idempotente de 2 decks favoritos iniciais para `lucas` (`ST01`, `OP01-ZORO`) para experiência rica imediata no navegador.
  - `AutenticacaoRecurso` e `AutenticacaoControlador`: expor `deckFavorito` (`DeckFavoritoAutorizacoes`) em `GET /backend/autenticacao`.
  - Tratamento de exceção: mapear `CatalogoIndisponivelExcecao` para `HTTP 503 Service Unavailable` com `{ "mensagem": "..." }` (em `@RestControllerAdvice` ou handler no controlador compatível com `mensagemErro(erro)` do frontend).
- **`DeckFavoritoControlador`** (`@RestController @RequestMapping(Backend.PREFIXO + "comunidade/deck-favorito")`):
  - `GET /pesquisa` — lista os decks favoritos do usuário autenticado (`{ total, limite: 4, decksFavoritosVisiveis, decks: [...] }`, incluindo flag `ativo` e resumo do líder/custo via Proxy).
  - `GET /perfil/{perfilId}` — lista os decks favoritos visíveis de um perfil público (próprio ou visitante), filtrado pelo `PerfilComunitarioVisibilidadeProxy` / `DeckFavoritoServico.listarVisiveisNoPerfil` (`RN004`).
  - `GET /catalogo?termo=&cor=` — pesquisa decks **ativos** disponíveis no Catálogo via `CatalogoDeckProxy`, indicando `jaFavoritado: boolean` (`P08`).
  - `GET /{deckId}/composicao` — retorna `ComposicaoDeckRecurso` expandido (líder, cartas agrupadas por tipo/custo com metadados `optcgapi.com`, totais por categoria, custo estimado de mercado, `conformidadeBandai` e texto pronto para cópia Bandai) via `CatalogoDeckProxy` (`P09`; retorna `503` se catálogo indisponível).
  - `POST /incluir` — body `{ deckId }` → adiciona deck favorito ao usuário autenticado (`RN001`, `RN002`, `RN005`; retorna `400`/`409`/`404`/`503`).
  - `POST /{deckId}/excluir` — remove deck favorito do usuário autenticado (`RN001`, `RN005`).
  - `POST /ocultar` — oculta decks favoritos no perfil público (`PCM_DECKS_FAVORITOS_VISIVEIS = false`, `RN004`).
  - `POST /reexibir` — reexibe decks favoritos no perfil público (`PCM_DECKS_FAVORITOS_VISIVEIS = true`, `RN004`).
- Nota D5: nunca anotar controladores/serviços com `@Transactional` (operações `@PostMapping` já são transacionais no D5).

### 5.5 `/frontend/` (Standalone — React 19 + Vite + Tailwind CSS v4 Stitch + TanStack Query + Zustand)
- **Design System**: 100% aderente a `frontend/src/styles/tokens.css` e `globals.css` (*Nautical Blue & White Marketplace*, **zero Bootstrap**), seguindo os layouts Stitch `a778927205374bb68481d5bb1f1dc11d` (decks favoritos no perfil/listagem) e `d26c9d5f978f422fa021153ec9cfe885` (modal de composição expandida).
- **Store Zustand (`frontend/src/stores/decksUiStore.ts`)** — somente estado efêmero de UI:
  - `modalAdicionarAberto: boolean`, `deckIdComposicaoAberta: string | null`, `termoBuscaCatalogo: string`, `corFiltroCatalogo: string`, `filtroTipoComposicao: 'TODOS' | 'Leader' | 'Character' | 'Event' | 'Stage'`, ações de abertura/fechamento/filtro e `resetarEstadoUi()`.
- **Feature `frontend/src/features/community/decks/`**:
  - `types/decks.types.ts` (tipos alinhados aos DTOs do BFF e `optcgapi.com`).
  - `api/decksApi.ts` e `api/decksQueries.ts` (`chamarApi`, `queryOptions`, `ensureQueryData`, `useSuspenseQuery`, `useMutation` invalidando `['comunidade', 'deck-favorito']` e `['comunidade', 'perfil-comunitario']`).
  - `utils/bandaiExport.ts` (formatação oficial Bandai `1xST01-001\n4xOP01-016...`).
  - `pages/FavoriteDecksPage.tsx` (rota `/comunidade/decks-favoritos`: contador `X/4 decks favoritos`, toggle `Exibir no Perfil Público`, cards horizontais, badge `Descontinuado / Inativo no Catálogo`, estados vazio/erro/carregamento).
  - `components/FavoriteDecksProfileCard.tsx` (integração em `ProfilePage.tsx` para `P06`, exibindo os decks ou aviso `Oculto no perfil público` ao dono e omitindo ao visitante quando oculto).
  - `components/AddFavoriteDeckModal.tsx` (modal/drawer `P08`: busca por nome/líder/cor no catálogo ativo, indicador `X de 4 slots ocupados`, botão `Favoritar` / `✓ Adicionado`, tratamento de indisponibilidade `503`).
  - `components/DeckCompositionModal.tsx` (modal `P09`: cabeçalho com líder e badge `50 + 1`, resumo por categoria e custo estimado de mercado em `font-tabular-price`, lista agrupada com imagens `optcgapi.com` + fallback SVG náutico, botão `Copiar Decklist (Bandai)` e tratamento de indisponibilidade `503`).
- **Rotas (`frontend/src/routes/index.tsx`) e Navegação (`CommunitySubNav.tsx`)**:
  - Adicionar rota lazy `comunidade/decks-favoritos` (`FavoriteDecksPage` + `carregarDecksFavoritos`) e redirect legado `comunidade/deck-favorito/pesquisa` → `/comunidade/decks-favoritos`.
  - Habilitar a aba `Decks Favoritos` em `CommunitySubNav.tsx` (e em `ProfilePage.tsx`).

---

## 6. Plano de Testes e Critérios de Aceite

1. **Testes BDD (`dominio-comunidade`)**:
   - Automatizar todos os **44 steps** em `DecksFavoritosStepDefinitions.java` exercitando `DeckFavoritoServico`, `CatalogoDeckProxy`, `DeckFavorito`, `ComposicaoDeck` e `PerfilComunitarioServico` com mocks Mockito em `ISegurancaServico`, `IDeckFavoritoRepositorio`, `IPerfilComunitarioRepositorio` e `ICatalogoDeckConsulta`.
   - **Atenção crítica**: manter o `@Dado("que existe o perfil comunitário do usuário {string} com apelido {string}")` sem duplicidade com `abertura-preferida.feature` (já reside exclusivamente em `DecksFavoritosStepDefinitions.java`).
   - Critério de aceite:
     - `mvn -f tcghub/pom.xml test -pl dominio-comunidade -Dcucumber.features="src/test/resources/features/decks-favoritos.feature"`: **`15/15` cenários passando** (`0` falhas, `0` erros, `0` skipped, `0` `PendingException`).
     - `mvn -f tcghub/pom.xml test -pl dominio-comunidade -Dcucumber.features="src/test/resources/features/perfil-e-amigos.feature"`: **`26/26`** sem regressão.
     - Dry-run global (`dominio-adm,dominio-comunidade,dominio-loja`): **`143/143`** (`BUILD SUCCESS`).
2. **Testes JPA (`infraestrutura`)**:
   - `@DataJpaTest` em `DeckFavoritoRepositorioTest` e `DeckFavoritoAplicacaoRepositorioTest` (persistência em `DFA_DECK_FAVORITO`, unicidade `(DFA_USR_ID, DFA_DECK_ID)`, contagem por usuário, ordenação `DFA_ORDEM`, identificador não numérico).
3. **Testes REST BFF (`ui-backend`)**:
   - Expandir `ComunidadeControladoresTest` (ou criar `DeckFavoritoControladorTest`) cobrindo listagem, busca de catálogo ativo, inclusão até 4, rejeição do 5º deck e duplicata, exclusão, composição via `CatalogoDeckProxy`, indisponibilidade `503`, ocultar/reexibir e filtro de perfil público para visitante.
4. **Testes Frontend (`Vitest` + Testing Library em `/frontend/`)**:
   - `decksUiStore.test.ts`, `bandaiExport.test.ts`, `FavoriteDecksPage.test.tsx`, `AddFavoriteDeckModal.test.tsx`, `DeckCompositionModal.test.tsx` + atualização de `ProfilePage.test.tsx`.
   - Critério: `pnpm test` 100% verde e `pnpm build` sem erros TypeScript/Vite.
5. **Verificação Docker & Proxy**:
   - Rebuild e subida do backend (`docker compose --env-file .env -f compose.yaml up -d --build --wait` em `tcghub/`), validando `/backend/comunidade/deck-favorito/pesquisa` e `/backend/comunidade/deck-favorito/catalogo` ao vivo.

---

## 7. Estratégia de Commits Incrementais (Conventional Commits em Inglês)

Após aprovação do planejamento e durante a execução da Etapa 3.2, o `@repo-guardian` consolidará cada fatia validada (sem `git push`):

1. `docs(plan): add Stage 3.2 technical plan and prototypes for F002 Decks favoritos` (`docs/plans/etapa-3-2-decks-favoritos.md`, `docs/prototipos/README.md`, `docs/user-story-map/README.md` se atualizado)
2. `docs(reports): add Stage 3.2 planning audit report` (`docs/reports/etapa-3-2-planejamento.md`)
3. *(Na implementação — Etapa 3)*:
   - `feat(comunidade): implement F002 domain model and CatalogoDeckProxy`
   - `test(bdd): automate F002 Decks favoritos Cucumber StepDefinitions`
   - `feat(infraestrutura): add DFA_DECK_FAVORITO JPA persistence and optcgapi deck catalog adapter`
   - `feat(aplicacao): add CQRS projections and queries for F002 Decks favoritos`
   - `feat(ui-backend): add DeckFavoritoControlador REST endpoints and proxy bean wiring`
   - `feat(frontend): implement F002 favorite decks views, composition modal, and Bandai export`
   - `docs(reports): add Stage 3.2 implementation audit report and AI interaction log`
