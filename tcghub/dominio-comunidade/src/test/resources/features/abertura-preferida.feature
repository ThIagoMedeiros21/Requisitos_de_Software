# language: pt
Funcionalidade: Escolher abertura preferida do anime One Piece e consultar ranking comunitário
  Para expressar minha abertura preferida no perfil e acompanhar as preferências da comunidade e de amigos
  Como usuário do TCGHub
  Quero escolher, substituir, ocultar e detalhar minha abertura preferida, além de consultar o ranking Top 5 e amigos com a mesma preferência

  Contexto:
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR"
    E que o usuário "lucas" ainda não escolheu uma abertura preferida

  Cenário: Listar apenas aberturas ativas para escolha com contagem de amigos
    Dado que existem as aberturas ativas número 1 "We Are!" e número 4 "BON VOYAGE!"
    E que existe a abertura inativa número 2 "Believe"
    E que o usuário "ricardo" é amigo de "lucas" e escolheu a abertura número 1
    Quando o usuário "lucas" consulta as aberturas disponíveis para escolha
    Então a listagem de escolha exibe as aberturas número 1 com 1 amigo e número 4 com 0 amigos
    E a abertura inativa número 2 não aparece na listagem para novas escolhas

  Cenário: Pesquisar aberturas ativas por nome ou artista
    Dado que existem as aberturas ativas número 1 "We Are!" de "Hiroshi Kitadani" e número 5 "Kokoro no Chizu" de "BOYSTYLE"
    Quando o usuário "lucas" pesquisa aberturas disponíveis pelo termo "Kitadani"
    Então a pesquisa retorna apenas a abertura número 1 "We Are!"

  Cenário: Escolher uma abertura ativa como preferida
    Dado que existe a abertura ativa número 1 "We Are!" de "Hiroshi Kitadani" exibida nos episódios "1 a 47"
    Quando o usuário "lucas" escolhe a abertura número 1 como sua abertura preferida
    Então a abertura preferida do usuário "lucas" passa a ser a abertura número 1
    E a abertura número 1 é exibida no perfil público de "lucas"

  Cenário: Não é possível escolher uma abertura inativa como preferida
    Dado que existe a abertura inativa número 2 "Believe"
    Quando o usuário "lucas" tenta escolher a abertura número 2 como sua abertura preferida
    Então a escolha da abertura preferida é rejeitada porque a abertura número 2 está inativa

  Cenário: Não é possível escolher uma abertura inexistente como preferida
    Quando o usuário "lucas" tenta escolher a abertura número 99 como sua abertura preferida
    Então a escolha da abertura preferida é rejeitada porque a abertura número 99 não existe

  Cenário: Substituir a abertura preferida atual por outra abertura ativa
    Dado que existem as aberturas ativas número 1 "We Are!" e número 6 "Brand New World"
    E que o usuário "lucas" já escolheu a abertura número 1 como sua abertura preferida
    Quando o usuário "lucas" escolhe a abertura número 6 como sua nova abertura preferida
    Então a abertura preferida do usuário "lucas" passa a ser a abertura número 6
    E o usuário "lucas" mantém apenas 1 abertura preferida cadastrada

  Cenário: Ocultar a abertura preferida do perfil público sem perder a escolha
    Dado que existe a abertura ativa número 1 "We Are!"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" escolheu a abertura número 1 como sua abertura preferida
    Quando o usuário "lucas" oculta a abertura preferida do seu perfil público
    Então o visitante "ricardo" não visualiza a abertura preferida no perfil público de "lucas"
    E o usuário "lucas" mantém a abertura número 1 salva como sua abertura preferida

  Cenário: Reexibir a abertura preferida no perfil público
    Dado que existe a abertura ativa número 1 "We Are!"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" escolheu a abertura número 1 com exibição oculta no perfil público
    Quando o usuário "lucas" reexibe a abertura preferida no seu perfil público
    Então o visitante "ricardo" passa a visualizar a abertura número 1 no perfil público de "lucas"

  Cenário: Consultar o ranking Top 5 da comunidade com quantidades, percentuais e escolhas ocultas contabilizadas
    Dado que existem as aberturas ativas número 1 "We Are!", número 2 "Believe", número 3 "Hikari e", número 4 "BON VOYAGE!", número 5 "Kokoro no Chizu" e número 6 "Brand New World"
    E que 4 usuários escolheram a abertura número 1, sendo 1 deles com exibição oculta no perfil público
    E que 2 usuários escolheram a abertura número 2
    E que 1 usuário escolheu a abertura número 3
    E que 1 usuário escolheu a abertura número 4
    E que 1 usuário escolheu a abertura número 5
    E que 1 usuário escolheu a abertura número 6
    Quando o usuário "lucas" consulta o ranking da comunidade de aberturas preferidas
    Então o ranking exibe exatamente as 5 aberturas mais escolhidas sobre o total de 10 escolhas
    E a 1ª posição do ranking é a abertura número 1 com 4 escolhas e 40.0% do total, sem expor o nome do perfil que a ocultou
    E a 2ª posição do ranking é a abertura número 2 com 2 escolhas e 20.0% do total

  Cenário: Consultar o ranking da comunidade quando ainda não existem escolhas
    Quando o usuário "lucas" consulta o ranking da comunidade de aberturas preferidas
    Então o ranking retorna vazio com total de 0 escolhas

  Cenário: Indicar no perfil de um amigo quando ele possui a mesma abertura preferida
    Dado que existe a abertura ativa número 1 "We Are!"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que "lucas" e "ricardo" são amigos
    E que o usuário "lucas" escolheu a abertura número 1 com exibição pública
    E que o usuário "ricardo" escolheu a abertura número 1 com exibição pública
    Quando o usuário "lucas" consulta o perfil do amigo "ricardo"
    Então o sistema indica que "ricardo" possui a mesma abertura preferida que "lucas"

  Cenário: Não indicar correspondência no perfil do amigo quando a abertura é diferente
    Dado que existem as aberturas ativas número 1 "We Are!" e número 6 "Brand New World"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que "lucas" e "ricardo" são amigos
    E que o usuário "lucas" escolheu a abertura número 1
    E que o usuário "ricardo" escolheu a abertura número 6
    Quando o usuário "lucas" consulta o perfil do amigo "ricardo"
    Então o sistema indica que "ricardo" não possui a mesma abertura preferida que "lucas"

  Cenário: Não expor correspondência no perfil público do amigo quando ele ocultou a abertura preferida
    Dado que existe a abertura ativa número 1 "We Are!"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que "lucas" e "ricardo" são amigos
    E que o usuário "lucas" escolheu a abertura número 1 com exibição pública
    E que o usuário "ricardo" escolheu a abertura número 1 com exibição oculta no perfil público
    Quando o usuário "lucas" consulta o perfil do amigo "ricardo"
    Então o sistema não exibe a abertura preferida oculta de "ricardo" nem indica correspondência pública

  Cenário: Consultar detalhes da abertura preferida com posição no ranking e amigos que a escolheram
    Dado que existe a abertura ativa número 1 "We Are!" de "Hiroshi Kitadani" exibida nos episódios "1 a 47"
    E que o usuário "lucas" escolheu a abertura número 1
    E que "lucas" é amigo de "ricardo" e "ana", e ambos também escolheram a abertura número 1
    Quando o usuário "lucas" consulta os detalhes da abertura número 1
    Então os detalhes exibem artista "Hiroshi Kitadani", episódios "1 a 47", posição 1 no ranking e 2 amigos com a mesma abertura preferida

  Cenário: Manter a escolha existente quando uma abertura é inativada, bloqueando apenas novas escolhas
    Dado que existe a abertura ativa número 1 "We Are!"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC"
    E que o usuário "lucas" já escolheu a abertura número 1 como sua abertura preferida
    Quando a abertura número 1 é inativada administrativamente
    Então o usuário "lucas" mantém a abertura número 1 como sua abertura preferida no perfil
    E a abertura número 1 deixa de aparecer na listagem de novas escolhas para "ricardo"
    Quando o usuário "ricardo" tenta escolher a abertura número 1 como sua abertura preferida
    Então a escolha da abertura preferida para "ricardo" é rejeitada porque a abertura número 1 está inativa

  Cenário: Usuário que possuía uma abertura inativada pode substituí-la por outra abertura ativa
    Dado que existe a abertura inativa número 1 "We Are!" que já havia sido escolhida por "lucas"
    E que existe a abertura ativa número 6 "Brand New World"
    Quando o usuário "lucas" escolhe a abertura número 6 como sua nova abertura preferida
    Então a abertura preferida do usuário "lucas" passa a ser a abertura número 6
