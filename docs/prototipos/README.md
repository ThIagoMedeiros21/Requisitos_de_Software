# Protótipos de Alta Fidelidade e Fluxos de Tela — TCGHub

> [!NOTE]
> Artefato de **DDD Preliminar e UX (Etapa 1 — Planejar, Onda 2)** cobrindo integralmente as **4 funcionalidades de Lucas Menezes** (`Perfil e amigos`, `Decks favoritos`, `Avaliação de cartas` e `Abertura preferida do anime One Piece`, incluindo a gestão administrativa em `adm`), em conformidade com [.agents.md](../../.agents.md), [tcghub/AGENTS.md](../../tcghub/AGENTS.md) e [desenvolver-frontend](../../tcghub/.agents/skills/desenvolver-frontend/SKILL.md).

---

## 1. Convenções Técnicas D5 Frontend e Padrão Visual

### 1.1 Stack e Padrões de Componentes
- **UI**: React 19.3.0 + Bootstrap 5.3.8 / React Bootstrap 2.10.10 (cabeçalhos, `Nav` por abas em pílula, `Card`, `Modal`, `Form`, `Badge`, `Button`, `Table`, `ProgressBar`, `Alert`, `Pagination`).
- **Roteamento e Carregamento**: React Router 8.5.0 dentro de `RotaProtegida` (`ui-frontend/src/main/react/src/app/rotas.tsx`), com carregamento `lazy` de componente e `loader` por rota, tratamento de erros de loader via `ErroRota` (`ErrorBoundary`) e permissões de menu via `autorizacoesDe('<agregado>')` a partir de `GET /backend/autenticacao`.
- **Estado Remoto e Mutações**: TanStack Query 5.104.0 (`queryOptions` + `clienteConsultas.ensureQueryData` no loader + `useSuspenseQuery` no componente; mutações via `useMutation` com `chamarApi` e `clienteConsultas.invalidateQueries`).
- **Formulários e Feedback**: React Hook Form 7.90.0 com validação em linha (`Form.Control.Feedback`) e erros de backend publicados via `publicar('erro', mensagemErro(erro))` do `useNotificacoes`.

### 1.2 Convenção de Rotas D5 (`/<subdominio>/<agregado>/<substantivo-singular>`)

> Conforme [desenvolver-frontend](../../tcghub/.agents/skills/desenvolver-frontend/SKILL.md), o último segmento da rota é um **substantivo singular** (`pesquisa`, `inclusao`, `alteracao`, `exclusao`, `visualizacao`, `ranking`). Todas as operações BFF usam prefixo `/backend`: `GET` no mesmo caminho da rota (para o `loader`) e `POST` substituindo o último segmento pelo nome da ação (`incluir`, `alterar`, `excluir`, `inativar`, `reativar`, `fixar-destaque`, `remover-destaque`, `aceitar`, `recusar`, `cancelar`, `remover`, `ocultar`, `reexibir`).

| Subdomínio | Agregado | Rotas Frontend (React Router) | Operações BFF (`/backend/...`) | Origem D5 |
| :--- | :--- | :--- | :--- | :--- |
| `adm` | `abertura` | `/adm/abertura/pesquisa`<br>`/adm/abertura/inclusao`<br>`/adm/abertura/:id/visualizacao`<br>`/adm/abertura/:id/alteracao`<br>`/adm/abertura/:id/exclusao` | `GET /backend/adm/abertura/pesquisa`<br>`GET .../inclusao` / `POST .../incluir`<br>`GET .../:id/visualizacao`<br>`GET .../:id/alteracao` / `POST .../:id/alterar`<br>`GET .../:id/exclusao` / `POST .../:id/excluir`<br>`POST /backend/adm/abertura/:id/inativar`<br>`POST /backend/adm/abertura/:id/reativar` | Padrão D5 + extras (`visualizacao`, `inativar`, `reativar`) |
| `comunidade` | `perfil-comunitario` | `/comunidade/perfil-comunitario/visualizacao`<br>`/comunidade/perfil-comunitario/:id/visualizacao`<br>`/comunidade/perfil-comunitario/:id/alteracao` | `GET /backend/comunidade/perfil-comunitario/visualizacao`<br>`GET .../:id/visualizacao`<br>`GET .../:id/alteracao` / `POST .../:id/alterar`<br>`POST /backend/comunidade/perfil-comunitario/:id/fixar-destaque`<br>`POST /backend/comunidade/perfil-comunitario/:id/remover-destaque` | Padrão D5 (`alteracao`) + extras (`visualizacao`, `fixar-destaque`, `remover-destaque`) |
| `comunidade` | `amizade` | `/comunidade/amizade/pesquisa`<br>`/comunidade/amizade/inclusao`<br>`/comunidade/amizade/:id/visualizacao` | `GET /backend/comunidade/amizade/pesquisa`<br>`GET .../inclusao` / `POST .../incluir`<br>`GET .../:id/visualizacao`<br>`POST /backend/comunidade/amizade/:id/aceitar`<br>`POST /backend/comunidade/amizade/:id/recusar`<br>`POST /backend/comunidade/amizade/:id/cancelar`<br>`POST /backend/comunidade/amizade/:id/remover` | Padrão D5 (`pesquisa`, `inclusao`) + extras (`visualizacao`, `aceitar`, `recusar`, `cancelar`, `remover`) |
| `comunidade` | `deck-favorito` | `/comunidade/deck-favorito/pesquisa`<br>`/comunidade/deck-favorito/inclusao`<br>`/comunidade/deck-favorito/:id/visualizacao`<br>`/comunidade/deck-favorito/:id/exclusao` | `GET /backend/comunidade/deck-favorito/pesquisa`<br>`GET .../inclusao` / `POST .../incluir`<br>`GET .../:id/visualizacao`<br>`GET .../:id/exclusao` / `POST .../:id/excluir`<br>`POST /backend/comunidade/deck-favorito/ocultar`<br>`POST /backend/comunidade/deck-favorito/reexibir` | Padrão D5 (`pesquisa`, `inclusao`, `exclusao`) + extras (`visualizacao`, `ocultar`, `reexibir`) |
| `comunidade` | `avaliacao-produto` | `/comunidade/avaliacao-produto/pesquisa`<br>`/comunidade/avaliacao-produto/inclusao`<br>`/comunidade/avaliacao-produto/:id/alteracao`<br>`/comunidade/avaliacao-produto/:id/visualizacao` | `GET /backend/comunidade/avaliacao-produto/pesquisa`<br>`GET .../inclusao` / `POST .../incluir`<br>`GET .../:id/alteracao` / `POST .../:id/alterar`<br>`GET .../:id/visualizacao` | Padrão D5 (`pesquisa`, `inclusao`, `alteracao`) + extra (`visualizacao`) |
| `comunidade` | `abertura-preferida` | `/comunidade/abertura-preferida/pesquisa`<br>`/comunidade/abertura-preferida/inclusao`<br>`/comunidade/abertura-preferida/:id/visualizacao`<br>`/comunidade/abertura-preferida/ranking` | `GET /backend/comunidade/abertura-preferida/pesquisa`<br>`GET .../inclusao` / `POST .../incluir`<br>`GET .../:id/visualizacao`<br>`GET /backend/comunidade/abertura-preferida/ranking`<br>`POST /backend/comunidade/abertura-preferida/ocultar`<br>`POST /backend/comunidade/abertura-preferida/reexibir` | Padrão D5 (`pesquisa`, `inclusao`) + extras (`visualizacao`, `ranking`, `ocultar`, `reexibir`) |

---

## 2. Protótipos de Alta Fidelidade — `comunidade / F001`: Perfil e Amigos

### Protótipo 1 — Visão Geral do Perfil (`P01`)
- **Rota D5**: `/comunidade/perfil-comunitario/visualizacao` (próprio) e `/comunidade/perfil-comunitario/:id/visualizacao` (visitante).
- **Operações BFF**: `GET /backend/comunidade/perfil-comunitario/visualizacao` | `GET /backend/comunidade/perfil-comunitario/:id/visualizacao`.
- **Layout e Componentes (React Bootstrap 5)**:
  - **Barra de navegação comunitária**: Abas em pílula (`Nav variant="pills"`): `Meu Perfil` (ativo), `Decks Favoritos`, `Minha Coleção`, `Rede de Amigos`, `Buscar Colecionadores`, `Abertura Preferida`.
  - **Card de Cabeçalho do Perfil (`Card`)**: Avatar circular com indicador de status, título com apelido comunitário (`TrafalgarAce_99`) + badge `Colecionador Verificado`, descrição/bio, metadados (`Membro desde ...`, `Cidade`, `Avaliação`), e na direita botão `Editar Perfil` (somente proprietário) + botão de compartilhamento (ou ações de amizade quando visitante: `Adicionar Amigo`, `Solicitação Pendente` / `Cancelar`, `Aceitar / Recusar`, `Remover Amigo`).
  - **Seção `Cartas Favoritas`**: Título + subtítulo `"Destaques da coleção pessoal expostos na comunidade (Máximo de 4 slots estritos)"`, badge `X de 4 slots preenchidos`, botão `Gerenciar Slots` (proprietário) e grid `Row/Col` com 4 cards.
  - **Seção inferior em 2 colunas (`Row`)**: Esquerda `Decks Favoritos` + botão `+ Adicionar deck favorito`; Direita `Wishlist Pública` (quando habilitada nas preferências).
  - **Bloco de Abertura Preferida**: Exibido quando configurada e pública (ou sempre visível para o proprietário com selo de oculta; quando visitante é amigo e ambos têm mesma abertura pública, exibe selo de correspondência).
- **Estados**:
  - **Principal**: Exibe todos os blocos preenchidos.
  - **Vazio**: Bio vazia exibe placeholder suave `"Nenhuma descrição adicionada"`; 0 destaques exibe 4 cards pontilhados `Slot Disponível`.
  - **Carregamento**: Skeleton em `Card` via `Suspense` enquanto `ensureQueryData` resolve.
  - **Erro**: `404` de `:id` inexistente aciona `ErroRota` com mensagem `"Perfil comunitário não encontrado"`.
  - **Privacidade / Proxy (`PerfilComunitarioVisibilidadeProxy`)**: Visitante não vê botões `Editar Perfil`, `Gerenciar Slots`, `×` de remoção de destaque, nem `+ Adicionar deck favorito`. Se `exibirDecksFavoritos == false` ou `aberturaPreferida == OCULTA`, o Proxy omite os respectivos blocos para o visitante.

### Protótipo 2 — Edição do Perfil (`P02`)
- **Rota D5**: `/comunidade/perfil-comunitario/:id/alteracao` (renderizado em `Modal` sobre o perfil ou rota dedicada).
- **Operações BFF**: `GET /backend/comunidade/perfil-comunitario/:id/alteracao` e `POST /backend/comunidade/perfil-comunitario/:id/alterar`.
- **Layout e Campos (`React Hook Form` + `Modal`)**:
  - Título `Editar Perfil`.
  - **Foto de Perfil**: Prévia circular + aviso `"PNG, JPG ou GIF recomendados até 5MB"`, botões `Alterar Foto` e `Remover`.
  - **Apelido Comunitário** (`@`): Campo texto obrigatório; helper `"Seu identificador único para amigos, trocas e cotações."`.
  - **Cidade e Estado**: Campo texto com ícone de localização (ex.: `São Paulo, SP`).
  - **Bio / Descrição de Perfil**: `Form.Control as="textarea"` com contador ao vivo `X / 200`.
  - **Visibilidade e Preferências**: Checkboxes `Wishlist pública com token seguro` (`"Permite que membros da comunidade cotem e proponham trocas diretamente."`) e `Exibir Decks Favoritos no perfil` (`"Mostra suas listas ativas no formato oficial Bandai para outros colecionadores."`).
  - **Rodapé**: `Cancelar` (descarta rascunho e preserva estado anterior) e `Salvar Alterações`.
- **Validações e Erros**:
  - Apelido vazio → erro em linha `"O apelido é obrigatório."`
  - Apelido já usado por outro usuário (inclusive variação de caixa, ex.: `ZoroTC` vs. `zorotc`) → `publicar('erro', 'O apelido "..." já está em uso.')`.
  - Tentativa de editar `:id` de outro usuário → bloqueio no `PerfilComunitarioVisibilidadeProxy` com erro de autorização (`403`).

### Protótipo 3 — Cartas Favoritas Preenchidas (`P03`)
- **Rota D5**: `/comunidade/perfil-comunitario/visualizacao` (seção `Cartas Favoritas`).
- **Operações BFF**: `POST /backend/comunidade/perfil-comunitario/:id/remover-destaque`.
- **Layout e Componentes**:
  - Grid de 4 cards (`4 de 4 slots preenchidos`), cada card exibindo: badges de raridade (`LÍDER / SEC`, `SR`, `SEC`) e edição/condição (`Edição Colecionador`, `Holo Foil`, `Art Alternativa`, `Near Mint`), botão circular `×` no canto superior direito (somente proprietário), imagem da carta, tag de cor (`Vermelho`, `Verde`, `Azul`), nome da carta, código (`OP01-001`), preço (`R$ 1.850,00 • Mkt Base`), botão `Ver no Catálogo` e atalho de bookmark.
- **Estados**:
  - **Proprietário vs. Visitante**: Visitante vê a vitrine e `Ver no Catálogo`, sem botões `×` e `Gerenciar Slots`.
  - **Erro**: Falha ao remover destaque publica erro via `useNotificacoes`.

### Protótipo 4 — Espaço Disponível nas Cartas Favoritas (`P04`)
- **Rota D5**: `/comunidade/perfil-comunitario/visualizacao` (estado com `< 4` slots ocupados).
- **Layout e Componentes**:
  - Badge atualizado para `3 de 4 slots preenchidos`.
  - O 4º card assume estilo pontilhado (`border-dashed bg-light`) com ícone `+`, título `"Slot Disponível"`, subtítulo `"Toque para adicionar da coleção"` e botão primário `+ Adicionar Carta` abaixo do slot.
- **Estados**:
  - **Vazio (0 de 4)**: Para o proprietário, exibe até 4 slots disponíveis com `+ Adicionar Carta`; para visitante, exibe estado informativo `"Este colecionador ainda não destacou cartas favoritas"`.

### Protótipo 5 — Seleção de Carta Favorita (`P05`)
- **Rota D5**: Modal acionado a partir de `/comunidade/perfil-comunitario/visualizacao` (`Slot Disponível` / `+ Adicionar Carta`).
- **Operações BFF**: `POST /backend/comunidade/perfil-comunitario/:id/fixar-destaque` (lista cartas da coleção do usuário).
- **Layout e Componentes**:
  - `Modal` título `Escolher Carta Favorita`.
  - `Alert variant="info"`: `"Regra de Negócio (4 Slots Estritos): Selecione uma carta da sua coleção para alocar no slot disponível da vitrine comunitária."`
  - Lista `CARTAS DISPONÍVEIS DA COLEÇÃO` com miniatura, nome, código • raridade • cor • preço e botão `Fixar Slot` (desabilitado/indicado se já estiver nos destaques).
  - Rodapé `Cancelar`.
- **Validações, Erros e Estados**:
  - **Vazio**: Se coleção vazia ou todas já fixadas, exibe `"Nenhuma nova carta disponível na sua coleção para fixação."`
  - **Erro de regra**: Tentar fixar 5ª carta ou carta duplicada retorna erro via `publicar('erro', mensagemErro(erro))` (`"Limite de 4 destaques atingido"` / `"Carta já está nos destaques"`).

### Protótipo 19 (Complementar Amigos) — Buscar Colecionadores e Enviar Solicitação (`P19`)
- **Rota D5**: `/comunidade/amizade/pesquisa` e `/comunidade/amizade/inclusao`.
- **Operações BFF**: `GET /backend/comunidade/amizade/pesquisa`, `GET /backend/comunidade/amizade/inclusao`, `POST /backend/comunidade/amizade/incluir`.
- **Layout e Componentes**:
  - Aba `Buscar Colecionadores` na navegação comunitária.
  - Barra de busca por apelido ou cidade (`Buscar colecionador por apelido...`).
  - Lista/cards de colecionadores com avatar, apelido, cidade, selo de abertura preferida pública (se houver) e botão contextual: `Adicionar Amigo`, `Solicitação Enviada` (com atalho `Cancelar`), `Responder Solicitação` ou `Amigos` + `Ver Perfil`.
- **Validações e Erros**:
  - Impede enviar solicitação para si mesmo, usuário inexistente, amizade já ativa ou solicitação pendente duplicada (exibe feedback via `useNotificacoes`).
  - **Vazio**: `"Nenhum colecionador encontrado para o termo pesquisado."`

### Protótipo 20 (Complementar Amigos) — Rede de Amigos e Solicitações Pendentes (`P20`)
- **Rota D5**: `/comunidade/amizade/pesquisa` (abas internas `Meus Amigos`, `Recebidas`, `Enviadas`) e `/comunidade/amizade/:id/visualizacao`.
- **Operações BFF**: `POST /backend/comunidade/amizade/:id/aceitar`, `POST /backend/comunidade/amizade/:id/recusar`, `POST /backend/comunidade/amizade/:id/cancelar`, `POST /backend/comunidade/amizade/:id/remover`.
- **Layout e Componentes**:
  - **Aba `Meus Amigos`**: Lista de amigos ativos com atalho `Ver Perfil` e botão `Remover Amigo` (com modal de confirmação avisando que a remoção é **bilateral** — apaga o vínculo dos dois lados).
  - **Aba `Solicitações Recebidas`**: Lista de solicitações `PENDENTE` recebidas com botões `Aceitar` e `Recusar`.
  - **Aba `Solicitações Enviadas`**: Lista de solicitações `PENDENTE` enviadas com botão `Cancelar Solicitação`.
- **Validações, Erros e Estados**:
  - Tentativa de aceitar/cancelar solicitação de terceiros ou remover usuário não amigo retorna erro de autorização/negócio via `publicar('erro', ...)`.
  - **Vazio**: Mensagens distintas para zero amigos, zero solicitações recebidas e zero solicitações enviadas.

---

## 3. Protótipos de Alta Fidelidade — `comunidade / F002`: Decks Favoritos

### Protótipo 6 — Perfil com Decks e Wishlist (`P06`)
- **Rota D5**: `/comunidade/perfil-comunitario/visualizacao` (e `/comunidade/perfil-comunitario/:id/visualizacao`).
- **Layout e Componentes**:
  - Visão integrada do perfil exibindo: barra comunitária (`Meu Perfil`, `Decks Favoritos`, `Minha Coleção`, `Rede de Amigos`, `Buscar Colecionadores`, `Abertura Preferida`), card do perfil, grid de `Cartas Favoritas` e, na base em 2 colunas, `Decks Favoritos` (esquerda) e `Wishlist Pública` (direita com link tokenizado `tcghub.com/w/...`, botão `Copiar` e atalho `Gerenciar Lista Completa na Wishlist →`).
- **Privacidade / Proxy (`PerfilComunitarioVisibilidadeProxy`)**: Se `exibirDecksFavoritos` estiver desmarcado, visitantes não visualizam o card de `Decks Favoritos` (exibido apenas ao proprietário com badge informativo `Oculto no perfil público`).

### Protótipo 7 — Lista de Decks Favoritos (`P07`)
- **Rota D5**: `/comunidade/deck-favorito/pesquisa` e `/comunidade/deck-favorito/:id/exclusao`.
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/pesquisa`, `POST /backend/comunidade/deck-favorito/:id/excluir`, `POST /backend/comunidade/deck-favorito/ocultar`, `POST /backend/comunidade/deck-favorito/reexibir`.
- **Layout e Componentes**:
  - Cabeçalho `Decks Favoritos`, badge `X/4 decks favoritos`, texto `"Formato Oficial: 1 Líder + 50 Cartas"`, controle de visibilidade (`Exibir no Perfil Público` / `Ocultar` / `Reexibir`) e botão `+ Adicionar deck favorito` (desabilitado quando `4/4`).
  - Cards horizontais para cada deck salvo: miniatura do líder, nome do deck (`Straw Hat Aggro Red`), badge `50 + 1`, subtítulo `Líder: Monkey D. Luffy (OP01-001)`, valor estimado (`R$ 890,00 • Base do Mercado`), botão `Visualizar deck` e botão `Remover` (somente proprietário).
  - Rodapé informativo com link de importação/apoio.
- **Estados**:
  - **Vazio**: `"Você ainda não adicionou decks favoritos. Adicione até 4 decks do Catálogo."`
  - **Deck Inativado no Catálogo**: Deck já favoritado permanece visível na lista com badge `Inativo no Catálogo`, preservando consulta/remoção pelo dono.
  - **Erro**: Remover deck inexistente/não favoritado ou ação de outro usuário dispara `publicar('erro', ...)`.

### Protótipo 8 — Adicionar Deck Favorito (`P08`)
- **Rota D5**: `/comunidade/deck-favorito/inclusao` (Modal sobre a lista/perfil).
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/inclusao` e `POST /backend/comunidade/deck-favorito/incluir`.
- **Layout e Componentes**:
  - `Modal` título `Adicionar Deck Favorito`, subtítulo `"Pesquise no catálogo oficial e fixe em seus decks favoritos"`.
  - Banner informativo: `"Limite oficial: máximo de 4 decks favoritos."` + contador `X de 4 slots ocupados`.
  - Campo de busca: `"Buscar deck por nome, líder ou código..."`.
  - Lista de decks **ativos** consultados via `CatalogoDeckProxy`: cada item exibe imagem, nome, `Líder: ...`, valor estimado e botão `✓ Adicionado` (desabilitado se já nos favoritos) ou `+ Adicionar`.
  - Rodapé: `"Formato: 1 Líder + 50 Cartas"` e botão `Cancelar`.
- **Validações, Erros e Proxy (`CatalogoDeckProxy`)**:
  - Rejeita inclusão se: limite de 4 atingido, deck duplicado, deck inativo ou deck inexistente.
  - Se a SPI do Catálogo estiver indisponível, exibe alerta amigável `"Serviço de Catálogo temporariamente indisponível. Tente novamente em instantes."` com ação de tentar novamente.

### Protótipo 9 — Composição do Deck Favorito (`P09`)
- **Rota D5**: `/comunidade/deck-favorito/:id/visualizacao` (Modal amplo de inspeção).
- **Operações BFF**: `GET /backend/comunidade/deck-favorito/:id/visualizacao`.
- **Layout e Componentes**:
  - Cabeçalho: Nome do deck (`Straw Hat Aggro Red`), `Líder: Monkey D. Luffy (OP01-001)`, badge `51 cartas (1 Líder + 50 Cartas)`, metadados e botão fechar (`×`).
  - Barra de resumo: pílulas `1 Líder`, `32 Personagens`, `12 Eventos`, `6 Estágios` e à direita `Custo Estimado no Mercado: R$ 890,00`.
  - Subtítulo `COMPOSIÇÃO DETALHADA E AGRUPADA (X ENTRADAS DE CATÁLOGO)` + selo `Conformidade Estrita (Máx 4 cópias)`.
  - Lista de cartas obtida via `CatalogoDeckProxy`: imagem/ícone de tipo, badge de raridade + código + categoria, nome, nota comunitária, custo/poder, badge de quantidade (`x1 Líder`, `x4 no Deck`), preço unitário (`R$ 145,00 preço unitário`) e botão `Ver no Catálogo →` (navega para a página da carta no catálogo).
  - Rodapé: `"Deck válido de 50 cartas • Estrutura 100% legalizada para torneios oficiais Bandai TCG."`, botões `Fechar` e `Copiar Decklist`.
- **Estados e Proxy (`CatalogoDeckProxy`)**:
  - **Carregamento**: Spinner/skeleton durante chamada ao Proxy do Catálogo.
  - **Indisponibilidade do Catálogo**: Mensagem explícita `"Não foi possível carregar a composição porque o serviço de Catálogo está indisponível."` sem quebrar a tela (`ErroRota` ou estado de erro tratável dentro do modal).

---

## 4. Protótipos de Alta Fidelidade — `comunidade / F003`: Avaliação de Cartas

### Protótipo 10 — Coleção Pessoal como Entrada da Avaliação (`P10`)
- **Contexto / Papel**: Tela de apoio de `Minha Coleção` (domínio de apoio / entrada de navegação) integrando atalhos para `Avaliação de cartas`.
- **Layout e Componentes**:
  - Cabeçalho `Cards Recentes no Binder` + `X Cards Registrados`.
  - Grid de cartas da coleção pessoal exibindo imagem, badges de raridade/condição, código + cor, nome, média comunitária (`★ 4.9 (42)` ou `☆ Sem avaliações`), controle de quantidade (`- 1 +`) e ação contextual de avaliação:
    - Se usuário já avaliou: badge `(Sua: 5.0)` + ícone de edição que abre `/comunidade/avaliacao-produto/:id/alteracao` (Protótipo 14).
    - Se usuário ainda não avaliou (e elegível via `CompraVerificada`): link/botão `Avaliar` que abre `/comunidade/avaliacao-produto/inclusao` (Protótipo 13).

### Protótipo 11 — Detalhes da Carta e Avaliações (`P11`)
- **Contexto / Papel**: Página de produto no Catálogo com seção integrada de `Avaliações da Comunidade` (`/comunidade/avaliacao-produto/pesquisa?produtoId=...`).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/pesquisa`.
- **Layout e Componentes**:
  - Bloco superior do produto (imagem da carta, código, nome, tipo, cor, poder, vida/custo, preço, estoque, ações comerciais da loja e gráfico `Histórico de Mercado`).
  - Bloco inferior `Avaliações da Comunidade` (detalhado no Protótipo 12) com botão primário `Avaliar Carta` (ou `Editar Minha Avaliação`).

### Protótipo 12 — Avaliações da Comunidade (`P12`)
- **Rota D5**: `/comunidade/avaliacao-produto/pesquisa` (integrada na página da carta).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/pesquisa`.
- **Layout e Componentes**:
  - Cabeçalho `Avaliações da Comunidade` (`"Opiniões de colecionadores e avaliações registradas no catálogo da rede"`) + botão `Avaliar Carta`.
  - Card de resumo à esquerda: nota média em destaque (`4.9` ou `0.0`), 5 estrelas visuais, `"Baseado em X avaliações válidas"` e, caso o usuário já tenha avaliado, `"• Sua nota: 5.0"` + link `Editar Minha Avaliação`.
  - Histograma à direita: barras `ProgressBar` para `5 ★`, `4 ★`, `3 ★`, `2 ★`, `1 ★` com contagens absolutas.
  - Lista de avaliações válidas: apelido do avaliador + selo obrigatório **`✓ Compra Verificada`**, data relativa (`Há 2 dias`), estrelas atribuídas e comentário textual.
- **Estados**:
  - **Vazio (0 avaliações)**: Exibe média `0.0`, `0 avaliações válidas`, barras zeradas e texto `"Esta carta ainda não possui avaliações da comunidade."`
  - **Invalidação por Reembolso**: Avaliações cujo único pedido elegível foi reembolsado desaparecem da lista pública e a média/contagem é recalculada automaticamente.

### Protótipo 13 — Nova Avaliação (`P13`)
- **Rota D5**: `/comunidade/avaliacao-produto/inclusao` (Modal sobre Coleção ou Catálogo).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/inclusao` e `POST /backend/comunidade/avaliacao-produto/incluir`.
- **Layout e Campos (`React Hook Form` + `Modal`)**:
  - Cabeçalho `Avaliar Carta` + badge de tipo (`LÍDER`).
  - Card resumo da carta (`OP01-003 • Vermelho`, nome, preço médio, cópia em coleção) + card com média comunitária atual.
  - Seletor interativo de **1 a 5 estrelas** (`"Sua Avaliação — Selecione uma nota de 1 a 5 estrelas"`).
  - Campo opcional `Seu Comentário (opcional):` (`textarea`).
  - Rodapé: `Cancelar` (mantém estado anterior sem salvar) e `Salvar Avaliação` (habilitado após escolher nota de 1 a 5).
- **Validações e Proxy (`AvaliacaoElegibilidadeProxy`)**:
  - Nota obrigatória entre `1` e `5` (`< 1` ou `> 5` rejeitados).
  - Antes de abrir ou ao submeter, `AvaliacaoElegibilidadeProxy` valida `CompraVerificada`: usuários sem pedido, com pagamento pendente ou com único pedido reembolsado têm a avaliação bloqueada/rejeitada com mensagem clara (`"Apenas compradores com pedido pago e não reembolsado podem avaliar esta carta."`).

### Protótipo 14 — Atualização da Avaliação (`P14`)
- **Rota D5**: `/comunidade/avaliacao-produto/:id/alteracao` (Modal sobre Coleção ou Catálogo).
- **Operações BFF**: `GET /backend/comunidade/avaliacao-produto/:id/alteracao` e `POST /backend/comunidade/avaliacao-produto/:id/alterar`.
- **Layout e Campos (`React Hook Form` + `Modal`)**:
  - Cabeçalho `Sua Avaliação da Carta` + resumo da carta e da comunidade.
  - Indicador `✓ Avaliação Enviada` + nota atual (`5.0 ★`) + data (`Avaliado em DD/MM/AAAA`).
  - Estrelas interativas preenchidas com legenda dinâmica (ex.: `Excelente - 5.0 estrelas`) e instrução `"Clique nas estrelas acima caso queira alterar a sua nota."`
  - Campo `Seu Comentário:` marcado como `Editável`.
  - Rodapé: `Cancelar` (preserva versão anterior) e `Atualizar Avaliação`.
- **Validações e Proxy (`AvaliacaoElegibilidadeProxy`)**:
  - Reavaliar atualiza o mesmo registro sem duplicar a contagem na média.
  - Impede edição de avaliação de outro usuário (`403` via Proxy) e impede atualização caso todas as compras tenham sido reembolsadas.

---

## 5. Protótipos de Alta Fidelidade — `adm / F001` e `comunidade / F004`: Abertura Preferida de One Piece

### Protótipo 15 — Gestão Administrativa das Aberturas (`P15`)
- **Rota D5**: `/adm/abertura/pesquisa` (e ações `:id/visualizacao`, `:id/exclusao`, `inativar`, `reativar`).
- **Operações BFF**: `GET /backend/adm/abertura/pesquisa`, `POST /backend/adm/abertura/:id/inativar`, `POST /backend/adm/abertura/:id/reativar`, `POST /backend/adm/abertura/:id/excluir`.
- **Layout e Componentes (`TCGHub Admin`)**:
  - Menu/item exibido somente quando `autorizacoesDe('abertura')` possui autorização `ADMINISTRADOR`.
  - Breadcrumb `Início > Mídia & Catálogo > Aberturas de One Piece`, título `Aberturas`, subtítulo `"Gerenciamento do catálogo oficial de temas musicais de One Piece"`, botão primário `+ Cadastrar abertura`.
  - Pílulas de filtro com contagem: `Todas (N)`, `Ativas (N)`, `Inativas (N)` + barra de busca `"Buscar por número ou nome (ex: Hope, 20)..."`.
  - Tabela (`Table`) com colunas: `MÍDIA` (miniatura 16:9), `Nº` (`#01`), `NOME DA ABERTURA`, `ARTISTA`, `EPISÓDIOS`, `PREFERÊNCIAS` (coração + total de escolhas na comunidade), `STATUS` (`• Ativa` / `• Inativa`), `AÇÕES` (ícones: Visualizar `/adm/abertura/:id/visualizacao`, Editar `/adm/abertura/:id/alteracao`, Inativar/Reativar e menu `⋮` com Excluir `/adm/abertura/:id/exclusao`).
  - Paginação inferior (`Exibindo 1-5 de N aberturas`).
- **Estados e Regras**:
  - **Vazio**: `"Nenhuma abertura encontrada para os filtros selecionados."`
  - **Regra de exclusão**: A ação `Excluir` é bloqueada pelo backend quando `preferencias > 0`, exibindo via `publicar('erro', ...)` a mensagem `"A abertura já foi escolhida por usuários e só pode ser inativada."`
  - **Autorização**: Usuários sem papel `ADMINISTRADOR` recebem rejeição de autorização em qualquer rota/ação de `adm/abertura`.

### Protótipo 16 — Cadastro Administrativo de Abertura (`P16`)
- **Rota D5**: `/adm/abertura/inclusao` e `/adm/abertura/:id/alteracao` (Modal ou página de formulário).
- **Operações BFF**: `GET /backend/adm/abertura/inclusao` / `POST /backend/adm/abertura/incluir`; `GET /backend/adm/abertura/:id/alteracao` / `POST /backend/adm/abertura/:id/alterar`.
- **Layout e Campos (`React Hook Form` + `Modal`)**:
  - Cabeçalho `Cadastrar abertura` (`"Adicione uma nova música tema à base canônica"`) ou `Editar abertura`.
  - `Número da Abertura *` (`number > 0`, único).
  - `Nome da Abertura *` (texto obrigatório, ex.: `Us!`).
  - `Artista / Intérprete *` (texto obrigatório, ex.: `Hiroshi Kitadani`).
  - `Ep. Inicial *` e `Ep. Final *` (compõem `episódios`, ex.: `1 a 47` ou `1120 - Atual`).
  - `URL da Imagem de Capa (Aspecto 16:9)` + botão `Testar` + caixa de pré-visualização `"Prévia da imagem aparecerá aqui"`.
  - `Referência de Vídeo Público (YouTube / URL)`.
  - Checkbox `Ativa para novas escolhas` (`"Usuários do TCGHub poderão selecionar este tema musical em seu perfil imediatamente."`).
  - Rodapé: `Cancelar` e `Cadastrar abertura` (ou `Salvar alterações`).
- **Validações e Erros**:
  - Número `<= 0` ou campos obrigatórios vazios → validação em linha e rejeição no domínio.
  - Número já existente em outra abertura (na inclusão ou na alteração) → `publicar('erro', 'Já existe uma abertura com o número X.')`.
  - Teste de imagem com URL quebrada exibe fallback de erro na caixa de prévia.

### Protótipo 17 — Catálogo e Ranking de Aberturas (`P17`)
- **Rota D5**: `/comunidade/abertura-preferida/pesquisa`, `/comunidade/abertura-preferida/inclusao` e `/comunidade/abertura-preferida/ranking`.
- **Operações BFF**: `GET /backend/comunidade/abertura-preferida/pesquisa`, `GET /backend/comunidade/abertura-preferida/ranking`, `POST /backend/comunidade/abertura-preferida/incluir`, `POST /backend/comunidade/abertura-preferida/ocultar`, `POST /backend/comunidade/abertura-preferida/reexibir`.
- **Layout e Componentes**:
  - **Banner Superior da Escolha Atual**: Miniatura, badge `ABERTURA ESCOLHIDA` + `X amigos também escolheram`, título `Op. 01 — We Are!`, artista + episódios, botão `Ver Detalhes` e switch `Exibir no Perfil Público` (`"Visível para outros nakamas"` / `"Oculta no perfil público"`). Se nenhuma escolhida ainda, exibe convite `"Escolha sua abertura preferida abaixo"`.
  - **Área Principal (Esquerda)**: Busca `"Filtrar por nome, número de abertura ou artista..."`, pílulas de filtro (`Todas as Aberturas`, `Escolhidas por Amigos`, `Top Ranking`), grid de cards apenas com aberturas **`ATIVAS`** exibindo: capa 16:9, badge `★ SUA PREFERIDA` ou `#N NO RANKING`, badge `Op. XX`, título, artista • episódios, total de votos e **contagem de amigos** (`X amigos`), botão `✓ Ativa no Perfil` ou `Definir como Preferida` + ícone de olho para alternar visibilidade.
  - **Sidebar Direita (`RANKING DA COMUNIDADE — Top 5 Escolhas Oficiais`)**: Card `Total de Votos Ativos`, lista ordenada `#1` a `#5` com barra proporcional (`ProgressBar`), percentual (ex.: `33,3%`) e votos, mais nota de rodapé de privacidade: `"Preferências com privacidade restrita somam no cálculo proporcional sem expor o usuário."`
- **Estados e Proxy (`AberturaPreferidaPrivacidadeProxy`)**:
  - **Ranking Vazio**: Exibe `Total de Votos Ativos: 0` e estado `"Nenhuma escolha registrada na comunidade ainda."`
  - **Abertura Inativa**: Aberturas inativadas no `adm` **não** aparecem na listagem para novas escolhas. Se a abertura atual do usuário tiver sido inativada, ela permanece visível no banner superior até que ele escolha outra abertura ativa.
  - **Privacidade**: Escolhas ocultas continuam contabilizadas no Top 5 sem revelar nomes no perfil público ou na correspondência entre amigos.

### Protótipo 18 — Detalhes da Abertura Preferida (`P18`)
- **Rota D5**: `/comunidade/abertura-preferida/:id/visualizacao`.
- **Operações BFF**: `GET /backend/comunidade/abertura-preferida/:id/visualizacao`.
- **Layout e Componentes**:
  - Mesmo banner superior com toggle `Exibir no Perfil Público`, link `← Voltar para Catálogo de Aberturas` e tag `Reprodução Oficial • Modo Foco`.
  - **Hero Banner**: Capa em destaque com overlay escuro, badges `OP. 01` e arco/episódios, título (`We Are!`), artista e episódios (`Hiroshi Kitadani • Episódios 001 a 047`), botão `▶ Assistir Abertura` (reproduz vídeo oficial) e botão `☆ Sua Preferida (Ativa)` (ou `Definir como Preferida`).
  - **3 Cards Informativos Inferiores**:
    1. `Amigos que Escolheram`: Total (`X Amigos`) + apelidos dos amigos cuja visibilidade é pública (filtrados pelo `AberturaPreferidaPrivacidadeProxy`).
    2. `Posição Global`: `#N no Ranking Oficial` + votos ativos e percentual.
    3. `Cronologia do Anime`: Quantidade/faixa de episódios.
- **Estados e Erros**:
  - `:id` inexistente aciona `ErroRota` (`404`).
  - Amigos com escolha oculta não aparecem na lista nominal de correspondência.

---

## 6. Matriz de Rastreabilidade (Protótipos ↔ Especificações `RN00X` ↔ Cenários BDD)

| Protótipo | Funcionalidade / Subdomínio | Rota(s) D5 Principal(is) | Regras de Negócio (`tcghub/docs/.../funcionalidades/`) | Cenários BDD (`.feature`) Relacionados |
| :--- | :--- | :--- | :--- | :--- |
| **P01** — Visão geral do perfil | `comunidade / F001` | `/comunidade/perfil-comunitario/visualizacao`, `:id/visualizacao` | `comunidade/F001`: `RN001`, `RN002`, `RN003` | `perfil-e-amigos.feature`: Consultar próprio perfil, Consultar perfil público, Rejeitar perfil inexistente |
| **P02** — Edição do perfil | `comunidade / F001` | `/comunidade/perfil-comunitario/:id/alteracao` | `comunidade/F001`: `RN001`, `RN002` | `perfil-e-amigos.feature`: Editar dados, Cancelar edição, Manter próprio apelido, Rejeitar apelido duplicado (inclusive *case-insensitive*), Rejeitar apelido vazio, Rejeitar edição de outro usuário |
| **P03** — Cartas favoritas preenchidas | `comunidade / F001` | `/comunidade/perfil-comunitario/visualizacao` | `comunidade/F001`: `RN002`, `RN003` | `perfil-e-amigos.feature`: Não fixar mais de 4 cartas, Remover carta dos destaques |
| **P04** — Espaço disponível nas cartas favoritas | `comunidade / F001` | `/comunidade/perfil-comunitario/visualizacao` | `comunidade/F001`: `RN003` | `perfil-e-amigos.feature`: Remover carta libera espaço para fixar outra |
| **P05** — Seleção de carta favorita | `comunidade / F001` | Modal em `/comunidade/perfil-comunitario/visualizacao` | `comunidade/F001`: `RN002`, `RN003` | `perfil-e-amigos.feature`: Fixar carta da coleção, Rejeitar >4 ou duplicata |
| **P19** — Buscar colecionadores e solicitar amizade | `comunidade / F001` | `/comunidade/amizade/pesquisa`, `/comunidade/amizade/inclusao` | `comunidade/F001`: `RN004` | `perfil-e-amigos.feature`: Enviar solicitação, Rejeitar autoamizade, Rejeitar inexistente, Rejeitar solicitação duplicada, Rejeitar amizade já ativa |
| **P20** — Rede de amigos e solicitações pendentes | `comunidade / F001` | `/comunidade/amizade/pesquisa`, `:id/visualizacao` | `comunidade/F001`: `RN005`, `RN006` | `perfil-e-amigos.feature`: Aceitar solicitação (bilateral), Rejeitar aceitação por terceiro, Recusar, Cancelar, Rejeitar cancelamento por terceiro, Remover bilateralmente, Rejeitar remoção de não amigo |
| **P06** — Perfil com decks e wishlist | `comunidade / F001` + `F002` | `/comunidade/perfil-comunitario/visualizacao` | `comunidade/F001`: `RN002`; `comunidade/F002`: `RN001`, `RN003`, `RN004` | `decks-favoritos.feature`: Ocultar e reexibir decks favoritos no perfil público |
| **P07** — Lista de decks favoritos | `comunidade / F002` | `/comunidade/deck-favorito/pesquisa`, `:id/exclusao` | `comunidade/F002`: `RN001`, `RN002`, `RN004`, `RN005` | `decks-favoritos.feature`: Remover deck libera vaga, Rejeitar remoção de não favorito, Preservar deck tornado inativo, Ocultar/reexibir, Rejeitar gestão de outro usuário |
| **P08** — Adicionar deck favorito | `comunidade / F002` | `/comunidade/deck-favorito/inclusao` | `comunidade/F002`: `RN001`, `RN002`, `RN005` | `decks-favoritos.feature`: Pesquisar decks disponíveis, Adicionar até 4, Rejeitar 5º deck, Rejeitar duplicata, Rejeitar deck inativo/inexistente |
| **P09** — Composição do deck favorito | `comunidade / F002` | `/comunidade/deck-favorito/:id/visualizacao` | `comunidade/F002`: `RN003` | `decks-favoritos.feature`: Visualizar composição via interface do Catálogo, Tratar indisponibilidade do Catálogo |
| **P10** — Coleção pessoal (entrada avaliação) | `comunidade / F003` *(apoio)* | Entrada para `/comunidade/avaliacao-produto/inclusao` / `:id/alteracao` | `comunidade/F003`: `RN001`, `RN002` | `avaliacao-de-cartas.feature`: Registrar avaliação, Reavaliar carta existente |
| **P11** — Detalhes da carta e avaliações | `comunidade / F003` *(apoio)* | Entrada para `/comunidade/avaliacao-produto/pesquisa` | `comunidade/F003`: `RN001`, `RN004`, `RN006` | `avaliacao-de-cartas.feature`: Consultar média e contagem no catálogo |
| **P12** — Avaliações da comunidade | `comunidade / F003` | `/comunidade/avaliacao-produto/pesquisa` | `comunidade/F003`: `RN004`, `RN005`, `RN006` | `avaliacao-de-cartas.feature`: Média e contagem, Carta sem avaliações (`0.0`), Selo compra verificada, Invalidação e recálculo após reembolso |
| **P13** — Nova avaliação | `comunidade / F003` | `/comunidade/avaliacao-produto/inclusao` | `comunidade/F003`: `RN001`, `RN003`, `RN006` | `avaliacao-de-cartas.feature`: Registrar avaliação (com/sem comentário, nota 1–5), Rejeitar sem compra/não pago/reembolsado, Permitir se houver outro pedido pago, Rejeitar nota `<1` ou `>5` |
| **P14** — Atualização da avaliação | `comunidade / F003` | `/comunidade/avaliacao-produto/:id/alteracao` | `comunidade/F003`: `RN002`, `RN003`, `RN005` | `avaliacao-de-cartas.feature`: Reavaliar sem duplicar contagem, Cancelar edição preserva anterior, Rejeitar alteração de outro usuário, Manter válida se houver outra compra |
| **P15** — Gestão administrativa das aberturas | `adm / F001` | `/adm/abertura/pesquisa`, `:id/visualizacao`, `:id/exclusao` | `adm/F001`: `RN001`, `RN002`, `RN004`, `RN005`, `RN006` | `abertura.feature`: Consultar por número, Pesquisar/filtrar por status, Inativar, Reativar, Excluir nunca escolhida, Rejeitar exclusão de já escolhida, Rejeitar sem autorização `ADMINISTRADOR` |
| **P16** — Cadastro administrativo de abertura | `adm / F001` | `/adm/abertura/inclusao`, `/adm/abertura/:id/alteracao` | `adm/F001`: `RN001`, `RN002`, `RN003` | `abertura.feature`: Cadastrar ativa/inativa, Editar dados, Rejeitar sem autorização, Rejeitar número duplicado (cadastro/edição), Rejeitar dados inválidos, Rejeitar edição inexistente |
| **P17** — Catálogo e ranking de aberturas | `comunidade / F004` | `/comunidade/abertura-preferida/pesquisa`, `inclusao`, `ranking` | `comunidade/F004`: `RN001`, `RN002`, `RN003`, `RN004`, `RN005`, `RN006` | `abertura-preferida.feature`: Listar apenas ativas com contagem de amigos, Pesquisar por nome/artista, Escolher/substituir preferida, Rejeitar inativa/inexistente, Ocultar/reexibir, Ranking Top 5 (com ocultas e vazio), Manter escolha inativada e permitir troca |
| **P18** — Detalhes da abertura preferida | `comunidade / F004` | `/comunidade/abertura-preferida/:id/visualizacao` | `comunidade/F004`: `RN003`, `RN004`, `RN005` | `abertura-preferida.feature`: Consultar detalhes com posição e amigos, Indicar/não indicar/ocultar correspondência no perfil do amigo |
