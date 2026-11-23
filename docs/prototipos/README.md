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
| **P06** | Perfil com decks e wishlist | `a778927205374bb68481d5bb1f1dc11d` / `9b383bb48db94fe485d3e71979323c57` | `/comunidade/perfil`<br>`/comunidade/perfil/:id` | `features/community/decks/components/FavoriteDecksProfileCard.tsx` (em `ProfilePage.tsx`)<br>BFF: `GET /backend/comunidade/deck-favorito/perfil/{perfilId}` |
| **P07** | Lista de decks favoritos | `a778927205374bb68481d5bb1f1dc11d` | `/comunidade/decks-favoritos` | `features/community/decks/pages/FavoriteDecksPage.tsx`, `stores/decksUiStore.ts`<br>BFF: `GET /backend/comunidade/deck-favorito/pesquisa`, `POST .../{deckId}/excluir`, `POST .../ocultar`, `POST .../reexibir` |
| **P08** | Adicionar deck favorito (Modal/Drawer) | `a778927205374bb68481d5bb1f1dc11d` (modal *Adicionar Deck Favorito*) | Modal em `/comunidade/decks-favoritos` e `/comunidade/perfil` | `features/community/decks/components/AddFavoriteDeckModal.tsx`<br>BFF: `GET /backend/comunidade/deck-favorito/catalogo?termo=&cor=`, `POST /backend/comunidade/deck-favorito/incluir` |
| **P09** | Composição do deck favorito (Modal) | `d26c9d5f978f422fa021153ec9cfe885` | Modal em `/comunidade/decks-favoritos` e `/comunidade/perfil/:id` | `features/community/decks/components/DeckCompositionModal.tsx`, `utils/bandaiExport.ts`<br>BFF: `GET /backend/comunidade/deck-favorito/{deckId}/composicao` |
| **P10** | Catálogo/Coleção como entrada de avaliações | `7e79485c587349ceab386d8665df5bbc` / `8bd5dfb86e354c6ab97f7f7e8b426090` | `/comunidade/avaliacoes` | `features/community/reviews/pages/CardReviewsCatalogPage.tsx`, `stores/reviewsUiStore.ts`<br>BFF: `GET /backend/comunidade/avaliacao-produto/catalogo?termo=` |
| **P11** | Detalhes da carta e avaliações | `21c3501f0e844d2388377f55414687b4` | `/comunidade/avaliacoes/:produtoId` | `features/community/reviews/pages/CardReviewDetailPage.tsx`<br>BFF: `GET /backend/comunidade/avaliacao-produto/{produtoId}/pesquisa` |
| **P12** | Avaliações da comunidade (média, distribuição e selo) | `21c3501f0e844d2388377f55414687b4` | `/comunidade/avaliacoes/:produtoId` | `features/community/reviews/components/StarRating.tsx`, `RatingDistributionBars.tsx`<br>BFF: `GET /backend/comunidade/avaliacao-produto/{produtoId}/pesquisa`, `POST .../compras/registrar`, `POST .../compras/{pedidoId}/reembolsar` |
| **P13** | Nova avaliação (Modal) | `c10dd91737b54bf49b75fb52fefae957` | Modal em `/comunidade/avaliacoes` e `/comunidade/avaliacoes/:produtoId` | `features/community/reviews/components/ReviewFormModal.tsx`<br>BFF: `POST /backend/comunidade/avaliacao-produto/{produtoId}/avaliar` |
| **P14** | Atualização da avaliação (Modal) | `233e6f9b6f424215954a85c92d59f53e` | Modal em `/comunidade/avaliacoes` e `/comunidade/avaliacoes/:produtoId` | `features/community/reviews/components/ReviewFormModal.tsx`<br>BFF: `POST /backend/comunidade/avaliacao-produto/{produtoId}/avaliar` |
| **P15** | Gestão administrativa das aberturas | `b2a45fd86fd84870b32e71fbd1c43fc4` | `/adm/aberturas` | `features/adm/openings/pages/AdminOpeningsPage.tsx`, `stores/openingsUiStore.ts`<br>BFF: `GET /backend/adm/abertura/pesquisa`, `POST .../{id}/{inativar,reativar,excluir}` |
| **P16** | Cadastro/edição administrativo de abertura (Modal) | `b2a45fd86fd84870b32e71fbd1c43fc4` (modal *Cadastrar abertura*) | Modal em `/adm/aberturas` | `features/adm/openings/components/AdminOpeningFormModal.tsx`<br>BFF: `POST /backend/adm/abertura/incluir`, `POST .../{id}/alterar` |
| **P17** | Catálogo, ranking Top 5 e card no perfil | `4734db8f5e32474ca6da6c27f95b7ada` / `8a09531ad18f495bbc2f422ed9d3f34d` | `/comunidade/aberturas`<br>Card em `/comunidade/perfil` e `/comunidade/perfil/:id` | `features/community/openings/pages/FavoriteOpeningsPage.tsx`, `components/FavoriteOpeningProfileCard.tsx`, `stores/openingsUiStore.ts`<br>BFF: `GET /backend/comunidade/abertura-preferida/{pesquisa,ranking,perfil/:id}`, `POST .../{escolher,ocultar,reexibir}` |
| **P18** | Detalhes da abertura preferida (Modal/Visualização) | `eb3be1f6e46e4ad8ae22f2fb8b39731e` | Modal em `/comunidade/aberturas` (e `/comunidade/perfil`) | `features/community/openings/components/OpeningDetailsModal.tsx`<br>BFF: `GET /backend/comunidade/abertura-preferida/{id}/visualizacao` |

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

## 4. Protótipos de Alta Fidelidade — `comunidade / F002`: Decks Favoritos (Etapa 3.2)

> [!NOTE]
> Especificação detalhada alinhada ao plano técnico [etapa-3-2-decks-favoritos.md](../plans/etapa-3-2-decks-favoritos.md), ao Design System Stitch *Nautical Blue & White Marketplace* (`projects/18247718655827442438`), ao padrão GoF exclusivo **`Proxy`** (`CatalogoDeckProxy` + `PerfilComunitarioVisibilidadeProxy`, **zero `Observer`**) e aos contratos `https://optcgapi.com` (`/api/allDecks/`, `/api/decks/{st_id}/`).

### 4.1 Inventário de Componentes e Estado Efêmero (`frontend/src/`)
- **Store Zustand (`src/stores/decksUiStore.ts`)**: `modalAdicionarAberto`, `deckIdComposicaoAberta`, `termoBuscaCatalogo`, `corFiltroCatalogo`, `filtroTipoComposicao` (`'TODOS' | 'Leader' | 'Character' | 'Event' | 'Stage'`), `ordenacaoComposicao` (`'TIPO' | 'CUSTO'`), ações de controle e `resetarEstadoUi()`.
- **Camada de Dados (`src/features/community/decks/`)**: `types/decks.types.ts`, `api/decksApi.ts`, `api/decksQueries.ts` (`queryOptions` + `useSuspenseQuery` + `useMutation` invalidando `['comunidade', 'deck-favorito']` e `['comunidade', 'perfil-comunitario']`), `utils/bandaiExport.ts`.
- **Navegação (`src/components/layout/CommunitySubNav.tsx` e `src/routes/index.tsx`)**: habilita a pílula **`Decks Favoritos`** apontando para `/comunidade/decks-favoritos` (com redirect legado `/comunidade/deck-favorito/pesquisa` → `/comunidade/decks-favoritos`).

### Protótipo 6 — Perfil com Decks e Wishlist (`P06`)
- **Telas Stitch**: `a778927205374bb68481d5bb1f1dc11d` (*Comunidade: Decks Favoritos & Perfil*) e `9b383bb48db94fe485d3e71979323c57` (*Perfil de @MugiwaraLuffy*).
- **Rota Frontend**: `/comunidade/perfil` e `/comunidade/perfil/:id` (componente `FavoriteDecksProfileCard.tsx` integrado a `ProfilePage.tsx`).
- **Operação BFF**: `GET /backend/comunidade/deck-favorito/perfil/{perfilId}`.
- **Layout e Tokens Stitch**:
  - Abaixo de `Cartas Favoritas`, seção de 2 colunas (`grid-cols-1 lg:grid-cols-12 gap-space-lg`): esquerda `Decks Favoritos` (`bg-pure-white rounded-xl border border-ice-border/60 p-space-lg shadow-sm`), direita `Wishlist Pública`.
  - Cabeçalho com ícone `view_carousel` (`text-ocean-blue`), título `Decks Favoritos` (`font-headline-md text-deep-navy`), pílula `X/4 decks favoritos` (`bg-sky-tint text-ocean-blue`), subtítulo `"Formato Oficial: 1 Líder + 50 Cartas"`, atalho `Gerenciar Decks` (`/comunidade/decks-favoritos`) e botão `+ Adicionar deck favorito` (somente dono, desabilitado em `4/4`).
  - Lista compacta (ou grade 2×2 na visualização pública de amigo) exibindo até 4 decks com miniatura do líder (`aspect-[2.5/3.5]` com fallback SVG náutico), nome, badge `50 + 1`, `Líder: <nome> (<código>)`, custo estimado em `font-tabular-price text-deep-navy`, botão `Ver Composição` / `Visualizar deck` (`bg-ocean-blue hover:bg-deep-navy text-pure-white`) e botão `Remover` (somente dono).
- **Privacidade / Proxy (`PerfilComunitarioVisibilidadeProxy` — `RN004`)**:
  - Quando `PCM_DECKS_FAVORITOS_VISIVEIS == false`, o Proxy retorna lista vazia/oculta para visitantes e o card não expõe os decks a terceiros; para o proprietário, exibe os decks com badge informativo `Oculto no perfil público` (`bg-surface-container text-secondary`) e o toggle de reexibição.

### Protótipo 7 — Lista e Gestão de Decks Favoritos (`P07`)
- **Tela Stitch**: `a778927205374bb68481d5bb1f1dc11d`.
- **Rota Frontend**: `/comunidade/decks-favoritos` (`FavoriteDecksPage.tsx`).
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/pesquisa`, `POST /backend/comunidade/deck-favorito/{deckId}/excluir`, `POST /backend/comunidade/deck-favorito/ocultar`, `POST /backend/comunidade/deck-favorito/reexibir`.
- **Layout e Componentes**:
  - `CommunitySubNav` com aba `Decks Favoritos` ativa (`bg-deep-navy text-pure-white`).
  - **Cabeçalho Operacional**: Contador `X/4 decks favoritos`, **Toggle de Visibilidade Pública** (`role="switch"`, `aria-checked={decksFavoritosVisiveis}`, rótulo `"Exibir no Perfil Público"` com legenda `"Visível para visitantes"` / `"Oculto no perfil público — somente você vê"` disparando `POST /ocultar` ou `POST /reexibir`), e CTA `+ Adicionar deck favorito` (`bg-ocean-blue text-pure-white`, desabilitado quando `total >= 4` com tooltip `"Limite de 4 decks favoritos atingido"`).
  - **Cards de Deck Favorito**: Miniatura do líder, nome (`Straw Hat Aggro Red`), badge `50 + 1`, código/cor do líder (`OP01-001 • Vermelho`), `Custo Estimado` em `font-tabular-price`, botões `Visualizar deck` (abre `P09`) e `Remover` (`text-error hover:bg-error-container/30`).
  - **Badge de Deck Descontinuado / Inativo no Catálogo (`RN002`)**: Quando um deck já favoritado se torna inativo (`ativo === false`), exibe pílula destacada **`Descontinuado / Inativo no Catálogo`** (`bg-status-warning-bg text-status-warning-text border border-steel-border`, ícone `inventory_2`) preservando visualização e remoção pelo dono, enquanto novas inclusões são bloqueadas.
- **Estados**:
  - **Vazio (`0/4`)**: Card pontilhado náutico `"Você ainda não favoritou nenhum deck. Explore o catálogo oficial e fixe até 4 decks competitivos."` + botão `+ Adicionar deck favorito`.
  - **Carregamento**: Skeleton em `bg-surface-container-low` via `Suspense`.
  - **Erro**: Rejeições (`403` usuário não proprietário, `404` deck não favoritado) publicadas no `#tcg-toast` (`useToastStore`).

### Protótipo 8 — Modal/Drawer de Busca de Decks Ativos no Catálogo (`P08`)
- **Tela Stitch**: `a778927205374bb68481d5bb1f1dc11d` (modal *Adicionar Deck Favorito*).
- **Rota Frontend**: Modal/Drawer `AddFavoriteDeckModal.tsx` acionado a partir de `/comunidade/decks-favoritos` ou `/comunidade/perfil`.
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/catalogo?termo=&cor=` (via `CatalogoDeckProxy` / `optcgapi.com/api/allDecks/`) e `POST /backend/comunidade/deck-favorito/incluir` (`{ deckId }`).
- **Layout e Componentes**:
  - Backdrop náutico (`bg-deep-navy/55 backdrop-blur-sm`) e container `bg-pure-white rounded-2xl shadow-level-4`.
  - Cabeçalho: título `Adicionar Deck Favorito`, subtítulo `"Pesquise decks ativos no catálogo oficial Bandai e fixe nos seus favoritos"`, botão fechar (`×`).
  - Banner de limite: `bg-status-positive-bg text-status-positive-text` (ou `bg-status-warning-bg text-status-warning-text` quando `4/4`) exibindo `"Limite oficial: máximo de 4 decks favoritos • X de 4 slots ocupados"`.
  - **Filtros em Tempo Real**:
    - Input de busca: `"Buscar deck por nome, líder ou código (ex: ST-01, Zoro)..."` (`ring-1 ring-steel-border focus:ring-2 focus:ring-ocean-blue`).
    - Chips de filtro por **Cor**: `Todas`, `Vermelho`, `Verde`, `Azul`, `Roxo`, `Preto`, `Amarelo` (ativo em `bg-deep-navy text-pure-white`, padrão em `bg-surface-container-low text-secondary`).
  - **Lista de Decks Ativos**: Cada item exibe miniatura do líder (fallback SVG náutico), nome do deck, código (`ST-01`), cor, `Líder: <nome> (<card_set_id>)`, custo estimado em `font-tabular-price` e ação contextual:
    - Se `jaFavoritado === true`: botão desabilitado `✓ Adicionado` (`bg-sky-tint text-ocean-blue`).
    - Se `total >= 4`: botão desabilitado `Limite 4/4`.
    - Caso contrário: CTA **`Favoritar`** (`+ Adicionar`, `bg-ocean-blue hover:bg-deep-navy text-pure-white`).
  - Rodapé: `"Formato Oficial: 1 Líder + 50 Cartas"` e botão `Cancelar`.
- **Estados e Proxy (`CatalogoDeckProxy` — `RN001`, `RN002`, `RN003`)**:
  - Decks inativos não aparecem na lista de escolha; tentativas diretas de inclusão de deck inativo/inexistente/duplicado/5º deck exibem mensagem clara no toast (`bg-error-container text-on-error-container`).
  - **Indisponibilidade do Catálogo (`503`)**: Exibe banner tratável `"Serviço de Catálogo temporariamente indisponível. Tente novamente em instantes."` com botão `Tentar novamente`.

### Protótipo 9 — Modal de Expansão da Composição do Deck (`P09`)
- **Tela Stitch**: `d26c9d5f978f422fa021153ec9cfe885` (*Comunidade: Modal Visualizar Deck*).
- **Rota Frontend**: Modal amplo `DeckCompositionModal.tsx` acionado de `P06`, `P07` ou perfil de amigo (`/comunidade/perfil/:id`).
- **Operação BFF**: `GET /backend/comunidade/deck-favorito/{deckId}/composicao` (via `CatalogoDeckProxy` / `optcgapi.com/api/decks/{st_id}/`).
- **Layout e Componentes**:
  - **Cabeçalho**: Nome do deck (`font-headline-lg text-deep-navy`), subtítulo `Líder: Monkey D. Luffy (OP01-001)`, badge `51 cartas (1 Líder + 50 Cartas)` (`bg-sky-tint text-ocean-blue`), badge `Descontinuado / Inativo` quando aplicável, e botão fechar (`×`).
  - **Faixa de Resumo e Custo**: Pílulas de filtro/contagem por tipo (`Todos`, `1 Líder`, `Personagens`, `Eventos`, `Estágios`), seletor de agrupamento **`Agrupar por Tipo` | `Agrupar por Custo`** e destaque à direita `Custo Estimado no Mercado: R$ 890,00` (`font-tabular-price text-deep-navy`).
  - **Barra de Conformidade Bandai**: `"COMPOSIÇÃO DETALHADA E AGRUPADA"` + selo verde/náutico `bg-status-positive-bg text-status-positive-text`: **`Conformidade Estrita (1 Líder + 50 Cartas • Máx 4 cópias)`**.
  - **Lista Agrupada de Cartas (`ItemComposicaoDeck`)**: Para cada carta (`card_set_id`, `card_name`, `card_type`, `card_color`, `card_cost`, `card_power`, `rarity`, `market_price`, `card_image`):
    - Miniatura `aspect-[2.5/3.5]` com fallback SVG náutico quando imagem indisponível;
    - Badges de raridade (`bg-deep-navy text-pure-white`), código `card_set_id` e tipo/cor;
    - Nome da carta + metadados (`Custo: X • Poder: Y`);
    - Badge de quantidade (`x1 Líder` ou `x4 no Deck`), preço unitário (`market_price`) + subtotal em `font-tabular-price` e link `Ver no Catálogo →` (`/catalogo/cartas/:card_set_id`).
  - **Rodapé**: Selo `"Deck válido de 50 cartas • Estrutura oficial Bandai TCG"`, botão `Fechar` (ghost) e botão primário **`Copiar Decklist (Formato Bandai)`** (`bg-ocean-blue hover:bg-deep-navy text-pure-white`, ícone `content_copy`), que usa `bandaiExport.ts` para copiar linhas `1xST01-001\n4xOP01-016...` na área de transferência e dispara toast `"Decklist copiada no formato Bandai!"`.
- **Estados e Proxy (`CatalogoDeckProxy` — `RN003`)**:
  - **Carregamento**: Skeleton de cabeçalho + 6 linhas enquanto `GET /{deckId}/composicao` carrega.
  - **Indisponibilidade do Catálogo (`503`)**: Estado de erro amigável dentro do modal `"Não foi possível carregar a composição porque o serviço de Catálogo está indisponível."` com botões `Tentar novamente` e `Fechar`, sem quebrar o restante da página.

---

## 5. Protótipos de Alta Fidelidade — `comunidade / F003`: Avaliação de Cartas (Etapa 3.4)

> [!NOTE]
> Especificação detalhada alinhada ao plano técnico [etapa-3-4-avaliacao-de-cartas.md](../plans/etapa-3-4-avaliacao-de-cartas.md), ao Design System Stitch *Nautical Blue & White Marketplace* (`projects/18247718655827442438`, zero Bootstrap), ao padrão GoF exclusivo **`Proxy`** (`AvaliacaoElegibilidadeProxy`, **zero `Observer`** inclusive na invalidação por reembolso da `RN005`), à projeção local desacoplada **`CompraVerificada`** (`CVE_COMPRA_VERIFICADA`) e ao contrato oficial **`https://optcgapi.com`**.

### 5.1 Inventário de Componentes, Store e Rotas (`frontend/src/`)
- **Store Zustand (`src/stores/reviewsUiStore.ts`)**: `termoBusca`, `filtroElegibilidade` (`'todas' | 'elegiveis' | 'avaliadas'`), `produtoModalAberto: string | null`, `modalAvaliacaoAberto: boolean`, `notaHover: number | null`, `painelPedidosAberto: boolean`, ações e `resetarEstadoUi()`.
- **Feature (`src/features/community/reviews/`)**: `types/reviews.types.ts`, `api/reviewsApi.ts`, `api/reviewsQueries.ts` (`queryOptions` + `useSuspenseQuery` + `useMutation` invalidando `['comunidade', 'avaliacao-produto']`), `pages/CardReviewsCatalogPage.tsx`, `pages/CardReviewDetailPage.tsx`, `components/ReviewFormModal.tsx`, `components/StarRating.tsx`, `components/RatingDistributionBars.tsx`.
- **Navegação (`CommunitySubNav.tsx` e `routes/index.tsx`)**: adiciona a aba **`Avaliações`** (`/comunidade/avaliacoes`), a rota de detalhe `/comunidade/avaliacoes/:produtoId` e o redirect legado `/comunidade/avaliacao-produto/pesquisa` → `/comunidade/avaliacoes`.

### Protótipo 10 — Vitrine de Cartas e Entrada da Avaliação (`P10`)
- **Telas Stitch**: `7e79485c587349ceab386d8665df5bbc` (*Minha Coleção & Progresso*) / `8bd5dfb86e354c6ab97f7f7e8b426090` (*Avaliar Cartas*).
- **Rota Frontend**: `/comunidade/avaliacoes` (`CardReviewsCatalogPage.tsx`).
- **Operação BFF**: `GET /backend/comunidade/avaliacao-produto/catalogo?termo=`.
- **Layout e Tokens Stitch**:
  - `CommunitySubNav` com aba `Avaliações` ativa (`bg-deep-navy text-pure-white`).
  - Barra de busca por nome ou código (`OP05-119`, `OP01-001`) + pílulas de filtro (`Todas`, `Elegíveis para Avaliar`, `Minhas Avaliações`).
  - Grid responsivo (`grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-space-md`) de cards `bg-pure-white rounded-xl border border-ice-border p-space-sm shadow-sm`:
    - Imagem `aspect-[2.5/3.5]` via `optcgapi.com` (com fallback SVG náutico), chips de raridade (`bg-deep-navy text-pure-white`) e cor, código `card_set_id`, nome (`font-headline-sm`), preço (`font-tabular-price`);
    - Linha de resumo comunitário: `StarRating` + média com 1 casa decimal e contagem (`★ 4.5 (2)`) ou estado neutro `☆ Sem avaliações (0.0)`;
    - Ação contextual de avaliação: se o usuário já avaliou, badge `(Sua: 5.0)` + botão `Editar` (abre `P14`); se `podeAvaliar === true`, botão primário `Avaliar` (`bg-ocean-blue text-pure-white`, abre `P13`); se não elegível, badge informativo `Requer compra paga` + link `Ver Avaliações` (`/comunidade/avaliacoes/:produtoId`).

### Protótipo 11 — Detalhes da Carta e Avaliações (`P11`)
- **Tela Stitch**: `21c3501f0e844d2388377f55414687b4` (*Detalhes da Carta*).
- **Rota Frontend**: `/comunidade/avaliacoes/:produtoId` (`CardReviewDetailPage.tsx`).
- **Operação BFF**: `GET /backend/comunidade/avaliacao-produto/{produtoId}/pesquisa`.
- **Layout e Componentes**:
  - Link `← Voltar para Avaliações` (`/comunidade/avaliacoes`).
  - Ficha superior da carta (`optcgapi.com`): imagem em destaque (`aspect-[2.5/3.5]`), badges `rarity` / `set_id`, título `card_name` (`font-headline-lg text-deep-navy`), atributos (`TIPO`, `COR`, `PODER`, `CUSTO`), `market_price` em `font-tabular-price` e resumo rápido `★ 4.5 (2 avaliações válidas)`.
  - Seção inferior `Avaliações da Comunidade` (`P12`) + **Painel colapsável de Compras Verificadas (`CompraVerificada`)** para demonstração integrada: exibe os pedidos do usuário autenticado para aquela carta (`Pago`, `Pendente`, `Reembolsado`), permitindo registrar compra paga/pendente (`POST /compras/registrar`) ou reembolsar (`POST /compras/{pedidoId}/reembolsar`) e observar a atualização imediata de elegibilidade (`RN001`) e invalidação/recálculo de média (`RN005`) sem uso de `Observer`.

### Protótipo 12 — Avaliações da Comunidade (`P12`)
- **Tela Stitch**: `21c3501f0e844d2388377f55414687b4` (seção *Avaliações da Comunidade*).
- **Rota Frontend**: Seção integrada em `/comunidade/avaliacoes/:produtoId` (`StarRating.tsx` + `RatingDistributionBars.tsx`).
- **Layout e Tokens Stitch**:
  - Cabeçalho `Avaliações da Comunidade` + botão CTA `Avaliar Carta` (ou `Editar Minha Avaliação` quando `minhaAvaliacao != null`).
  - **Card Resumo (Esquerda)**: fundo `bg-surface-container-low rounded-xl p-space-lg`, média grande em `font-display-lg text-deep-navy tabular-nums` (`4.5` ou `0.0`), 5 estrelas (`StarRating`), `"Baseado em X avaliações válidas"` (`RN004`) e, quando existente, `"• Sua nota: 5.0"` + botão `Editar Minha Avaliação`.
  - **Histograma (`RatingDistributionBars.tsx` — Direita)**: 5 barras (`5 ★` a `1 ★`) preenchidas em `bg-ocean-blue` sobre trilho `bg-surface-container` com contagem tabular à direita.
  - **Lista de Avaliações Válidas**: Cada item (`bg-pure-white rounded-xl border border-ice-border p-space-md`) exibe apelido do autor, **selo obrigatório `✓ Compra Verificada`** (`bg-status-positive-bg text-status-positive-text`, ícone `verified`, `RN006`), data relativa, estrelas e comentário (quando informado).
- **Estados**:
  - **Vazio (0 avaliações)**: Exibe média `0.0`, `0 avaliações válidas`, histograma zerado e mensagem `"Esta carta ainda não possui avaliações da comunidade."`
  - **Invalidação por Reembolso (`RN005`)**: Quando o único pedido válido é reembolsado, a avaliação deixa de constar na lista pública e a média/histograma é recalculada automaticamente (mantendo-se visível caso exista outro pedido pago e não reembolsado).

### Protótipo 13 — Nova Avaliação (`P13`)
- **Tela Stitch**: `c10dd91737b54bf49b75fb52fefae957` (*Modal Adicionar Avaliação*).
- **Rota Frontend**: Modal `ReviewFormModal.tsx` (modo criação) aberto de `P10` ou `P11`/`P12`.
- **Operação BFF**: `POST /backend/comunidade/avaliacao-produto/{produtoId}/avaliar` (`{ nota, comentario? }`).
- **Campos e Validações (`React Hook Form` + `AvaliacaoElegibilidadeProxy`)**:
  - Resumo da carta (`card_set_id`, `card_name`, cor, `market_price`) e média atual da comunidade.
  - Seletor interativo de **1 a 5 estrelas** (`notaHover` via `reviewsUiStore`, obrigatório `1 <= nota <= 5`, legenda dinâmica `"1 estrela"`..`"5 estrelas"` — `RN003`).
  - Campo opcional `Seu Comentário (opcional)` (`textarea` até 1000 caracteres).
  - Rodapé: `Cancelar` (ghost, descarta rascunho sem enviar requisição) e `Salvar Avaliação` (`bg-ocean-blue hover:bg-deep-navy text-pure-white`, desabilitado enquanto nenhuma nota válida `1..5` for selecionada).
  - **Proteção Proxy (`RN001`)**: Se o usuário não possuir compra paga e não reembolsada (`!podeAvaliar`), exibe alerta em `bg-status-warning-bg text-status-warning-text` e qualquer submissão direta é rejeitada pelo `AvaliacaoElegibilidadeProxy` (`bg-error-container text-on-error-container` no `#tcg-toast`).

### Protótipo 14 — Atualização da Avaliação (`P14`)
- **Tela Stitch**: `233e6f9b6f424215954a85c92d59f53e` (*Modal de Avaliação / Edição*).
- **Rota Frontend**: Modal `ReviewFormModal.tsx` (modo edição) aberto de `P10` ou `P11`/`P12`.
- **Operação BFF**: `POST /backend/comunidade/avaliacao-produto/{produtoId}/avaliar` (`{ nota, comentario? }`).
- **Layout e Regras (`RN002`, `RN003`, `RN005`)**:
  - Cabeçalho `Sua Avaliação da Carta`, badge `✓ Avaliação Enviada` + nota atual (`5.0 ★`) + data, estrelas pré-preenchidas e editáveis, campo `Seu Comentário` pré-preenchido (`Editável`).
  - Botão **`Cancelar`** fecha o modal descartando alterações não salvas e **preservando integralmente a nota e comentário anteriores** (`RN002`).
  - Botão **`Atualizar Avaliação`** atualiza o registro existente de forma idempotente (sem duplicar contagem na média); `AvaliacaoElegibilidadeProxy` bloqueia tentativas de alterar avaliação de outro usuário (`403`) ou após reembolso de todas as compras elegíveis.

---

## 6. Protótipos de Alta Fidelidade — `adm / F001` e `comunidade / F004`: Abertura Preferida de One Piece (Etapa 3.3)

> [!NOTE]
> Especificação detalhada alinhada ao plano técnico [etapa-3-3-aberturas.md](../plans/etapa-3-3-aberturas.md), ao Design System Stitch *Nautical Blue & White Marketplace* (`projects/18247718655827442438`), ao padrão GoF exclusivo **`Proxy`** (`AberturaPreferidaPrivacidadeProxy`, **zero `Observer`**), ao grafo acíclico **`comunidade → adm`** e ao contrato oficial **`https://api.animethemes.moe`** (`/anime/one_piece?include=animethemes.song.artists,animethemes.animethemeentries.videos,images`).

### 6.1 Inventário de Componentes, Store e Rotas (`frontend/src/`)
- **Store Zustand (`src/stores/openingsUiStore.ts`)**:
  - Comunidade: `termoBusca`, `filtroComunidade` (`'todas' | 'amigos' | 'ranking'`), `aberturaDetalheId`, `modalDetalhesAberto`.
  - Admin: `termoBuscaAdmin`, `filtroStatusAdmin` (`'todas' | 'ativas' | 'inativas'`), `modalAdminAberto`, `aberturaEmEdicao`, `alvoExclusaoAdmin`.
- **Feature Comunidade (`src/features/community/openings/`)**: `types/openings.types.ts`, `api/openingsApi.ts`, `api/openingsQueries.ts`, `pages/FavoriteOpeningsPage.tsx`, `components/OpeningDetailsModal.tsx`, `components/FavoriteOpeningProfileCard.tsx`.
- **Feature Admin (`src/features/adm/openings/`)**: `types/adminOpenings.types.ts`, `api/adminOpeningsApi.ts`, `api/adminOpeningsQueries.ts`, `pages/AdminOpeningsPage.tsx`, `components/AdminOpeningFormModal.tsx`.
- **Navegação (`CommunitySubNav.tsx`, `HeaderNavbar.tsx` e `routes/index.tsx`)**:
  - Aba **`Abertura Preferida`** em `CommunitySubNav.tsx` apontando para `/comunidade/aberturas` (com redirect legado `/comunidade/abertura-preferida` → `/comunidade/aberturas`).
  - Item **`Admin • Aberturas`** (`/adm/aberturas`) exibido no cabeçalho **somente** quando `autorizacoesDe('abertura')?.incluir === true` (`ADMINISTRADOR`).

### Protótipo 15 — Gestão Administrativa das Aberturas (`P15`)
- **Tela Stitch**: `b2a45fd86fd84870b32e71fbd1c43fc4` (*TCGHub Admin - Gerenciamento de Aberturas de One Piece*).
- **Rota Frontend**: `/adm/aberturas` (`AdminOpeningsPage.tsx`, protegida por papel `ADMINISTRADOR`).
- **Operações BFF**: `GET /backend/adm/abertura/pesquisa?termo=&status=`, `GET /backend/adm/abertura/{id}/visualizacao`, `POST /backend/adm/abertura/{id}/{inativar,reativar,excluir}`.
- **Layout e Tokens Stitch**:
  - Breadcrumb `Início > Mídia & Catálogo > Aberturas de One Piece`, título `Aberturas` (`font-headline-lg text-deep-navy`), subtítulo `"Gerenciamento do catálogo oficial de temas musicais de One Piece"` e botão primário `+ Cadastrar abertura` (`bg-ocean-blue hover:bg-deep-navy text-pure-white`).
  - Pílulas de filtro com contadores dinâmicos: `Todas (N)`, `Ativas (N)` e `Inativas (N)` (ativo em `bg-deep-navy text-pure-white`, padrão em `bg-surface-container-low text-secondary`) + busca `"Buscar por número, nome ou artista (ex: Hope, 20)..."`.
  - Tabela (`bg-pure-white rounded-xl border border-ice-border shadow-sm`) com cabeçalho `bg-deep-navy text-pure-white` e colunas: `MÍDIA` (miniatura 16:9 com fallback náutico), `Nº` (`#01` em `font-tabular-stat`), `NOME DA ABERTURA`, `ARTISTA`, `EPISÓDIOS`, `PREFERÊNCIAS` (contagem de escolhas na comunidade), `STATUS` (`• Ativa` em `bg-status-positive-bg text-status-positive-text` / `• Inativa` em `bg-status-warning-bg text-status-warning-text`), `AÇÕES` (`Editar`, `Inativar` / `Reativar`, `Excluir`).
- **Regras, Alerta `RN006` e Estados**:
  - **Proteção contra exclusão (`adm/F001 RN006`)**: Quando `preferencias > 0`, a ação `Excluir` exibe alerta informativo claro no modal de confirmação (e rejeição no backend `409`/`400`): `"Esta abertura já foi escolhida por usuários na comunidade e não pode ser excluída — utilize a opção Inativar para preservar as escolhas existentes."`
  - **Autorização (`RN002`)**: Acesso direto por usuário `USUARIO` (sem `ADMINISTRADOR`) exibe estado de acesso restrito (`403`) sem expor controles de escrita.
  - **Vazio / Carregamento**: Skeleton de tabela durante carregamento; estado vazio `"Nenhuma abertura encontrada para os filtros selecionados."`.

### Protótipo 16 — Modal Administrativo Criar/Editar Abertura (`P16`)
- **Tela Stitch**: `b2a45fd86fd84870b32e71fbd1c43fc4` (modal *Cadastrar abertura*).
- **Rota Frontend**: Modal `AdminOpeningFormModal.tsx` em `/adm/aberturas`.
- **Operações BFF**: `POST /backend/adm/abertura/incluir` e `POST /backend/adm/abertura/{id}/alterar`.
- **Campos (`React Hook Form` + Contrato `api.animethemes.moe`)**:
  - `Número da Abertura *` (`sequence`, inteiro `> 0`, único — `RN003`).
  - `Nome da Abertura *` (`song.title`, obrigatório — ex.: `"We Are!"`).
  - `Artista / Intérprete *` (`song.artists[].name`, obrigatório — ex.: `"Hiroshi Kitadani"`).
  - `Episódios de Exibição *` (`animethemeentries[].episodes`, obrigatório — ex.: `"1 a 47"`).
  - `URL da Imagem de Capa (Aspecto 16:9)` (`images[].link`) + botão **`Testar Prévia`** + caixa de pré-visualização 16:9 com fallback náutico caso a URL falhe.
  - `URL de Vídeo Público (AnimeThemes / YouTube)` (`videos[].link`).
  - Checkbox `Ativa para novas escolhas` (`ativa: true | false`).
  - Rodapé: `Cancelar` (ghost) e `Cadastrar abertura` / `Salvar alterações` (`bg-ocean-blue text-pure-white`).
- **Validações e Erros**: Rejeita número `<= 0` ou campos obrigatórios vazios (`text-error`); número duplicado na inclusão ou edição retorna `409` publicado no `#tcg-toast` (`bg-error-container text-on-error-container`).

### Protótipo 17 — Página `/comunidade/aberturas` (Catálogo Ativo, Ranking Top 5) e Card no Perfil (`P17`)
- **Telas Stitch**: `4734db8f5e32474ca6da6c27f95b7ada` / `8a09531ad18f495bbc2f422ed9d3f34d` (*Aberturas de One Piece & Ranking Oficial*) e integração no Perfil (`a20513d71abd449ba2b482f6bcc24089` / `9b383bb48db94fe485d3e71979323c57`).
- **Rotas Frontend**: `/comunidade/aberturas` (`FavoriteOpeningsPage.tsx`) + Card integrado em `/comunidade/perfil` e `/comunidade/perfil/:id` (`FavoriteOpeningProfileCard.tsx`).
- **Operações BFF**: `GET /backend/comunidade/abertura-preferida/pesquisa?termo=&filtro=`, `GET /backend/comunidade/abertura-preferida/ranking`, `GET /backend/comunidade/abertura-preferida/perfil/{perfilId}`, `POST /backend/comunidade/abertura-preferida/escolher`, `POST .../ocultar`, `POST .../reexibir`.
- **Layout da Página `/comunidade/aberturas`**:
  - **Banner Superior da Preferência Atual**: Miniatura 16:9, badge `ABERTURA ESCOLHIDA` + `X amigos também escolheram`, título `Op. 01 — We Are!`, artista • episódios, badge `Inativa para novas escolhas` (caso a abertura atual tenha sido inativada no `adm` — `RN006`, preservada até substituição), botão `Ver Detalhes` e **Toggle `Exibir no Perfil Público`** (`role="switch"`, controla `APR_VISIVEL_NO_PERFIL` via `POST /ocultar` e `POST /reexibir`).
  - **Área Principal (Esquerda — Catálogo de Aberturas Ativas)**:
    - Busca em tempo real: `"Filtrar por nome, número ou artista..."` (`RN001`).
    - Pílulas de filtro: `Todas as Aberturas`, `Escolhidas por Amigos`, `Top Ranking`.
    - Grid apenas com aberturas **`ATIVAS`** (`bg-pure-white rounded-xl border border-ice-border shadow-sm`): capa 16:9 (fallback SVG náutico), badges `★ SUA PREFERIDA` / `#N NO RANKING` / `Op. XX`, título, artista • episódios, contagem de votos e **contagem de amigos que escolheram cada abertura** (`X amigos`), botão **`Escolher como Preferida`** (`bg-ocean-blue hover:bg-deep-navy text-pure-white`) ou estado `✓ Ativa no Perfil`, e botão de abrir detalhes (`P18`).
  - **Sidebar Direita (`RANKING DA COMUNIDADE — Top 5 Escolhas Oficiais`)**:
    - Card `Total de Votos Ativos` (`font-tabular-stat`), lista ordenada `#1` a `#5` com barra proporcional (`bg-ocean-blue`), percentual exato (ex.: `40,0%`) e quantidade de votos (`RN004`).
    - Nota de privacidade: `"Preferências ocultas no perfil público somam normalmente no cálculo proporcional sem expor a identidade do usuário."`
    - **Estado Vazio do Ranking**: Exibe `Total de Votos Ativos: 0` e mensagem `"Nenhuma escolha registrada na comunidade ainda."`
- **Card da Abertura Preferida no Perfil Comunitário (`FavoriteOpeningProfileCard.tsx`)**:
  - **Visão do Proprietário (`/comunidade/perfil`)**: Exibe capa 16:9, `Op. XX — <Nome>`, artista, episódios, player/link de vídeo AnimeThemes, toggle de visibilidade pública (`Oculto no perfil público` / `Visível no perfil público`) e atalho `Trocar Abertura` (`/comunidade/aberturas`).
  - **Visão de Visitante / Amigo (`/comunidade/perfil/:id`, mediado por `AberturaPreferidaPrivacidadeProxy`)**:
    - Se pública: exibe a abertura, link/player AnimeThemes e, quando o visitante é amigo com a mesma abertura preferida, exibe badge destacado **`Mesma abertura preferida que você!`** (`bg-status-positive-bg text-status-positive-text`, `RN005`).
    - Se oculta (`visivelNoPerfil === false`): o Proxy omite completamente a abertura e **não** exibe correspondência pública (`RN003`, `RN005`).

### Protótipo 18 — Modal de Detalhes da Abertura Preferida (`P18`)
- **Tela Stitch**: `eb3be1f6e46e4ad8ae22f2fb8b39731e` (*Detalhes da Abertura — Modo Foco*).
- **Rota Frontend**: Modal `OpeningDetailsModal.tsx` aberto em `/comunidade/aberturas` ou a partir do card no Perfil.
- **Operação BFF**: `GET /backend/comunidade/abertura-preferida/{id}/visualizacao`.
- **Layout e Componentes**:
  - Hero 16:9 com capa (ou fallback SVG náutico), overlay escuro, badge `OP. XX`, título, artista • episódios, **player embutido/link oficial de vídeo (`AnimeThemes`)** (`▶ Assistir Abertura`) e CTA `Escolher como Preferida` / `★ Sua Preferida (Ativa)` + toggle de visibilidade pública quando for a escolha do usuário.
  - 3 cards informativos inferiores:
    1. **`Amigos que Escolheram`**: Contagem total + apelidos dos amigos com visibilidade pública (filtrados pelo `AberturaPreferidaPrivacidadeProxy`, sem jamais vazar amigos com escolha oculta).
    2. **`Posição Global`**: `#N no Ranking Oficial` + votos e percentual.
    3. **`Cronologia do Anime`**: Faixa de episódios (`Ep. 1 a 47`) e status administrativo.

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
| **P10** — Vitrine/Coleção como entrada de avaliações | `comunidade / F003` | `/comunidade/avaliacoes` | `comunidade/F003`: `RN001`, `RN002`, `RN004` | `avaliacao-de-cartas.feature`: Registrar avaliação, Reavaliar carta existente, Consultar resumo sem avaliações |
| **P11** — Detalhes da carta e avaliações | `comunidade / F003` | `/comunidade/avaliacoes/:produtoId` | `comunidade/F003`: `RN001`, `RN004`, `RN005`, `RN006` | `avaliacao-de-cartas.feature`: Consultar média e contagem no catálogo, Reembolso invalidando ou mantendo avaliação |
| **P12** — Avaliações da comunidade | `comunidade / F003` | `/comunidade/avaliacoes/:produtoId` | `comunidade/F003`: `RN004`, `RN005`, `RN006` | `avaliacao-de-cartas.feature`: Média e contagem, Carta sem avaliações (`0.0`), Selo compra verificada, Invalidação e recálculo após reembolso |
| **P13** — Nova avaliação | `comunidade / F003` | Modal em `/comunidade/avaliacoes` e `/comunidade/avaliacoes/:produtoId` | `comunidade/F003`: `RN001`, `RN003`, `RN006` | `avaliacao-de-cartas.feature`: Registrar avaliação (com/sem comentário, nota 1–5), Rejeitar sem compra/não pago/reembolsado, Permitir se houver outro pedido pago, Rejeitar nota `<1` ou `>5` |
| **P14** — Atualização da avaliação | `comunidade / F003` | Modal em `/comunidade/avaliacoes` e `/comunidade/avaliacoes/:produtoId` | `comunidade/F003`: `RN002`, `RN003`, `RN005` | `avaliacao-de-cartas.feature`: Reavaliar sem duplicar contagem, Cancelar edição preserva anterior, Rejeitar alteração de outro usuário, Manter válida se houver outra compra |
| **P15** — Gestão administrativa das aberturas | `adm / F001` | `/adm/aberturas` | `adm/F001`: `RN001`, `RN002`, `RN004`, `RN005`, `RN006` | `abertura.feature`: Consultar por número, Pesquisar/filtrar por status, Inativar, Reativar, Excluir nunca escolhida, Rejeitar exclusão de já escolhida, Rejeitar sem autorização `ADMINISTRADOR` |
| **P16** — Cadastro administrativo de abertura | `adm / F001` | Modal em `/adm/aberturas` | `adm/F001`: `RN001`, `RN002`, `RN003` | `abertura.feature`: Cadastrar ativa/inativa, Editar dados, Rejeitar sem autorização, Rejeitar número duplicado (cadastro/edição), Rejeitar dados inválidos, Rejeitar edição inexistente |
| **P17** — Catálogo, ranking Top 5 e card no perfil | `comunidade / F004` | `/comunidade/aberturas`, `/comunidade/perfil`, `/comunidade/perfil/:id` | `comunidade/F004`: `RN001`, `RN002`, `RN003`, `RN004`, `RN005`, `RN006` | `abertura-preferida.feature`: Listar apenas ativas com contagem de amigos, Pesquisar por nome/artista, Escolher/substituir preferida, Rejeitar inativa/inexistente, Ocultar/reexibir, Ranking Top 5 (com ocultas e vazio), Manter escolha inativada e permitir troca, Correspondência no perfil do amigo |
| **P18** — Detalhes da abertura preferida | `comunidade / F004` | Modal em `/comunidade/aberturas` (e `/comunidade/perfil`) | `comunidade/F004`: `RN003`, `RN004`, `RN005` | `abertura-preferida.feature`: Consultar detalhes com posição e amigos, Indicar/não indicar/ocultar correspondência no perfil do amigo |

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
