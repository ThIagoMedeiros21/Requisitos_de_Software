# language: pt
Funcionalidade: Recomendar cartas
  Para descobrir cartas alinhadas ao meu perfil e interesses na loja TCGHub
  Como cliente autenticado do sistema
  Quero gerenciar minhas preferências personalizadas e visualizar recomendações
  calculadas por histórico de compras e preferências, respeitando disponibilidade e fallback

  Contexto:
    Dado que existem coleções, cores, tipos de carta e raridades catalogados no sistema

  # RN2: CRUD de preferências personalizadas

  Cenário: Cadastrar, consultar, alterar e remover preferências personalizadas de recomendação
    Dado que sou o cliente "cliente-1" autenticado sem preferências cadastradas
    Quando eu cadastro preferências com coleções "OP-01" e "OP-02", cores "Vermelho" e "Verde", tipos "Leader" e "Character", raridades "SR" e "SEC" e faixa de preço de "10.00" a "80.00"
    Então as preferências de "cliente-1" são salvas e retornadas na consulta
    Quando eu altero minhas preferências removendo a coleção "OP-02", alterando a faixa de preço para "20.00" a "100.00"
    Então as preferências de "cliente-1" passam a refletir apenas a coleção "OP-01" e faixa de "20.00" a "100.00"
    Quando eu removo todas as minhas preferências personalizadas
    Então o cliente "cliente-1" deixa de possuir preferências cadastradas

  Cenário: Não é possível cadastrar faixa de preço inválida nas preferências
    Dado que sou o cliente "cliente-1" autenticado
    Quando eu tento cadastrar uma faixa de preço com mínimo "-5.00" e máximo "50.00"
    Então o cadastro da preferência é rejeitado por faixa de preço inválida
    Quando eu tento cadastrar uma faixa de preço com mínimo "100.00" e máximo "50.00"
    Então o cadastro da preferência é rejeitado por faixa de preço inválida

  # RN6: Recomendações alternativas (fallback)

  Cenário: Exibir cartas populares disponíveis quando não há histórico nem preferências
    Dado que sou o cliente "cliente-1" autenticado sem histórico de compras pagas e sem preferências cadastradas
    E que a lista manual de cartas populares tem na posição 1 a carta "Carta Pop 1" com variante ativa e estoque "2"
    E na posição 2 a carta "Carta Pop 2" sem estoque disponível
    E na posição 3 a carta "Carta Pop 3" com variante ativa e estoque "5"
    Quando eu solicito minhas recomendações de cartas
    Então são recomendadas em ordem apenas "Carta Pop 1" e "Carta Pop 3"
    E a carta indisponível "Carta Pop 2" não é exibida nas recomendações

  # RN1, RN3 e RN5: Perfil de consumo, pontuação combinada (40% / 60%), pesos 30/25/15/15/15, unicidade e disponibilidade

  Cenário: Calcular recomendações combinando 40 por cento histórico e 60 por cento preferências com pesos por atributo
    Dado que sou o cliente "cliente-1" autenticado
    E que meu histórico de pedidos pagos tem preferência predominante por coleção "OP-01", cor "Vermelho", tipo "Character", raridade "SR" e faixa de "20.00" a "60.00"
    E que minhas preferências explícitas informam apenas coleção "OP-02" e cor "Azul"
    E que a carta candidata "Carta A" possui coleção "OP-02", cor "Azul", tipo "Character", raridade "SR", variante ativa por "40.00" com estoque "3"
    E que a carta candidata "Carta B" possui coleção "OP-03", cor "Verde", tipo "Event", raridade "C", variante ativa por "5.00" com estoque "3"
    Quando eu solicito minhas recomendações de cartas
    Então a pontuação considera 40 por cento do histórico e 60 por cento das preferências usando os pesos 30 de coleção, 25 de cor, 15 de tipo, 15 de raridade e 15 de preço
    E os atributos não informados nas preferências não reduzem a nota de preferências da "Carta A"
    E "Carta A" é ranqueada acima de "Carta B"

  Cenário: Utilizar 100 por cento da fonte existente quando apenas histórico ou apenas preferências existirem
    Dado que sou o cliente "cliente-1" autenticado sem histórico de compras pagas
    E que cadastrei preferências para coleção "OP-01", cor "Vermelho", tipo "Leader", raridade "L" e faixa de preço de "30.00" a "90.00"
    E que a carta "Carta Ideal" atende a todos os cinco critérios e possui variante ativa com estoque "1"
    Quando eu solicito minhas recomendações de cartas
    Então as preferências explícitas recebem 100 por cento do peso e "Carta Ideal" atinge pontuação máxima de compatibilidade

  Cenário: Exibir cada carta apenas uma vez mesmo com múltiplas variantes de produto e filtrar indisponíveis
    Dado que sou o cliente "cliente-1" autenticado com preferências compatíveis com "Carta Multi" e "Carta Esgotada"
    E que "Carta Multi" possui variante "Near Mint" ativa com estoque "2" por "50.00" e variante "Played" ativa com estoque "4" por "35.00"
    E que "Carta Esgotada" possui variante ativa com estoque total "2" e estoque reservado "2"
    E que "Carta Inativa" possui variante inativa com estoque disponível "10"
    Quando eu solicito minhas recomendações de cartas
    Então a carta "Carta Multi" aparece uma única vez na lista de recomendações
    E as cartas "Carta Esgotada" e "Carta Inativa" não aparecem nas recomendações

  Cenário: Revalidar disponibilidade ao tentar adicionar carta recomendada cujo estoque acabou
    Dado que sou o cliente "cliente-1" autenticado
    E que a carta recomendada "Carta Disputada" foi exibida com estoque disponível "1"
    E que outro cliente comprou a última unidade de "Carta Disputada" antes da minha ação
    Quando eu tento adicionar "Carta Disputada" ao carrinho a partir das recomendações
    Então a operação é rejeitada por insuficiência de estoque disponível na revalidação

  Cenário: Pontuar carta multicolorida e múltiplas preferências sem duplicar pontuação artificialmente
    Dado que sou o cliente "cliente-1" autenticado com preferência pelas cores "Vermelho" e "Verde"
    E que a carta "Luffy Multicolor" possui as cores "Vermelho" e "Verde" e variante ativa com estoque "2"
    Quando eu solicito minhas recomendações de cartas
    Então o critério de cor para "Luffy Multicolor" contribui no máximo com os 25 por cento definidos para o atributo cor

  # RN4: Penalização de cartas já compradas

  Cenário: Penalizar cartas já compradas em 10 pontos percentuais sem gerar pontuação negativa
    Dado que sou o cliente "cliente-1" autenticado
    E que já comprei anteriormente "3" unidades da carta "Carta Comprada Fraca" em pedido pago
    E que "Carta Comprada Fraca" teria compatibilidade bruta de "0.05"
    E que "Carta Comprada Forte" já comprada teria compatibilidade bruta de "0.80" enquanto "Carta Nova" equivalente teria "0.80"
    Quando eu solicito minhas recomendações de cartas
    Então "Carta Comprada Forte" recebe pontuação final de "0.70" e fica abaixo de "Carta Nova" com "0.80"
    E "Carta Comprada Fraca" permanece elegível com pontuação final mínima de "0.00" e nunca negativa

  # RN7: Atualização e reversão do perfil de consumo por eventos sem duplicação

  Cenário: Atualizar perfil de consumo apenas em pagamento aprovado e reverter em reembolso sem duplicar eventos
    Dado que sou o cliente "cliente-1" autenticado sem histórico de compras
    E que possuo o pedido pendente "ped-1" com "2" unidades da carta "Zoro" de coleção "OP-01" e cor "Verde"
    Quando eu consulto meu perfil de consumo antes da aprovação
    Então o perfil histórico não contabiliza as "2" unidades de "Zoro"
    Quando é processado o evento de pagamento aprovado "evt-pag-1" para o pedido "ped-1"
    Então o perfil histórico contabiliza "2" unidades da carta "Zoro"
    Quando o mesmo evento "evt-pag-1" é recebido novamente
    Então a contagem no perfil histórico permanece exatamente "2" unidades sem duplicação
    Quando é processado o evento de reembolso confirmado "evt-reemb-1" de "1" unidade de "Zoro" do pedido "ped-1"
    Então o perfil histórico passa a contabilizar "1" unidade da carta "Zoro"
    Quando o mesmo evento "evt-reemb-1" é recebido novamente
    Então o perfil histórico mantém "1" unidade sem duplicar a reversão
