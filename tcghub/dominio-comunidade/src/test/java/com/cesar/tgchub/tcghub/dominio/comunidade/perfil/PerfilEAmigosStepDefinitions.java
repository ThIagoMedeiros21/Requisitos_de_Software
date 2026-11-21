package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class PerfilEAmigosStepDefinitions {

	@Dado("que não existe nenhum perfil comunitário cadastrado")
	public void que_nao_existe_nenhum_perfil_comunitario_cadastrado() {
		throw new PendingException();
	}

	@Dado("que não existe nenhuma solicitação ou amizade cadastrada")
	public void que_nao_existe_nenhuma_solicitacao_ou_amizade_cadastrada() {
		throw new PendingException();
	}

	@Dado("que existe o perfil comunitário do usuário {string} com apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void que_existe_o_perfil_comunitario_do_usuario_com_apelido_cidade_descricao_e_foto(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta o seu próprio perfil comunitário")
	public void o_usuario_consulta_o_seu_proprio_perfil_comunitario(String string) {
		throw new PendingException();
	}

	@Entao("o perfil comunitário retornado exibe apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_comunitario_retornado_exibe_apelido_cidade_descricao_e_foto(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta o perfil público do usuário {string}")
	public void o_usuario_consulta_o_perfil_publico_do_usuario(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o perfil público retornado exibe apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_publico_retornado_exibe_apelido_cidade_descricao_e_foto(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta consultar o perfil público do usuário {string}")
	public void o_usuario_tenta_consultar_o_perfil_publico_do_usuario(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a consulta do perfil comunitário é rejeitada porque o perfil {string} não existe")
	public void a_consulta_do_perfil_comunitario_e_rejeitada_porque_o_perfil_nao_existe(String string) {
		throw new PendingException();
	}

	@Quando("o usuário {string} edita seu perfil comunitário para apelido {string}, cidade {string}, descrição {string}, foto {string} e preferências públicas {string}")
	public void o_usuario_edita_seu_perfil_comunitario_para_apelido_cidade_descricao_foto_e_preferencias_publicas(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("a edição do perfil comunitário é concluída com sucesso")
	public void a_edicao_do_perfil_comunitario_e_concluida_com_sucesso() {
		throw new PendingException();
	}

	@Entao("o perfil comunitário do usuário {string} passa a ter apelido {string}, cidade {string}, descrição {string}, foto {string} e preferências públicas {string}")
	public void o_perfil_comunitario_do_usuario_passa_a_ter_apelido_cidade_descricao_foto_e_preferencias_publicas(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("o usuário {string} inicia a edição do perfil com apelido {string} e cancela a alteração")
	public void o_usuario_inicia_a_edicao_do_perfil_com_apelido_e_cancela_a_alteracao(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o perfil comunitário do usuário {string} permanece com apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_perfil_comunitario_do_usuario_permanece_com_apelido_cidade_descricao_e_foto(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("o usuário {string} edita seu perfil comunitário para apelido {string}, cidade {string}, descrição {string} e foto {string}")
	public void o_usuario_edita_seu_perfil_comunitario_para_apelido_cidade_descricao_e_foto(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("o perfil comunitário do usuário {string} passa a ter apelido {string} e cidade {string}")
	public void o_perfil_comunitario_do_usuario_passa_a_ter_apelido_e_cidade(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta editar seu perfil comunitário para o apelido {string}")
	public void o_usuario_tenta_editar_seu_perfil_comunitario_para_o_apelido(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a edição do perfil comunitário é rejeitada porque o apelido {string} já está em uso")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_porque_o_apelido_ja_esta_em_uso(String string) {
		throw new PendingException();
	}

	@Entao("a edição do perfil comunitário é rejeitada porque o apelido é obrigatório")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_porque_o_apelido_e_obrigatorio() {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta editar o perfil comunitário do usuário {string} para o apelido {string}")
	public void o_usuario_tenta_editar_o_perfil_comunitario_do_usuario_para_o_apelido(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a edição do perfil comunitário é rejeitada por falta de autorização")
	public void a_edicao_do_perfil_comunitario_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Dado("que o usuário {string} possui a carta {string} na sua coleção")
	public void que_o_usuario_possui_a_carta_na_sua_colecao(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} fixa a carta {string} como destaque do seu perfil")
	public void o_usuario_fixa_a_carta_como_destaque_do_seu_perfil(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a carta {string} passa a constar nos destaques de cartas favoritas do perfil de {string}")
	public void a_carta_passa_a_constar_nos_destaques_de_cartas_favoritas_do_perfil_de(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} já possui {int} cartas fixadas nos destaques do seu perfil: {string}, {string}, {string} e {string}")
	public void que_o_usuario_ja_possui_cartas_fixadas_nos_destaques_do_seu_perfil_e(String string, Integer int1, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta fixar a carta {string} como destaque do seu perfil")
	public void o_usuario_tenta_fixar_a_carta_como_destaque_do_seu_perfil(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a fixação da carta nos destaques é rejeitada porque o limite de {int} destaques foi atingido")
	public void a_fixacao_da_carta_nos_destaques_e_rejeitada_porque_o_limite_de_destaques_foi_atingido(Integer int1) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} já possui a carta {string} fixada nos destaques do seu perfil")
	public void que_o_usuario_ja_possui_a_carta_fixada_nos_destaques_do_seu_perfil(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta fixar a carta {string} novamente nos destaques do seu perfil")
	public void o_usuario_tenta_fixar_a_carta_novamente_nos_destaques_do_seu_perfil(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a fixação da carta nos destaques é rejeitada porque a carta {string} já está nos destaques")
	public void a_fixacao_da_carta_nos_destaques_e_rejeitada_porque_a_carta_ja_esta_nos_destaques(String string) {
		throw new PendingException();
	}

	@Quando("o usuário {string} remove a carta {string} dos destaques do seu perfil")
	public void o_usuario_remove_a_carta_dos_destaques_do_seu_perfil(String string, String string2) {
		throw new PendingException();
	}

	@Quando("fixa a carta {string} como destaque do seu perfil")
	public void fixa_a_carta_como_destaque_do_seu_perfil(String string) {
		throw new PendingException();
	}

	@Entao("os destaques de cartas favoritas do perfil de {string} contêm {string}, {string}, {string} e {string}")
	public void os_destaques_de_cartas_favoritas_do_perfil_de_contem_e(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("o usuário {string} envia uma solicitação de amizade para o usuário {string}")
	public void o_usuario_envia_uma_solicitacao_de_amizade_para_o_usuario(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade de {string} para {string} fica com status pendente")
	public void a_solicitacao_de_amizade_de_para_fica_com_status_pendente(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta enviar uma solicitação de amizade para o usuário {string}")
	public void o_usuario_tenta_enviar_uma_solicitacao_de_amizade_para_o_usuario(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade é rejeitada porque não é possível adicionar a si mesmo como amigo")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_nao_e_possivel_adicionar_a_si_mesmo_como_amigo() {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade é rejeitada porque o usuário {string} não existe")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_o_usuario_nao_existe(String string) {
		throw new PendingException();
	}

	@Dado("que existe uma solicitação de amizade pendente de {string} para {string}")
	public void que_existe_uma_solicitacao_de_amizade_pendente_de_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade é rejeitada porque já existe uma solicitação pendente entre {string} e {string}")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_ja_existe_uma_solicitacao_pendente_entre_e(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que {string} e {string} já são amigos")
	public void que_e_ja_sao_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade é rejeitada porque {string} e {string} já são amigos")
	public void a_solicitacao_de_amizade_e_rejeitada_porque_e_ja_sao_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} aceita a solicitação de amizade enviada por {string}")
	public void o_usuario_aceita_a_solicitacao_de_amizade_enviada_por(String string, String string2) {
		throw new PendingException();
	}

	@Entao("{string} e {string} passam a ser amigos bilateralmente")
	public void e_passam_a_ser_amigos_bilateralmente(String string, String string2) {
		throw new PendingException();
	}

	@Entao("{string} consta na lista de amigos de {string}")
	public void consta_na_lista_de_amigos_de(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta aceitar a solicitação de amizade de {string} para {string}")
	public void o_usuario_tenta_aceitar_a_solicitacao_de_amizade_de_para(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a resposta à solicitação de amizade é rejeitada por falta de autorização")
	public void a_resposta_a_solicitacao_de_amizade_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Quando("o usuário {string} recusa a solicitação de amizade enviada por {string}")
	public void o_usuario_recusa_a_solicitacao_de_amizade_enviada_por(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade de {string} para {string} é encerrada sem criar amizade")
	public void a_solicitacao_de_amizade_de_para_e_encerrada_sem_criar_amizade(String string, String string2) {
		throw new PendingException();
	}

	@Entao("{string} e {string} não são amigos")
	public void e_nao_sao_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} cancela a solicitação de amizade enviada para {string}")
	public void o_usuario_cancela_a_solicitacao_de_amizade_enviada_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a solicitação de amizade pendente de {string} para {string} deixa de existir")
	public void a_solicitacao_de_amizade_pendente_de_para_deixa_de_existir(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta cancelar a solicitação de amizade de {string} para {string}")
	public void o_usuario_tenta_cancelar_a_solicitacao_de_amizade_de_para(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o cancelamento da solicitação de amizade é rejeitado por falta de autorização")
	public void o_cancelamento_da_solicitacao_de_amizade_e_rejeitado_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Quando("o usuário {string} remove {string} da sua lista de amigos")
	public void o_usuario_remove_da_sua_lista_de_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a amizade entre {string} e {string} é removida dos dois lados")
	public void a_amizade_entre_e_e_removida_dos_dois_lados(String string, String string2) {
		throw new PendingException();
	}

	@Entao("{string} não consta mais na lista de amigos de {string}")
	public void nao_consta_mais_na_lista_de_amigos_de(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta remover {string} da sua lista de amigos")
	public void o_usuario_tenta_remover_da_sua_lista_de_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a remoção da amizade é rejeitada porque {string} e {string} não são amigos")
	public void a_remocao_da_amizade_e_rejeitada_porque_e_nao_sao_amigos(String string, String string2) {
		throw new PendingException();
	}

}
