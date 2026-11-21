# Protótipos de Alta Fidelidade, Design System e Fluxos de Tela — TCGHub

> [!NOTE]
> Artefato de **DDD Preliminar, Design System e UX** cobrindo integralmente as **4 funcionalidades de Lucas Menezes** (`Perfil e amigos`, `Decks favoritos`, `Avaliação de cartas` e `Abertura preferida do anime One Piece`, incluindo a gestão administrativa em `adm`), em conformidade com [.agents.md](../../.agents.md), [frontend-developer](../../.agents/skills/frontend-developer/SKILL.md) e o Design System Stitch **`Nautical Blue & White Marketplace`** (`projects/18247718655827442438`).

---

## 1. Arquitetura Frontend Standalone (`/frontend`) e Design System Stitch

### 1.1 Stack e Estrutura Feature-Based (`/frontend/src/`)
- **Aplicação Desacoplada**: O frontend vive na raiz em `/frontend/` (independente do módulo Maven `tcghub/`), construído com **React 19.3.0**, **Vite 8.3.0**, **Tailwind CSS v4 (`@tailwindcss/vite`)**, **React Router 8.5.0**, **TanStack Query 5.104.0**, **React Hook Form 7.90.0** e **Zustand 5.1.0** — **sem Bootstrap** e **sem Angular**.
- **Organização de Diretórios**:
  - `src/styles/` — Design tokens CSS + `@theme` Tailwind v4 ([tokens.css](../../frontend/src/styles/tokens.css)) e estilos globais ([globals.css](../../frontend/src/styles/globals.css)).
  - `src/lib/` — Cliente HTTP (`fetch` + CSRF/sessão para `/backend/...`), instância do `QueryClient` e utilitários compartilhados.
  - `src/stores/` — Stores Zustand exclusivamente para estado efêmero de UI (abas ativas, modais abertos, filtros locais).
  - `src/components/layout/` — `AppHeader` (navegação principal + busca global `⌘K` + badge de carrinho + menu do perfil), `MobileDrawer`, `CommunitySubNav` (pílulas da comunidade) e banners de notificação.
  - `src/features/auth/` — Autenticação (`/login`, contexto de sessão e `RequireAuth`).
  - `src/features/community/` — Componentes, hooks TanStack Query e formulários de `PerfilComunitario`, `Amizade`, `DeckFavorito`, `AvaliacaoProduto` e `AberturaPreferida`.
  - `src/routes/` — Mapeamento de rotas de mercado em React Router (`lazy` + `ErrorBoundary`).
- **Integração com BFF (`/backend/...`)**: O servidor Vite (`pnpm dev` na porta `5173`) faz proxy para o `ui-backend` Spring Boot (`http://localhost:8080/backend/...`), preservando os endpoints REST do BFF sem acoplar o roteamento visual do React às convenções internas de backend.

### 1.2 Tabela Completa de Tokens CSS — *Nautical Blue & White Marketplace*

Fonte oficial: Stitch (`projects/18247718655827442438/screens/9084713632167662986` — `DESIGN.md`), implementados em [tokens.css](../../frontend/src/styles/tokens.css) (`:root` + `@theme` Tailwind v4) e [globals.css](../../frontend/src/styles/globals.css).

#### Cores Semânticas e de Marca

| Categoria | Token CSS (`:root` / `@theme`) | Valor Hex | Papel na Interface |
| :--- | :--- | :--- | :--- |
| **Marca Náutica** | `--color-deep-navy` | `#0a2540` | Âncora estrutural: cabeçalhos, abas ativas, texto principal, pílulas de raridade (`LÍDER / SEC`, `SR`) |
| **Marca Náutica** | `--color-ocean-blue` | `#0f4c81` | Ação primária (CTAs: *Adicionar*, *Ver Perfil*, *Salvar Avaliação*), anéis de foco, ícones de destaque |
| **Marca Náutica** | `--color-primary` | `#00355f` | Estado ativo no header superior (`Comunidade`), marca *TCGHub* |
| **Marca Náutica** | `--color-cobalt-accent` | `#2563eb` | Micro-acentos, indicador de aba ativa, indicador online, tendências e links (`Mkt Base`) |
| **Marca Náutica** | `--color-sky-tint` | `#e0ecf8` | Hover suave de botões secundários, badges informativos (`4 de 4 slots preenchidos`, `Colecionador Verificado`) |
| **Superfícies** | `--color-surface` / `--color-background` | `#f6fafe` | Canvas principal da aplicação (`bg-surface`) |
| **Superfícies** | `--color-pure-white` / `--color-surface-container-lowest` | `#ffffff` | Cards de perfil/colecionador/carta, modais, barra de subnavegação (`bg-pure-white`) |
| **Superfícies** | `--color-off-white-bg` / `--color-surface-container-low` | `#f0f4f8` | Campos internos, placeholders de imagem, botões neutros e caixas de resumo |
| **Superfícies** | `--color-surface-container` | `#eaeef2` | Hover de superfícies neutras, badges secundários |
| **Superfícies** | `--color-surface-container-high` / `-highest` | `#e4e9ed` / `#dfe3e7` | Containers terciários e estados pressionados |
| **Superfícies** | `--color-surface-dim` | `#d6dade` | Divisores de baixa ênfase |
| **Superfícies** | `--color-inverse-surface` / `--color-inverse-on-surface` | `#2c3134` / `#edf1f5` | Tooltips e toasts escuros |
| **Texto & Bordas** | `--color-text-primary` / `--color-on-surface` | `#0a2540` / `#171c1f` | Títulos e texto principal |
| **Texto & Bordas** | `--color-text-muted` / `--color-on-surface-variant` | `#475569` / `#42474f` | Bios, subtítulos, metadados e rótulos secundários |
| **Texto & Bordas** | `--color-secondary` | `#49607e` | Ícones auxiliares e textos de apoio na subnavegação |
| **Texto & Bordas** | `--color-outline` / `--color-outline-variant` | `#727780` / `#c2c7d1` | Placeholders e contornos de suporte |
| **Texto & Bordas** | `--color-ice-border` / `--color-steel-border` | `#e2e8f0` / `#cbd5e1` | Bordas 1px de cards (`border-ice-border`) e anéis de inputs (`ring-steel-border`) |
| **Status Monocromático** | `--color-status-positive-bg` / `-text` | `#ebf5ff` / `#1d4ed8` | Elegível, *Amigo Mútuo*, *Compra Verificada*, *Em Estoque* |
| **Status Monocromático** | `--color-status-warning-bg` / `-text` | `#f0f7ff` / `#1e40af` | Avisos de duplicidade já existente, baixa disponibilidade |
| **Status Monocromático** | `--color-status-critical-bg` / `-text` | `#0a2540` / `#ffffff` | Alertas críticos de alto contraste |
| **Erro de Domínio** | `--color-error` / `-container` / `--color-on-error-container` | `#ba1a1a` / `#ffdad6` / `#93000a` | Validações rejeitadas (autoamizade, apelido em uso, nota inválida) |

#### Tipografia, Espaçamento, Raios e Elevação

| Grupo | Tokens Principais | Valores | Uso |
| :--- | :--- | :--- | :--- |
| **Famílias** | `--font-display`, `--font-sans` | `Montserrat`, `Inter` | Títulos geométricos (`Montserrat`) + corpo/UI/tabelas (`Inter`) |
| **Display & Títulos** | `display-lg`, `headline-xl`, `headline-lg`, `headline-md`, `headline-sm` | `48/56px (800)`, `36/44px (700)`, `24/32px (700)`, `20/28px (600)`, `16/24px (600)` | Títulos de página, seções, modais e cards |
| **Corpo & Rótulos** | `body-lg`, `body-md`, `body-sm`, `label-md`, `label-sm` | `16/24px (400)`, `14/20px (400)`, `12/16px (400)`, `14/20px (500)`, `12/16px (600)` | Descrições, abas, botões, chips e mensagens de erro |
| **Numéricos Tabulares** | `tabular-price`, `tabular-stat`, `.tabular-nums` | `18/24px (700)`, `14/20px (600)` + `tabular-nums` | Preços (`R$ 1.850,00`), códigos (`OP01-001`), contagens e rankings |
| **Espaçamento (Base 8pt)** | `space-xs..xxl`, `gutter`, `gutter-md`, `margin`, `margin-md`, `margin-lg` | `0.25rem` (`4px`), `0.5rem`, `1rem`, `1.5rem`, `2rem`, `3rem` | Ritmo interno de cards, grids (`gap-space-md`) e container (`max-w-7xl px-margin md:px-margin-lg`) |
| **Raios** | `--radius-sm`, `--radius-md`, `--radius-lg`, `--radius-xl`, `--radius-2xl`, `--radius-full` | `0.25rem`, `0.375rem` (`6px`), `0.5rem` (`8px`), `0.75rem` (`12px`), `1rem` (`16px`), `9999px` | Molduras de cartas (`6px`), botões/inputs (`8px`), cards (`12px`/`16px`), chips (`full`) |
| **Sombras Náuticas** | `--shadow-level-1` .. `--shadow-level-4` | Níveis 1–4 com tint `#0a2540` | Repouso de card, hover elevado, drawer lateral e backdrop/modal |

---

## 2. Mapeamento: Telas do Drive (`requisitos`) ↔ Stitch Screen IDs ↔ Rotas/Componentes `/frontend/src/`

| Protótipo (Drive) | Título / Fluxo | Stitch Screen ID (`projects/18247718655827442438/screens/...`) | Rota Frontend (`/frontend`) | Componente(s) React (`src/...`) & Endpoints BFF |
| :--- | :--- | :--- | :--- | :--- |
| *(Acesso)* | Autenticação do usuário | — | `/login` | `features/auth/LoginPage.tsx` (`GET/POST /backend/autenticacao`) |
| **P01** | Visão geral do perfil (próprio e público) | `a20513d71abd449ba2b482f6bcc24089` (`comunidade_perfil_rede_amigos.html`) / `9b383bb48db94fe485d3e71979323c57` (`perfil_mugiwara_luffy.html`) | `/comunidade/perfil`<br>`/comunidade/perfil/:id` | `features/community/profile/ProfilePage.tsx`, `ProfileHeaderCard.tsx`, `CommunitySubNav.tsx`<br>BFF: `GET /backend/comunidade/perfil-comunitario/visualizacao`, `GET .../:id/visualizacao` |
| **P02** | Edição do perfil (Modal) | `14474086553e45008b8331db0009b212` (`meu_perfil_modal_editar.html`) | Modal em `/comunidade/perfil` | `features/community/profile/EditProfileModal.tsx`<br>BFF: `GET .../:id/alteracao`, `POST .../:id/alterar` |
| **P03** | Cartas favoritas preenchidas (4 slots) | `a20513d71abd449ba2b482f6bcc24089` | `/comunidade/perfil` | `features/community/profile/FavoriteCardsGrid.tsx`<br>BFF: `POST .../:id/remover-destaque` |
| **P04** | Espaço disponível nas cartas favoritas | `a20513d71abd449ba2b482f6bcc24089` (estado `< 4` slots) | `/comunidade/perfil` | `features/community/profile/FavoriteCardsGrid.tsx` |
| **P05** | Seleção de carta favorita (Modal) | `a20513d71abd449ba2b482f6bcc24089` (modal *Escolher Carta Favorita*) | Modal em `/comunidade/perfil` | `features/community/profile/SelectFavoriteCardModal.tsx`<br>BFF: `POST .../:id/fixar-destaque` |
| **P19** | Buscar colecionadores e solicitar amizade | `41b6841da5df4aa1933c977876f1b673` (`buscar_colecionadores.html`) | `/comunidade/colecionadores` | `features/community/friends/FindCollectorsPage.tsx`<br>BFF: `GET /backend/comunidade/amizade/inclusao`, `POST /backend/comunidade/amizade/incluir` |
| **P20** | Rede de amigos e solicitações pendentes | `f30b49e5ac5b47f793be5f623ad26d43` (`rede_de_amigos.html`) | `/comunidade/amigos` | `features/community/friends/FriendsNetworkPage.tsx`<br>BFF: `GET /backend/comunidade/amizade/pesquisa`, `POST .../aceitar`, `recusar`, `cancelar`, `remover` |
| **P06** | Perfil com decks e wishlist | `a778927205374bb68481d5bb1f1dc11d` / `9b383bb48db94fe485d3e71979323c57` | `/comunidade/perfil` | `ProfilePage.tsx` (seção 2 colunas: `FavoriteDecksSection` + `PublicWishlistCard`) |
| **P07** | Lista de decks favoritos | `a778927205374bb68481d5bb1f1dc11d` | `/comunidade/decks-favoritos` | `features/community/decks/FavoriteDecksPage.tsx`<br>BFF: `GET /backend/comunidade/deck-favorito/pesquisa`, `POST .../:id/excluir`, `ocultar`, `reexibir` |
| **P08** | Adicionar deck favorito (Modal) | `a778927205374bb68481d5bb1f1dc11d` (modal *Adicionar Deck Favorito*) | Modal em `/comunidade/decks-favoritos` | `features/community/decks/AddFavoriteDeckModal.tsx`<br>BFF: `GET /backend/comunidade/deck-favorito/inclusao`, `POST .../incluir` |
| **P09** | Composição do deck favorito (Modal) | `d26c9d5f978f422fa021153ec9cfe885` | Modal em `/comunidade/decks-favoritos` e `/comunidade/perfil/:id` | `features/community/decks/DeckCompositionModal.tsx`<br>BFF: `GET /backend/comunidade/deck-favorito/:id/visualizacao` |
| **P10** | Coleção pessoal como entrada da avaliação | `7e79485c587349ceab386d8665df5bbc` / `8bd5dfb86e354c6ab97f7f7e8b426090` | `/minha-colecao` *(entrada de apoio)* | `features/community/reviews/CollectionReviewEntryCard.tsx` |
| **P11** | Detalhes da carta e avaliações | `21c3501f0e844d2388377f55414687b4` | `/catalogo/cartas/:id` *(entrada de apoio)* | `features/community/reviews/ProductReviewsSection.tsx` |
| **P12** | Avaliações da comunidade | `21c3501f0e844d2388377f55414687b4` | Seção em `/catalogo/cartas/:id` | `features/community/reviews/ProductReviewsSection.tsx`<br>BFF: `GET /backend/comunidade/avaliacao-produto/pesquisa` |
| **P13** | Nova avaliação (Modal) | `c10dd91737b54bf49b75fb52fefae957` | Modal em `/minha-colecao` ou `/catalogo/cartas/:id` | `features/community/reviews/ReviewFormModal.tsx`<br>BFF: `GET /backend/comunidade/avaliacao-produto/inclusao`, `POST .../incluir` |
| **P14** | Atualização da avaliação (Modal) | `233e6f9b6f424215954a85c92d59f53e` | Modal em `/minha-colecao` ou `/catalogo/cartas/:id` | `features/community/reviews/ReviewFormModal.tsx`<br>BFF: `GET .../:id/alteracao`, `POST .../:id/alterar` |
| **P15** | Gestão administrativa das aberturas | `b2a45fd86fd84870b32e71fbd1c43fc4` | `/adm/aberturas` | `features/admin/openings/AdminOpeningsPage.tsx`<br>BFF: `GET /backend/adm/abertura/pesquisa`, `POST .../:id/{inativar,reativar,excluir}` |
| **P16** | Cadastro/edição administrativo de abertura (Modal) | `b2a45fd86fd84870b32e71fbd1c43fc4` (modal *Cadastrar abertura*) | Modal em `/adm/aberturas` | `features/admin/openings/OpeningFormModal.tsx`<br>BFF: `GET/POST /backend/adm/abertura/{inclusao,incluir,:id/alteracao,:id/alterar}` |
| **P17** | Catálogo e ranking de aberturas | `4734db8f5e32474ca6da6c27f95b7ada` / `8a09531ad18f495bbc2f422ed9d3f34d` | `/comunidade/abertura-preferida` | `features/community/openings/OpeningsCatalogPage.tsx`<br>BFF: `GET /backend/comunidade/abertura-preferida/{pesquisa,ranking}`, `POST .../{incluir,ocultar,reexibir}` |
| **P18** | Detalhes da abertura preferida | `eb3be1f6e46e4ad8ae22f2fb8b39731e` | `/comunidade/abertura-preferida/:id` | `features/community/openings/OpeningDetailsPage.tsx`<br>BFF: `GET /backend/comunidade/abertura-preferida/:id/visualizacao` |

---

## 3. Protótipos de Alta Fidelidade — `comunidade / F001`: Perfil e Amigos

### Protótipo 1 — Visão Geral do Perfil (`P01`)
- **Rota Frontend**: `/comunidade/perfil` (próprio) e `/comunidade/perfil/:id` (visitante, ex.: `@MugiwaraLuffy` em `perfil_mugiwara_luffy.html`).
- **Operações BFF**: `GET /backend/comunidade/perfil-comunitario/visualizacao` | `GET /backend/comunidade/perfil-comunitario/:id/visualizacao`.
- **Layout e Componentes (Tailwind v4 + Nautical Design System)**:
  - **Barra Superior (`AppHeader`)**: Fundo `bg-pure-white/90 backdrop-blur-xl`, logo `TCGHub`, links (`Catálogo`, `Decks`, `Comunidade` ativo em `bg-primary text-pure-white` com sublinhado `bg-cobalt-accent`, `Wishlist`), busca global `⌘K`, carrinho e botão de avatar; no mobile, menu abre `MobileDrawer`.
  - **Sub-navegação Comunitária (`CommunitySubNav`)**: Pílulas em container `bg-pure-white rounded-xl p-space-sm border-ice-border/60`: `Meu Perfil` (ativo em `bg-deep-navy text-pure-white`), `Minha Coleção`, `Rede de Amigos (N)`, `Buscar Colecionadores`, `Abertura Preferida`.
  - **Card de Cabeçalho do Perfil**: `bg-pure-white rounded-xl p-space-lg shadow-sm border-ice-border/60`, avatar circular `w-20 h-20` com indicador online `bg-cobalt-accent`, `h1` `font-headline-lg text-deep-navy` (`TrafalgarAce_99`) + pílula `bg-sky-tint text-ocean-blue` (`Colecionador Verificado`), bio em `text-text-muted`, metadados (`Membro desde ...`, `Cidade`, `Avaliação`), e à direita botões `Editar Perfil` + `share` (ou, para visitante, badge `Amigo Mútuo` / ações de amizade e `Ver Wishlist`).
  - **Seção `Cartas Favoritas`**: Subtítulo `"Destaques da coleção pessoal expostos na comunidade (Máximo de 4 slots estritos)"`, badge `X de 4 slots preenchidos`, botão `Gerenciar Slots` (proprietário) e grid responsivo `grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-md`.
  - **Seção Inferior 2 Colunas**: Esquerda `Decks Favoritos`; Direita `Wishlist Pública` (quando habilitada).
- **Estados**:
  - **Principal**: Todos os blocos visíveis conforme autorização.
  - **Vazio**: Bio vazia exibe placeholder suave; 0 destaques exibe 4 slots pontilhados para o proprietário ou estado informativo para visitante.
  - **Carregamento**: Skeleton em `bg-surface-container-low` enquanto TanStack Query carrega.
  - **Erro**: `:id` inexistente exibe tela de erro tratada (`Perfil comunitário não encontrado`) com atalho de retorno.
  - **Privacidade / Proxy (`PerfilComunitarioVisibilidadeProxy`)**: Visitante não visualiza controles de edição, remoção de destaque nem `Gerenciar Slots`. Se `decksFavoritosVisiveis == false` ou `aberturaPreferida == OCULTA`, o Proxy omite esses blocos para visitantes.

### Protótipo 2 — Edição do Perfil (`P02`)
- **Rota Frontend**: Modal aberto sobre `/comunidade/perfil` (Stitch `meu_perfil_modal_editar.html` — `14474086553e45008b8331db0009b212`).
- **Operações BFF**: `GET /backend/comunidade/perfil-comunitario/:id/alteracao` e `POST /backend/comunidade/perfil-comunitario/:id/alterar`.
- **Layout e Campos (`React Hook Form`)**:
  - Modal sobre backdrop `bg-deep-navy/55 backdrop-blur-sm` (`shadow-level-4`), título `Editar Perfil`.
  - **Foto de Perfil**: Prévia circular, aviso `"PNG, JPG ou GIF recomendados até 5MB"`, botões `Alterar Foto` e `Remover`.
  - **Apelido Comunitário** (`@`): Campo texto obrigatório com prefixo `@` e helper `"Seu identificador único para amigos, trocas e cotações."`.
  - **Cidade e Estado**: Input com ícone de localização (`São Paulo, SP`).
  - **Bio / Descrição de Perfil**: `textarea` com contador ao vivo `X / 200`.
  - **Visibilidade e Preferências**: Checkboxes `Wishlist pública com token seguro` e `Exibir Decks Favoritos no perfil`.
  - **Rodapé**: `Cancelar` (ghost, fecha sem mutação preservando estado anterior) e `Salvar Alterações` (`bg-ocean-blue hover:bg-deep-navy text-pure-white`).
- **Validações e Erros**:
  - Apelido vazio → erro em linha (`text-error`).
  - Apelido já usado por outro usuário (inclusive variação de caixa, ex.: `ZoroTC` vs. `zorotc`) → banner/toast em `bg-error-container text-on-error-container`.
  - Tentativa de editar `:id` de outro usuário → rejeitada pelo `PerfilComunitarioVisibilidadeProxy` (`403`).

### Protótipo 3 — Cartas Favoritas Preenchidas (`P03`)
- **Rota Frontend**: Seção `Cartas Favoritas` em `/comunidade/perfil`.
- **Operações BFF**: `POST /backend/comunidade/perfil-comunitario/:id/remover-destaque`.
- **Layout e Componentes**:
  - 4 cards com proporção `aspect-[2.5/3.5]` (`rounded-lg`), chip top-left `bg-deep-navy text-pure-white` (`LÍDER / SEC`, `SR`, `SEC`), chip top-right `bg-pure-white/95 text-ocean-blue` (`Edição Colecionador`, `Holo Foil`, `Art Alternativa`, `Near Mint`), botão circular `×` (hover `bg-error`, somente proprietário), tag de cor bottom-left (`Vermelho`, `Verde`, `Azul`), nome em `font-headline-sm`, código (`OP01-001`), preço em `font-tabular-price` (`R$ 1.850,00`), selo `Mkt Base`, botão `Ver no Catálogo` e atalho de bookmark.

### Protótipo 4 — Espaço Disponível nas Cartas Favoritas (`P04`)
- **Rota Frontend**: Seção `Cartas Favoritas` em `/comunidade/perfil` quando há `< 4` slots ocupados.
- **Layout e Componentes**:
  - Contador atualizado para `X de 4 slots preenchidos`.
  - Slots livres renderizam card pontilhado (`border-2 border-dashed border-steel-border bg-surface-container-low/60`) com ícone `+`, título `"Slot Disponível"`, subtítulo `"Toque para adicionar da coleção"` e CTA `+ Adicionar Carta` em `bg-ocean-blue text-pure-white`.

### Protótipo 5 — Seleção de Carta Favorita (`P05`)
- **Rota Frontend**: Modal `Escolher Carta Favorita` acionado em `/comunidade/perfil`.
- **Operações BFF**: `POST /backend/comunidade/perfil-comunitario/:id/fixar-destaque`.
- **Layout e Componentes**:
  - Banner informativo `bg-status-positive-bg text-status-positive-text`: `"Regra de Negócio (4 Slots Estritos): Selecione uma carta da sua coleção para alocar no slot disponível da vitrine comunitária."`
  - Lista `CARTAS DISPONÍVEIS DA COLEÇÃO` com miniatura, código • raridade • cor • preço tabular e botão `Fixar Slot`.
  - Rodapé `Cancelar`.
- **Validações e Erros**: Rejeita se limite de 4 já atingido ou carta duplicada (`bg-error-container text-on-error-container`).

### Protótipo 19 — Buscar Colecionadores e Solicitar Amizade (`P19`)
- **Rota Frontend**: `/comunidade/colecionadores` (Stitch `buscar_colecionadores.html` — `41b6841da5df4aa1933c977876f1b673`).
- **Operações BFF**: `GET /backend/comunidade/amizade/inclusao?termo=...` e `POST /backend/comunidade/amizade/incluir`.
- **Layout e Componentes**:
  - Layout em 12 colunas (`lg:col-span-7` busca + painel dinâmico de validação + explicação de conexão bilateral; `lg:col-span-5` sugestões/resultados).
  - Input de busca por `@apelido` ou cidade com botão `Adicionar` (`bg-ocean-blue text-pure-white`) e lista de sugestões com avatar, apelido, cidade e status da relação (`NENHUMA`, `PENDENTE_ENVIADA`, `PENDENTE_RECEBIDA`, `AMIGO`).
  - Painel de regras/feedback usando os tokens semânticos Stitch:
    - Auto-adição bloqueada: `bg-error-container text-on-error-container` (`block`)
    - Solicitação duplicada ou amizade já existente: `bg-status-warning-bg text-status-warning-text` (`warning`)
    - Colecionador elegível: `bg-status-positive-bg text-status-positive-text` (`check_circle`)
- **Estados**: Estado vazio quando nenhum colecionador corresponde ao termo; rejeição explícita para usuário inexistente.

### Protótipo 20 — Rede de Amigos e Solicitações Pendentes (`P20`)
- **Rota Frontend**: `/comunidade/amigos` (Stitch `rede_de_amigos.html` — `f30b49e5ac5b47f793be5f623ad26d43`).
- **Operações BFF**: `GET /backend/comunidade/amizade/pesquisa`, `POST /backend/comunidade/amizade/:id/{aceitar,recusar,cancelar,remover}`.
- **Layout e Componentes**:
  - Barra de filtros em pílulas (`Meus Amigos (N)`, `Solicitações Recebidas (N)`, `Solicitações Enviadas (N)`), filtro por `@apelido` e seletor de ordenação.
  - Grid `grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6` de cards `rounded-2xl bg-pure-white p-5 shadow-sm`:
    - **Meus Amigos**: Avatar, `@apelido`, selo `verified`, botão `Ver Perfil` (`bg-ocean-blue text-pure-white`) e ícone `person_remove` (`hover:bg-error-container/30 hover:text-error`) abrindo modal de confirmação de **remoção bilateral** (apaga vínculo dos dois lados).
    - **Recebidas**: Botões `Aceitar` (`bg-ocean-blue text-pure-white`) e `Recusar` (ghost/outline).
    - **Enviadas**: Status `Pendente` e botão `Cancelar Solicitação`.
- **Estados**: Estados vazios distintos por sub-aba; erros de autorização (ex.: aceitar/cancelar solicitação de terceiro) exibidos em `bg-error-container`.

---

## 4. Protótipos de Alta Fidelidade — `comunidade / F002`: Decks Favoritos

### Protótipo 6 — Perfil com Decks e Wishlist (`P06`)
- **Rota Frontend**: `/comunidade/perfil` e `/comunidade/perfil/:id` (Stitch `a778927205374bb68481d5bb1f1dc11d` / `9b383bb48db94fe485d3e71979323c57`).
- **Layout**: Abaixo de `Cartas Favoritas`, grid de 2 colunas: esquerda `Decks Favoritos` (até 4 decks no formato oficial `1 Líder + 50 Cartas`) e direita `Wishlist Pública` (link tokenizado `tcghub.com/w/...` + botão `Copiar`).
- **Privacidade / Proxy (`PerfilComunitarioVisibilidadeProxy`)**: Quando `decksFavoritosVisiveis == false`, visitantes não veem o bloco de decks favoritos no perfil público; o proprietário continua vendo com selo `Oculto no perfil público`.

### Protótipo 7 — Lista de Decks Favoritos (`P07`)
- **Rota Frontend**: `/comunidade/decks-favoritos`.
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/pesquisa`, `POST /backend/comunidade/deck-favorito/:id/excluir`, `POST /backend/comunidade/deck-favorito/ocultar`, `POST /backend/comunidade/deck-favorito/reexibir`.
- **Layout**: Cabeçalho com badge `X/4 decks favoritos`, toggle `Exibir no Perfil Público` (`ocultar`/`reexibir`) e botão `+ Adicionar deck favorito` (desabilitado em `4/4`). Lista com miniatura do líder, nome do deck, badge `50 + 1`, `Líder: ...`, custo em `tabular-price`, botão `Visualizar deck` e botão `Remover`.
- **Estados**: Deck tornado `INATIVO` no Catálogo após favoritado permanece visível para quem já o favoritou com badge `Inativo no Catálogo`; estado vazio orienta inclusão de até 4 decks.

### Protótipo 8 — Adicionar Deck Favorito (`P08`)
- **Rota Frontend**: Modal `Adicionar Deck Favorito` acionado em `/comunidade/decks-favoritos`.
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/inclusao` e `POST /backend/comunidade/deck-favorito/incluir`.
- **Layout**: Banner `"Limite oficial: máximo de 4 decks favoritos • X de 4 slots ocupados"`, campo de busca `"Buscar deck por nome, líder ou código..."`, lista de decks **ativos** via `CatalogoDeckProxy` com `✓ Adicionado` (desabilitado) ou `+ Adicionar`, e rodapé `"Formato: 1 Líder + 50 Cartas"` + `Cancelar`.
- **Erros e Proxy (`CatalogoDeckProxy`)**: Rejeita 5º deck, duplicata, deck inativo ou inexistente; se a SPI do Catálogo estiver indisponível, exibe banner de indisponibilidade sem quebrar a tela.

### Protótipo 9 — Composição do Deck Favorito (`P09`)
- **Rota Frontend**: Modal amplo `Composição do Deck` (Stitch `d26c9d5f978f422fa021153ec9cfe885`).
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/:id/visualizacao`.
- **Layout**: Cabeçalho com nome do deck, líder, badge `51 cartas (1 Líder + 50 Cartas)`, pílulas `1 Líder`, `Personagens`, `Eventos`, `Estágios`, `Custo Estimado no Mercado` em `tabular-price`, lista agrupada com quantidade (`x1 Líder`, `x4 no Deck`), preço unitário, link `Ver no Catálogo →` e rodapé com `Fechar` e `Copiar Decklist`.
- **Proxy (`CatalogoDeckProxy`)**: Carrega via SPI do Catálogo sem acessar banco de `loja`; exibe fallback tratável quando o serviço de Catálogo estiver indisponível.

---

## 5. Protótipos de Alta Fidelidade — `comunidade / F003`: Avaliação de Cartas

### Protótipo 10 — Coleção Pessoal como Entrada da Avaliação (`P10`)
- **Rota Frontend**: `/minha-colecao` (entrada de apoio — Stitch `7e79485c587349ceab386d8665df5bbc` / `8bd5dfb86e354c6ab97f7f7e8b426090`).
- **Layout**: Cards da coleção exibindo nota comunitária (`4.9 (42)` ou `Sem avaliações`), badge `(Sua: 5.0)` + ícone editar (abre `P14`) ou atalho `Avaliar` (abre `P13`).

### Protótipo 11 — Detalhes da Carta e Avaliações (`P11`)
- **Rota Frontend**: `/catalogo/cartas/:id` (entrada de apoio — Stitch `21c3501f0e844d2388377f55414687b4`).
- **Layout**: Ficha da carta (especificações, preço, estoque, gráfico de mercado) conectada ao bloco inferior `Avaliações da Comunidade` (`P12`) com botão `Avaliar Carta` / `Editar Minha Avaliação`.

### Protótipo 12 — Avaliações da Comunidade (`P12`)
- **Rota Frontend**: Seção integrada em `/catalogo/cartas/:id`.
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/pesquisa?produtoId=...`.
- **Layout**: Card esquerdo com média em destaque (`4.9` ou `0.0`), 5 estrelas e total de avaliações válidas; histograma `5★` a `1★` à direita; lista de comentários com selo obrigatório **`Compra Verificada`** (`bg-status-positive-bg text-status-positive-text`), data e nota.
- **Regras**: Avaliações cujo único pedido elegível foi reembolsado deixam de constar na média e na lista automaticamente.

### Protótipo 13 — Nova Avaliação (`P13`)
- **Rota Frontend**: Modal `Avaliar Carta` (Stitch `c10dd91737b54bf49b75fb52fefae957`).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/inclusao` e `POST /backend/comunidade/avaliacao-produto/incluir`.
- **Campos (`React Hook Form`)**: Seletor interativo de **1 a 5 estrelas** obrigatório (`1 <= nota <= 5`), campo opcional `Seu Comentário (opcional)`, botões `Cancelar` e `Salvar Avaliação`.
- **Proxy (`AvaliacaoElegibilidadeProxy`)**: Valida `CompraVerificada` (ao menos um pedido pago e não reembolsado); bloqueia sem compra, pagamento pendente ou único pedido reembolsado.

### Protótipo 14 — Atualização da Avaliação (`P14`)
- **Rota Frontend**: Modal `Sua Avaliação da Carta` (Stitch `233e6f9b6f424215954a85c92d59f53e`).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/:id/alteracao` e `POST /backend/comunidade/avaliacao-produto/:id/alterar`.
- **Layout e Regras**: Exibe nota atual (`Avaliação Enviada 5.0 ★`), data, estrelas editáveis, comentário editável, `Cancelar` (preserva versão anterior) e `Atualizar Avaliação`. Atualiza de forma idempotente sem duplicar contagem na média; `AvaliacaoElegibilidadeProxy` impede edição por outro usuário (`403`).

---

## 6. Protótipos de Alta Fidelidade — `adm / F001` e `comunidade / F004`: Abertura Preferida de One Piece

### Protótipo 15 — Gestão Administrativa das Aberturas (`P15`)
- **Rota Frontend**: `/adm/aberturas` (Stitch `b2a45fd86fd84870b32e71fbd1c43fc4`).
- **Operações BFF**: `GET /backend/adm/abertura/pesquisa`, `POST /backend/adm/abertura/:id/{inativar,reativar,excluir}`.
- **Layout**: Visível apenas para `ADMINISTRADOR`; breadcrumb `Início > Mídia & Catálogo > Aberturas de One Piece`, botão `+ Cadastrar abertura`, pílulas `Todas (N)`, `Ativas (N)`, `Inativas (N)`, busca por número/nome, tabela com cabeçalho `bg-deep-navy text-pure-white` (`MÍDIA`, `Nº`, `NOME DA ABERTURA`, `ARTISTA`, `EPISÓDIOS`, `PREFERÊNCIAS`, `STATUS`, `AÇÕES`) e paginação.
- **Regras**: Rejeita exclusão física quando `preferencias > 0`, orientando o uso de `Inativar`.

### Protótipo 16 — Cadastro Administrativo de Abertura (`P16`)
- **Rota Frontend**: Modal em `/adm/aberturas`.
- **Operações BFF**: `GET/POST /backend/adm/abertura/{inclusao,incluir,:id/alteracao,:id/alterar}`.
- **Campos (`React Hook Form`)**: `Número da Abertura *` (`> 0`, único), `Nome da Abertura *`, `Artista / Intérprete *`, `Ep. Inicial *`, `Ep. Final *`, `URL da Imagem de Capa (Aspecto 16:9)` + botão `Testar` + caixa de prévia, `Referência de Vídeo Público (YouTube / URL)`, checkbox `Ativa para novas escolhas`, botões `Cancelar` e `Cadastrar abertura`.

### Protótipo 17 — Catálogo e Ranking de Aberturas (`P17`)
- **Rota Frontend**: `/comunidade/abertura-preferida` (Stitch `4734db8f5e32474ca6da6c27f95b7ada` / `8a09531ad18f495bbc2f422ed9d3f34d`).
- **Operações BFF**: `GET /backend/comunidade/abertura-preferida/{pesquisa,ranking}`, `POST /backend/comunidade/abertura-preferida/{incluir,ocultar,reexibir}`.
- **Layout**:
  - **Banner Superior**: Abertura atualmente escolhida (`Op. 01 — We Are!`), contagem de amigos, botão `Ver Detalhes` e switch `Exibir no Perfil Público`.
  - **Catálogo Ativo (Esquerda)**: Busca por nome, número ou artista; pílulas `Todas as Aberturas`, `Escolhidas por Amigos`, `Top Ranking`; grid apenas com aberturas **`ATIVAS`**, mostrando capa 16:9, votos, amigos, botão `Definir como Preferida` / `✓ Ativa no Perfil` e ícone de privacidade.
  - **Sidebar Direita (`RANKING DA COMUNIDADE — Top 5 Escolhas Oficiais`)**: `Total de Votos Ativos`, barras proporcionais `#1`–`#5` com percentual e contagem, e aviso de que escolhas ocultas somam no cálculo sem expor o usuário (`AberturaPreferidaPrivacidadeProxy`).

### Protótipo 18 — Detalhes da Abertura Preferida (`P18`)
- **Rota Frontend**: `/comunidade/abertura-preferida/:id` (Stitch `eb3be1f6e46e4ad8ae22f2fb8b39731e`).
- **Operações BFF**: `GET /backend/comunidade/abertura-preferida/:id/visualizacao`.
- **Layout**: Hero cinematográfico com capa, número, arco/episódios, artista, botão `▶ Assistir Abertura`, botão `☆ Sua Preferida (Ativa)` e 3 cards inferiores (`Amigos que Escolheram` filtrado pelo `AberturaPreferidaPrivacidadeProxy`, `Posição Global` no ranking e `Cronologia do Anime`).

---

## 7. Matriz de Rastreabilidade (Protótipos ↔ Especificações `RN00X` ↔ Cenários BDD)

| Protótipo | Funcionalidade / Subdomínio | Rota Frontend (`/frontend`) | Regras de Negócio (`tcghub/docs/.../funcionalidades/`) | Cenários BDD (`.feature`) Relacionados |
| :--- | :--- | :--- | :--- | :--- |
| **P01** — Visão geral do perfil | `comunidade / F001` | `/comunidade/perfil`, `/comunidade/perfil/:id` | `comunidade/F001`: `RN001`, `RN002`, `RN003` | `perfil-e-amigos.feature`: Consultar próprio perfil, Consultar perfil público, Rejeitar perfil inexistente |
| **P02** — Edição do perfil | `comunidade / F001` | Modal em `/comunidade/perfil` | `comunidade/F001`: `RN001`, `RN002` | `perfil-e-amigos.feature`: Editar dados, Cancelar edição, Manter próprio apelido, Rejeitar apelido duplicado (*case-insensitive*), Rejeitar apelido vazio, Rejeitar edição de outro usuário |
| **P03** — Cartas favoritas preenchidas | `comunidade / F001` | `/comunidade/perfil` | `comunidade/F001`: `RN002`, `RN003` | `perfil-e-amigos.feature`: Não fixar mais de 4 cartas, Remover carta dos destaques |
| **P04** — Espaço disponível nas cartas favoritas | `comunidade / F001` | `/comunidade/perfil` | `comunidade/F001`: `RN003` | `perfil-e-amigos.feature`: Remover carta libera espaço para fixar outra |
| **P05** — Seleção de carta favorita | `comunidade / F001` | Modal em `/comunidade/perfil` | `comunidade/F001`: `RN002`, `RN003` | `perfil-e-amigos.feature`: Fixar carta da coleção, Rejeitar >4 ou duplicata |
| **P19** — Buscar colecionadores e solicitar amizade | `comunidade / F001` | `/comunidade/colecionadores` | `comunidade/F001`: `RN004` | `perfil-e-amigos.feature`: Enviar solicitação, Rejeitar autoamizade, Rejeitar inexistente, Rejeitar solicitação duplicada, Rejeitar amizade já ativa |
| **P20** — Rede de amigos e solicitações pendentes | `comunidade / F001` | `/comunidade/amigos` | `comunidade/F001`: `RN005`, `RN006` | `perfil-e-amigos.feature`: Aceitar solicitação (bilateral), Rejeitar aceitação por terceiro, Recusar, Cancelar, Rejeitar cancelamento por terceiro, Remover bilateralmente, Rejeitar remoção de não amigo |
| **P06** — Perfil com decks e wishlist | `comunidade / F001` + `F002` | `/comunidade/perfil`, `/comunidade/perfil/:id` | `comunidade/F001`: `RN002`; `comunidade/F002`: `RN001`, `RN003`, `RN004` | `decks-favoritos.feature`: Ocultar e reexibir decks favoritos no perfil público |
| **P07** — Lista de decks favoritos | `comunidade / F002` | `/comunidade/decks-favoritos` | `comunidade/F002`: `RN001`, `RN002`, `RN004`, `RN005` | `decks-favoritos.feature`: Remover deck libera vaga, Rejeitar remoção de não favorito, Preservar deck tornado inativo, Ocultar/reexibir, Rejeitar gestão de outro usuário |
| **P08** — Adicionar deck favorito | `comunidade / F002` | Modal em `/comunidade/decks-favoritos` | `comunidade/F002`: `RN001`, `RN002`, `RN005` | `decks-favoritos.feature`: Pesquisar decks disponíveis, Adicionar até 4, Rejeitar 5º deck, Rejeitar duplicata, Rejeitar deck inativo/inexistente |
| **P09** — Composição do deck favorito | `comunidade / F002` | Modal em `/comunidade/decks-favoritos` | `comunidade/F002`: `RN003` | `decks-favoritos.feature`: Visualizar composição via interface do Catálogo, Tratar indisponibilidade do Catálogo |
| **P10** — Coleção pessoal (entrada avaliação) | `comunidade / F003` *(apoio)* | `/minha-colecao` | `comunidade/F003`: `RN001`, `RN002` | `avaliacao-de-cartas.feature`: Registrar avaliação, Reavaliar carta existente |
| **P11** — Detalhes da carta e avaliações | `comunidade / F003` *(apoio)* | `/catalogo/cartas/:id` | `comunidade/F003`: `RN001`, `RN004`, `RN006` | `avaliacao-de-cartas.feature`: Consultar média e contagem no catálogo |
| **P12** — Avaliações da comunidade | `comunidade / F003` | Seção em `/catalogo/cartas/:id` | `comunidade/F003`: `RN004`, `RN005`, `RN006` | `avaliacao-de-cartas.feature`: Média e contagem, Carta sem avaliações (`0.0`), Selo compra verificada, Invalidação e recálculo após reembolso |
| **P13** — Nova avaliação | `comunidade / F003` | Modal em `/minha-colecao` / `/catalogo/cartas/:id` | `comunidade/F003`: `RN001`, `RN003`, `RN006` | `avaliacao-de-cartas.feature`: Registrar avaliação (com/sem comentário, nota 1–5), Rejeitar sem compra/não pago/reembolsado, Permitir se houver outro pedido pago, Rejeitar nota `<1` ou `>5` |
| **P14** — Atualização da avaliação | `comunidade / F003` | Modal em `/minha-colecao` / `/catalogo/cartas/:id` | `comunidade/F003`: `RN002`, `RN003`, `RN005` | `avaliacao-de-cartas.feature`: Reavaliar sem duplicar contagem, Cancelar edição preserva anterior, Rejeitar alteração de outro usuário, Manter válida se houver outra compra |
| **P15** — Gestão administrativa das aberturas | `adm / F001` | `/adm/aberturas` | `adm/F001`: `RN001`, `RN002`, `RN004`, `RN005`, `RN006` | `abertura.feature`: Consultar por número, Pesquisar/filtrar por status, Inativar, Reativar, Excluir nunca escolhida, Rejeitar exclusão de já escolhida, Rejeitar sem autorização `ADMINISTRADOR` |
| **P16** — Cadastro administrativo de abertura | `adm / F001` | Modal em `/adm/aberturas` | `adm/F001`: `RN001`, `RN002`, `RN003` | `abertura.feature`: Cadastrar ativa/inativa, Editar dados, Rejeitar sem autorização, Rejeitar número duplicado (cadastro/edição), Rejeitar dados inválidos, Rejeitar edição inexistente |
| **P17** — Catálogo e ranking de aberturas | `comunidade / F004` | `/comunidade/abertura-preferida` | `comunidade/F004`: `RN001`, `RN002`, `RN003`, `RN004`, `RN005`, `RN006` | `abertura-preferida.feature`: Listar apenas ativas com contagem de amigos, Pesquisar por nome/artista, Escolher/substituir preferida, Rejeitar inativa/inexistente, Ocultar/reexibir, Ranking Top 5 (com ocultas e vazio), Manter escolha inativada e permitir troca |
| **P18** — Detalhes da abertura preferida | `comunidade / F004` | `/comunidade/abertura-preferida/:id` | `comunidade/F004`: `RN003`, `RN004`, `RN005` | `abertura-preferida.feature`: Consultar detalhes com posição e amigos, Indicar/não indicar/ocultar correspondência no perfil do amigo |

---

## 8. Fluxo de Cadastro/Login Real e Contratos de APIs Externas

### 8.1 Autenticação e Cadastro de Conta (`/login` e `/cadastro`)
- **Rotas Frontend**: `/login` (`LoginPage.tsx`) e `/cadastro` (`RegisterPage.tsx`) com alternância por abas no card náutico (`Entrar` ↔ `Criar Conta`).
- **Operações BFF e Persistência (`USR_USUARIO_CONTA` + `PCM_PERFIL_COMUNITARIO`)**:
  - `POST /backend/autenticacao/cadastro`: recebe `{ usuario, apelido, email, senha, cidade?, descricao? }`, valida unicidade case-insensitive de `usuario`, `email` e `apelido` (`RN001`), grava `UsuarioContaJpa` (`USR_USUARIO_CONTA`) com senha codificada (`PasswordEncoder`) e provisiona automaticamente o `PerfilComunitario` (`PCM_PERFIL_COMUNITARIO`), autenticando a sessão em seguida.
  - `POST /backend/autenticacao` / `GET /backend/autenticacao`: autentica no banco relacional por `usuario` ou `email`.

### 8.2 Contratos Oficiais de Dados Externos
- **Cartas e Decks — One Piece TCG API (`https://optcgapi.com`)**:
  - Endpoints de referência: `/api/sets/card/{card_set_id}/`, `/api/allSetCards/`, `/api/allDecks/`, `/api/decks/{st_id}/`.
  - Campos exibidos nos cards e modais (`P03`–`P05`, `P07`–`P14`): `card_set_id`, `card_name`, `set_id`, `set_name`, `rarity`, `card_color`, `card_type`, `card_cost`, `card_power`, `sub_types`, `counter_amount`, `attribute`, `market_price`, `inventory_price`, `card_image_id`, `card_image` (com fallback SVG náutico offline).
- **Aberturas de One Piece — AnimeThemes API (`https://api.animethemes.moe`)**:
  - Endpoint de referência: `/anime/one_piece?include=animethemes.song.artists,animethemes.animethemeentries.videos,images`.
  - Campos exibidos em `P15`–`P18`: `slug`, `sequence`, `song.title`, `song.artists[].name`, `animethemeentries[].episodes`, `animethemeentries[].videos[].link`, `images[].link`.
