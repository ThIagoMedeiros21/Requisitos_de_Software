# language: pt
Funcionalidade: Avaliar cartas compradas e consultar média da comunidade
  Para garantir avaliações confiáveis baseadas em compradores reais
  Como usuário do TCGHub
  Quero registrar, atualizar e consultar avaliações de cartas,
  respeitando a elegibilidade por compra paga e não reembolsada, a faixa de notas de 1 a 5, o selo de compra verificada e a invalidação por reembolso

  Contexto:
    Dado que existe o produto de carta "OP05-119" de nome "Monkey.D.Luffy Manga"
    E que não existe nenhuma avaliação cadastrada para a carta "OP05-119"

  Cenário: Registrar avaliação por comprador com pedido pago e não reembolsado
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" avalia a carta "OP05-119" com nota 5 e comentário "Carta chegou perfeita"
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" é registrada com sucesso
    E a avaliação exibe o selo "compra verificada"
    E o resumo da carta "OP05-119" passa a exibir média 5.0 e 1 avaliação válida

  Cenário: Registrar avaliação sem comentário opcional
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" avalia a carta "OP05-119" com nota 4 e sem comentário
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" é registrada com sucesso
    E a avaliação exibe o selo "compra verificada"

  Cenário: Registrar avaliação com nota mínima válida 1
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" avalia a carta "OP05-119" com nota 1 e comentário "Veio com leve desgaste"
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" é registrada com sucesso
    E o resumo da carta "OP05-119" passa a exibir média 1.0 e 1 avaliação válida

  Cenário: Não é possível avaliar uma carta sem tê-la comprado
    Dado que o usuário "ricardo" não possui nenhum pedido contendo a carta "OP05-119"
    Quando o usuário "ricardo" tenta avaliar a carta "OP05-119" com nota 5 e comentário "Parece ótima"
    Então a avaliação é rejeitada porque o usuário "ricardo" não possui compra paga e não reembolsada da carta "OP05-119"

  Cenário: Não é possível avaliar uma carta com pedido ainda não pago
    Dado que o usuário "ricardo" possui apenas o pedido "PED-101" com pagamento pendente contendo a carta "OP05-119"
    Quando o usuário "ricardo" tenta avaliar a carta "OP05-119" com nota 5 e comentário "Aguardando"
    Então a avaliação é rejeitada porque o usuário "ricardo" não possui compra paga e não reembolsada da carta "OP05-119"

  Cenário: Não é possível avaliar uma carta quando o único pedido foi reembolsado
    Dado que o usuário "ricardo" possui apenas o pedido "PED-102" reembolsado contendo a carta "OP05-119"
    Quando o usuário "ricardo" tenta avaliar a carta "OP05-119" com nota 4 e comentário "Reembolsei"
    Então a avaliação é rejeitada porque o usuário "ricardo" não possui compra paga e não reembolsada da carta "OP05-119"

  Cenário: Usuário com um pedido reembolsado e outro pago não reembolsado pode avaliar a carta
    Dado que o usuário "lucas" possui o pedido "PED-100" reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" também possui o pedido "PED-103" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" avalia a carta "OP05-119" com nota 5 e comentário "Segunda compra perfeita"
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" é registrada com sucesso
    E a avaliação exibe o selo "compra verificada"

  Cenário: Não é possível avaliar uma carta com nota menor que 1
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" tenta avaliar a carta "OP05-119" com nota 0 e comentário "Nota zero"
    Então a avaliação é rejeitada porque a nota deve estar entre 1 e 5

  Cenário: Não é possível avaliar uma carta com nota maior que 5
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    Quando o usuário "lucas" tenta avaliar a carta "OP05-119" com nota 6 e comentário "Nota seis"
    Então a avaliação é rejeitada porque a nota deve estar entre 1 e 5

  Cenário: Reavaliar uma carta atualiza a nota existente sem duplicar a contagem
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" já avaliou a carta "OP05-119" com nota 3 e comentário "Boa carta"
    Quando o usuário "lucas" reavalia a carta "OP05-119" com nota 5 e comentário "Subiu no deck"
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" é atualizada com sucesso para nota 5 e comentário "Subiu no deck"
    E o resumo da carta "OP05-119" passa a exibir média 5.0 e 1 avaliação válida

  Cenário: Cancelar edição da avaliação preserva a nota e o comentário anteriores
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" já avaliou a carta "OP05-119" com nota 4 e comentário "Muito boa"
    Quando o usuário "lucas" inicia a edição da avaliação para nota 1 e cancela a alteração
    Então a avaliação da carta "OP05-119" pelo usuário "lucas" permanece com nota 4 e comentário "Muito boa"

  Cenário: Consultar média e contagem de avaliações da carta no catálogo
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "ricardo" possui o pedido "PED-200" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" avaliou a carta "OP05-119" com nota 5 e comentário "Excelente"
    E que o usuário "ricardo" avaliou a carta "OP05-119" com nota 3 e comentário "Razoável"
    Quando um visitante consulta as avaliações da carta "OP05-119" no catálogo
    Então o resumo exibido apresenta média 4.0 e 2 avaliações válidas
    E todas as avaliações exibidas possuem o selo "compra verificada"

  Cenário: Consultar resumo de avaliações de uma carta ainda sem avaliações
    Quando um visitante consulta as avaliações da carta "OP05-119" no catálogo
    Então o resumo exibido apresenta média 0.0 e 0 avaliações válidas

  Cenário: Reembolso do único pedido válido invalida a avaliação e recalcula a média
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "ricardo" possui o pedido "PED-200" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" avaliou a carta "OP05-119" com nota 2 e comentário "Não gostei"
    E que o usuário "ricardo" avaliou a carta "OP05-119" com nota 5 e comentário "Perfeita"
    Quando o pedido "PED-100" do usuário "lucas" é reembolsado
    Então a avaliação do usuário "lucas" para a carta "OP05-119" é invalidada
    E o resumo da carta "OP05-119" passa a exibir média 5.0 e 1 avaliação válida

  Cenário: Reembolso de apenas um pedido mantém a avaliação válida se houver outra compra elegível
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" também possui o pedido "PED-103" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" avaliou a carta "OP05-119" com nota 5 e comentário "Excelente"
    Quando o pedido "PED-100" do usuário "lucas" é reembolsado
    Então a avaliação do usuário "lucas" para a carta "OP05-119" continua válida com o selo "compra verificada"
    E o resumo da carta "OP05-119" permanece com média 5.0 e 1 avaliação válida

  Cenário: Não é possível atualizar a avaliação de outro usuário
    Dado que o usuário "lucas" possui o pedido "PED-100" pago e não reembolsado contendo a carta "OP05-119"
    E que o usuário "lucas" avaliou a carta "OP05-119" com nota 5 e comentário "Excelente"
    Quando o usuário "ricardo" tenta alterar diretamente a avaliação do usuário "lucas" para a carta "OP05-119"
    Então a alteração da avaliação é rejeitada por falta de autorização
