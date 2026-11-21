package com.cesar.tgchub.tcghub.dominio.adm.aviseme;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AviseMeStepDefinitions {

	@Dado("que existe o produto disponível para aviso {string} com estoque {string}, visibilidade {string} e Avise-me {string}")
	public void que_existe_o_produto_disponivel_para_aviso_com_estoque_visibilidade_e_avise_me(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que sou o cliente {string} autenticado na loja")
	public void que_sou_o_cliente_autenticado_na_loja(String string) {
		throw new PendingException();
	}

	@Quando("eu visualizo o produto {string}")
	public void eu_visualizo_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("o botão Avise-me é exibido para o produto {string}")
	public void o_botao_avise_me_e_exibido_para_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("o botão Avise-me não é exibido para o produto {string}")
	public void o_botao_avise_me_nao_e_exibido_para_o_produto(String string) {
		throw new PendingException();
	}

	@Quando("eu tento me inscrever no Avise-me do produto {string}")
	public void eu_tento_me_inscrever_no_avise_me_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("a inscrição no Avise-me é rejeitada porque o produto não está esgotado")
	public void a_inscricao_no_avise_me_e_rejeitada_porque_o_produto_nao_esta_esgotado() {
		throw new PendingException();
	}

	@Quando("eu me inscrevo no Avise-me do produto {string}")
	public void eu_me_inscrevo_no_avise_me_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("o cliente {string} passa a constar na fila de aviso do produto {string} com a data e hora da inscrição")
	public void o_cliente_passa_a_constar_na_fila_de_aviso_do_produto_com_a_data_e_hora_da_inscricao(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} já está inscrito no Avise-me do produto {string}")
	public void que_o_cliente_ja_esta_inscrito_no_avise_me_do_produto(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a inscrição no Avise-me é rejeitada porque o cliente já possui aviso para este produto")
	public void a_inscricao_no_avise_me_e_rejeitada_porque_o_cliente_ja_possui_aviso_para_este_produto() {
		throw new PendingException();
	}

	@Entao("a fila de aviso do produto {string} continua com {string} inscrição do cliente {string}")
	public void a_fila_de_aviso_do_produto_continua_com_inscricao_do_cliente(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu cancelo minha inscrição no Avise-me do produto {string}")
	public void eu_cancelo_minha_inscricao_no_avise_me_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("o cliente {string} deixa de constar na fila de aviso do produto {string}")
	public void o_cliente_deixa_de_constar_na_fila_de_aviso_do_produto(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que a fila de aviso do produto {string} possui, em ordem de inscrição, os clientes {string}, {string}, {string} e {string}")
	public void que_a_fila_de_aviso_do_produto_possui_em_ordem_de_inscricao_os_clientes_e(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("o administrador repõe {string} unidades do produto {string} disparando o evento EstoqueReposto")
	public void o_administrador_repoe_unidades_do_produto_disparando_o_evento_estoquereposto(String string, String string2) {
		throw new PendingException();
	}

	@Entao("os clientes {string} e {string} são atendidos com {string} unidade cada")
	public void os_clientes_e_sao_atendidos_com_unidade_cada(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("cada cliente atendido recebe uma notificação na plataforma, por e-mail e por push com o link direto do produto {string}")
	public void cada_cliente_atendido_recebe_uma_notificacao_na_plataforma_por_e_mail_e_por_push_com_o_link_direto_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("cada cliente atendido tem {string} unidade do produto {string} reservada por {string} minutos")
	public void cada_cliente_atendido_tem_unidade_do_produto_reservada_por_minutos(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("os avisos de {string} e {string} são removidos da fila")
	public void os_avisos_de_e_sao_removidos_da_fila(String string, String string2) {
		throw new PendingException();
	}

	@Entao("os clientes {string} e {string} continuam na fila de aviso do produto {string} na mesma ordem")
	public void os_clientes_e_continuam_na_fila_de_aviso_do_produto_na_mesma_ordem(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("é gravado um lote de disparo para o produto {string} com quantidade reposta {string}, {string} clientes atendidos e {string} clientes restantes na fila")
	public void e_gravado_um_lote_de_disparo_para_o_produto_com_quantidade_reposta_clientes_atendidos_e_clientes_restantes_na_fila(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que a fila de aviso do produto {string} possui, em ordem de inscrição, os clientes {string} e {string}")
	public void que_a_fila_de_aviso_do_produto_possui_em_ordem_de_inscricao_os_clientes_e_2(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a fila de aviso do produto {string} fica vazia")
	public void a_fila_de_aviso_do_produto_fica_vazia(String string) {
		throw new PendingException();
	}

	@Entao("{string} unidades do produto {string} ficam disponíveis para compra livre")
	public void unidades_do_produto_ficam_disponiveis_para_compra_livre(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} foi atendido na reposição do produto {string} às {string} com {string} unidade reservada")
	public void que_o_cliente_foi_atendido_na_reposicao_do_produto_as_com_unidade_reservada(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o cliente {string} não conclui a compra até {string}")
	public void o_cliente_nao_conclui_a_compra_ate(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a reserva do cliente {string} expira às {string}")
	public void a_reserva_do_cliente_expira_as(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a unidade reservada volta ao estoque disponível do produto {string}")
	public void a_unidade_reservada_volta_ao_estoque_disponivel_do_produto(String string) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} possui {string} notificações não lidas e {string} notificações lidas")
	public void que_o_cliente_possui_notificacoes_nao_lidas_e_notificacoes_lidas(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu abro o menu da plataforma")
	public void eu_abro_o_menu_da_plataforma() {
		throw new PendingException();
	}

	@Entao("o menu exibe {string} notificações não lidas")
	public void o_menu_exibe_notificacoes_nao_lidas(String string) {
		throw new PendingException();
	}

	@Quando("eu marco a notificação de reposição do produto {string} como lida")
	public void eu_marco_a_notificacao_de_reposicao_do_produto_como_lida(String string) {
		throw new PendingException();
	}

	@Quando("eu dispenso a notificação de reposição do produto {string}")
	public void eu_dispenso_a_notificacao_de_reposicao_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("a notificação de reposição do produto {string} deixa de ser exibida na lista de notificações")
	public void a_notificacao_de_reposicao_do_produto_deixa_de_ser_exibida_na_lista_de_notificacoes(String string) {
		throw new PendingException();
	}

	@Dado("que a fila de aviso do produto {string} possui, em ordem de inscrição, os clientes {string}, {string} e {string}")
	public void que_a_fila_de_aviso_do_produto_possui_em_ordem_de_inscricao_os_clientes_e_3(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o administrador oculta o produto {string}")
	public void o_administrador_oculta_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("nenhum e-mail de aviso de reposição é enviado aos clientes {string}, {string} e {string}")
	public void nenhum_e_mail_de_aviso_de_reposicao_e_enviado_aos_clientes_e(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o cliente {string} recebeu a notificação de reposição do produto {string} em {string}")
	public void que_o_cliente_recebeu_a_notificacao_de_reposicao_do_produto_em(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o cliente {string} tem um pedido pago do produto {string} em {string}")
	public void o_cliente_tem_um_pedido_pago_do_produto_em(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a notificação de reposição do cliente {string} é marcada como convertida")
	public void a_notificacao_de_reposicao_do_cliente_e_marcada_como_convertida(String string) {
		throw new PendingException();
	}

	@Entao("a notificação de reposição do cliente {string} não é marcada como convertida")
	public void a_notificacao_de_reposicao_do_cliente_nao_e_marcada_como_convertida(String string) {
		throw new PendingException();
	}

	@Dado("que foram enviadas {string} notificações de reposição, das quais {string} foram convertidas")
	public void que_foram_enviadas_notificacoes_de_reposicao_das_quais_foram_convertidas(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu consulto a taxa de conversão do Avise-me")
	public void eu_consulto_a_taxa_de_conversao_do_avise_me() {
		throw new PendingException();
	}

	@Entao("a taxa de conversão exibida é {string} por cento")
	public void a_taxa_de_conversao_exibida_e_por_cento(String string) {
		throw new PendingException();
	}
}
