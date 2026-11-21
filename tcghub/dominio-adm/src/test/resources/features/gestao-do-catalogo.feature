# language: pt
Funcionalidade: Gestão do catálogo One Piece TCG
  Para manter a vitrine da loja TCGHub correta, disponível e auditável
  Como administrador do TCGHub
  Quero cadastrar, editar, listar, esgotar e ocultar produtos de carta avulsa, booster box e deck,
  controlando estoque, preço e histórico auditado, e manter as coleções do catálogo

  Contexto:
    Dado que existe a coleção "OP-01" com nome "Romance Dawn"
    E que existe a carta "OP01-001" com nome "Roronoa Zoro" do tipo "LIDER" na coleção "OP-01"

  # Coleções

  Cenário: Cadastrar, editar e listar coleções do catálogo
    Dado que sou um usuário administrador
    Quando eu cadastro a coleção "OP-02" com nome "Paramount War"
    Então a coleção "OP-02" é cadastrada com sucesso
    Quando eu edito o nome da coleção "OP-02" para "Paramount War - Reedição"
    Então a coleção "OP-02" passa a ter o nome "Paramount War - Reedição"
    Quando eu listo as coleções do catálogo
    Então a listagem de coleções contém "OP-01" e "OP-02"

  Cenário: Excluir coleção sem cartas nem produtos vinculados
    Dado que sou um usuário administrador
    E que existe a coleção "OP-09" com nome "Emperors in the New World" sem cartas nem produtos vinculados
    Quando eu excluo a coleção "OP-09"
    Então a coleção "OP-09" é excluída com sucesso
    E a coleção "OP-09" deixa de existir no catálogo

  Cenário: Não é possível excluir coleção com cartas ou produtos vinculados
    Dado que sou um usuário administrador
    Quando eu tento excluir a coleção "OP-01"
    Então a exclusão da coleção é rejeitada porque a coleção "OP-01" possui cartas ou produtos vinculados
    E a coleção "OP-01" continua a existir no catálogo

  Cenário: Não é possível gerenciar o catálogo sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    Quando eu tento cadastrar a coleção "OP-03" com nome "Pillars of Strength"
    Então a operação no catálogo é rejeitada por falta de autorização

  # RN1: Código de carta único

  Cenário: Cadastrar carta com código inédito
    Dado que sou um usuário administrador
    Quando eu cadastro a carta "OP01-025" com nome "Monkey.D.Luffy" do tipo "PERSONAGEM", cor "VERMELHO" e raridade "SR" na coleção "OP-01"
    Então a carta "OP01-025" é cadastrada com sucesso

  Cenário: Não é possível cadastrar duas cartas com o mesmo código
    Dado que sou um usuário administrador
    Quando eu tento cadastrar a carta "OP01-001" com nome "Outra Carta" do tipo "PERSONAGEM", cor "VERDE" e raridade "C" na coleção "OP-01"
    Então o cadastro da carta é rejeitado porque já existe uma carta com o código "OP01-001"

  Cenário: Buscar carta por código ou por nome
    Dado que existe a carta "OP01-025" com nome "Monkey.D.Luffy" do tipo "PERSONAGEM" na coleção "OP-01"
    Quando eu busco cartas pelo código "OP01-025"
    Então a busca de cartas retorna apenas a carta "OP01-025"
    Quando eu busco cartas pelo nome "zoro"
    Então a busca de cartas retorna apenas a carta "OP01-001"

  # RN2: Composição válida de deck

  Cenário: Cadastrar deck com composição válida
    Dado que sou um usuário administrador
    Quando eu cadastro o deck "Starter Zoro" com o líder "OP01-001", "50" cartas no deck principal com no máximo "4" cópias por carta e "10" cartas DON!!
    Então o deck "Starter Zoro" é cadastrado com sucesso
    E a composição do deck "Starter Zoro" soma "50" cartas, "1" líder e "10" cartas DON!!

  Cenário: Não é possível cadastrar deck sem líder
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o deck "Sem Líder" sem líder, com "50" cartas no deck principal e "10" cartas DON!!
    Então o cadastro do deck é rejeitado porque o deck precisa de exatamente "1" líder

  Cenário: Não é possível cadastrar deck com quantidade de cartas diferente de 50
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o deck "Incompleto" com o líder "OP01-001", "48" cartas no deck principal e "10" cartas DON!!
    Então o cadastro do deck é rejeitado porque o deck principal deve ter exatamente "50" cartas

  Cenário: Não é possível cadastrar deck com mais de 4 cópias da mesma carta
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o deck "Muitas Cópias" com o líder "OP01-001", "50" cartas no deck principal incluindo "5" cópias da carta "OP01-025" e "10" cartas DON!!
    Então o cadastro do deck é rejeitado porque a carta "OP01-025" excede o limite de "4" cópias

  Cenário: Não é possível cadastrar deck com quantidade de cartas DON!! diferente de 10
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o deck "Pouco DON" com o líder "OP01-001", "50" cartas no deck principal e "8" cartas DON!!
    Então o cadastro do deck é rejeitado porque o deck deve ter exatamente "10" cartas DON!!

  # Produtos: cadastro, edição e listagem

  Cenário: Cadastrar produtos de carta avulsa, booster box e deck
    Dado que sou um usuário administrador
    Quando eu cadastro o produto "Zoro Líder NM" da categoria "CARTA_AVULSA" vinculado à carta "OP01-001", preço "25.00", preço mínimo de segurança "15.00" e estoque "8"
    E eu cadastro o produto "Booster Box OP-01" da categoria "BOOSTER_BOX" vinculado à coleção "OP-01", preço "650.00", preço mínimo de segurança "500.00" e estoque "3"
    E eu cadastro o produto "Starter Deck Zoro" da categoria "DECK" vinculado ao deck "Starter Zoro", preço "90.00", preço mínimo de segurança "70.00" e estoque "5"
    Então os produtos "Zoro Líder NM", "Booster Box OP-01" e "Starter Deck Zoro" são cadastrados com visibilidade "ATIVO"
    E cada cadastro gera um ajuste de estoque do tipo "CADASTRO" com a quantidade inicial

  Cenário: Editar dados de um produto existente
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu edito a descrição do produto "Zoro Líder NM" para "Carta líder em estado Near Mint"
    Então o produto "Zoro Líder NM" passa a ter a descrição "Carta líder em estado Near Mint"

  Cenário: Listar produtos filtrando por categoria, coleção e visibilidade
    Dado que sou um usuário administrador
    E que existem produtos das categorias "CARTA_AVULSA", "BOOSTER_BOX" e "DECK" com visibilidades "ATIVO", "ESGOTADO" e "OCULTO"
    Quando eu listo os produtos filtrando pela categoria "BOOSTER_BOX", coleção "OP-01" e visibilidade "ATIVO"
    Então a listagem de produtos retorna apenas os produtos compatíveis com todos os filtros

  # RN3: Alteração de preço gera histórico

  Cenário: Alterar preço gera registro no histórico e evento PrecoAlterado
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu altero o preço do produto "Zoro Líder NM" para "30.00"
    Então o produto "Zoro Líder NM" passa a ter o preço "30.00"
    E é gravado um registro de histórico de preço do produto "Zoro Líder NM" de "25.00" para "30.00" com o administrador responsável e a data da alteração
    E é disparado o evento "PrecoAlterado" para o produto "Zoro Líder NM"

  Cenário: Consultar o histórico de preços de um produto
    Dado que sou um usuário administrador
    E que o produto "Zoro Líder NM" teve o preço alterado de "25.00" para "30.00" e depois de "30.00" para "28.00"
    Quando eu consulto o histórico de preços do produto "Zoro Líder NM"
    Então o histórico de preços exibe "2" registros ordenados da alteração mais recente para a mais antiga

  # RN4: Estoque e preço válidos

  Cenário: Não é possível gravar produto com preço zero ou negativo
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o produto "Carta Grátis" da categoria "CARTA_AVULSA" com preço "0.00", preço mínimo de segurança "0.00" e estoque "1"
    Então a gravação do produto é rejeitada porque o preço deve ser maior que zero

  Cenário: Não é possível gravar produto com preço abaixo do preço mínimo de segurança
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu tento alterar o preço do produto "Zoro Líder NM" para "10.00"
    Então a gravação do produto é rejeitada porque o preço não pode ser menor que o preço mínimo de segurança "15.00"
    E nenhum registro de histórico de preço é gravado para o produto "Zoro Líder NM"

  Cenário: Não é possível gravar produto com estoque negativo
    Dado que sou um usuário administrador
    Quando eu tento cadastrar o produto "Carta Negativa" da categoria "CARTA_AVULSA" com preço "10.00", preço mínimo de segurança "5.00" e estoque "-1"
    Então a gravação do produto é rejeitada porque o estoque não pode ser negativo

  Cenário: Produto cadastrado como ativo com estoque zero passa a esgotado
    Dado que sou um usuário administrador
    Quando eu cadastro o produto "Nami Parallel" da categoria "CARTA_AVULSA" com preço "120.00", preço mínimo de segurança "100.00", estoque "0" e visibilidade "ATIVO"
    Então o produto "Nami Parallel" é cadastrado com visibilidade "ESGOTADO"

  # RN5: Produto vendido nunca é apagado

  Cenário: Excluir fisicamente produto que nunca foi vendido
    Dado que sou um usuário administrador
    E que existe o produto "Produto Teste" com preço "10.00", preço mínimo de segurança "5.00", estoque "2" e visibilidade "ATIVO"
    E que o produto "Produto Teste" nunca foi vendido
    Quando eu excluo o produto "Produto Teste"
    Então o produto "Produto Teste" é excluído do catálogo

  Cenário: Produto já vendido é ocultado em vez de excluído
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    E que o produto "Zoro Líder NM" já consta em itens de pedido
    Quando eu tento excluir o produto "Zoro Líder NM"
    Então a exclusão física do produto é bloqueada porque o produto "Zoro Líder NM" já foi vendido
    E o produto "Zoro Líder NM" passa a ter visibilidade "OCULTO"
    E o produto "Zoro Líder NM" continua registrado no banco, mas indisponível para compra

  Cenário: Ocultar e reexibir um produto manualmente
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu oculto o produto "Zoro Líder NM"
    Então o produto "Zoro Líder NM" passa a ter visibilidade "OCULTO"
    Quando eu reexibo o produto "Zoro Líder NM"
    Então o produto "Zoro Líder NM" passa a ter visibilidade "ATIVO"

  # RN6: Esgotar estoque com auditoria

  Esquema do Cenário: Esgotar produto informando o motivo gera ajuste auditado e evento ProdutoEsgotado
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu esgoto o produto "Zoro Líder NM" com o motivo "<motivo>" e Avise-me "<aviseMe>"
    Então o produto "Zoro Líder NM" passa a ter estoque "0" e visibilidade "ESGOTADO"
    E é gerado um ajuste de estoque do tipo "ESGOTAMENTO" com o motivo "<motivo>", quantidade anterior "8" e um código de auditoria único
    E é disparado o evento "ProdutoEsgotado" para o produto "Zoro Líder NM"
    E o botão Avise-me do produto "Zoro Líder NM" fica "<aviseMe>"

    Exemplos:
      | motivo                  | aviseMe      |
      | ESTOQUE_FISICO_ZERADO   | habilitado   |
      | RESERVA_MANUTENCAO_LOTE | desabilitado |
      | DEFEITO_AVARIA          | habilitado   |

  Cenário: Não é possível esgotar produto sem informar o motivo
    Dado que sou um usuário administrador
    E que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando eu tento esgotar o produto "Zoro Líder NM" sem informar o motivo
    Então o esgotamento é rejeitado porque o motivo é obrigatório
    E o produto "Zoro Líder NM" continua com estoque "8" e visibilidade "ATIVO"

  # RN7: Movimentação automática de estoque

  Cenário: Pedido pago baixa o estoque e gera ajuste de venda
    Dado que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "8" e visibilidade "ATIVO"
    Quando o pedido "PED-100" com "3" unidades do produto "Zoro Líder NM" é pago
    Então o produto "Zoro Líder NM" passa a ter estoque "5"
    E é gerado um ajuste de estoque do tipo "VENDA" de "3" unidades referente ao pedido "PED-100"

  Esquema do Cenário: Cancelamento, reembolso ou estorno devolve as unidades ao estoque
    Dado que existe o produto "Zoro Líder NM" com preço "25.00", preço mínimo de segurança "15.00", estoque "5" e visibilidade "ATIVO"
    E que o pedido "PED-100" com "3" unidades do produto "Zoro Líder NM" foi pago
    Quando o pedido "PED-100" sofre "<operacao>"
    Então o produto "Zoro Líder NM" passa a ter estoque "8"
    E é gerado um ajuste de estoque do tipo "DEVOLUCAO" de "3" unidades referente ao pedido "PED-100"

    Exemplos:
      | operacao     |
      | cancelamento |
      | reembolso    |
      | estorno      |

  Cenário: Produto passa sozinho para esgotado quando a venda zera o estoque
    Dado que existe o produto "Booster Box OP-01" com preço "650.00", preço mínimo de segurança "500.00", estoque "2" e visibilidade "ATIVO"
    Quando o pedido "PED-200" com "2" unidades do produto "Booster Box OP-01" é pago
    Então o produto "Booster Box OP-01" passa a ter estoque "0" e visibilidade "ESGOTADO"
    E é gerado um ajuste de estoque do tipo "VENDA" de "2" unidades referente ao pedido "PED-200"

  Cenário: Repor estoque de produto esgotado gera ajuste e evento EstoqueReposto
    Dado que sou um usuário administrador
    E que existe o produto "Booster Box OP-01" com preço "650.00", preço mínimo de segurança "500.00", estoque "0" e visibilidade "ESGOTADO"
    Quando eu reponho "4" unidades do produto "Booster Box OP-01"
    Então o produto "Booster Box OP-01" passa a ter estoque "4" e visibilidade "ATIVO"
    E é gerado um ajuste de estoque do tipo "REPOSICAO" de "4" unidades
    E é disparado o evento "EstoqueReposto" para o produto "Booster Box OP-01"
