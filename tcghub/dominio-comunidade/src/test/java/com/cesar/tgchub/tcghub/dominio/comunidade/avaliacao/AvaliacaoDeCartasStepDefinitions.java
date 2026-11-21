package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacao;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AvaliacaoDeCartasStepDefinitions {

	@Dado("que existe o produto de carta {string} de nome {string}")
	public void que_existe_o_produto_de_carta_de_nome(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que não existe nenhuma avaliação cadastrada para a carta {string}")
	public void que_nao_existe_nenhuma_avaliacao_cadastrada_para_a_carta(String string) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} possui o pedido {string} pago e não reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_o_pedido_pago_e_nao_reembolsado_contendo_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o usuário {string} avalia a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_avalia_a_carta_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} é registrada com sucesso")
	public void a_avaliacao_da_carta_pelo_usuario_e_registrada_com_sucesso(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a avaliação exibe o selo {string}")
	public void a_avaliacao_exibe_o_selo(String string) {
		throw new PendingException();
	}

	@Entao("o resumo da carta {string} passa a exibir média {double} e {int} avaliação válida")
	public void o_resumo_da_carta_passa_a_exibir_media_e_avaliacao_valida(String string, Double double1, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} avalia a carta {string} com nota {int} e sem comentário")
	public void o_usuario_avalia_a_carta_com_nota_e_sem_comentario(String string, String string2, Integer int1) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} não possui nenhum pedido contendo a carta {string}")
	public void que_o_usuario_nao_possui_nenhum_pedido_contendo_a_carta(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta avaliar a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_tenta_avaliar_a_carta_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Entao("a avaliação é rejeitada porque o usuário {string} não possui compra paga e não reembolsada da carta {string}")
	public void a_avaliacao_e_rejeitada_porque_o_usuario_nao_possui_compra_paga_e_nao_reembolsada_da_carta(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} possui apenas o pedido {string} com pagamento pendente contendo a carta {string}")
	public void que_o_usuario_possui_apenas_o_pedido_com_pagamento_pendente_contendo_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} possui apenas o pedido {string} reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_apenas_o_pedido_reembolsado_contendo_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} possui o pedido {string} reembolsado contendo a carta {string}")
	public void que_o_usuario_possui_o_pedido_reembolsado_contendo_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} também possui o pedido {string} pago e não reembolsado contendo a carta {string}")
	public void que_o_usuario_tambem_possui_o_pedido_pago_e_nao_reembolsado_contendo_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a avaliação é rejeitada porque a nota deve estar entre {int} e {int}")
	public void a_avaliacao_e_rejeitada_porque_a_nota_deve_estar_entre_e(Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} já avaliou a carta {string} com nota {int} e comentário {string}")
	public void que_o_usuario_ja_avaliou_a_carta_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Quando("o usuário {string} reavalia a carta {string} com nota {int} e comentário {string}")
	public void o_usuario_reavalia_a_carta_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} é atualizada com sucesso para nota {int} e comentário {string}")
	public void a_avaliacao_da_carta_pelo_usuario_e_atualizada_com_sucesso_para_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Quando("o usuário {string} inicia a edição da avaliação para nota {int} e cancela a alteração")
	public void o_usuario_inicia_a_edicao_da_avaliacao_para_nota_e_cancela_a_alteracao(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a avaliação da carta {string} pelo usuário {string} permanece com nota {int} e comentário {string}")
	public void a_avaliacao_da_carta_pelo_usuario_permanece_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} avaliou a carta {string} com nota {int} e comentário {string}")
	public void que_o_usuario_avaliou_a_carta_com_nota_e_comentario(String string, String string2, Integer int1, String string3) {
		throw new PendingException();
	}

	@Quando("um visitante consulta as avaliações da carta {string} no catálogo")
	public void um_visitante_consulta_as_avaliacoes_da_carta_no_catalogo(String string) {
		throw new PendingException();
	}

	@Entao("o resumo exibido apresenta média {double} e {int} avaliações válidas")
	public void o_resumo_exibido_apresenta_media_e_avaliacoes_validas(Double double1, Integer int1) {
		throw new PendingException();
	}

	@Entao("todas as avaliações exibidas possuem o selo {string}")
	public void todas_as_avaliacoes_exibidas_possuem_o_selo(String string) {
		throw new PendingException();
	}

	@Quando("o pedido {string} do usuário {string} é reembolsado")
	public void o_pedido_do_usuario_e_reembolsado(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a avaliação do usuário {string} para a carta {string} é invalidada")
	public void a_avaliacao_do_usuario_para_a_carta_e_invalidada(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a avaliação do usuário {string} para a carta {string} continua válida com o selo {string}")
	public void a_avaliacao_do_usuario_para_a_carta_continua_valida_com_o_selo(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o resumo da carta {string} permanece com média {double} e {int} avaliação válida")
	public void o_resumo_da_carta_permanece_com_media_e_avaliacao_valida(String string, Double double1, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta alterar diretamente a avaliação do usuário {string} para a carta {string}")
	public void o_usuario_tenta_alterar_diretamente_a_avaliacao_do_usuario_para_a_carta(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a alteração da avaliação é rejeitada por falta de autorização")
	public void a_alteracao_da_avaliacao_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

}
