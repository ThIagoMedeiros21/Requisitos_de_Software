package com.cesar.tgchub.tcghub.dominio.loja.compra;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class ComprarProdutoStepDefinitions {

	@Dado("que existem produtos ativos cadastrados no catálogo com seus respectivos estoques")
	public void que_existem_produtos_ativos_cadastrados_no_catalogo_com_seus_respectivos_estoques() {
		throw new PendingException();
	}

	@Dado("que existe uma tabela de tarifas de frete configurada para PAC e Sedex")
	public void que_existe_uma_tabela_de_tarifas_de_frete_configurada_para_pac_e_sedex() {
		throw new PendingException();
	}

	@Dado("que existem produtos de cartas e boxes cadastrados em diferentes coleções, cores, raridades, tipos e preços")
	public void que_existem_produtos_de_cartas_e_boxes_cadastrados_em_diferentes_colecoes_cores_raridades_tipos_e_precos() {
		throw new PendingException();
	}

	@Quando("eu consulto o catálogo aplicando filtros de coleção {string}, cor {string}, raridade {string}, tipo {string} e preço entre {string} e {string}")
	public void eu_consulto_o_catalogo_aplicando_filtros_de_colecao_cor_raridade_tipo_e_preco_entre_e(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("são exibidos apenas os produtos compatíveis com todos os filtros e com sua disponibilidade atual")
	public void sao_exibidos_apenas_os_produtos_compativeis_com_todos_os_filtros_e_com_sua_disponibilidade_atual() {
		throw new PendingException();
	}

	@Dado("que a carta {string} possui variante {string} por {string} com estoque {string} e variante {string} por {string} com estoque {string}")
	public void que_a_carta_possui_variante_por_com_estoque_e_variante_por_com_estoque(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
		throw new PendingException();
	}

	@Quando("eu consulto os detalhes da variante {string} da carta {string}")
	public void eu_consulto_os_detalhes_da_variante_da_carta(String string, String string2) {
		throw new PendingException();
	}

	@Entao("são exibidos o preço {string}, a condição {string} e a disponibilidade de {string} unidades")
	public void sao_exibidos_o_preco_a_condicao_e_a_disponibilidade_de_unidades(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que sou o cliente {string} autenticado")
	public void que_sou_o_cliente_autenticado(String string) {
		throw new PendingException();
	}

	@Dado("que o produto {string} possui estoque total {string} e estoque reservado {string}")
	public void que_o_produto_possui_estoque_total_e_estoque_reservado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu adiciono {string} unidades de {string} ao meu carrinho")
	public void eu_adiciono_unidades_de_ao_meu_carrinho(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} é mantido no carrinho com quantidade {string}")
	public void o_produto_e_mantido_no_carrinho_com_quantidade(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o estoque reservado de {string} permanece {string}")
	public void o_estoque_reservado_de_permanece(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o estoque disponível de {string} permanece {string}")
	public void o_estoque_disponivel_de_permanece(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o produto {string} possui estoque disponível {string}")
	public void que_o_produto_possui_estoque_disponivel(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que meu carrinho já possui {string} unidades de {string}")
	public void que_meu_carrinho_ja_possui_unidades_de(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu adiciono {string} unidade de {string} ao meu carrinho")
	public void eu_adiciono_unidade_de_ao_meu_carrinho(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o meu carrinho possui apenas um item para {string} com quantidade {string}")
	public void o_meu_carrinho_possui_apenas_um_item_para_com_quantidade(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que meu carrinho possui {string} unidades de {string} e {string} unidade de {string}")
	public void que_meu_carrinho_possui_unidades_de_e_unidade_de(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu altero a quantidade de {string} no carrinho para {string}")
	public void eu_altero_a_quantidade_de_no_carrinho_para(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu removo {string} do carrinho")
	public void eu_removo_do_carrinho(String string) {
		throw new PendingException();
	}

	@Entao("o meu carrinho passa a conter apenas {string} com quantidade {string}")
	public void o_meu_carrinho_passa_a_conter_apenas_com_quantidade(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu tento adicionar {string} unidades de {string} ao meu carrinho")
	public void eu_tento_adicionar_unidades_de_ao_meu_carrinho(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a operação no carrinho é rejeitada porque a quantidade deve ser um inteiro positivo")
	public void a_operacao_no_carrinho_e_rejeitada_porque_a_quantidade_deve_ser_um_inteiro_positivo() {
		throw new PendingException();
	}

	@Entao("a operação no carrinho é rejeitada por insuficiência de estoque disponível")
	public void a_operacao_no_carrinho_e_rejeitada_por_insuficiencia_de_estoque_disponivel() {
		throw new PendingException();
	}

	@Dado("que sou o cliente {string} autenticado com endereço cadastrado de CEP {string}")
	public void que_sou_o_cliente_autenticado_com_endereco_cadastrado_de_cep(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o produto {string} possui estoque total {string}, reservado {string}, preço {string} e peso {string} gramas")
	public void que_o_produto_possui_estoque_total_reservado_preco_e_peso_gramas(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Dado("que meu carrinho possui {string} unidades de {string}")
	public void que_meu_carrinho_possui_unidades_de(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu crio o pedido no checkout com o endereço selecionado, modalidade {string} e método {string}")
	public void eu_crio_o_pedido_no_checkout_com_o_endereco_selecionado_modalidade_e_metodo(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o pedido é criado com status {string} e prazo de expiração de {string} minutos")
	public void o_pedido_e_criado_com_status_e_prazo_de_expiracao_de_minutos(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter estoque reservado {string} e estoque disponível {string}")
	public void o_produto_passa_a_ter_estoque_reservado_e_estoque_disponivel(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o item do pedido registra preço unitário congelado de {string}")
	public void o_item_do_pedido_registra_preco_unitario_congelado_de(String string) {
		throw new PendingException();
	}

	@Dado("que o produto {string} possui estoque total {string} e reservado {string}")
	public void que_o_produto_possui_estoque_total_e_reservado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} criou um pedido pendente com {string} unidades de {string}")
	public void que_o_cliente_criou_um_pedido_pendente_com_unidades_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento criar um pedido com {string} unidade de {string}")
	public void eu_tento_criar_um_pedido_com_unidade_de(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a criação do pedido é rejeitada por insuficiência de estoque disponível")
	public void a_criacao_do_pedido_e_rejeitada_por_insuficiencia_de_estoque_disponivel() {
		throw new PendingException();
	}

	@Dado("que existe tarifa de frete para a faixa de CEP {string} a {string} e peso até {string} gramas com valor {string} em {string} e prazo {string} dias")
	public void que_existe_tarifa_de_frete_para_a_faixa_de_cep_a_e_peso_ate_gramas_com_valor_em_e_prazo_dias(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Dado("que meu carrinho possui {string} unidades de {string} com peso unitário de {string} gramas")
	public void que_meu_carrinho_possui_unidades_de_com_peso_unitario_de_gramas(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu simulo o frete para o CEP {string} na modalidade {string}")
	public void eu_simulo_o_frete_para_o_cep_na_modalidade(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o frete calculado é {string} com prazo de {string} dias")
	public void o_frete_calculado_e_com_prazo_de_dias(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que meu carrinho possui {string} unidade de {string} com peso unitário de {string} gramas")
	public void que_meu_carrinho_possui_unidade_de_com_peso_unitario_de_gramas(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento simular o frete para o CEP {string} na modalidade {string}")
	public void eu_tento_simular_o_frete_para_o_cep_na_modalidade(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o cálculo de frete é rejeitado porque não existe tarifa disponível para o CEP e peso informados")
	public void o_calculo_de_frete_e_rejeitado_porque_nao_existe_tarifa_disponivel_para_o_cep_e_peso_informados() {
		throw new PendingException();
	}

	@Dado("que a tarifa {string} para o pedido é {string}")
	public void que_a_tarifa_para_o_pedido_e(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que meu carrinho possui {string} unidades de {string} ao preço de {string} cada")
	public void que_meu_carrinho_possui_unidades_de_ao_preco_de_cada(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu seleciono o método de pagamento {string} no checkout")
	public void eu_seleciono_o_metodo_de_pagamento_no_checkout(String string) {
		throw new PendingException();
	}

	@Entao("o resumo exibe subtotal {string}, frete {string}, desconto {string} e total {string}")
	public void o_resumo_exibe_subtotal_frete_desconto_e_total(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu troco o método de pagamento para {string} no checkout")
	public void eu_troco_o_metodo_de_pagamento_para_no_checkout(String string) {
		throw new PendingException();
	}

	@Entao("o resumo recalcula para subtotal {string}, frete {string}, desconto {string} e total {string}")
	public void o_resumo_recalcula_para_subtotal_frete_desconto_e_total(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu troco o método de pagamento novamente para {string} no checkout")
	public void eu_troco_o_metodo_de_pagamento_novamente_para_no_checkout(String string) {
		throw new PendingException();
	}

	@Dado("que cadastrei o endereço {string} com logradouro {string}, número {string}, cidade {string}, UF {string} e CEP {string}")
	public void que_cadastrei_o_endereco_com_logradouro_numero_cidade_uf_e_cep(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Dado("que criei o pedido {string} selecionando o endereço {string}")
	public void que_criei_o_pedido_selecionando_o_endereco(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu altero o meu endereço cadastrado {string} para logradouro {string}, número {string} e CEP {string}")
	public void eu_altero_o_meu_endereco_cadastrado_para_logradouro_numero_e_cep(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o endereço de entrega preservado no pedido {string} permanece com logradouro {string}, número {string} e CEP {string}")
	public void o_endereco_de_entrega_preservado_no_pedido_permanece_com_logradouro_numero_e_cep(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que criei o pedido pendente {string} com {string} unidades de {string}, total {string} e tentativa vigente {string} no método {string}")
	public void que_criei_o_pedido_pendente_com_unidades_de_total_e_tentativa_vigente_no_metodo(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("o gateway envia confirmação válida para a tentativa {string} no valor de {string} e método {string} dentro dos {int} minutos")
	public void o_gateway_envia_confirmacao_valida_para_a_tentativa_no_valor_de_e_metodo_dentro_dos_minutos(String string, String string2, String string3, Integer int1) {
		throw new PendingException();
	}

	@Entao("o pedido {string} passa para o status {string} com data de pagamento registrada")
	public void o_pedido_passa_para_o_status_com_data_de_pagamento_registrada(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a tentativa {string} passa para o status {string}")
	public void a_tentativa_passa_para_o_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a reserva de estoque do pedido {string} passa para {string}")
	public void a_reserva_de_estoque_do_pedido_passa_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter estoque total {string}, reservado {string} e disponível {string}")
	public void o_produto_passa_a_ter_estoque_total_reservado_e_disponivel(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o gateway reenvia a mesma confirmação para a tentativa {string}")
	public void o_gateway_reenvia_a_mesma_confirmacao_para_a_tentativa(String string) {
		throw new PendingException();
	}

	@Entao("a movimentação de estoque não é duplicada e o produto {string} permanece com estoque total {string} e reservado {string}")
	public void a_movimentacao_de_estoque_nao_e_duplicada_e_o_produto_permanece_com_estoque_total_e_reservado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que criei o pedido pendente {string} às {string} com expiração às {string}, subtotal {string}, frete {string} e tentativa vigente {string} no método {string} no valor de {string}")
	public void que_criei_o_pedido_pendente_as_com_expiracao_as_subtotal_frete_e_tentativa_vigente_no_metodo_no_valor_de(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
		throw new PendingException();
	}

	@Quando("a tentativa {string} é recusada às {string}")
	public void a_tentativa_e_recusada_as(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a tentativa {string} fica registrada como {string}")
	public void a_tentativa_fica_registrada_como(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o pedido {string} permanece {string} com expiração mantida às {string} e estoque ainda reservado")
	public void o_pedido_permanece_com_expiracao_mantida_as_e_estoque_ainda_reservado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu inicio uma nova tentativa {string} às {string} escolhendo o método {string}")
	public void eu_inicio_uma_nova_tentativa_as_escolhendo_o_metodo(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o valor autorizado da tentativa {string} é recalculado para {string}")
	public void o_valor_autorizado_da_tentativa_e_recalculado_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a expiração do pedido {string} continua às {string} sem reinício de prazo")
	public void a_expiracao_do_pedido_continua_as_sem_reinicio_de_prazo(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} criou o pedido pendente {string} às {string} com expiração às {string} e {string} unidades reservadas de {string}")
	public void que_o_cliente_criou_o_pedido_pendente_as_com_expiracao_as_e_unidades_reservadas_de(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Dado("que a tentativa {string} falhou às {string} e nenhuma nova tentativa foi aprovada até {string}")
	public void que_a_tentativa_falhou_as_e_nenhuma_nova_tentativa_foi_aprovada_ate(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o processamento de expiração é executado às {string}")
	public void o_processamento_de_expiracao_e_executado_as(String string) {
		throw new PendingException();
	}

	@Entao("o pedido {string} passa para o status {string}")
	public void o_pedido_passa_para_o_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} volta a ter estoque total {string}, reservado {string} e disponível {string}")
	public void o_produto_volta_a_ter_estoque_total_reservado_e_disponivel(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que o pedido {string} criado às {string} expirou às {string}")
	public void que_o_pedido_criado_as_expirou_as(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento iniciar uma nova tentativa de pagamento para {string} às {string}")
	public void eu_tento_iniciar_uma_nova_tentativa_de_pagamento_para_as(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a nova tentativa é rejeitada porque o pedido {string} está expirado ou cancelado")
	public void a_nova_tentativa_e_rejeitada_porque_o_pedido_esta_expirado_ou_cancelado(String string) {
		throw new PendingException();
	}

	@Dado("que o pedido pendente {string} possui a tentativa vigente {string} no método {string} e valor {string}")
	public void que_o_pedido_pendente_possui_a_tentativa_vigente_no_metodo_e_valor(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu crio uma nova tentativa {string} no método {string} no valor de {string} dentro do prazo")
	public void eu_crio_uma_nova_tentativa_no_metodo_no_valor_de_dentro_do_prazo(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a tentativa {string} é marcada como {string} e apenas {string} fica vigente")
	public void a_tentativa_e_marcada_como_e_apenas_fica_vigente(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("chega uma confirmação tardia referente à tentativa invalidada {string}")
	public void chega_uma_confirmacao_tardia_referente_a_tentativa_invalidada(String string) {
		throw new PendingException();
	}

	@Entao("o pedido {string} não é aprovado automaticamente pela tentativa {string}")
	public void o_pedido_nao_e_aprovado_automaticamente_pela_tentativa(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o recebimento é registrado para conciliação ou estorno")
	public void o_recebimento_e_registrado_para_conciliacao_ou_estorno() {
		throw new PendingException();
	}

	@Dado("que o pedido {string} possui tentativa vigente {string} autorizada no valor de {string} e método {string}")
	public void que_o_pedido_possui_tentativa_vigente_autorizada_no_valor_de_e_metodo(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("chega uma confirmação para {string} com valor divergente de {string}")
	public void chega_uma_confirmacao_para_com_valor_divergente_de(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o pedido {string} não é aprovado automaticamente e o pagamento é encaminhado para conciliação")
	public void o_pedido_nao_e_aprovado_automaticamente_e_o_pagamento_e_encaminhado_para_conciliacao(String string) {
		throw new PendingException();
	}

	@Dado("que o pedido {string} foi cancelado por expiração")
	public void que_o_pedido_foi_cancelado_por_expiracao(String string) {
		throw new PendingException();
	}

	@Quando("chega uma confirmação tardia para o pedido {string}")
	public void chega_uma_confirmacao_tardia_para_o_pedido(String string) {
		throw new PendingException();
	}

	@Entao("o pedido {string} permanece {string} sem reativação automática e o pagamento é encaminhado para conciliação ou estorno")
	public void o_pedido_permanece_sem_reativacao_automatica_e_o_pagamento_e_encaminhado_para_conciliacao_ou_estorno(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que possuo o pedido pago {string} com envio postado e código de rastreio {string}")
	public void que_possuo_o_pedido_pago_com_envio_postado_e_codigo_de_rastreio(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} possui o pedido {string}")
	public void que_o_cliente_possui_o_pedido(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu listo o meu histórico de pedidos")
	public void eu_listo_o_meu_historico_de_pedidos() {
		throw new PendingException();
	}

	@Entao("são retornados apenas os pedidos vinculados a {string}, incluindo {string} e omitindo {string}")
	public void sao_retornados_apenas_os_pedidos_vinculados_a_incluindo_e_omitindo(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu consulto os detalhes e o rastreamento do pedido {string}")
	public void eu_consulto_os_detalhes_e_o_rastreamento_do_pedido(String string) {
		throw new PendingException();
	}

	@Entao("são exibidos os itens, os valores e o código de rastreio {string}")
	public void sao_exibidos_os_itens_os_valores_e_o_codigo_de_rastreio(String string) {
		throw new PendingException();
	}

	@Quando("eu tento consultar os detalhes ou o rastreamento do pedido {string}")
	public void eu_tento_consultar_os_detalhes_ou_o_rastreamento_do_pedido(String string) {
		throw new PendingException();
	}

	@Entao("a consulta é rejeitada por falta de autorização")
	public void a_consulta_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

}
