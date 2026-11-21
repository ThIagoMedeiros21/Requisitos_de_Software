# language: pt
Funcionalidade: Aviso de reposição de estoque
  Para não perder a chance de comprar produtos esgotados assim que voltarem ao estoque
  Como cliente da loja TCGHub
  Quero entrar na fila de aviso de um produto esgotado e ser notificado na plataforma, por e-mail e por push
  com o link direto do produto e uma reserva temporária de 30 minutos quando o administrador repuser o estoque

  Contexto:
    Dado que existe o produto disponível para aviso "Booster Box OP-01" com estoque "0", visibilidade "ESGOTADO" e Avise-me "habilitado"

  # RN1: Só produto esgotado

  Cenário: Exibir o botão Avise-me para produto esgotado com Avise-me habilitado
    Dado que sou o cliente "Nami" autenticado na loja
    Quando eu visualizo o produto "Booster Box OP-01"
    Então o botão Avise-me é exibido para o produto "Booster Box OP-01"

  Esquema do Cenário: Não exibir o botão Avise-me fora das condições permitidas
    Dado que sou o cliente "Nami" autenticado na loja
    E que existe o produto disponível para aviso "Zoro Líder NM" com estoque "<estoque>", visibilidade "<visibilidade>" e Avise-me "<aviseMe>"
    Quando eu visualizo o produto "Zoro Líder NM"
    Então o botão Avise-me não é exibido para o produto "Zoro Líder NM"

    Exemplos:
      | estoque | visibilidade | aviseMe      |
      | 5       | ATIVO        | habilitado   |
      | 0       | ESGOTADO     | desabilitado |
      | 0       | OCULTO       | habilitado   |

  Cenário: Não é possível entrar na fila de aviso de produto com estoque disponível
    Dado que sou o cliente "Nami" autenticado na loja
    E que existe o produto disponível para aviso "Zoro Líder NM" com estoque "5", visibilidade "ATIVO" e Avise-me "habilitado"
    Quando eu tento me inscrever no Avise-me do produto "Zoro Líder NM"
    Então a inscrição no Avise-me é rejeitada porque o produto não está esgotado

  # RN2: Um aviso por produto

  Cenário: Entrar na fila de aviso de um produto esgotado
    Dado que sou o cliente "Nami" autenticado na loja
    Quando eu me inscrevo no Avise-me do produto "Booster Box OP-01"
    Então o cliente "Nami" passa a constar na fila de aviso do produto "Booster Box OP-01" com a data e hora da inscrição

  Cenário: Não é possível criar dois avisos para o mesmo produto
    Dado que sou o cliente "Nami" autenticado na loja
    E que o cliente "Nami" já está inscrito no Avise-me do produto "Booster Box OP-01"
    Quando eu tento me inscrever no Avise-me do produto "Booster Box OP-01"
    Então a inscrição no Avise-me é rejeitada porque o cliente já possui aviso para este produto
    E a fila de aviso do produto "Booster Box OP-01" continua com "1" inscrição do cliente "Nami"

  Cenário: Cancelar a própria inscrição no Avise-me
    Dado que sou o cliente "Nami" autenticado na loja
    E que o cliente "Nami" já está inscrito no Avise-me do produto "Booster Box OP-01"
    Quando eu cancelo minha inscrição no Avise-me do produto "Booster Box OP-01"
    Então o cliente "Nami" deixa de constar na fila de aviso do produto "Booster Box OP-01"

  # RN3: Notificação na reposição

  Cenário: Reposição atende a fila por ordem de inscrição até a quantidade reposta
    Dado que a fila de aviso do produto "Booster Box OP-01" possui, em ordem de inscrição, os clientes "Nami", "Usopp", "Sanji" e "Robin"
    Quando o administrador repõe "2" unidades do produto "Booster Box OP-01" disparando o evento EstoqueReposto
    Então os clientes "Nami" e "Usopp" são atendidos com "1" unidade cada
    E cada cliente atendido recebe uma notificação na plataforma, por e-mail e por push com o link direto do produto "Booster Box OP-01"
    E cada cliente atendido tem "1" unidade do produto "Booster Box OP-01" reservada por "30" minutos
    E os avisos de "Nami" e "Usopp" são removidos da fila
    E os clientes "Sanji" e "Robin" continuam na fila de aviso do produto "Booster Box OP-01" na mesma ordem

  Cenário: Cada reposição grava um registro de disparo
    Dado que a fila de aviso do produto "Booster Box OP-01" possui, em ordem de inscrição, os clientes "Nami", "Usopp", "Sanji" e "Robin"
    Quando o administrador repõe "2" unidades do produto "Booster Box OP-01" disparando o evento EstoqueReposto
    Então é gravado um lote de disparo para o produto "Booster Box OP-01" com quantidade reposta "2", "2" clientes atendidos e "2" clientes restantes na fila

  Cenário: Reposição maior que a fila atende todos os inscritos
    Dado que a fila de aviso do produto "Booster Box OP-01" possui, em ordem de inscrição, os clientes "Nami" e "Usopp"
    Quando o administrador repõe "5" unidades do produto "Booster Box OP-01" disparando o evento EstoqueReposto
    Então os clientes "Nami" e "Usopp" são atendidos com "1" unidade cada
    E a fila de aviso do produto "Booster Box OP-01" fica vazia
    E "3" unidades do produto "Booster Box OP-01" ficam disponíveis para compra livre

  Cenário: Reserva temporária expira após 30 minutos sem compra
    Dado que o cliente "Nami" foi atendido na reposição do produto "Booster Box OP-01" às "10:00" com "1" unidade reservada
    Quando o cliente "Nami" não conclui a compra até "10:30"
    Então a reserva do cliente "Nami" expira às "10:30"
    E a unidade reservada volta ao estoque disponível do produto "Booster Box OP-01"

  # RN4: Notificações no menu

  Cenário: Exibir a quantidade de notificações não lidas no menu
    Dado que sou o cliente "Nami" autenticado na loja
    E que o cliente "Nami" possui "3" notificações não lidas e "2" notificações lidas
    Quando eu abro o menu da plataforma
    Então o menu exibe "3" notificações não lidas

  Cenário: Marcar notificação como lida
    Dado que sou o cliente "Nami" autenticado na loja
    E que o cliente "Nami" possui "3" notificações não lidas e "2" notificações lidas
    Quando eu marco a notificação de reposição do produto "Booster Box OP-01" como lida
    Então o menu exibe "2" notificações não lidas

  Cenário: Dispensar notificação
    Dado que sou o cliente "Nami" autenticado na loja
    E que o cliente "Nami" possui "3" notificações não lidas e "2" notificações lidas
    Quando eu dispenso a notificação de reposição do produto "Booster Box OP-01"
    Então a notificação de reposição do produto "Booster Box OP-01" deixa de ser exibida na lista de notificações
    E o menu exibe "2" notificações não lidas

  # RN5: Produto oculto cancela avisos

  Cenário: Ocultar produto apaga os avisos pendentes sem enviar e-mail
    Dado que a fila de aviso do produto "Booster Box OP-01" possui, em ordem de inscrição, os clientes "Nami", "Usopp" e "Sanji"
    Quando o administrador oculta o produto "Booster Box OP-01"
    Então a fila de aviso do produto "Booster Box OP-01" fica vazia
    E nenhum e-mail de aviso de reposição é enviado aos clientes "Nami", "Usopp" e "Sanji"

  Cenário: Não é possível entrar na fila de aviso de produto oculto
    Dado que sou o cliente "Nami" autenticado na loja
    E que existe o produto disponível para aviso "Zoro Líder NM" com estoque "0", visibilidade "OCULTO" e Avise-me "habilitado"
    Quando eu tento me inscrever no Avise-me do produto "Zoro Líder NM"
    Então a inscrição no Avise-me é rejeitada porque o produto não está esgotado

  # RN6: Conversão pós-aviso

  Cenário: Notificação conta como convertida quando o cliente paga o pedido em até 24 horas
    Dado que o cliente "Nami" recebeu a notificação de reposição do produto "Booster Box OP-01" em "2026-11-20 10:00"
    Quando o cliente "Nami" tem um pedido pago do produto "Booster Box OP-01" em "2026-11-21 09:00"
    Então a notificação de reposição do cliente "Nami" é marcada como convertida

  Cenário: Notificação não conta como convertida quando o pagamento ocorre após 24 horas
    Dado que o cliente "Nami" recebeu a notificação de reposição do produto "Booster Box OP-01" em "2026-11-20 10:00"
    Quando o cliente "Nami" tem um pedido pago do produto "Booster Box OP-01" em "2026-11-21 10:01"
    Então a notificação de reposição do cliente "Nami" não é marcada como convertida

  Cenário: Calcular a taxa de conversão do Avise-me
    Dado que sou um usuário administrador
    E que foram enviadas "20" notificações de reposição, das quais "5" foram convertidas
    Quando eu consulto a taxa de conversão do Avise-me
    Então a taxa de conversão exibida é "25.00" por cento
