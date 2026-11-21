# language: pt
Funcionalidade: Dashboard interativo de análise
  Para acompanhar a saúde comercial da loja TCGHub e tomar decisões baseadas em dados
  Como administrador do TCGHub
  Quero visualizar relatórios estatísticos de vendas, reembolsos, falhas, transações revogadas e alterações de preço,
  aplicar filtros combinados, exportar snapshots imutáveis para o banco analítico e comparar snapshots

  Contexto:
    Dado que existem as coleções analíticas "OP-01" e "OP-02"
    E que existem os produtos analíticos "Zoro Líder NM" da categoria "CARTA_AVULSA", "Booster Box OP-01" da categoria "BOOSTER_BOX" e "Starter Deck Zoro" da categoria "DECK"

  Cenário: Não é possível acessar o dashboard sem autorização de administrador
    Dado que sou um usuário sem autorização de administrador
    Quando eu tento abrir o dashboard de vendas
    Então o acesso ao dashboard é rejeitado por falta de autorização

  # RN1: Receita líquida

  Cenário: Calcular receita líquida do período descontando reembolsos e estornos
    Dado que sou um usuário administrador
    E que existem os seguintes eventos de negócio:
      | data       | tipo               | pedido  | valor   |
      | 2026-11-16 | VENDA              | PED-001 | 1000.00 |
      | 2026-11-17 | VENDA              | PED-002 | 500.00  |
      | 2026-11-18 | REEMBOLSO          | PED-002 | 500.00  |
      | 2026-11-19 | VENDA              | PED-003 | 300.00  |
      | 2026-11-20 | TRANSACAO_REVOGADA | PED-003 | 300.00  |
      | 2026-11-21 | VENDA              | PED-004 | 200.00  |
    Quando eu consulto o dashboard no período de "2026-11-16" a "2026-11-22"
    Então a receita líquida do período é "1200.00"
    E a receita líquida por dia exibe "1000.00" em "2026-11-16" e "-500.00" em "2026-11-18"
    E a receita líquida por semana exibe "1200.00" na semana iniciada em "2026-11-16"

  Cenário: Comparar a receita líquida com o período anterior de mesma duração
    Dado que sou um usuário administrador
    E que a receita líquida entre "2026-11-09" e "2026-11-15" foi "1000.00"
    E que a receita líquida entre "2026-11-16" e "2026-11-22" foi "1200.00"
    Quando eu consulto o dashboard no período de "2026-11-16" a "2026-11-22"
    Então a receita líquida é comparada ao período de "2026-11-09" a "2026-11-15"
    E a variação da receita líquida em relação ao período anterior é "20.00" por cento

  # RN2: Taxa de falha e reembolso

  Cenário: Calcular taxas de falha e de reembolso sobre o total de pedidos
    Dado que sou um usuário administrador
    E que no período de "2026-11-16" a "2026-11-22" existem "20" pedidos, "2" eventos de falha de pagamento e "1" evento de reembolso
    Quando eu consulto o dashboard no período de "2026-11-16" a "2026-11-22"
    Então a taxa de falha exibida é "10.00" por cento
    E a taxa de reembolso exibida é "5.00" por cento

  Cenário: Taxas ficam zeradas quando não há pedidos no período
    Dado que sou um usuário administrador
    E que no período de "2026-10-01" a "2026-10-07" não existe nenhum pedido
    Quando eu consulto o dashboard no período de "2026-10-01" a "2026-10-07"
    Então a taxa de falha exibida é "0.00" por cento
    E a taxa de reembolso exibida é "0.00" por cento

  # RN3: Ranking de produtos

  Cenário: Exibir ranking de produtos com crescimento em relação ao período anterior
    Dado que sou um usuário administrador
    E que no período anterior o produto "Booster Box OP-01" vendeu "10" unidades e o produto "Zoro Líder NM" vendeu "20" unidades
    E que no período atual o produto "Booster Box OP-01" vendeu "15" unidades e o produto "Zoro Líder NM" vendeu "18" unidades
    Quando eu consulto o ranking de produtos do período atual
    Então o ranking exibe "Zoro Líder NM" em primeiro lugar com crescimento de "-10.00" por cento
    E o ranking exibe "Booster Box OP-01" em segundo lugar com crescimento de "50.00" por cento

  Cenário: Exibir vendas por coleção e distribuição da receita por categoria
    Dado que sou um usuário administrador
    E que no período consultado a receita foi "600.00" em cartas avulsas, "1300.00" em booster boxes e "100.00" em decks
    E que a coleção "OP-01" vendeu "1500.00" e a coleção "OP-02" vendeu "500.00"
    Quando eu consulto o dashboard no período de "2026-11-16" a "2026-11-22"
    Então as vendas por coleção exibem "1500.00" para "OP-01" e "500.00" para "OP-02"
    E a distribuição da receita por categoria exibe "30.00" por cento em "CARTA_AVULSA", "65.00" por cento em "BOOSTER_BOX" e "5.00" por cento em "DECK"

  # RN4: Histórico de preço

  Cenário: Exibir gráfico de oscilação de preço de um produto
    Dado que sou um usuário administrador
    E que o produto analítico "Zoro Líder NM" possui eventos de alteração de preço para "25.00" em "2026-11-01", "30.00" em "2026-11-10" e "28.00" em "2026-11-20"
    Quando eu consulto o histórico de preço do produto analítico "Zoro Líder NM"
    Então o gráfico de oscilação exibe "3" pontos de preço ordenados por data
    E o último ponto do gráfico é "28.00" em "2026-11-20"

  # RN5: Filtros combinados

  Cenário: Aplicar filtros combinados de período, coleção, tipo de evento e categoria
    Dado que sou um usuário administrador
    E que existem eventos de negócio de vendas e reembolsos das coleções "OP-01" e "OP-02" em todas as categorias de produto
    Quando eu filtro o dashboard pelo período de "2026-11-16" a "2026-11-22", coleção "OP-01", tipo de evento "VENDA" e categoria "BOOSTER_BOX"
    Então todas as métricas consideram apenas eventos do tipo "VENDA" de produtos "BOOSTER_BOX" da coleção "OP-01" no período informado

  Cenário: Filtros opcionais não informados não restringem o resultado
    Dado que sou um usuário administrador
    E que existem eventos de negócio de vendas e reembolsos das coleções "OP-01" e "OP-02" em todas as categorias de produto
    Quando eu filtro o dashboard apenas pelo período de "2026-11-16" a "2026-11-22"
    Então as métricas consideram eventos de todas as coleções, tipos de evento e categorias no período informado

  Cenário: Não é possível filtrar com período inválido
    Dado que sou um usuário administrador
    Quando eu tento filtrar o dashboard pelo período de "2026-11-22" a "2026-11-16"
    Então o filtro é rejeitado porque a data inicial não pode ser posterior à data final

  # RN6: Ticket médio

  Cenário: Calcular ticket médio sobre os pedidos pagos
    Dado que sou um usuário administrador
    E que no período de "2026-11-16" a "2026-11-22" a receita foi "1200.00" com "8" pedidos pagos
    Quando eu consulto o dashboard no período de "2026-11-16" a "2026-11-22"
    Então o ticket médio exibido é "150.00"

  Cenário: Ticket médio é zero quando não há pedidos pagos
    Dado que sou um usuário administrador
    E que no período de "2026-10-01" a "2026-10-07" a receita foi "0.00" com "0" pedidos pagos
    Quando eu consulto o dashboard no período de "2026-10-01" a "2026-10-07"
    Então o ticket médio exibido é "0.00"

  # RN7: Exportação de snapshot

  Cenário: Exportar o dashboard como snapshot imutável
    Dado que sou um usuário administrador
    E que estou visualizando o dashboard "VENDAS_RECEITA" no período de "2026-11-16" a "2026-11-22"
    Quando eu exporto o dashboard como snapshot para a tabela de destino "analitico.vendas_receita"
    Então é gravado um snapshot do tipo "VENDAS_RECEITA" com as métricas exibidas, a tabela de destino "analitico.vendas_receita" e o total de registros exportados
    E o snapshot registra um hash SHA-256 de integridade e a versão do esquema "1.0"

  Cenário: Não é possível alterar um snapshot já exportado
    Dado que sou um usuário administrador
    E que existe o snapshot "SNAP-001" do tipo "VENDAS_RECEITA"
    Quando eu tento alterar as métricas do snapshot "SNAP-001"
    Então a alteração é rejeitada porque o snapshot "SNAP-001" é imutável
    E o hash de integridade do snapshot "SNAP-001" permanece o mesmo

  Cenário: Não é possível exportar snapshot com hash já existente
    Dado que sou um usuário administrador
    E que já existe um snapshot exportado com o mesmo hash SHA-256 das métricas atuais
    Quando eu tento exportar o dashboard como snapshot para a tabela de destino "analitico.vendas_receita"
    Então a exportação é rejeitada porque já existe um snapshot com o mesmo hash de integridade

  # RN8: Comparação de snapshots

  Cenário: Comparar dois snapshots do mesmo tipo exibindo a variação percentual de cada métrica
    Dado que sou um usuário administrador
    E que existe o snapshot "SNAP-001" do tipo "VENDAS_RECEITA" com receita líquida "1000.00" e ticket médio "100.00"
    E que existe o snapshot "SNAP-002" do tipo "VENDAS_RECEITA" com receita líquida "1200.00" e ticket médio "90.00"
    Quando eu comparo o snapshot "SNAP-001" com o snapshot "SNAP-002"
    Então a comparação exibe variação de "20.00" por cento na receita líquida
    E a comparação exibe variação de "-10.00" por cento no ticket médio

  Cenário: Não é possível comparar snapshots de tipos diferentes
    Dado que sou um usuário administrador
    E que existe o snapshot "SNAP-001" do tipo "VENDAS_RECEITA"
    E que existe o snapshot "SNAP-003" do tipo "CATALOGO_ESTOQUE"
    Quando eu comparo o snapshot "SNAP-001" com o snapshot "SNAP-003"
    Então a comparação é rejeitada porque os snapshots devem ser do mesmo tipo de dashboard

  Cenário: Não é possível comparar quantidade de snapshots diferente de dois
    Dado que sou um usuário administrador
    E que existem os snapshots "SNAP-001", "SNAP-002" e "SNAP-004" do tipo "VENDAS_RECEITA"
    Quando eu tento comparar os snapshots "SNAP-001", "SNAP-002" e "SNAP-004" ao mesmo tempo
    Então a comparação é rejeitada porque devem ser escolhidos exatamente dois snapshots
