# language: pt
Funcionalidade: Gerenciar decks favoritos no perfil comunitário
  Para exibir meus principais decks no perfil e consultar suas cartas e preços
  Como usuário do TCGHub
  Quero adicionar, remover, ocultar e visualizar a composição dos meus decks favoritos,
  respeitando o limite de 4 decks, a ausência de duplicatas e a integração de leitura com o Catálogo

  Contexto:
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR"
    E que o usuário "lucas" não possui decks favoritos cadastrados

  Cenário: Adicionar um deck favorito ao perfil
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    Quando o usuário "lucas" adiciona o deck "D1" aos seus decks favoritos
    Então o deck "D1" é adicionado aos decks favoritos de "lucas" com sucesso
    E a lista de decks favoritos de "lucas" contém 1 deck

  Cenário: Pesquisar decks disponíveis para adicionar aos favoritos
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que existe o deck ativo "D2" de nome "Blue Crocodile Control" com líder "OP01-062"
    Quando o usuário "lucas" pesquisa decks disponíveis pelo termo "Zoro"
    Então o resultado da pesquisa de decks contém o deck "D1" de nome "Red Zoro Rush"

  Cenário: Adicionar até o limite máximo de 4 decks favoritos
    Dado que existem os decks ativos "D1", "D2", "D3" e "D4"
    E que o usuário "lucas" já possui os decks favoritos "D1", "D2" e "D3"
    Quando o usuário "lucas" adiciona o deck "D4" aos seus decks favoritos
    Então o deck "D4" é adicionado aos decks favoritos de "lucas" com sucesso
    E a lista de decks favoritos de "lucas" contém 4 decks

  Cenário: Não é possível adicionar um quinto deck favorito
    Dado que existem os decks ativos "D1", "D2", "D3", "D4" e "D5"
    E que o usuário "lucas" já possui 4 decks favoritos: "D1", "D2", "D3" e "D4"
    Quando o usuário "lucas" tenta adicionar o deck "D5" aos seus decks favoritos
    Então a inclusão do deck favorito é rejeitada porque o limite de 4 decks favoritos foi atingido

  Cenário: Não é possível adicionar o mesmo deck duas vezes aos favoritos
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos
    Quando o usuário "lucas" tenta adicionar o deck "D1" novamente aos seus decks favoritos
    Então a inclusão do deck favorito é rejeitada porque o deck "D1" já está nos favoritos

  Cenário: Não é possível adicionar um deck inativo aos favoritos
    Dado que existe o deck inativo "D9" de nome "Deck Descontinuado" com líder "OP01-001"
    Quando o usuário "lucas" tenta adicionar o deck "D9" aos seus decks favoritos
    Então a inclusão do deck favorito é rejeitada porque o deck "D9" está inativo

  Cenário: Não é possível adicionar um deck inexistente aos favoritos
    Quando o usuário "lucas" tenta adicionar o deck "D999" aos seus decks favoritos
    Então a inclusão do deck favorito é rejeitada porque o deck "D999" não existe

  Cenário: Remover um deck favorito libera vaga para outro deck
    Dado que existem os decks ativos "D1", "D2", "D3", "D4" e "D5"
    E que o usuário "lucas" já possui 4 decks favoritos: "D1", "D2", "D3" e "D4"
    Quando o usuário "lucas" remove o deck "D4" dos seus decks favoritos
    E adiciona o deck "D5" aos seus decks favoritos
    Então a lista de decks favoritos de "lucas" contém os decks "D1", "D2", "D3" e "D5"

  Cenário: Não é possível remover um deck que não está nos favoritos
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    Quando o usuário "lucas" tenta remover o deck "D1" dos seus decks favoritos
    Então a remoção do deck favorito é rejeitada porque o deck "D1" não está nos favoritos de "lucas"

  Cenário: Visualizar composição do deck favorito obtida via interface do Catálogo
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos
    E que a interface do Catálogo fornece para o deck "D1" o líder "Roronoa Zoro (OP01-001)" e as cartas "Nami (OP01-016)" quantidade 4 preço "12.50" e "Otama (OP01-006)" quantidade 4 preço "8.00"
    Quando o usuário "lucas" visualiza a composição expandida do deck favorito "D1"
    Então a composição exibida traz os dados consultados via interface do Catálogo com líder "Roronoa Zoro (OP01-001)", carta "Nami (OP01-016)" quantidade 4 preço "12.50" e carta "Otama (OP01-006)" quantidade 4 preço "8.00"

  Cenário: Tratar indisponibilidade da interface do Catálogo ao expandir composição de deck favorito
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos
    E que a interface do Catálogo está temporariamente indisponível
    Quando o usuário "lucas" tenta visualizar a composição expandida do deck favorito "D1"
    Então a consulta da composição do deck favorito é rejeitada porque o serviço de Catálogo está indisponível

  Cenário: Deck favorito que se torna inativo não fica disponível para novas escolhas
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos
    Quando o deck "D1" é marcado como inativo
    E o usuário "ricardo" tenta adicionar o deck "D1" aos seus decks favoritos
    Então a inclusão do deck favorito para "ricardo" é rejeitada porque o deck "D1" está inativo

  Cenário: Ocultar os decks favoritos no perfil público sem perder a lista cadastrada
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos
    Quando o usuário "lucas" oculta seus decks favoritos do perfil público
    E o usuário "ricardo" consulta os decks favoritos no perfil público de "lucas"
    Então nenhum deck favorito é exibido para o visitante "ricardo"
    Quando o usuário "lucas" consulta os próprios decks favoritos
    Então o usuário "lucas" continua visualizando o deck favorito "D1"

  Cenário: Reexibir os decks favoritos no perfil público
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" já possui o deck "D1" em seus decks favoritos com exibição oculta no perfil público
    Quando o usuário "lucas" reexibe seus decks favoritos no perfil público
    E o usuário "ricardo" consulta os decks favoritos no perfil público de "lucas"
    Então o visitante "ricardo" visualiza o deck favorito "D1" no perfil de "lucas"

  Cenário: Não é possível gerenciar decks favoritos de outro usuário
    Dado que existe o deck ativo "D1" de nome "Red Zoro Rush" com líder "OP01-001"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    Quando o usuário "ricardo" tenta adicionar o deck "D1" aos decks favoritos do perfil de "lucas"
    Então a operação em decks favoritos é rejeitada por falta de autorização
