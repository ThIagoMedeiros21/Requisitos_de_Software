# Linguagem Onipresente — TCGHub

> [!NOTE]
> Artefato de **DDD Preliminar (Etapa 1 — Planejar, Onda 2)** para as funcionalidades de **Lucas Menezes** no projeto **TCGHub**, em conformidade com [.agents.md](../../.agents.md) e [tcghub/AGENTS.md](../../tcghub/AGENTS.md).

---

## 1. Propósito e Governança Terminológica

Este glossário estabelece a **Linguagem Onipresente** oficial (em **português do Brasil**) compartilhada entre negócio, protótipos de alta fidelidade, especificações de funcionalidade (`tcghub/docs/<subdominio>/funcionalidades/`), cenários BDD (`.feature`), modelos Context Mapper (`.cml`), modelo relacional (`.dbml`) e código Java/React D5.

- **Escopo da Onda 2**: As **4 funcionalidades de Lucas Menezes** (`Perfil e amigos`, `Decks favoritos`, `Avaliação de cartas` e `Abertura preferida do anime One Piece`, incluindo o cadastro administrativo de aberturas em `adm`) e os conceitos compartilhados que as sustentam.
- **Trava de Padrão GoF**: Todo controle de acesso, visibilidade, privacidade, elegibilidade ou integração de consulta das funcionalidades de Lucas Menezes é mediado pelo padrão **Proxy** (exclusivo de Lucas), enquanto os fluxos estruturais base do framework D5 utilizam **Template Method** (exclusivo da camada compartilhada/transversal). O padrão **Observer** é exclusivo de Ricardo Freitas e **não** integra este vocabulário operacional de Lucas.

---

## 2. Mapa de Subdomínios e Agregados (Escopo Lucas Menezes)

| Funcionalidade (Negócio) | Subdomínio | ID na Especificação | Agregado(s) / Projeção | Proxy GoF Associado |
| :--- | :--- | :--- | :--- | :--- |
| Gestão administrativa de aberturas de One Piece | `adm` | `F001` | `Abertura` (`AberturaIdentificador`) | *(mediado por autorização `ADMINISTRADOR` no D5)* |
| Perfil comunitário e rede de amigos | `comunidade` | `F001` | `PerfilComunitario` (`PerfilComunitarioIdentificador`), `Amizade` (`AmizadeIdentificador`) | `PerfilComunitarioVisibilidadeProxy` |
| Decks favoritos | `comunidade` | `F002` | `DeckFavorito` (`DeckFavoritoIdentificador`) | `CatalogoDeckProxy` |
| Avaliação de cartas | `comunidade` | `F003` | `AvaliacaoProduto` (`AvaliacaoProdutoIdentificador`) + projeção local `CompraVerificada` | `AvaliacaoElegibilidadeProxy` |
| Abertura preferida do anime One Piece | `comunidade` | `F004` | `AberturaPreferida` (`AberturaPreferidaIdentificador`) | `AberturaPreferidaPrivacidadeProxy` |

---

## 3. Termos Compartilhados e Transversais (`compartilhado`, `loja`, D5)

| Termo | Categoria | Definição no Domínio TCGHub | Sinônimos Proibidos / Observações |
| :--- | :--- | :--- | :--- |
| **Usuário** | Papel (`Autorizacao.USUARIO`) | Pessoa autenticada na plataforma com permissão para manter perfil comunitário, rede de amigos, decks favoritos, avaliações de cartas compradas e abertura preferida. | Evitar *cliente* quando se referir à interação comunitária. |
| **Administrador** | Papel (`Autorizacao.ADMINISTRADOR`) | Operador autorizado do portal *TCGHub Admin* responsável pelo cadastro, edição, inativação, reativação e exclusão segura do catálogo oficial de aberturas do anime One Piece. | — |
| **Catálogo** | Contexto Delimitado (`loja`) / SPI | Contexto *upstream* que detém os produtos, cartas, preços de referência e composição oficial dos decks. O subdomínio `comunidade` consulta o Catálogo exclusivamente por interface/SPI (via `CatalogoDeckProxy`), nunca acessando seu banco diretamente. | Proibido acoplamento direto ao banco de `loja`. |
| **Carta** | Conceito de Referência (`loja`) | Item colecionável de One Piece TCG identificado por código oficial (ex.: `OP01-001`, `OP05-119`), nome, tipo (Líder, Personagem, Evento, Estágio), cor, raridade, condição e preço de mercado. | — |
| **Deck** | Conceito de Referência (`loja`) | Lista oficial de jogo no formato padrão Bandai (1 Líder + 50 Cartas = 51 cartas), com identificador único, nome, líder, status (`ATIVO`/`INATIVO`) e composição detalhada de cartas e preços no Catálogo. | — |
| **Produto** | Conceito de Referência (`loja`) | Item comercializável no Catálogo (como uma carta avulsa) vinculado a pedidos de compra e elegível a avaliações da comunidade. | — |
| **Pedido Pago** | Evento / Estado de Referência (`loja`) | Compra confirmada com pagamento aprovado e **não reembolsada**, contendo determinado produto para um usuário. É o pré-requisito para emissão do registro na projeção local `CompraVerificada`. | Pedido pendente ou cancelado não habilita avaliação. |
| **Reembolso** | Evento de Referência (`loja`) | Estorno de um pedido pago. Caso o usuário não possua outro pedido pago e não reembolsado para o mesmo produto, invalida sua avaliação na média da comunidade. | — |
| **Coleção Pessoal** | Contexto de Apoio (`comunidade`) | Acervo particular de cartas do usuário (*Binder*). Atua como fonte para fixar cartas nos 4 destaques do perfil e como ponto de entrada para avaliar cartas elegíveis. | Sob responsabilidade paralela de Ricardo (`Minha coleção`), integrada apenas como ponto de entrada/consulta. |
| **Wishlist Pública** | Área Pública de Apoio (`loja`/`comunidade`) | Bloco opcional exibido no perfil comunitário com link verificado por token seguro quando habilitado nas preferências de visibilidade do usuário. | — |
| **Proxy** | Padrão GoF (Exclusivo Lucas) | Intermediário estrutural que controla acesso, visibilidade, privacidade, elegibilidade de compra e integração segura com SPIs externas sem alterar o contrato do objeto real. | Uso exclusivo de Lucas Menezes; `Observer` é bloqueado. |
| **Template Method** | Padrão GoF (Transversal D5) | Esqueleto invariante das operações de domínio, aplicação e infraestrutura no framework D5 (`AgregadoServicoAbstrato`, `AgregadoRepositorioAbstrato`), delegando variações a ganchos das subclasses. | Exclusivo da camada compartilhada/transversal. |

---

## 4. Subdomínio `adm` — `F001`: Gestão Administrativa de Aberturas de One Piece

### 4.1 Conceitos, Entidades e Objetos de Valor

| Termo | Tipo DDD | Definição e Regras |
| :--- | :--- | :--- |
| **Abertura** | Entidade Raiz de Agregado (`Abertura`) | Tema musical oficial do anime *One Piece* mantido pelo Administrador na base canônica da plataforma para exibição e escolha pelos usuários da comunidade. |
| **Identificador da Abertura** | Objeto de Valor (`AberturaIdentificador`) | Chave identificadora única do agregado `Abertura` no subdomínio `adm`. |
| **Número da Abertura** | Objeto de Valor (`NumeroAbertura`) | Número inteiro positivo (> 0) e **único** na base administrativa (ex.: `1`, `2`, `20`). Dois registros nunca podem compartilhar o mesmo número, nem na criação nem na edição. |
| **Nome da Abertura** | Objeto de Valor (`NomeAbertura`) | Título oficial da música tema (ex.: `"We Are!"`, `"Believe"`, `"Hope"`). Campo obrigatório. |
| **Artista / Intérprete** | Objeto de Valor (`Artista`) | Cantor ou banda responsável pela abertura (ex.: `"Hiroshi Kitadani"`, `"Folder5"`). Campo obrigatório. |
| **Episódios de Exibição** | Objeto de Valor (`EpisodiosExibicao`) | Faixa de episódios em que a abertura foi exibida no anime (ex.: `"1 a 47"`, composta por episódio inicial e episódio final ou indicação de exibição atual). |
| **Imagem de Capa** | Objeto de Valor (`UrlImagemCapa`) | URL/referência visual da capa da abertura em aspecto 16:9, com suporte a teste de pré-visualização no cadastro administrativo. |
| **Vídeo Público** | Objeto de Valor (`UrlVideoPublico`) | Referência de vídeo público (ex.: YouTube / URL de mídia) utilizada para reprodução oficial no modo foco dos detalhes da abertura. |
| **Status da Abertura** | Enumeração (`StatusAbertura`) | Estado administrativo da abertura: `ATIVA` (disponível para novas escolhas na comunidade) ou `INATIVA` (oculta para novas escolhas, porém preservada para quem já a escolheu). |

### 4.2 Ações, Estados e Invariantes (`adm`)

- **Cadastrar Abertura**: Cria nova `Abertura` com status inicial `ATIVA` ou `INATIVA`, exigindo papel `ADMINISTRADOR`, campos obrigatórios válidos e unicidade do `Número da Abertura`.
- **Consultar / Pesquisar / Filtrar Aberturas**: Lista aberturas por número ou nome e filtra por status (`Todas`, `Ativas`, `Inativas`), exibindo contagem de preferências da comunidade.
- **Editar Abertura**: Atualiza dados da abertura (`nome`, `artista`, `episódios`, `imagem`, `vídeo`, `status` ou `número`), mantendo bloqueio de número duplicado e exigência de autorização `ADMINISTRADOR`.
- **Inativar Abertura**: Altera o status para `INATIVA`. Não remove escolhas já efetuadas por usuários, apenas impede novas escolhas.
- **Reativar Abertura**: Restaura uma abertura `INATIVA` para `ATIVA`, tornando-a novamente elegível para novas escolhas.
- **Excluir Abertura**: Exclusão física permitida **somente** se a abertura nunca tiver sido escolhida como preferida por nenhum usuário. Se já houver vínculo em `AberturaPreferida`, a exclusão é rejeitada e o administrador deve usar **Inativar Abertura**.

---

## 5. Subdomínio `comunidade` — `F001`: Perfil Comunitário e Rede de Amigos

### 5.1 Conceitos, Entidades e Objetos de Valor

| Termo | Tipo DDD | Definição e Regras |
| :--- | :--- | :--- |
| **Perfil Comunitário** | Entidade Raiz de Agregado (`PerfilComunitario`) | Representação social pública e editável de um usuário autenticado no TCGHub, contendo identidade pública, preferências de exibição e até 4 destaques de cartas favoritas. |
| **Identificador do Perfil Comunitário** | Objeto de Valor (`PerfilComunitarioIdentificador`) | Identificador único do perfil vinculado ao usuário proprietário. |
| **Apelido Comunitário** | Objeto de Valor (`Apelido`) | Identificador público único do colecionador (ex.: `TrafalgarAce_99`, `LuffyBR`, `ZoroTC`). Obrigatório e **único sem distinção de maiúsculas/minúsculas** (*case-insensitive* — ex.: `"ZoroTC"` impede `"zorotc"`). |
| **Cidade / Localização** | Objeto de Valor (`Cidade`) | Cidade e/ou estado informados pelo usuário para identificação regional e trocas presenciais (ex.: `"São Paulo, SP"`, `"Recife"`). |
| **Bio / Descrição de Perfil** | Objeto de Valor (`DescricaoPerfil`) | Texto de apresentação do colecionador (limite visual de 200 caracteres nos protótipos). |
| **Foto de Perfil** | Objeto de Valor (`FotoPerfil`) | Imagem/avatar do usuário (PNG, JPG ou GIF recomendados até 5 MB nos protótipos), com opção de alteração ou remoção. |
| **Preferências Públicas** | Objeto de Valor (`PreferenciasPublicas`) | Conjunto de preferências de privacidade e interação social (ex.: exibição da wishlist pública com token seguro, exibição de decks favoritos no perfil, observações públicas como trocas presenciais). |
| **Cartas Favoritas / Destaques do Perfil** | Coleção Limitada (`DestaquesPerfil`) | Vitrine de **no máximo 4 slots** de cartas provenientes da `Coleção Pessoal` do usuário, sem repetição da mesma carta. Cada destaque exibe código, raridade, cor, condição, valor de mercado e atalho `Ver no Catálogo`. |
| **Slot Disponível** | Estado de Vitrine | Posição vazia dentre os 4 espaços de `Cartas Favoritas`, liberada ao remover um destaque e preenchível via seleção de carta da coleção. |
| **Proprietário do Perfil** | Papel Contextual | Usuário dono do `PerfilComunitario`; único autorizado a editar dados cadastrais, gerir destaques, alterar preferências de visibilidade e administrar suas amizades. |
| **Visitante** | Papel Contextual | Outro usuário autenticado que consulta o perfil público de um colecionador através do `PerfilComunitarioVisibilidadeProxy`. |
| **Amizade** | Entidade Raiz de Agregado (`Amizade`) | Relação social bilateral entre dois usuários distintos estabelecida após solicitação enviada por um solicitante e aceita pelo destinatário. |
| **Identificador da Amizade** | Objeto de Valor (`AmizadeIdentificador`) | Identificador único do registro de solicitação/vínculo de amizade entre o par de usuários. |
| **Solicitação de Amizade** | Estado Inicial de `Amizade` | Pedido direcionado de um usuário (`Solicitante`) para outro usuário (`Destinatario`) distinto, iniciando com status `PENDENTE`. Não permite autoamizade nem duplicidade entre o mesmo par. |
| **Status da Amizade** | Enumeração (`StatusAmizade`) | Ciclo de vida da relação: `PENDENTE` (aguardando resposta do destinatário), `ATIVA` (amizade bilateral consolidada), `RECUSADA` (encerrada pelo destinatário sem criar amizade), `CANCELADA` (retirada pelo solicitante enquanto pendente). |
| **Remoção Bilateral** | Regra / Ação de Domínio | Ao remover um amigo, o vínculo é apagado dos dois lados simultaneamente: ambos deixam de constar na lista de amigos um do outro. |
| **Proxy de Visibilidade do Perfil** | Proxy GoF (`PerfilComunitarioVisibilidadeProxy`) | Intercepta consultas e mutações no `PerfilComunitario`, restringindo alterações ao proprietário autenticado e filtrando seções ocultas (como decks favoritos e abertura preferida ocultos) para visitantes. |

---

## 6. Subdomínio `comunidade` — `F002`: Decks Favoritos

### 6.1 Conceitos, Entidades e Objetos de Valor

| Termo | Tipo DDD | Definição e Regras |
| :--- | :--- | :--- |
| **Deck Favorito** | Entidade Raiz de Agregado (`DeckFavorito`) | Vínculo entre o perfil comunitário do usuário e um `Deck` oficial do Catálogo, permitindo exibição no perfil e inspeção interativa da composição. |
| **Identificador do Deck Favorito** | Objeto de Valor (`DeckFavoritoIdentificador`) | Chave identificadora do registro de deck favorito no perfil do usuário. |
| **Limite de 4 Decks Favoritos** | Invariante de Negócio | Cada perfil pode ter **no máximo 4 decks favoritos**. A tentativa de incluir o 5º deck é rejeitada até que outro seja removido. |
| **Visibilidade dos Decks Favoritos** | Objeto de Valor / Estado (`PUBLICO` \| `OCULTO`) | Controle de privacidade que permite ocultar a lista de decks favoritos para visitantes no perfil público sem apagar os decks salvos pelo proprietário. |
| **Composição do Deck** | Projeção sob Demanda (`ComposicaoDeck`) | Estrutura expandida de um deck retornada via SPI do Catálogo: líder (código, nome, cor, custo/vida/poder), contagem agrupada (1 Líder, Personagens, Eventos, Estágios — total 50+1), lista de cartas com quantidade, preço unitário e custo estimado total no mercado. |
| **Copiar Decklist** | Ação de Interface | Ação na visualização expandida que copia a lista estruturada de cartas do deck para a área de transferência do usuário. |
| **Deck Inativo** | Estado de Referência | Deck marcado como `INATIVO` no Catálogo. Não pode ser adicionado por novos usuários, mas permanece preservado para quem já o tinha nos favoritos antes da inativação. |
| **Proxy de Integração com Catálogo** | Proxy GoF (`CatalogoDeckProxy`) | Proxy responsável por consultar a SPI do contexto de `Catálogo` (`loja`) para validar existência/status do deck na inclusão e obter a `ComposicaoDeck` atualizada na expansão, tratando falhas de indisponibilidade do serviço remoto sem acessar o banco de `loja`. |

---

## 7. Subdomínio `comunidade` — `F003`: Avaliação de Cartas

### 7.1 Conceitos, Entidades e Objetos de Valor

| Termo | Tipo DDD | Definição e Regras |
| :--- | :--- | :--- |
| **Avaliação de Produto** | Entidade Raiz de Agregado (`AvaliacaoProduto`) | Opinião registrada por um comprador real sobre uma carta/produto do Catálogo, composta por nota numérica e comentário opcional. |
| **Identificador da Avaliação** | Objeto de Valor (`AvaliacaoProdutoIdentificador`) | Identificador único da avaliação (unicidade lógica por par usuário + produto: no máximo uma avaliação por usuário para o mesmo produto). |
| **Nota da Avaliação** | Objeto de Valor (`NotaAvaliacao`) | Valor inteiro obrigatório na faixa estrita de **1 a 5** estrelas. Valores < 1 ou > 5 são rejeitados. |
| **Comentário da Avaliação** | Objeto de Valor (`ComentarioAvaliacao`) | Texto opcional relatando experiência de jogo, estado de envio ou pontos fortes da carta. |
| **Compra Verificada** | Projeção Local (`CompraVerificada`) | Registro mantido em `comunidade` indicando que determinado usuário possui ao menos um **Pedido Pago e não reembolsado** contendo o produto avaliado. |
| **Selo "Compra Verificada"** | Indicador Visual / Estado (`SeloCompraVerificada`) | Emblema exibido publicamente junto a toda avaliação válida, comprovando que o autor é um comprador real do produto. |
| **Reavaliação** | Ação de Domínio | Atualização de nota e/ou comentário de uma avaliação já existente do mesmo usuário para o mesmo produto, substituindo o registro anterior sem duplicar a contagem na média. |
| **Invalidação por Reembolso** | Regra / Estado (`INVALIDADA_POR_REEMBOLSO`) | Quando o pedido associado é reembolsado e o usuário não possui outra compra elegível para o mesmo produto, a avaliação é invalidada e deixa de compor a média e a contagem exibidas no Catálogo. |
| **Resumo de Avaliações do Produto** | Projeção de Leitura (`ResumoAvaliacoesProduto`) | Agregado de leitura exibido no Catálogo e na Coleção: média aritmética (ex.: `4.9`, ou `0.0` quando vazia), quantidade de avaliações válidas e distribuição por estrelas (`5★` a `1★`). |
| **Proxy de Elegibilidade de Avaliação** | Proxy GoF (`AvaliacaoElegibilidadeProxy`) | Intercepta tentativas de inclusão e atualização de avaliação para verificar elegibilidade via `CompraVerificada` (pedido pago e não reembolsado) e autoria exclusiva do usuário autenticado. |

---

## 8. Subdomínio `comunidade` — `F004`: Abertura Preferida do Anime One Piece

### 8.1 Conceitos, Entidades e Objetos de Valor

| Termo | Tipo DDD | Definição e Regras |
| :--- | :--- | :--- |
| **Abertura Preferida** | Entidade Raiz de Agregado (`AberturaPreferida`) | Escolha única do usuário dentre as aberturas **ativas** cadastradas pelo Administrador no subdomínio `adm`. Escolher outra abertura substitui automaticamente a anterior (máximo 1 por usuário). |
| **Identificador da Abertura Preferida** | Objeto de Valor (`AberturaPreferidaIdentificador`) | Identificador único do vínculo de preferência musical do usuário. |
| **Visibilidade da Abertura Preferida** | Objeto de Valor (`VisibilidadeAberturaPreferida`) | Controle booleano/enumerado (`PUBLICA` \| `OCULTA`) que define se a abertura escolhida aparece no perfil público do usuário e na indicação de correspondência para amigos. |
| **Ranking da Comunidade (Top 5)** | Projeção / Consulta Analítica (`RankingComunidade`) | Lista ordenada das **5 aberturas mais escolhidas**, contendo posição (`#1` a `#5`), quantidade absoluta de votos/escolhas e percentual proporcional sobre o **Total de Votos Ativos** (ex.: `33,3%`). |
| **Escolha Oculta Contabilizada** | Regra de Privacidade do Ranking | Usuários que ocultaram a abertura do perfil público **continuam somando** na quantidade de votos e no percentual do ranking Top 5, porém sem exposição de sua identidade. |
| **Amigos com a Mesma Preferência** | Consulta Relacional (`AmigosMesmaPreferencia`) | Cruzamento entre `AberturaPreferida` e `Amizade` (`ATIVA`) indicando: (a) na listagem de escolha e nos detalhes da abertura, quantos amigos escolheram cada abertura; (b) no perfil público de um amigo, se ele compartilha a mesma abertura (respeitando a visibilidade pública de ambos). |
| **Preservação por Inativação** | Invariante de Ciclo de Vida | Caso a abertura preferida de um usuário seja inativada no `adm`, o usuário mantém sua escolha até decidir substituí-la por outra abertura `ATIVA`. |
| **Proxy de Privacidade da Abertura Preferida** | Proxy GoF (`AberturaPreferidaPrivacidadeProxy`) | Protege consultas de perfil público e correspondência entre amigos quando `VisibilidadeAberturaPreferida == OCULTA`, garantindo sigilo nominal mesmo enquanto a escolha permanece contabilizada no ranking anônimo Top 5. |

---

## 9. Termos de Integração e Eventos / SPIs

| Contrato / SPI / Evento | Origem → Destino | Descrição na Linguagem Onipresente |
| :--- | :--- | :--- |
| **SPI de Consulta ao Catálogo de Decks e Cartas** | `comunidade` → `loja` (`CatalogoDeckProxy`) | Consulta leitura de existência/status de deck, listagem pesquisável de decks ativos e composição detalhada (líder, cartas, quantidades e preços unitários). |
| **Projeção `CompraVerificada` (Pedido Pago / Reembolso)** | `loja` → `comunidade` (`AvaliacaoElegibilidadeProxy`) | Atualização da elegibilidade de compra por usuário e produto a partir de confirmação de pagamento e estorno/reembolso, mantendo isolamento de banco entre subdomínios. |
| **SPI de Aberturas Administrativas** | `comunidade` → `adm` | Leitura de aberturas `ATIVAS` para escolha comunitária, verificação de existência de escolhas em `AberturaPreferida` (para impedir exclusão física no `adm`) e preservação de dados de abertura inativada já escolhida. |
| **Integração de Leitura da Coleção Pessoal** | `comunidade` (`ColecaoPessoal`) → `PerfilComunitario` / `AvaliacaoProduto` | Consulta das cartas pertencentes à coleção do usuário para fixação nos 4 destaques e atalho de entrada na avaliação de cartas. |
