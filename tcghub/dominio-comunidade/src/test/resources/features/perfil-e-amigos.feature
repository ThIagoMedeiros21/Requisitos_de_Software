# language: pt
Funcionalidade: Gerenciar perfil comunitário e rede de amigos
  Para construir minha presença na comunidade e interagir com outros jogadores
  Como usuário do TCGHub
  Quero editar meu perfil comunitário, gerenciar destaques de cartas favoritas e administrar minha rede de amigos,
  respeitando a unicidade de apelido, a validade das amizades e a remoção bilateral

  Contexto:
    Dado que não existe nenhum perfil comunitário cadastrado
    E que não existe nenhuma solicitação ou amizade cadastrada

  Cenário: Consultar o próprio perfil comunitário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador de One Piece" e foto "avatar-luffy.png"
    Quando o usuário "lucas" consulta o seu próprio perfil comunitário
    Então o perfil comunitário retornado exibe apelido "LuffyBR", cidade "Recife", descrição "Colecionador de One Piece" e foto "avatar-luffy.png"

  Cenário: Consultar o perfil público de outro usuário
    Dado que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador de torneios" e foto "avatar-zoro.png"
    Quando o usuário "lucas" consulta o perfil público do usuário "ricardo"
    Então o perfil público retornado exibe apelido "ZoroTC", cidade "Olinda", descrição "Jogador de torneios" e foto "avatar-zoro.png"

  Cenário: Não é possível consultar um perfil comunitário inexistente
    Quando o usuário "lucas" tenta consultar o perfil público do usuário "inexistente"
    Então a consulta do perfil comunitário é rejeitada porque o perfil "inexistente" não existe

  Cenário: Editar dados do próprio perfil comunitário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" edita seu perfil comunitário para apelido "MonkeyD", cidade "São Paulo", descrição "Foco em OP-09", foto "foto2.png" e preferências públicas "Trocas presenciais"
    Então a edição do perfil comunitário é concluída com sucesso
    E o perfil comunitário do usuário "lucas" passa a ter apelido "MonkeyD", cidade "São Paulo", descrição "Foco em OP-09", foto "foto2.png" e preferências públicas "Trocas presenciais"

  Cenário: Cancelar edição do perfil comunitário mantém o estado anterior
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" inicia a edição do perfil com apelido "OutroApelido" e cancela a alteração
    Então o perfil comunitário do usuário "lucas" permanece com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"

  Cenário: Editar o perfil mantendo o próprio apelido
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" edita seu perfil comunitário para apelido "LuffyBR", cidade "Jaboatão", descrição "Colecionador e jogador" e foto "foto1.png"
    Então a edição do perfil comunitário é concluída com sucesso
    E o perfil comunitário do usuário "lucas" passa a ter apelido "LuffyBR" e cidade "Jaboatão"

  Cenário: Não é possível definir um apelido já utilizado por outro usuário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    Quando o usuário "lucas" tenta editar seu perfil comunitário para o apelido "ZoroTC"
    Então a edição do perfil comunitário é rejeitada porque o apelido "ZoroTC" já está em uso

  Cenário: Não é possível definir um apelido já utilizado por outro usuário ainda que com variação de maiúsculas e minúsculas
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    Quando o usuário "lucas" tenta editar seu perfil comunitário para o apelido "zorotc"
    Então a edição do perfil comunitário é rejeitada porque o apelido "zorotc" já está em uso

  Cenário: Não é possível editar o perfil comunitário com apelido vazio
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" tenta editar seu perfil comunitário para o apelido ""
    Então a edição do perfil comunitário é rejeitada porque o apelido é obrigatório

  Cenário: Não é possível editar o perfil comunitário de outro usuário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    Quando o usuário "lucas" tenta editar o perfil comunitário do usuário "ricardo" para o apelido "Hackeado"
    Então a edição do perfil comunitário é rejeitada por falta de autorização

  Cenário: Fixar carta da coleção em um espaço disponível dos destaques do perfil
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que o usuário "lucas" possui a carta "OP01-001" na sua coleção
    Quando o usuário "lucas" fixa a carta "OP01-001" como destaque do seu perfil
    Então a carta "OP01-001" passa a constar nos destaques de cartas favoritas do perfil de "lucas"

  Cenário: Não é possível fixar mais de quatro cartas nos destaques do perfil
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que o usuário "lucas" já possui 4 cartas fixadas nos destaques do seu perfil: "OP01-001", "OP01-002", "OP01-003" e "OP01-004"
    E que o usuário "lucas" possui a carta "OP01-005" na sua coleção
    Quando o usuário "lucas" tenta fixar a carta "OP01-005" como destaque do seu perfil
    Então a fixação da carta nos destaques é rejeitada porque o limite de 4 destaques foi atingido

  Cenário: Não é possível fixar a mesma carta duas vezes nos destaques do perfil
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que o usuário "lucas" já possui a carta "OP01-001" fixada nos destaques do seu perfil
    Quando o usuário "lucas" tenta fixar a carta "OP01-001" novamente nos destaques do seu perfil
    Então a fixação da carta nos destaques é rejeitada porque a carta "OP01-001" já está nos destaques

  Cenário: Remover uma carta dos destaques libera espaço para fixar outra carta
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que o usuário "lucas" já possui 4 cartas fixadas nos destaques do seu perfil: "OP01-001", "OP01-002", "OP01-003" e "OP01-004"
    E que o usuário "lucas" possui a carta "OP01-005" na sua coleção
    Quando o usuário "lucas" remove a carta "OP01-004" dos destaques do seu perfil
    E fixa a carta "OP01-005" como destaque do seu perfil
    Então os destaques de cartas favoritas do perfil de "lucas" contêm "OP01-001", "OP01-002", "OP01-003" e "OP01-005"

  Cenário: Enviar solicitação de amizade a outro usuário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    Quando o usuário "lucas" envia uma solicitação de amizade para o usuário "ricardo"
    Então a solicitação de amizade de "lucas" para "ricardo" fica com status pendente

  Cenário: Não é possível enviar solicitação de amizade para si mesmo
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" tenta enviar uma solicitação de amizade para o usuário "lucas"
    Então a solicitação de amizade é rejeitada porque não é possível adicionar a si mesmo como amigo

  Cenário: Não é possível enviar solicitação de amizade para um usuário inexistente
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    Quando o usuário "lucas" tenta enviar uma solicitação de amizade para o usuário "inexistente"
    Então a solicitação de amizade é rejeitada porque o usuário "inexistente" não existe

  Cenário: Não é possível duplicar uma solicitação de amizade pendente
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "lucas" tenta enviar uma solicitação de amizade para o usuário "ricardo"
    Então a solicitação de amizade é rejeitada porque já existe uma solicitação pendente entre "lucas" e "ricardo"

  Cenário: Não é possível enviar solicitação de amizade quando já existe amizade ativa
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que "lucas" e "ricardo" já são amigos
    Quando o usuário "lucas" tenta enviar uma solicitação de amizade para o usuário "ricardo"
    Então a solicitação de amizade é rejeitada porque "lucas" e "ricardo" já são amigos

  Cenário: Aceitar uma solicitação de amizade estabelece amizade bilateral
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "ricardo" aceita a solicitação de amizade enviada por "lucas"
    Então "lucas" e "ricardo" passam a ser amigos bilateralmente
    E "ricardo" consta na lista de amigos de "lucas"
    E "lucas" consta na lista de amigos de "ricardo"

  Cenário: Não é possível aceitar solicitação de amizade destinada a outro usuário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe o perfil comunitário do usuário "thiago" com apelido "SanjiOP", cidade "Recife", descrição "Jogador" e foto "foto3.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "thiago" tenta aceitar a solicitação de amizade de "lucas" para "ricardo"
    Então a resposta à solicitação de amizade é rejeitada por falta de autorização

  Cenário: Recusar uma solicitação de amizade recebida não estabelece amizade
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "ricardo" recusa a solicitação de amizade enviada por "lucas"
    Então a solicitação de amizade de "lucas" para "ricardo" é encerrada sem criar amizade
    E "lucas" e "ricardo" não são amigos

  Cenário: Cancelar uma solicitação de amizade enviada
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "lucas" cancela a solicitação de amizade enviada para "ricardo"
    Então a solicitação de amizade pendente de "lucas" para "ricardo" deixa de existir

  Cenário: Não é possível cancelar uma solicitação de amizade enviada por outro usuário
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que existe uma solicitação de amizade pendente de "lucas" para "ricardo"
    Quando o usuário "ricardo" tenta cancelar a solicitação de amizade de "lucas" para "ricardo"
    Então o cancelamento da solicitação de amizade é rejeitado por falta de autorização

  Cenário: Remover um amigo apaga a relação nos dois lados
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    E que "lucas" e "ricardo" já são amigos
    Quando o usuário "lucas" remove "ricardo" da sua lista de amigos
    Então a amizade entre "lucas" e "ricardo" é removida dos dois lados
    E "ricardo" não consta mais na lista de amigos de "lucas"
    E "lucas" não consta mais na lista de amigos de "ricardo"

  Cenário: Não é possível remover um usuário que não é amigo
    Dado que existe o perfil comunitário do usuário "lucas" com apelido "LuffyBR", cidade "Recife", descrição "Colecionador" e foto "foto1.png"
    E que existe o perfil comunitário do usuário "ricardo" com apelido "ZoroTC", cidade "Olinda", descrição "Jogador" e foto "foto2.png"
    Quando o usuário "lucas" tenta remover "ricardo" da sua lista de amigos
    Então a remoção da amizade é rejeitada porque "lucas" e "ricardo" não são amigos
