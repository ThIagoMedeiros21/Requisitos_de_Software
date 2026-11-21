package com.cesar.tgchub.tcghub.dominio.adm.pedido;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class GestaoDePedidosStepDefinitions {

	@Dado("que existe o cliente {string} com e-mail {string}")
	public void que_existe_o_cliente_com_e_mail(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe o produto de pedido {string} com preço {string} e estoque {string}")
	public void que_existe_o_produto_de_pedido_com_preco_e_estoque(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} do cliente {string} com {string} unidades de {string}, frete {string}, total {string} e status {string}")
	public void que_existe_o_pedido_do_cliente_com_unidades_de_frete_total_e_status(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
		throw new PendingException();
	}

	@Dado("que o pedido {string} possui pagamento {string} com status {string} e custódia {string}")
	public void que_o_pedido_possui_pagamento_com_status_e_custodia(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que o pedido {string} foi despachado pela transportadora {string} com código de rastreio {string} e possui {string} eventos de rastreio")
	public void que_o_pedido_foi_despachado_pela_transportadora_com_codigo_de_rastreio_e_possui_eventos_de_rastreio(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu consulto os detalhes do pedido {string}")
	public void eu_consulto_os_detalhes_do_pedido(String string) {
		throw new PendingException();
	}

	@Entao("são exibidos o cliente {string}, os itens, os valores, o frete {string} e o endereço de entrega do pedido {string}")
	public void sao_exibidos_o_cliente_os_itens_os_valores_o_frete_e_o_endereco_de_entrega_do_pedido(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("são exibidos a situação do pagamento {string} e a situação da custódia {string}")
	public void sao_exibidos_a_situacao_do_pagamento_e_a_situacao_da_custodia(String string, String string2) {
		throw new PendingException();
	}

	@Entao("são exibidos a transportadora {string}, o código de rastreio {string} e o histórico com {string} eventos de rastreio")
	public void sao_exibidos_a_transportadora_o_codigo_de_rastreio_e_o_historico_com_eventos_de_rastreio(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento consultar os detalhes do pedido {string}")
	public void eu_tento_consultar_os_detalhes_do_pedido(String string) {
		throw new PendingException();
	}

	@Entao("a consulta do pedido é rejeitada porque não existe pedido com o número {string}")
	public void a_consulta_do_pedido_e_rejeitada_porque_nao_existe_pedido_com_o_numero(String string) {
		throw new PendingException();
	}

	@Entao("o acesso ao ambiente de pedidos é rejeitado por falta de autorização")
	public void o_acesso_ao_ambiente_de_pedidos_e_rejeitado_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Dado("que existem {string} pedidos com status {string}, {string} com status {string}, {string} com status {string} e {string} com status {string}")
	public void que_existem_pedidos_com_status_com_status_com_status_e_com_status(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
		throw new PendingException();
	}

	@Quando("eu abro a aba de pedidos com status {string}")
	public void eu_abro_a_aba_de_pedidos_com_status(String string) {
		throw new PendingException();
	}

	@Entao("a listagem retorna apenas os {string} pedidos com status {string}")
	public void a_listagem_retorna_apenas_os_pedidos_com_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("as abas exibem as quantidades {string} pagos, {string} enviados, {string} entregues e {string} reembolsado")
	public void as_abas_exibem_as_quantidades_pagos_enviados_entregues_e_reembolsado(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} criado em {string} com status {string}")
	public void que_existe_o_pedido_criado_em_com_status(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu filtro os pedidos com status {string} no período de {string} a {string}")
	public void eu_filtro_os_pedidos_com_status_no_periodo_de_a(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a listagem retorna apenas o pedido {string}")
	public void a_listagem_retorna_apenas_o_pedido(String string) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} do cliente {string} com e-mail {string}")
	public void que_existe_o_pedido_do_cliente_com_e_mail(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu busco pedidos pelo termo {string}")
	public void eu_busco_pedidos_pelo_termo(String string) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} com status {string}")
	public void que_existe_o_pedido_com_status(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu despacho o pedido {string} pela transportadora {string}, código de rastreio {string}, data de envio {string} e previsão de entrega {string}")
	public void eu_despacho_o_pedido_pela_transportadora_codigo_de_rastreio_data_de_envio_e_previsao_de_entrega(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("o pedido {string} passa para o status {string}")
	public void o_pedido_passa_para_o_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o envio do pedido {string} registra a transportadora {string} e o código de rastreio {string}")
	public void o_envio_do_pedido_registra_a_transportadora_e_o_codigo_de_rastreio(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento despachar o pedido {string} pela transportadora {string}, código de rastreio {string}, data de envio {string} e previsão de entrega {string}")
	public void eu_tento_despachar_o_pedido_pela_transportadora_codigo_de_rastreio_data_de_envio_e_previsao_de_entrega(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("o despacho é rejeitado porque apenas pedidos com status {string} podem ser despachados")
	public void o_despacho_e_rejeitado_porque_apenas_pedidos_com_status_podem_ser_despachados(String string) {
		throw new PendingException();
	}

	@Entao("o pedido {string} permanece com o status {string}")
	public void o_pedido_permanece_com_o_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o despacho é rejeitado porque transportadora, código de rastreio, data de envio e previsão de entrega são obrigatórios")
	public void o_despacho_e_rejeitado_porque_transportadora_codigo_de_rastreio_data_de_envio_e_previsao_de_entrega_sao_obrigatorios() {
		throw new PendingException();
	}

	@Quando("eu tento marcar o pedido {string} como entregue com recebedor {string}, data e hora {string} e assinatura digital {string}")
	public void eu_tento_marcar_o_pedido_como_entregue_com_recebedor_data_e_hora_e_assinatura_digital(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("a transição é rejeitada porque o pedido deve seguir CRIADO, PAGO, ENVIADO e ENTREGUE sem pular etapas")
	public void a_transicao_e_rejeitada_porque_o_pedido_deve_seguir_criado_pago_enviado_e_entregue_sem_pular_etapas() {
		throw new PendingException();
	}

	@Quando("eu registro no pedido {string} o evento de rastreio {string} em {string} na cidade {string}")
	public void eu_registro_no_pedido_o_evento_de_rastreio_em_na_cidade(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o histórico de rastreio do pedido {string} passa a conter o evento {string}")
	public void o_historico_de_rastreio_do_pedido_passa_a_conter_o_evento(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} com status {string} e custódia {string} no valor de {string}")
	public void que_existe_o_pedido_com_status_e_custodia_no_valor_de(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu marco o pedido {string} como entregue com recebedor {string}, data e hora {string} e assinatura digital {string}")
	public void eu_marco_o_pedido_como_entregue_com_recebedor_data_e_hora_e_assinatura_digital(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o comprovante de entrega do pedido {string} registra o recebedor {string}, a data e hora {string} e a assinatura digital")
	public void o_comprovante_de_entrega_do_pedido_registra_o_recebedor_a_data_e_hora_e_a_assinatura_digital(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a confirmação de entrega é rejeitada porque o comprovante exige recebedor, data e hora e assinatura digital")
	public void a_confirmacao_de_entrega_e_rejeitada_porque_o_comprovante_exige_recebedor_data_e_hora_e_assinatura_digital() {
		throw new PendingException();
	}

	@Entao("a confirmação de entrega é rejeitada porque apenas pedidos com status {string} podem ser entregues")
	public void a_confirmacao_de_entrega_e_rejeitada_porque_apenas_pedidos_com_status_podem_ser_entregues(String string) {
		throw new PendingException();
	}

	@Dado("que sou o cliente {string} autenticado sem autorização de administrador")
	public void que_sou_o_cliente_autenticado_sem_autorizacao_de_administrador(String string) {
		throw new PendingException();
	}

	@Dado("que o produto de pedido {string} possui estoque {string}")
	public void que_o_produto_de_pedido_possui_estoque(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} com {string} unidades de {string}, status {string} e custódia {string} no valor de {string}")
	public void que_existe_o_pedido_com_unidades_de_status_e_custodia_no_valor_de(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("eu reembolso o pedido {string} informando o motivo {string}")
	public void eu_reembolso_o_pedido_informando_o_motivo(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o pagamento do pedido {string} passa para o status {string}")
	public void o_pagamento_do_pedido_passa_para_o_status(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a custódia do pedido {string} passa para {string} com {string} devolvidos ao comprador")
	public void a_custodia_do_pedido_passa_para_com_devolvidos_ao_comprador(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o produto de pedido {string} volta a ter estoque {string}")
	public void o_produto_de_pedido_volta_a_ter_estoque(String string, String string2) {
		throw new PendingException();
	}

	@Entao("é disparado o evento de pedido {string} para o pedido {string}")
	public void e_disparado_o_evento_de_pedido_para_o_pedido(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o meio de pagamento informa que a transação do pedido {string} foi revogada")
	public void o_meio_de_pagamento_informa_que_a_transacao_do_pedido_foi_revogada(String string) {
		throw new PendingException();
	}

	@Quando("eu tento reembolsar o pedido {string} informando o motivo {string}")
	public void eu_tento_reembolsar_o_pedido_informando_o_motivo(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o reembolso é rejeitado porque o pedido {string} já foi reembolsado")
	public void o_reembolso_e_rejeitado_porque_o_pedido_ja_foi_reembolsado(String string) {
		throw new PendingException();
	}

	@Entao("o estoque e a custódia do pedido {string} não são alterados novamente")
	public void o_estoque_e_a_custodia_do_pedido_nao_sao_alterados_novamente(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cancelar o pedido {string}")
	public void eu_tento_cancelar_o_pedido(String string) {
		throw new PendingException();
	}

	@Entao("o cancelamento é rejeitado porque pedido entregue só pode ser reembolsado")
	public void o_cancelamento_e_rejeitado_porque_pedido_entregue_so_pode_ser_reembolsado() {
		throw new PendingException();
	}

	@Entao("o valor de {string} é devolvido ao comprador do pedido {string}")
	public void o_valor_de_e_devolvido_ao_comprador_do_pedido(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe o pedido {string} com status {string} e total {string}")
	public void que_existe_o_pedido_com_status_e_total(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o pagamento do pedido {string} é aprovado")
	public void o_pagamento_do_pedido_e_aprovado(String string) {
		throw new PendingException();
	}

	@Entao("a custódia do pedido {string} passa para {string} com o valor de {string}")
	public void a_custodia_do_pedido_passa_para_com_o_valor_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que a carteira do vendedor possui saldo {string}")
	public void que_a_carteira_do_vendedor_possui_saldo(String string) {
		throw new PendingException();
	}

	@Entao("a custódia do pedido {string} passa para {string}")
	public void a_custodia_do_pedido_passa_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a carteira do vendedor passa a ter saldo {string}")
	public void a_carteira_do_vendedor_passa_a_ter_saldo(String string) {
		throw new PendingException();
	}

	@Quando("eu cancelo o pedido {string}")
	public void eu_cancelo_o_pedido(String string) {
		throw new PendingException();
	}

	@Entao("a alteração do pedido e da custódia é gravada na mesma transação")
	public void a_alteracao_do_pedido_e_da_custodia_e_gravada_na_mesma_transacao() {
		throw new PendingException();
	}

	@Dado("que o pedido {string} foi pago em {string} e ainda não foi despachado")
	public void que_o_pedido_foi_pago_em_e_ainda_nao_foi_despachado(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o horário atual é {string}")
	public void que_o_horario_atual_e(String string) {
		throw new PendingException();
	}

	@Entao("o pedido {string} é exibido com prioridade {string} por prazo de despacho vencido")
	public void o_pedido_e_exibido_com_prioridade_por_prazo_de_despacho_vencido(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o pedido {string} é exibido sem prioridade alta")
	public void o_pedido_e_exibido_sem_prioridade_alta(String string) {
		throw new PendingException();
	}
}
