# language: pt
Funcionalidade: Gerenciar wishlist e presentes
  Para organizar cartas desejadas, monitorar preços e reposições e receber presentes com segurança
  Como usuário do sistema e visitante autorizado
  Quero cadastrar e manter itens na wishlist, controlar compartilhamento por token,
  receber alertas sem repetição e permitir compras de presentes protegendo o endereço do destinatário

  Contexto:
    Dado que existem cartas catalogadas com suas variantes comerciais de produto e estoque

  # RN1 e RN2: Gerenciamento de itens e unicidade por carta + condição

  Cenário: Adicionar, consultar, editar e remover item da wishlist
    Dado que sou o usuário "dono-1" autenticado
    E que existe a carta "Nami OP-01" no catálogo
    Quando eu adiciono "Nami OP-01" à minha wishlist com condição "Near Mint", quantidade desejada "2", preço-alvo "40.00", alerta de preço ativo e alerta de reposição ativo
    Então o item "Nami OP-01" na condição "Near Mint" é cadastrado na wishlist com quantidade desejada "2"
    Quando eu consulto minha wishlist
    Então o item "Nami OP-01" é exibido com condição "Near Mint", quantidade desejada "2" e quantidade restante "2"
    Quando eu edito o item "Nami OP-01" na condição "Near Mint" para quantidade desejada "3" e preço-alvo "35.00"
    Então o item passa a ter quantidade desejada "3" e preço-alvo "35.00"
    Quando eu removo o item "Nami OP-01" na condição "Near Mint" da minha wishlist
    Então o item deixa de constar na minha wishlist ativa

  Cenário: Impedir cadastro duplicado da mesma carta com a mesma condição mas permitir condições distintas
    Dado que sou o usuário "dono-1" autenticado
    E que minha wishlist já contém a carta "Nami OP-01" na condição "Near Mint"
    Quando eu tento adicionar novamente "Nami OP-01" na condição "Near Mint"
    Então a inclusão é rejeitada por duplicidade de carta e condição na wishlist
    Quando eu adiciono "Nami OP-01" na condição "Played" com quantidade desejada "1"
    Então a wishlist passa a conter "Nami OP-01" em "Near Mint" e "Nami OP-01" em "Played"

  Cenário: Não é possível adicionar item à wishlist com quantidade desejada menor ou igual a zero
    Dado que sou o usuário "dono-1" autenticado
    Quando eu tento adicionar "Nami OP-01" na condição "Near Mint" com quantidade desejada "0"
    Então a inclusão é rejeitada porque a quantidade desejada deve ser positiva

  # RN3: Privacidade padrão, compartilhamento por token único e revogação

  Cenário: Controlar privacidade padrão, compartilhamento por token único e revogação de acesso
    Dado que sou o usuário "dono-1" autenticado com a wishlist contendo "Nami OP-01"
    E que o usuário "visitante-1" tenta acessar a wishlist de "dono-1" sem token
    Então o acesso de "visitante-1" é rejeitado porque a wishlist é privada por padrão
    Quando eu habilito o compartilhamento da minha wishlist
    Então é gerado um token público único "tok-1" associado à wishlist
    Quando "visitante-1" acessa a wishlist usando o token "tok-1"
    Então "visitante-1" visualiza os itens desejados da wishlist
    Quando "visitante-1" tenta adicionar ou remover um item na wishlist de "dono-1"
    Então a alteração é rejeitada por falta de autorização
    Quando eu desabilito o compartilhamento da minha wishlist
    Então o token "tok-1" é revogado
    Quando "visitante-1" tenta acessar novamente a wishlist com o token "tok-1"
    Então o acesso é rejeitado porque o compartilhamento foi revogado

  # RN4: Disponibilidade de itens incompatíveis ou sem estoque

  Cenário: Manter item indisponível visível na wishlist porém impedir sua compra como presente
    Dado que a wishlist compartilhada de "dono-1" possui a carta "Zoro" na condição desejada "Near Mint" e quantidade "1"
    E que existe apenas uma variante ativa de "Zoro" na condição "Played" com estoque "3" e nenhuma na condição "Near Mint" com estoque disponível
    Quando o visitante "visitante-1" visualiza a wishlist compartilhada pelo token válido
    Então o item "Zoro" em condição "Near Mint" é exibido como indisponível
    Quando "visitante-1" tenta iniciar a compra de presente para o item "Zoro"
    Então a compra é rejeitada porque não há variante ativa compatível com a condição desejada e com estoque disponível

  # RN5, RN6 e RN11: Alertas de preço-alvo e reposição apenas na transição falso -> verdadeiro

  Cenário: Gerar alerta de preço considerando o menor preço compatível apenas na transição sem repetição
    Dado que sou o usuário "dono-1" autenticado
    E que minha wishlist possui "Sanji" na condição "Near Mint", preço-alvo "50.00" e alerta de preço ativado
    E que as variantes compatíveis ativas e com estoque de "Sanji" em "Near Mint" custam "60.00" e "55.00"
    Quando o monitoramento verifica os preços disponíveis
    Então nenhuma notificação de preço é gerada porque o menor preço compatível é "55.00"
    Quando uma das variantes compatíveis tem seu preço reduzido para "48.00"
    Então é gerada uma única notificação de alerta de preço para "Sanji" informando o menor preço "48.00"
    Quando outra variante compatível também reduz o preço para "45.00" enquanto a condição já estava atendida
    Então nenhuma notificação repetida de preço é gerada
    Quando os preços voltam a subir para "60.00" e depois caem novamente para "49.00"
    Então uma nova notificação de preço é gerada na nova transição para condição atendida

  Cenário: Gerar alerta de reposição apenas na transição de indisponível para disponível sem repetição
    Dado que sou o usuário "dono-1" autenticado
    E que minha wishlist possui "Robin" na condição "Near Mint" com alerta de reposição ativado e atualmente sem estoque compatível
    Quando uma variante ativa de "Robin" na condição "Near Mint" recebe estoque "2"
    Então é gerada uma única notificação de reposição para "Robin"
    Quando outra variante compatível também recebe estoque "4" enquanto o item já estava disponível
    Então nenhuma notificação repetida de reposição é gerada

  # RN7 e RN8: Compra de presente e validação de endereço do destinatário

  Cenário: Não é possível iniciar compra de presente quando o proprietário não possui endereço válido
    Dado que a wishlist compartilhada de "dono-1" com token "tok-1" possui "Nami OP-01" em "Near Mint" disponível
    E que "dono-1" não possui nenhum endereço de entrega válido cadastrado
    E que sou o visitante "visitante-1" autenticado
    Quando eu tento iniciar a compra de presente de "1" unidade de "Nami OP-01" da wishlist de "dono-1"
    Então a compra de presente é rejeitada porque o destinatário não possui endereço de entrega válido

  Cenário: Comprar presente pela wishlist compartilhada mantendo endereço completo do destinatário oculto
    Dado que "dono-1" possui endereço válido cadastrado "Av. Brasil, 500, Apto 12, Rio de Janeiro - RJ, CEP 20040-020"
    E que a wishlist compartilhada de "dono-1" com token "tok-1" possui "Nami OP-01" em "Near Mint" com quantidade desejada "2"
    E que existe variante compatível ativa com estoque "5" e preço "40.00"
    E que sou o visitante "visitante-1" autenticado
    Quando eu crio um pedido de presente para "1" unidade de "Nami OP-01" usando o token "tok-1"
    Então o pedido de presente é criado em nome do comprador "visitante-1" com destinatário "dono-1"
    E o endereço completo de "dono-1" permanece oculto para "visitante-1" no checkout, nos detalhes do pedido e no rastreamento

  # RN9 e RN10: Limite de presentes, efetivação e liberação em cancelamento/expiração

  Cenário: Limitar reservas e presentes adquiridos à quantidade desejada ignorando compras pessoais
    Dado que "dono-1" possui endereço válido cadastrado e wishlist compartilhada com "Nami OP-01" em "Near Mint" com quantidade desejada "2"
    E que "dono-1" comprou "3" unidades de "Nami OP-01" em um pedido normal próprio fora da wishlist
    E que o visitante "visitante-1" criou um pedido de presente pendente reservando "1" unidade de "Nami OP-01"
    E que sou o visitante "visitante-2" autenticado
    Quando eu tento comprar "2" unidades de "Nami OP-01" como presente na wishlist de "dono-1"
    Então a compra é rejeitada porque resta apenas "1" unidade disponível para presentear
    Quando eu compro "1" unidade de "Nami OP-01" como presente na wishlist de "dono-1"
    Então a compra de "1" unidade é aceita e a quantidade restante da wishlist passa a ser "0"

  Cenário: Efetivar presente no pagamento e liberar reserva apenas no cancelamento ou expiração
    Dado que o visitante "visitante-1" criou o pedido de presente pendente "ped-pres-1" para "1" unidade de "Nami OP-01" da wishlist de "dono-1" com quantidade desejada "1"
    Quando uma tentativa de pagamento de "ped-pres-1" falha dentro do prazo de 30 minutos
    Então a reserva de presente de "ped-pres-1" permanece "RESERVADA" e o estoque do produto permanece reservado
    Quando o pedido "ped-pres-1" expira após 30 minutos ou é cancelado antes do pagamento
    Então a reserva de presente passa para "LIBERADA"
    E o item "Nami OP-01" volta a ter "1" unidade restante disponível para ser presenteada por outro visitante
    Quando o visitante "visitante-2" cria o pedido de presente "ped-pres-2" para "1" unidade e o pagamento é aprovado
    Então a reserva de presente de "ped-pres-2" passa para "ADQUIRIDA"
    E o item "Nami OP-01" passa a registrar "1" unidade adquirida e "0" restante
