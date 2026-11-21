# language: pt
Funcionalidade: Comprar produto
  Para adquirir cartas, boxes e decks disponíveis na loja TCGHub
  Como cliente autenticado do sistema
  Quero navegar pelo catálogo, gerenciar meu carrinho, fechar pedidos com reserva temporária,
  calcular frete e descontos, pagar e acompanhar meus pedidos, respeitando a privacidade e as regras de negócio

  Contexto:
    Dado que existem produtos ativos cadastrados no catálogo com seus respectivos estoques
    E que existe uma tabela de tarifas de frete configurada para PAC e Sedex

  # Catálogo e Carrinho (RF Catálogo e RF Carrinho)

  Cenário: Consultar catálogo filtrando produtos por coleção, cor, raridade, tipo e faixa de preço
    Dado que existem produtos de cartas e boxes cadastrados em diferentes coleções, cores, raridades, tipos e preços
    Quando eu consulto o catálogo aplicando filtros de coleção "OP-01", cor "Vermelho", raridade "SR", tipo "Character" e preço entre "10.00" e "100.00"
    Então são exibidos apenas os produtos compatíveis com todos os filtros e com sua disponibilidade atual

  Cenário: Consultar detalhes de variantes distintas de uma mesma carta
    Dado que a carta "Monkey.D.Luffy" possui variante "Near Mint" por "45.00" com estoque "3" e variante "Played" por "30.00" com estoque "2"
    Quando eu consulto os detalhes da variante "Near Mint" da carta "Monkey.D.Luffy"
    Então são exibidos o preço "45.00", a condição "Near Mint" e a disponibilidade de "3" unidades

  Cenário: Adicionar produto ao carrinho sem reservar estoque
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque total "5" e estoque reservado "0"
    Quando eu adiciono "2" unidades de "Monkey.D.Luffy NM" ao meu carrinho
    Então o produto "Monkey.D.Luffy NM" é mantido no carrinho com quantidade "2"
    E o estoque reservado de "Monkey.D.Luffy NM" permanece "0"
    E o estoque disponível de "Monkey.D.Luffy NM" permanece "5"

  Cenário: Consolidar quantidade ao adicionar novamente o mesmo produto ao carrinho
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque disponível "5"
    E que meu carrinho já possui "2" unidades de "Monkey.D.Luffy NM"
    Quando eu adiciono "1" unidade de "Monkey.D.Luffy NM" ao meu carrinho
    Então o meu carrinho possui apenas um item para "Monkey.D.Luffy NM" com quantidade "3"

  Cenário: Alterar quantidade e remover produto do carrinho
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque disponível "5"
    E que meu carrinho possui "2" unidades de "Monkey.D.Luffy NM" e "1" unidade de "Box OP-01"
    Quando eu altero a quantidade de "Monkey.D.Luffy NM" no carrinho para "4"
    E eu removo "Box OP-01" do carrinho
    Então o meu carrinho passa a conter apenas "Monkey.D.Luffy NM" com quantidade "4"

  # RN1: Validação de quantidade

  Cenário: Não é possível adicionar ao carrinho produto com quantidade zero ou negativa
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque disponível "5"
    Quando eu tento adicionar "0" unidades de "Monkey.D.Luffy NM" ao meu carrinho
    Então a operação no carrinho é rejeitada porque a quantidade deve ser um inteiro positivo
    Quando eu tento adicionar "-2" unidades de "Monkey.D.Luffy NM" ao meu carrinho
    Então a operação no carrinho é rejeitada porque a quantidade deve ser um inteiro positivo

  Cenário: Não é possível adicionar ao carrinho ou criar pedido com quantidade superior ao estoque disponível
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque total "5" e estoque reservado "2"
    Quando eu tento adicionar "4" unidades de "Monkey.D.Luffy NM" ao meu carrinho
    Então a operação no carrinho é rejeitada por insuficiência de estoque disponível

  # RN2: Reserva de estoque por 30 minutos

  Cenário: Criar pedido pendente reservando estoque por 30 minutos
    Dado que sou o cliente "cliente-1" autenticado com endereço cadastrado de CEP "01310-100"
    E que o produto "Monkey.D.Luffy NM" possui estoque total "5", reservado "0", preço "50.00" e peso "10" gramas
    E que meu carrinho possui "2" unidades de "Monkey.D.Luffy NM"
    Quando eu crio o pedido no checkout com o endereço selecionado, modalidade "PAC" e método "CARTAO"
    Então o pedido é criado com status "PENDENTE" e prazo de expiração de "30" minutos
    E o produto "Monkey.D.Luffy NM" passa a ter estoque reservado "2" e estoque disponível "3"
    E o item do pedido registra preço unitário congelado de "50.00"

  Cenário: Impedir que outro cliente compre unidades já reservadas em pedido pendente
    Dado que o produto "Monkey.D.Luffy NM" possui estoque total "2" e reservado "0"
    E que o cliente "cliente-1" criou um pedido pendente com "2" unidades de "Monkey.D.Luffy NM"
    E que sou o cliente "cliente-2" autenticado com endereço cadastrado de CEP "20040-020"
    Quando eu tento criar um pedido com "1" unidade de "Monkey.D.Luffy NM"
    Então a criação do pedido é rejeitada por insuficiência de estoque disponível

  # RN3: Cálculo de frete

  Cenário: Calcular frete pela tabela de tarifas considerando CEP, peso total e modalidade PAC ou Sedex
    Dado que sou o cliente "cliente-1" autenticado
    E que existe tarifa de frete para a faixa de CEP "01000-000" a "01999-999" e peso até "500" gramas com valor "18.00" em "PAC" e prazo "5" dias
    E que existe tarifa de frete para a faixa de CEP "01000-000" a "01999-999" e peso até "500" gramas com valor "32.00" em "SEDEX" e prazo "2" dias
    E que meu carrinho possui "2" unidades de "Box OP-01" com peso unitário de "200" gramas
    Quando eu simulo o frete para o CEP "01310-100" na modalidade "PAC"
    Então o frete calculado é "18.00" com prazo de "5" dias
    Quando eu simulo o frete para o CEP "01310-100" na modalidade "SEDEX"
    Então o frete calculado é "32.00" com prazo de "2" dias

  Cenário: Não é possível calcular frete para CEP ou peso sem tarifa aplicável
    Dado que sou o cliente "cliente-1" autenticado
    E que meu carrinho possui "1" unidade de "Box Pesado" com peso unitário de "50000" gramas
    Quando eu tento simular o frete para o CEP "99999-999" na modalidade "PAC"
    Então o cálculo de frete é rejeitado porque não existe tarifa disponível para o CEP e peso informados

  # RN4: Cálculo do valor do pedido e desconto de 5% no PIX

  Cenário: Calcular total do pedido no cartão e aplicar 5 por cento de desconto apenas sobre o subtotal no PIX
    Dado que sou o cliente "cliente-1" autenticado com endereço cadastrado de CEP "01310-100"
    E que a tarifa "PAC" para o pedido é "20.00"
    E que meu carrinho possui "2" unidades de "Carta A" ao preço de "100.00" cada
    Quando eu seleciono o método de pagamento "CARTAO" no checkout
    Então o resumo exibe subtotal "200.00", frete "20.00", desconto "0.00" e total "220.00"
    Quando eu troco o método de pagamento para "PIX" no checkout
    Então o resumo recalcula para subtotal "200.00", frete "20.00", desconto "10.00" e total "210.00"
    Quando eu troco o método de pagamento novamente para "CARTAO" no checkout
    Então o resumo recalcula para subtotal "200.00", frete "20.00", desconto "0.00" e total "220.00"

  # RN5: Preservação do endereço

  Cenário: Preservar os dados do endereço de entrega do pedido mesmo após alteração cadastral do cliente
    Dado que sou o cliente "cliente-1" autenticado
    E que cadastrei o endereço "end-1" com logradouro "Av. Paulista", número "1000", cidade "São Paulo", UF "SP" e CEP "01310-100"
    E que criei o pedido "ped-1" selecionando o endereço "end-1"
    Quando eu altero o meu endereço cadastrado "end-1" para logradouro "Rua Augusta", número "500" e CEP "01305-000"
    Então o endereço de entrega preservado no pedido "ped-1" permanece com logradouro "Av. Paulista", número "1000" e CEP "01310-100"

  # RN6: Pagamento aprovado

  Cenário: Aprovar pagamento dentro do prazo efetivando a venda sem duplicar a baixa de estoque
    Dado que sou o cliente "cliente-1" autenticado
    E que o produto "Monkey.D.Luffy NM" possui estoque total "5" e reservado "0"
    E que criei o pedido pendente "ped-1" com "2" unidades de "Monkey.D.Luffy NM", total "120.00" e tentativa vigente "tent-1" no método "PIX"
    Quando o gateway envia confirmação válida para a tentativa "tent-1" no valor de "120.00" e método "PIX" dentro dos 30 minutos
    Então o pedido "ped-1" passa para o status "PAGO" com data de pagamento registrada
    E a tentativa "tent-1" passa para o status "APROVADA"
    E a reserva de estoque do pedido "ped-1" passa para "EFETIVADA"
    E o produto "Monkey.D.Luffy NM" passa a ter estoque total "3", reservado "0" e disponível "3"
    Quando o gateway reenvia a mesma confirmação para a tentativa "tent-1"
    Então a movimentação de estoque não é duplicada e o produto "Monkey.D.Luffy NM" permanece com estoque total "3" e reservado "0"

  # RN7 e RN8: Falha, nova tentativa sem reiniciar prazo e expiração após 30 minutos

  Cenário: Permitir nova tentativa de pagamento inclusive trocando de método sem reiniciar o prazo de 30 minutos
    Dado que sou o cliente "cliente-1" autenticado
    E que criei o pedido pendente "ped-1" às "10:00" com expiração às "10:30", subtotal "100.00", frete "20.00" e tentativa vigente "tent-1" no método "CARTAO" no valor de "120.00"
    Quando a tentativa "tent-1" é recusada às "10:10"
    Então a tentativa "tent-1" fica registrada como "RECUSADA"
    E o pedido "ped-1" permanece "PENDENTE" com expiração mantida às "10:30" e estoque ainda reservado
    Quando eu inicio uma nova tentativa "tent-2" às "10:15" escolhendo o método "PIX"
    Então o valor autorizado da tentativa "tent-2" é recalculado para "115.00"
    E a expiração do pedido "ped-1" continua às "10:30" sem reinício de prazo

  Cenário: Cancelar pedido expirado após 30 minutos liberando as reservas de estoque
    Dado que o produto "Monkey.D.Luffy NM" possui estoque total "5" e reservado "0"
    E que o cliente "cliente-1" criou o pedido pendente "ped-1" às "10:00" com expiração às "10:30" e "2" unidades reservadas de "Monkey.D.Luffy NM"
    E que a tentativa "tent-1" falhou às "10:10" e nenhuma nova tentativa foi aprovada até "10:30"
    Quando o processamento de expiração é executado às "10:31"
    Então o pedido "ped-1" passa para o status "CANCELADO"
    E a reserva de estoque do pedido "ped-1" passa para "LIBERADA"
    E o produto "Monkey.D.Luffy NM" volta a ter estoque total "5", reservado "0" e disponível "5"

  Cenário: Não é possível iniciar nova tentativa de pagamento após a expiração do pedido
    Dado que sou o cliente "cliente-1" autenticado
    E que o pedido "ped-1" criado às "10:00" expirou às "10:30"
    Quando eu tento iniciar uma nova tentativa de pagamento para "ped-1" às "10:31"
    Então a nova tentativa é rejeitada porque o pedido "ped-1" está expirado ou cancelado

  # RN10: Invalidação da tentativa anterior e conciliação para confirmação tardia ou divergente

  Cenário: Invalidar tentativa anterior ao criar nova tentativa e encaminhar pagamento tardio para conciliação
    Dado que sou o cliente "cliente-1" autenticado
    E que o pedido pendente "ped-1" possui a tentativa vigente "tent-1" no método "PIX" e valor "115.00"
    Quando eu crio uma nova tentativa "tent-2" no método "CARTAO" no valor de "120.00" dentro do prazo
    Então a tentativa "tent-1" é marcada como "INVALIDADA" e apenas "tent-2" fica vigente
    Quando chega uma confirmação tardia referente à tentativa invalidada "tent-1"
    Então o pedido "ped-1" não é aprovado automaticamente pela tentativa "tent-1"
    E o recebimento é registrado para conciliação ou estorno

  Cenário: Encaminhar para conciliação pagamento com valor divergente do autorizado
    Dado que o pedido "ped-1" possui tentativa vigente "tent-1" autorizada no valor de "120.00" e método "CARTAO"
    Quando chega uma confirmação para "tent-1" com valor divergente de "100.00"
    Então o pedido "ped-1" não é aprovado automaticamente e o pagamento é encaminhado para conciliação

  Cenário: Encaminhar para conciliação pagamento tardio referente a pedido já cancelado
    Dado que o pedido "ped-2" foi cancelado por expiração
    Quando chega uma confirmação tardia para o pedido "ped-2"
    Então o pedido "ped-2" permanece "CANCELADO" sem reativação automática e o pagamento é encaminhado para conciliação ou estorno

  # RN9 e RF Pedidos/Rastreamento: Privacidade do histórico e acompanhamento

  Cenário: Consultar somente os próprios pedidos e acompanhar rastreamento da entrega
    Dado que sou o cliente "cliente-1" autenticado
    E que possuo o pedido pago "ped-1" com envio postado e código de rastreio "BR123456789"
    E que o cliente "cliente-2" possui o pedido "ped-99"
    Quando eu listo o meu histórico de pedidos
    Então são retornados apenas os pedidos vinculados a "cliente-1", incluindo "ped-1" e omitindo "ped-99"
    Quando eu consulto os detalhes e o rastreamento do pedido "ped-1"
    Então são exibidos os itens, os valores e o código de rastreio "BR123456789"
    Quando eu tento consultar os detalhes ou o rastreamento do pedido "ped-99"
    Então a consulta é rejeitada por falta de autorização
