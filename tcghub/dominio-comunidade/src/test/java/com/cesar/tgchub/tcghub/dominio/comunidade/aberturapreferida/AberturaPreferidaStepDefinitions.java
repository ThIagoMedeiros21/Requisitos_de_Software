package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class AberturaPreferidaStepDefinitions {

	@Dado("que o usuário {string} ainda não escolheu uma abertura preferida")
	public void que_o_usuario_ainda_nao_escolheu_uma_abertura_preferida(String string) {
		throw new PendingException();
	}

	@Dado("que existem as aberturas ativas número {int} {string} e número {int} {string}")
	public void que_existem_as_aberturas_ativas_numero_e_numero(Integer int1, String string, Integer int2, String string2) {
		throw new PendingException();
	}

	@Dado("que existe a abertura inativa número {int} {string}")
	public void que_existe_a_abertura_inativa_numero(Integer int1, String string) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} é amigo de {string} e escolheu a abertura número {int}")
	public void que_o_usuario_e_amigo_de_e_escolheu_a_abertura_numero(String string, String string2, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta as aberturas disponíveis para escolha")
	public void o_usuario_consulta_as_aberturas_disponiveis_para_escolha(String string) {
		throw new PendingException();
	}

	@Entao("a listagem de escolha exibe as aberturas número {int} com {int} amigo e número {int} com {int} amigos")
	public void a_listagem_de_escolha_exibe_as_aberturas_numero_com_amigo_e_numero_com_amigos(Integer int1, Integer int2, Integer int3, Integer int4) {
		throw new PendingException();
	}

	@Entao("a abertura inativa número {int} não aparece na listagem para novas escolhas")
	public void a_abertura_inativa_numero_nao_aparece_na_listagem_para_novas_escolhas(Integer int1) {
		throw new PendingException();
	}

	@Dado("que existem as aberturas ativas número {int} {string} de {string} e número {int} {string} de {string}")
	public void que_existem_as_aberturas_ativas_numero_de_e_numero_de(Integer int1, String string, String string2, Integer int2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o usuário {string} pesquisa aberturas disponíveis pelo termo {string}")
	public void o_usuario_pesquisa_aberturas_disponiveis_pelo_termo(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a pesquisa retorna apenas a abertura número {int} {string}")
	public void a_pesquisa_retorna_apenas_a_abertura_numero(Integer int1, String string) {
		throw new PendingException();
	}

	@Dado("que existe a abertura ativa número {int} {string} de {string} exibida nos episódios {string}")
	public void que_existe_a_abertura_ativa_numero_de_exibida_nos_episodios(Integer int1, String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o usuário {string} escolhe a abertura número {int} como sua abertura preferida")
	public void o_usuario_escolhe_a_abertura_numero_como_sua_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura preferida do usuário {string} passa a ser a abertura número {int}")
	public void a_abertura_preferida_do_usuario_passa_a_ser_a_abertura_numero(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} é exibida no perfil público de {string}")
	public void a_abertura_numero_e_exibida_no_perfil_publico_de(Integer int1, String string) {
		throw new PendingException();
	}

	@Quando("o usuário {string} tenta escolher a abertura número {int} como sua abertura preferida")
	public void o_usuario_tenta_escolher_a_abertura_numero_como_sua_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a escolha da abertura preferida é rejeitada porque a abertura número {int} está inativa")
	public void a_escolha_da_abertura_preferida_e_rejeitada_porque_a_abertura_numero_esta_inativa(Integer int1) {
		throw new PendingException();
	}

	@Entao("a escolha da abertura preferida é rejeitada porque a abertura número {int} não existe")
	public void a_escolha_da_abertura_preferida_e_rejeitada_porque_a_abertura_numero_nao_existe(Integer int1) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} já escolheu a abertura número {int} como sua abertura preferida")
	public void que_o_usuario_ja_escolheu_a_abertura_numero_como_sua_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} escolhe a abertura número {int} como sua nova abertura preferida")
	public void o_usuario_escolhe_a_abertura_numero_como_sua_nova_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("o usuário {string} mantém apenas {int} abertura preferida cadastrada")
	public void o_usuario_mantem_apenas_abertura_preferida_cadastrada(String string, Integer int1) {
		throw new PendingException();
	}

	@Dado("que existe a abertura ativa número {int} {string}")
	public void que_existe_a_abertura_ativa_numero(Integer int1, String string) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} como sua abertura preferida")
	public void que_o_usuario_escolheu_a_abertura_numero_como_sua_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} oculta a abertura preferida do seu perfil público")
	public void o_usuario_oculta_a_abertura_preferida_do_seu_perfil_publico(String string) {
		throw new PendingException();
	}

	@Entao("o visitante {string} não visualiza a abertura preferida no perfil público de {string}")
	public void o_visitante_nao_visualiza_a_abertura_preferida_no_perfil_publico_de(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o usuário {string} mantém a abertura número {int} salva como sua abertura preferida")
	public void o_usuario_mantem_a_abertura_numero_salva_como_sua_abertura_preferida(String string, Integer int1) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} com exibição oculta no perfil público")
	public void que_o_usuario_escolheu_a_abertura_numero_com_exibicao_oculta_no_perfil_publico(String string, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} reexibe a abertura preferida no seu perfil público")
	public void o_usuario_reexibe_a_abertura_preferida_no_seu_perfil_publico(String string) {
		throw new PendingException();
	}

	@Entao("o visitante {string} passa a visualizar a abertura número {int} no perfil público de {string}")
	public void o_visitante_passa_a_visualizar_a_abertura_numero_no_perfil_publico_de(String string, Integer int1, String string2) {
		throw new PendingException();
	}

	@Dado("que existem as aberturas ativas número {int} {string}, número {int} {string}, número {int} {string}, número {int} {string}, número {int} {string} e número {int} {string}")
	public void que_existem_as_aberturas_ativas_numero_numero_numero_numero_numero_e_numero(Integer int1, String string, Integer int2, String string2, Integer int3, String string3, Integer int4, String string4, Integer int5, String string5, Integer int6, String string6) {
		throw new PendingException();
	}

	@Dado("que {int} usuários escolheram a abertura número {int}, sendo {int} deles com exibição oculta no perfil público")
	public void que_usuarios_escolheram_a_abertura_numero_sendo_deles_com_exibicao_oculta_no_perfil_publico(Integer int1, Integer int2, Integer int3) {
		throw new PendingException();
	}

	@Dado("que {int} usuários escolheram a abertura número {int}")
	public void que_usuarios_escolheram_a_abertura_numero(Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Dado("que {int} usuário escolheu a abertura número {int}")
	public void que_usuario_escolheu_a_abertura_numero(Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta o ranking da comunidade de aberturas preferidas")
	public void o_usuario_consulta_o_ranking_da_comunidade_de_aberturas_preferidas(String string) {
		throw new PendingException();
	}

	@Entao("o ranking exibe exatamente as {int} aberturas mais escolhidas sobre o total de {int} escolhas")
	public void o_ranking_exibe_exatamente_as_aberturas_mais_escolhidas_sobre_o_total_de_escolhas(Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Entao("a 1ª posição do ranking é a abertura número {int} com {int} escolhas e {double}% do total, sem expor o nome do perfil que a ocultou")
	public void a_1a_posicao_do_ranking_e_a_abertura_numero_com_escolhas_e_do_total_sem_expor_o_nome_do_perfil_que_a_ocultou(Integer int1, Integer int2, Double double1) {
		throw new PendingException();
	}

	@Entao("a 2ª posição do ranking é a abertura número {int} com {int} escolhas e {double}% do total")
	public void a_2a_posicao_do_ranking_e_a_abertura_numero_com_escolhas_e_do_total(Integer int1, Integer int2, Double double1) {
		throw new PendingException();
	}

	@Entao("o ranking retorna vazio com total de {int} escolhas")
	public void o_ranking_retorna_vazio_com_total_de_escolhas(Integer int1) {
		throw new PendingException();
	}

	@Dado("que {string} e {string} são amigos")
	public void que_e_sao_amigos(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} escolheu a abertura número {int} com exibição pública")
	public void que_o_usuario_escolheu_a_abertura_numero_com_exibicao_publica(String string, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta o perfil do amigo {string}")
	public void o_usuario_consulta_o_perfil_do_amigo(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o sistema indica que {string} possui a mesma abertura preferida que {string}")
	public void o_sistema_indica_que_possui_a_mesma_abertura_preferida_que(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} escolheu a abertura número {int}")
	public void que_o_usuario_escolheu_a_abertura_numero(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("o sistema indica que {string} não possui a mesma abertura preferida que {string}")
	public void o_sistema_indica_que_nao_possui_a_mesma_abertura_preferida_que(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o sistema não exibe a abertura preferida oculta de {string} nem indica correspondência pública")
	public void o_sistema_nao_exibe_a_abertura_preferida_oculta_de_nem_indica_correspondencia_publica(String string) {
		throw new PendingException();
	}

	@Dado("que {string} é amigo de {string} e {string}, e ambos também escolheram a abertura número {int}")
	public void que_e_amigo_de_e_e_ambos_tambem_escolheram_a_abertura_numero(String string, String string2, String string3, Integer int1) {
		throw new PendingException();
	}

	@Quando("o usuário {string} consulta os detalhes da abertura número {int}")
	public void o_usuario_consulta_os_detalhes_da_abertura_numero(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("os detalhes exibem artista {string}, episódios {string}, posição {int} no ranking e {int} amigos com a mesma abertura preferida")
	public void os_detalhes_exibem_artista_episodios_posicao_no_ranking_e_amigos_com_a_mesma_abertura_preferida(String string, String string2, Integer int1, Integer int2) {
		throw new PendingException();
	}

	@Quando("a abertura número {int} é inativada administrativamente")
	public void a_abertura_numero_e_inativada_administrativamente(Integer int1) {
		throw new PendingException();
	}

	@Entao("o usuário {string} mantém a abertura número {int} como sua abertura preferida no perfil")
	public void o_usuario_mantem_a_abertura_numero_como_sua_abertura_preferida_no_perfil(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a abertura número {int} deixa de aparecer na listagem de novas escolhas para {string}")
	public void a_abertura_numero_deixa_de_aparecer_na_listagem_de_novas_escolhas_para(Integer int1, String string) {
		throw new PendingException();
	}

	@Entao("a escolha da abertura preferida para {string} é rejeitada porque a abertura número {int} está inativa")
	public void a_escolha_da_abertura_preferida_para_e_rejeitada_porque_a_abertura_numero_esta_inativa(String string, Integer int1) {
		throw new PendingException();
	}

	@Dado("que existe a abertura inativa número {int} {string} que já havia sido escolhida por {string}")
	public void que_existe_a_abertura_inativa_numero_que_ja_havia_sido_escolhida_por(Integer int1, String string, String string2) {
		throw new PendingException();
	}

}
