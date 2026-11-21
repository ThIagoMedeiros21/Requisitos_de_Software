# language: pt
Funcionalidade: Gerenciar minha coleção
  Para registrar meu acervo pessoal de cartas independentemente de compras na loja
  Como usuário autenticado do sistema
  Quero cadastrar, consolidar, editar e remover cartas na minha coleção, acompanhar progresso
  e valuation estimado, controlar visibilidade do resumo público e receber aviso informativo na wishlist

  Contexto:
    Dado que existem coleções e cartas catalogadas com suas cotações de referência aplicáveis

  # RN1 e RN2: Cadastro, edição, remoção e validação de quantidade positiva (zero remove)

  Cenário: Cadastrar, consultar, editar e remover carta na coleção pessoal
    Dado que sou o usuário "colecionador-1" autenticado
    E que existe a carta "Monkey.D.Luffy" da coleção "OP-01"
    Quando eu cadastro "2" exemplares de "Monkey.D.Luffy" no idioma "Português", condição "Near Mint" e observação "Comprada em evento"
    Então o item é registrado na coleção pessoal de "colecionador-1" com quantidade "2" e observação "Comprada em evento"
    Quando eu altero a quantidade do item para "4" e a observação para "Foil de torneio"
    Então o item passa a constar na coleção pessoal com quantidade "4" e observação "Foil de torneio"
    Quando eu removo o item da minha coleção pessoal
    Então o item deixa de existir na coleção pessoal de "colecionador-1"

  Cenário: Não é possível cadastrar carta na coleção com quantidade zero ou negativa
    Dado que sou o usuário "colecionador-1" autenticado
    Quando eu tento cadastrar "0" exemplares de "Monkey.D.Luffy" no idioma "Japonês" e condição "Near Mint"
    Então o cadastro é rejeitado porque a quantidade deve ser inteira e positiva

  Cenário: Remover automaticamente o item ao reduzir a quantidade para zero
    Dado que sou o usuário "colecionador-1" autenticado
    E que minha coleção pessoal possui "2" exemplares de "Monkey.D.Luffy" no idioma "Japonês" e condição "Near Mint"
    Quando eu altero a quantidade de "Monkey.D.Luffy" no idioma "Japonês" e condição "Near Mint" para "0"
    Então o item de "Monkey.D.Luffy" no idioma "Japonês" e condição "Near Mint" é removido da coleção ativa

  Cenário: Não é possível alterar a quantidade de um item da coleção para valor negativo
    Dado que sou o usuário "colecionador-1" autenticado
    E que possuo "1" exemplar de "Zoro" no idioma "Inglês" e condição "Played"
    Quando eu tento alterar a quantidade de "Zoro" no idioma "Inglês" e condição "Played" para "-1"
    Então a alteração é rejeitada porque a quantidade não pode ser negativa

  # RN3: Consolidação de exemplares com mesma carta, idioma e condição

  Cenário: Consolidar quantidade ao adicionar novamente carta com mesmo idioma e condição e separar combinações distintas
    Dado que sou o usuário "colecionador-1" autenticado
    E que minha coleção pessoal possui "2" exemplares de "Nami" no idioma "Português" e condição "Near Mint"
    Quando eu adiciono mais "3" exemplares de "Nami" no idioma "Português" e condição "Near Mint"
    Então a coleção pessoal mantém apenas um registro para "Nami" em "Português" e "Near Mint" com quantidade consolidada "5"
    Quando eu adiciono "1" exemplar de "Nami" no idioma "Inglês" e condição "Near Mint"
    Então a coleção pessoal passa a conter um registro separado para "Nami" em "Inglês" e "Near Mint" com quantidade "1"

  # RN4: Progresso por coleção (cartas distintas / total catalogado)

  Cenário: Calcular progresso por coleção contando apenas cartas distintas independentemente de cópias, idiomas e condições
    Dado que sou o usuário "colecionador-1" autenticado
    E que a coleção "OP-01" possui um total de "4" cartas catalogadas
    Quando eu cadastro "3" exemplares de "Carta 1" em "Português" e "Near Mint"
    E cadastro "2" exemplares de "Carta 1" em "Japonês" e "Played"
    E cadastro "1" exemplar de "Carta 2" em "Português" e "Near Mint"
    Então o progresso calculado da coleção "OP-01" é de "2" cartas distintas de "4", equivalente a "50.0" por cento
    Quando eu removo o registro de "Carta 1" em "Japonês" e "Played"
    Então o progresso da coleção "OP-01" permanece em "50.0" por cento porque "Carta 1" ainda existe em "Português"

  # RN5: Valuation estimado com cotação específica e sinalização quando ausente

  Cenário: Calcular valuation estimado com cotação por idioma e condição e sinalizar ausência de cotação
    Dado que sou o usuário "colecionador-1" autenticado
    E que existe cotação de referência de "50.00" para "Luffy" em "Português" e "Near Mint"
    E que existe cotação de referência de "80.00" para "Luffy" em "Japonês" e "Near Mint"
    E que não existe cotação de referência cadastrada para "Carta Rara Sem Cotacao" em "Inglês" e "Mint"
    Quando eu cadastro "2" exemplares de "Luffy" em "Português" e "Near Mint"
    E cadastro "1" exemplar de "Luffy" em "Japonês" e "Near Mint"
    Então o valuation estimado completo da minha coleção é "180.00"
    Quando eu cadastro "1" exemplar de "Carta Rara Sem Cotacao" em "Inglês" e "Mint"
    Então o valuation estimado passa a ser informado como parcial no valor de "180.00"
    E o sistema informa a ausência de cotação para "Carta Rara Sem Cotacao" sem atribuir valor zero como cotação válida

  # RN6: Visibilidade do resumo público e privacidade da listagem detalhada

  Cenário: Controlar a exibição do resumo da coleção no perfil público sem expor a listagem detalhada
    Dado que sou o usuário "colecionador-1" autenticado com exemplares cadastrados em "OP-01"
    E que outro usuário "visitante-1" consulta meu perfil público por padrão
    Então o resumo da coleção de "colecionador-1" fica oculto para "visitante-1"
    Quando eu ativo a exibição do resumo público da minha coleção
    E "visitante-1" consulta meu perfil público
    Então "visitante-1" visualiza apenas o resumo com progresso e valuation autorizado
    E "visitante-1" não tem acesso à listagem integral de cartas nem às observações da coleção de "colecionador-1"
    Quando eu desativo a exibição do resumo público da minha coleção
    E "visitante-1" consulta meu perfil público novamente
    Então o resumo da coleção volta a ficar oculto para "visitante-1"

  # RN7: Aviso informativo na wishlist de cópias já possuídas sem bloquear

  Cenário: Exibir aviso informativo na wishlist com a soma de cópias já possuídas sem impedir o cadastro
    Dado que sou o usuário "colecionador-1" autenticado
    E que possuo em minha coleção pessoal "2" exemplares de "Sanji" em "Português" e "Near Mint" e "1" exemplar de "Sanji" em "Japonês" e "Played"
    Quando eu adiciono "Sanji" na condição "Near Mint" e quantidade "1" à minha wishlist
    Então o sistema exibe aviso informativo de que já possuo "3" exemplares da carta "Sanji" na minha coleção
    E o cadastro de "Sanji" na wishlist é concluído com sucesso sem bloqueio
    Quando eu adiciono "Chopper" na condição "Near Mint" e quantidade "1" à minha wishlist sem possuir exemplares na coleção
    Então o cadastro de "Chopper" na wishlist é concluído com sucesso sem exibir aviso de posse
