# language: pt
Funcionalidade: Ambiente de pedidos do administrador
  Para garantir que cada venda da loja TCGHub seja despachada, entregue e liquidada corretamente
  Como administrador do TCGHub
  Quero consultar os detalhes dos pedidos, despachar com transportadora e rastreio, acompanhar o trânsito,
  confirmar a entrega liberando o valor em custódia e tratar reembolsos e estornos

  Contexto:
    Dado que existe o cliente "Nami" com e-mail "nami@tcghub.com"
    E que existe o produto de pedido "Zoro Líder NM" com preço "25.00" e estoque "10"

  # RN1: Detalhes do pedido

  Cenário: Consultar os detalhes completos de um pedido
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" do cliente "Nami" com "2" unidades de "Zoro Líder NM", frete "18.00", total "68.00" e status "ENVIADO"
    E que o pedido "PED-100" possui pagamento "PIX" com status "APROVADO" e custódia "RETIDA"
    E que o pedido "PED-100" foi despachado pela transportadora "Correios" com código de rastreio "BR123456789BR" e possui "2" eventos de rastreio
    Quando eu consulto os detalhes do pedido "PED-100"
    Então são exibidos o cliente "Nami", os itens, os valores, o frete "18.00" e o endereço de entrega do pedido "PED-100"
    E são exibidos a situação do pagamento "APROVADO" e a situação da custódia "RETIDA"
    E são exibidos a transportadora "Correios", o código de rastreio "BR123456789BR" e o histórico com "2" eventos de rastreio

  Cenário: Não é possível consultar um pedido inexistente
    Dado que sou um usuário administrador
    Quando eu tento consultar os detalhes do pedido "PED-999"
    Então a consulta do pedido é rejeitada porque não existe pedido com o número "PED-999"

  Cenário: Não é possível acessar o ambiente de pedidos sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    Quando eu tento consultar os detalhes do pedido "PED-100"
    Então o acesso ao ambiente de pedidos é rejeitado por falta de autorização

  # RN2: Listagem por status

  Cenário: Listar pedidos por aba de status com a quantidade em cada aba
    Dado que sou um usuário administrador
    E que existem "3" pedidos com status "PAGO", "2" com status "ENVIADO", "4" com status "ENTREGUE" e "1" com status "REEMBOLSADO"
    Quando eu abro a aba de pedidos com status "PAGO"
    Então a listagem retorna apenas os "3" pedidos com status "PAGO"
    E as abas exibem as quantidades "3" pagos, "2" enviados, "4" entregues e "1" reembolsado

  Cenário: Filtrar pedidos por período
    Dado que sou um usuário administrador
    E que existe o pedido "PED-101" criado em "2026-11-01" com status "PAGO"
    E que existe o pedido "PED-102" criado em "2026-11-15" com status "PAGO"
    Quando eu filtro os pedidos com status "PAGO" no período de "2026-11-10" a "2026-11-20"
    Então a listagem retorna apenas o pedido "PED-102"

  Esquema do Cenário: Buscar pedidos por número, nome ou e-mail do cliente
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" do cliente "Nami" com e-mail "nami@tcghub.com"
    E que existe o pedido "PED-200" do cliente "Usopp" com e-mail "usopp@tcghub.com"
    Quando eu busco pedidos pelo termo "<termo>"
    Então a listagem retorna apenas o pedido "PED-100"

    Exemplos:
      | termo           |
      | PED-100         |
      | Nami            |
      | nami@tcghub.com |

  # RN3: Transição de status e despacho

  Cenário: Despachar pedido pago informando os dados de envio
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "PAGO"
    Quando eu despacho o pedido "PED-100" pela transportadora "Correios", código de rastreio "BR123456789BR", data de envio "2026-11-21" e previsão de entrega "2026-11-28"
    Então o pedido "PED-100" passa para o status "ENVIADO"
    E o envio do pedido "PED-100" registra a transportadora "Correios" e o código de rastreio "BR123456789BR"

  Esquema do Cenário: Não é possível despachar pedido que não está pago
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "<status>"
    Quando eu tento despachar o pedido "PED-100" pela transportadora "Correios", código de rastreio "BR123456789BR", data de envio "2026-11-21" e previsão de entrega "2026-11-28"
    Então o despacho é rejeitado porque apenas pedidos com status "PAGO" podem ser despachados
    E o pedido "PED-100" permanece com o status "<status>"

    Exemplos:
      | status    |
      | CRIADO    |
      | ENVIADO   |
      | ENTREGUE  |
      | CANCELADO |

  Esquema do Cenário: Não é possível despachar sem informar todos os dados de envio
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "PAGO"
    Quando eu tento despachar o pedido "PED-100" pela transportadora "<transportadora>", código de rastreio "<rastreio>", data de envio "<envio>" e previsão de entrega "<previsao>"
    Então o despacho é rejeitado porque transportadora, código de rastreio, data de envio e previsão de entrega são obrigatórios
    E o pedido "PED-100" permanece com o status "PAGO"

    Exemplos:
      | transportadora | rastreio      | envio      | previsao   |
      |                | BR123456789BR | 2026-11-21 | 2026-11-28 |
      | Correios       |               | 2026-11-21 | 2026-11-28 |
      | Correios       | BR123456789BR |            | 2026-11-28 |
      | Correios       | BR123456789BR | 2026-11-21 |            |

  Cenário: Não é possível pular etapas no fluxo do pedido
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "CRIADO"
    Quando eu tento marcar o pedido "PED-100" como entregue com recebedor "Nami", data e hora "2026-11-28 14:30" e assinatura digital "assinatura-nami.png"
    Então a transição é rejeitada porque o pedido deve seguir CRIADO, PAGO, ENVIADO e ENTREGUE sem pular etapas
    E o pedido "PED-100" permanece com o status "CRIADO"

  Cenário: Acompanhar o trânsito registrando eventos de rastreio
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "ENVIADO"
    Quando eu registro no pedido "PED-100" o evento de rastreio "Objeto em trânsito" em "2026-11-23 09:00" na cidade "Curitiba - PR"
    Então o histórico de rastreio do pedido "PED-100" passa a conter o evento "Objeto em trânsito"

  # RN4: Entrega apenas pelo admin

  Cenário: Confirmar a entrega de um pedido enviado com comprovante
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "ENVIADO" e custódia "RETIDA" no valor de "68.00"
    Quando eu marco o pedido "PED-100" como entregue com recebedor "Nami", data e hora "2026-11-28 14:30" e assinatura digital "assinatura-nami.png"
    Então o pedido "PED-100" passa para o status "ENTREGUE"
    E o comprovante de entrega do pedido "PED-100" registra o recebedor "Nami", a data e hora "2026-11-28 14:30" e a assinatura digital

  Esquema do Cenário: Não é possível confirmar entrega sem comprovante completo
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "ENVIADO"
    Quando eu tento marcar o pedido "PED-100" como entregue com recebedor "<recebedor>", data e hora "<dataHora>" e assinatura digital "<assinatura>"
    Então a confirmação de entrega é rejeitada porque o comprovante exige recebedor, data e hora e assinatura digital
    E o pedido "PED-100" permanece com o status "ENVIADO"

    Exemplos:
      | recebedor | dataHora         | assinatura          |
      |           | 2026-11-28 14:30 | assinatura-nami.png |
      | Nami      |                  | assinatura-nami.png |
      | Nami      | 2026-11-28 14:30 |                     |

  Cenário: Não é possível confirmar entrega de pedido que não está enviado
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "PAGO"
    Quando eu tento marcar o pedido "PED-100" como entregue com recebedor "Nami", data e hora "2026-11-28 14:30" e assinatura digital "assinatura-nami.png"
    Então a confirmação de entrega é rejeitada porque apenas pedidos com status "ENVIADO" podem ser entregues

  Cenário: Cliente não pode marcar o próprio pedido como entregue
    Dado que sou o cliente "Nami" autenticado sem autorização de administrador
    E que existe o pedido "PED-100" com status "ENVIADO"
    Quando eu tento marcar o pedido "PED-100" como entregue com recebedor "Nami", data e hora "2026-11-28 14:30" e assinatura digital "assinatura-nami.png"
    Então o acesso ao ambiente de pedidos é rejeitado por falta de autorização
    E o pedido "PED-100" permanece com o status "ENVIADO"

  # RN5: Reembolso e estorno

  Cenário: Reembolsar pedido repõe estoque, devolve custódia e dispara PedidoReembolsado
    Dado que sou um usuário administrador
    E que o produto de pedido "Zoro Líder NM" possui estoque "8"
    E que existe o pedido "PED-100" com "2" unidades de "Zoro Líder NM", status "PAGO" e custódia "RETIDA" no valor de "68.00"
    Quando eu reembolso o pedido "PED-100" informando o motivo "Arrependimento do cliente"
    Então o pedido "PED-100" passa para o status "REEMBOLSADO"
    E o pagamento do pedido "PED-100" passa para o status "REEMBOLSADO"
    E a custódia do pedido "PED-100" passa para "DEVOLVIDA" com "68.00" devolvidos ao comprador
    E o produto de pedido "Zoro Líder NM" volta a ter estoque "10"
    E é disparado o evento de pedido "PedidoReembolsado" para o pedido "PED-100"

  Cenário: Transação revogada pelo meio de pagamento estorna o pedido e dispara TransacaoRevogada
    Dado que o produto de pedido "Zoro Líder NM" possui estoque "8"
    E que existe o pedido "PED-100" com "2" unidades de "Zoro Líder NM", status "ENVIADO" e custódia "RETIDA" no valor de "68.00"
    Quando o meio de pagamento informa que a transação do pedido "PED-100" foi revogada
    Então o pedido "PED-100" passa para o status "ESTORNADO"
    E o pagamento do pedido "PED-100" passa para o status "REVOGADO"
    E a custódia do pedido "PED-100" passa para "DEVOLVIDA" com "68.00" devolvidos ao comprador
    E o produto de pedido "Zoro Líder NM" volta a ter estoque "10"
    E é disparado o evento de pedido "TransacaoRevogada" para o pedido "PED-100"

  Cenário: Não é possível reembolsar duas vezes o mesmo pedido
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "REEMBOLSADO"
    Quando eu tento reembolsar o pedido "PED-100" informando o motivo "Duplicado"
    Então o reembolso é rejeitado porque o pedido "PED-100" já foi reembolsado
    E o estoque e a custódia do pedido "PED-100" não são alterados novamente

  # RN6: Pedido entregue não volta

  Cenário: Não é possível cancelar pedido entregue
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "ENTREGUE"
    Quando eu tento cancelar o pedido "PED-100"
    Então o cancelamento é rejeitado porque pedido entregue só pode ser reembolsado
    E o pedido "PED-100" permanece com o status "ENTREGUE"

  Cenário: Reembolsar pedido entregue
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com status "ENTREGUE" e custódia "LIBERADA" no valor de "68.00"
    Quando eu reembolso o pedido "PED-100" informando o motivo "Produto com defeito"
    Então o pedido "PED-100" passa para o status "REEMBOLSADO"
    E o valor de "68.00" é devolvido ao comprador do pedido "PED-100"

  # RN7: Custódia do pagamento

  Cenário: Valor fica retido em custódia a partir do pagamento
    Dado que existe o pedido "PED-100" com status "CRIADO" e total "68.00"
    Quando o pagamento do pedido "PED-100" é aprovado
    Então o pedido "PED-100" passa para o status "PAGO"
    E a custódia do pedido "PED-100" passa para "RETIDA" com o valor de "68.00"

  Cenário: Entrega libera o valor da custódia para a carteira do vendedor
    Dado que sou um usuário administrador
    E que a carteira do vendedor possui saldo "100.00"
    E que existe o pedido "PED-100" com status "ENVIADO" e custódia "RETIDA" no valor de "68.00"
    Quando eu marco o pedido "PED-100" como entregue com recebedor "Nami", data e hora "2026-11-28 14:30" e assinatura digital "assinatura-nami.png"
    Então a custódia do pedido "PED-100" passa para "LIBERADA"
    E a carteira do vendedor passa a ter saldo "168.00"

  Cenário: Cancelamento de pedido pago devolve a custódia ao comprador na mesma transação
    Dado que sou um usuário administrador
    E que existe o pedido "PED-100" com "2" unidades de "Zoro Líder NM", status "PAGO" e custódia "RETIDA" no valor de "68.00"
    Quando eu cancelo o pedido "PED-100"
    Então o pedido "PED-100" passa para o status "CANCELADO"
    E a custódia do pedido "PED-100" passa para "DEVOLVIDA" com "68.00" devolvidos ao comprador
    E a alteração do pedido e da custódia é gravada na mesma transação

  # RN8: Prazo de despacho

  Cenário: Pedido pago há mais de 24 horas sem despacho aparece como prioridade alta
    Dado que sou um usuário administrador
    E que o pedido "PED-101" foi pago em "2026-11-20 08:00" e ainda não foi despachado
    E que o pedido "PED-102" foi pago em "2026-11-21 07:00" e ainda não foi despachado
    E que o horário atual é "2026-11-21 10:00"
    Quando eu abro a aba de pedidos com status "PAGO"
    Então o pedido "PED-101" é exibido com prioridade "ALTA" por prazo de despacho vencido
    E o pedido "PED-102" é exibido sem prioridade alta
