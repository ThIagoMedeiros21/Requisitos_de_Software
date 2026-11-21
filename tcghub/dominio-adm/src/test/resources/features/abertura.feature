# language: pt
Funcionalidade: Gerenciar cadastro administrativo de aberturas de One Piece
  Para manter o catálogo de aberturas do anime disponíveis para a comunidade
  Como administrador do TCGHub
  Quero cadastrar, consultar, editar, inativar e excluir aberturas de One Piece,
  respeitando a unicidade do número, a autorização administrativa e a preservação de aberturas já escolhidas

  Contexto:
    Dado que não existe nenhuma abertura cadastrada

  Cenário: Cadastrar uma abertura ativa de One Piece
    Dado que sou um usuário administrador
    Quando eu cadastro a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47", imagem "we-are.jpg", vídeo "we-are.mp4" e status ativa
    Então a abertura número 1 é cadastrada com sucesso
    E a abertura número 1 passa a existir com status ativa

  Cenário: Cadastrar uma abertura inicialmente inativa
    Dado que sou um usuário administrador
    Quando eu cadastro a abertura número 2, nome "Believe", artista "Folder5", episódios "48 a 115", imagem "believe.jpg", vídeo "believe.mp4" e status inativa
    Então a abertura número 2 é cadastrada com sucesso
    E a abertura número 2 passa a existir com status inativa

  Cenário: Não é possível cadastrar abertura sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    Quando eu tento cadastrar a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47", imagem "we-are.jpg", vídeo "we-are.mp4" e status ativa
    Então o cadastro da abertura é rejeitado por falta de autorização

  Cenário: Não é possível cadastrar abertura com número já existente
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    Quando eu tento cadastrar a abertura número 1, nome "Outra Música", artista "Outro Artista", episódios "1 a 20", imagem "outra.jpg", vídeo "outra.mp4" e status ativa
    Então o cadastro da abertura é rejeitado porque já existe uma abertura com o número 1

  Cenário: Não é possível cadastrar abertura com número inválido ou dados obrigatórios vazios
    Dado que sou um usuário administrador
    Quando eu tento cadastrar a abertura número 0, nome "", artista "", episódios "1 a 47", imagem "img.jpg", vídeo "vid.mp4" e status ativa
    Então o cadastro da abertura é rejeitado por dados obrigatórios inválidos

  Cenário: Consultar uma abertura existente pelo número
    Dado que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    Quando eu consulto a abertura número 1
    Então a abertura encontrada tem nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa

  Cenário: Não é possível consultar uma abertura inexistente
    Quando eu tento consultar a abertura número 99
    Então a consulta da abertura é rejeitada porque não existe abertura com o número 99

  Cenário: Pesquisar e filtrar aberturas administrativas por status
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    E que existe a abertura número 2, nome "Believe", artista "Folder5", episódios "48 a 115" e status inativa
    Quando eu filtro as aberturas administrativas pelo status inativa
    Então a listagem administrativa retorna apenas a abertura número 2

  Cenário: Editar os dados de uma abertura existente
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    Quando eu edito a abertura número 1 para nome "We Are! (Remaster)", artista "Hiroshi Kitadani", episódios "1 a 47 e especial" e status ativa
    Então a edição da abertura é concluída com sucesso
    E a abertura número 1 passa a ter nome "We Are! (Remaster)" e episódios "1 a 47 e especial"

  Cenário: Não é possível editar abertura sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    Quando eu tento editar a abertura número 1 para nome "Alterado", artista "Outro", episódios "1 a 10" e status ativa
    Então a edição da abertura é rejeitada por falta de autorização

  Cenário: Não é possível editar uma abertura inexistente
    Dado que sou um usuário administrador
    Quando eu tento editar a abertura número 99 para nome "Inexistente", artista "Ninguém", episódios "1" e status ativa
    Então a edição da abertura é rejeitada porque não existe abertura com o número 99

  Cenário: Não é possível alterar o número de uma abertura para o número de outra já cadastrada
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    E que existe a abertura número 2, nome "Believe", artista "Folder5", episódios "48 a 115" e status ativa
    Quando eu tento alterar o número da abertura 2 para o número 1
    Então a edição da abertura é rejeitada porque já existe uma abertura com o número 1

  Cenário: Inativar uma abertura existente
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    E que a abertura número 1 já foi escolhida como preferida por usuários
    Quando eu inativo a abertura número 1
    Então a abertura número 1 é inativada com sucesso
    E a abertura número 1 passa a ter status inativa

  Cenário: Reativar uma abertura inativa
    Dado que sou um usuário administrador
    E que existe a abertura número 2, nome "Believe", artista "Folder5", episódios "48 a 115" e status inativa
    Quando eu reativo a abertura número 2
    Então a abertura número 2 é reativada com sucesso
    E a abertura número 2 passa a ter status ativa

  Cenário: Não é possível inativar abertura sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    Quando eu tento inativar a abertura número 1
    Então a inativação da abertura é rejeitada por falta de autorização

  Cenário: Não é possível inativar uma abertura inexistente
    Dado que sou um usuário administrador
    Quando eu tento inativar a abertura número 99
    Então a inativação da abertura é rejeitada porque não existe abertura com o número 99

  Cenário: Excluir uma abertura que nunca foi escolhida por nenhum usuário
    Dado que sou um usuário administrador
    E que existe a abertura número 3, nome "Hikari e", artista "The Babystars", episódios "116 a 168" e status ativa
    E que a abertura número 3 nunca foi escolhida como preferida por nenhum usuário
    Quando eu excluo a abertura número 3
    Então a abertura número 3 é excluída com sucesso
    E a abertura número 3 deixa de existir

  Cenário: Não é possível excluir uma abertura já escolhida por usuários
    Dado que sou um usuário administrador
    E que existe a abertura número 1, nome "We Are!", artista "Hiroshi Kitadani", episódios "1 a 47" e status ativa
    E que a abertura número 1 já foi escolhida como preferida por usuários
    Quando eu tento excluir a abertura número 1
    Então a exclusão da abertura é rejeitada porque a abertura número 1 já foi escolhida por usuários e só pode ser inativada
    E a abertura número 1 continua a existir

  Cenário: Não é possível excluir abertura sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    E que existe a abertura número 3, nome "Hikari e", artista "The Babystars", episódios "116 a 168" e status ativa
    Quando eu tento excluir a abertura número 3
    Então a exclusão da abertura é rejeitada por falta de autorização
