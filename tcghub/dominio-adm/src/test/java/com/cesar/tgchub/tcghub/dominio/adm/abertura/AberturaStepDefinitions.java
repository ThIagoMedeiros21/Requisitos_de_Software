package com.cesar.tgchub.tcghub.dominio.adm.abertura;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AberturaStepDefinitions {

	@Dado("que não existe nenhuma abertura cadastrada")
	public void que_nao_existe_nenhuma_abertura_cadastrada() {
		throw new PendingException();
	}

	@Dado("que sou um usuário administrador")
	public void que_sou_um_usuario_administrador() {
		throw new PendingException();
	}

	@Quando("eu cadastro a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status ativa")
	public void eu_cadastro_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_ativa(Integer int1, String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} é cadastrada com sucesso")
	public void a_abertura_numero_e_cadastrada_com_sucesso(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} passa a existir com status ativa")
	public void a_abertura_numero_passa_a_existir_com_status_ativa(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu cadastro a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status inativa")
	public void eu_cadastro_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_inativa(Integer int1, String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} passa a existir com status inativa")
	public void a_abertura_numero_passa_a_existir_com_status_inativa(Integer int1) {
		throw new PendingException();
	}

	@Dado("que sou um usuário sem autorização de administrador")
	public void que_sou_um_usuario_sem_autorizacao_de_administrador() {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar a abertura número {int}, nome {string}, artista {string}, episódios {string}, imagem {string}, vídeo {string} e status ativa")
	public void eu_tento_cadastrar_a_abertura_numero_nome_artista_episodios_imagem_video_e_status_ativa(Integer int1, String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("o cadastro da abertura é rejeitado por falta de autorização")
	public void o_cadastro_da_abertura_e_rejeitado_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Dado("que existe a abertura número {int}, nome {string}, artista {string}, episódios {string} e status ativa")
	public void que_existe_a_abertura_numero_nome_artista_episodios_e_status_ativa(Integer int1, String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o cadastro da abertura é rejeitado porque já existe uma abertura com o número {int}")
	public void o_cadastro_da_abertura_e_rejeitado_porque_ja_existe_uma_abertura_com_o_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("o cadastro da abertura é rejeitado por dados obrigatórios inválidos")
	public void o_cadastro_da_abertura_e_rejeitado_por_dados_obrigatorios_invalidos() {
		throw new PendingException();
	}

	@Quando("eu consulto a abertura número {int}")
	public void eu_consulto_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura encontrada tem nome {string}, artista {string}, episódios {string} e status ativa")
	public void a_abertura_encontrada_tem_nome_artista_episodios_e_status_ativa(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento consultar a abertura número {int}")
	public void eu_tento_consultar_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a consulta da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_consulta_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer int1) {
		throw new PendingException();
	}

	@Dado("que existe a abertura número {int}, nome {string}, artista {string}, episódios {string} e status inativa")
	public void que_existe_a_abertura_numero_nome_artista_episodios_e_status_inativa(Integer int1, String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu filtro as aberturas administrativas pelo status inativa")
	public void eu_filtro_as_aberturas_administrativas_pelo_status_inativa() {
		throw new PendingException();
	}

	@Entao("a listagem administrativa retorna apenas a abertura número {int}")
	public void a_listagem_administrativa_retorna_apenas_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu edito a abertura número {int} para nome {string}, artista {string}, episódios {string} e status ativa")
	public void eu_edito_a_abertura_numero_para_nome_artista_episodios_e_status_ativa(Integer int1, String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a edição da abertura é concluída com sucesso")
	public void a_edicao_da_abertura_e_concluida_com_sucesso() {
		throw new PendingException();
	}

	@Entao("a abertura número {int} passa a ter nome {string} e episódios {string}")
	public void a_abertura_numero_passa_a_ter_nome_e_episodios(Integer int1, String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu tento editar a abertura número {int} para nome {string}, artista {string}, episódios {string} e status ativa")
	public void eu_tento_editar_a_abertura_numero_para_nome_artista_episodios_e_status_ativa(Integer int1, String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a edição da abertura é rejeitada por falta de autorização")
	public void a_edicao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Entao("a edição da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_edicao_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu tento alterar o número da abertura {int} para o número {int}")
	public void eu_tento_alterar_o_numero_da_abertura_para_o_numero(Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Entao("a edição da abertura é rejeitada porque já existe uma abertura com o número {int}")
	public void a_edicao_da_abertura_e_rejeitada_porque_ja_existe_uma_abertura_com_o_numero(Integer int1) {
		throw new PendingException();
	}

	@Dado("que a abertura número {int} já foi escolhida como preferida por usuários")
	public void que_a_abertura_numero_ja_foi_escolhida_como_preferida_por_usuarios(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu inativo a abertura número {int}")
	public void eu_inativo_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} é inativada com sucesso")
	public void a_abertura_numero_e_inativada_com_sucesso(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} passa a ter status inativa")
	public void a_abertura_numero_passa_a_ter_status_inativa(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu reativo a abertura número {int}")
	public void eu_reativo_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} é reativada com sucesso")
	public void a_abertura_numero_e_reativada_com_sucesso(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} passa a ter status ativa")
	public void a_abertura_numero_passa_a_ter_status_ativa(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu tento inativar a abertura número {int}")
	public void eu_tento_inativar_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a inativação da abertura é rejeitada por falta de autorização")
	public void a_inativacao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Entao("a inativação da abertura é rejeitada porque não existe abertura com o número {int}")
	public void a_inativacao_da_abertura_e_rejeitada_porque_nao_existe_abertura_com_o_numero(Integer int1) {
		throw new PendingException();
	}

	@Dado("que a abertura número {int} nunca foi escolhida como preferida por nenhum usuário")
	public void que_a_abertura_numero_nunca_foi_escolhida_como_preferida_por_nenhum_usuario(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu excluo a abertura número {int}")
	public void eu_excluo_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} é excluída com sucesso")
	public void a_abertura_numero_e_excluida_com_sucesso(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} deixa de existir")
	public void a_abertura_numero_deixa_de_existir(Integer int1) {
		throw new PendingException();
	}

	@Quando("eu tento excluir a abertura número {int}")
	public void eu_tento_excluir_a_abertura_numero(Integer int1) {
		throw new PendingException();
	}

	@Entao("a exclusão da abertura é rejeitada porque a abertura número {int} já foi escolhida por usuários e só pode ser inativada")
	public void a_exclusao_da_abertura_e_rejeitada_porque_a_abertura_numero_ja_foi_escolhida_por_usuarios_e_so_pode_ser_inativada(Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} continua a existir")
	public void a_abertura_numero_continua_a_existir(Integer int1) {
		throw new PendingException();
	}

	@Entao("a exclusão da abertura é rejeitada por falta de autorização")
	public void a_exclusao_da_abertura_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

}
