package com.cesar.tgchub.tcghub.dominio.adm.catalogo;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class GestaoDoCatalogoStepDefinitions {

	@Dado("que existe a coleção {string} com nome {string}")
	public void que_existe_a_colecao_com_nome(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe a carta {string} com nome {string} do tipo {string} na coleção {string}")
	public void que_existe_a_carta_com_nome_do_tipo_na_colecao(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu cadastro a coleção {string} com nome {string}")
	public void eu_cadastro_a_colecao_com_nome(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a coleção {string} é cadastrada com sucesso")
	public void a_colecao_e_cadastrada_com_sucesso(String string) {
		throw new PendingException();
	}

	@Quando("eu edito o nome da coleção {string} para {string}")
	public void eu_edito_o_nome_da_colecao_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a coleção {string} passa a ter o nome {string}")
	public void a_colecao_passa_a_ter_o_nome(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu listo as coleções do catálogo")
	public void eu_listo_as_colecoes_do_catalogo() {
		throw new PendingException();
	}

	@Entao("a listagem de coleções contém {string} e {string}")
	public void a_listagem_de_colecoes_contem_e(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existe a coleção {string} com nome {string} sem cartas nem produtos vinculados")
	public void que_existe_a_colecao_com_nome_sem_cartas_nem_produtos_vinculados(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu excluo a coleção {string}")
	public void eu_excluo_a_colecao(String string) {
		throw new PendingException();
	}

	@Entao("a coleção {string} é excluída com sucesso")
	public void a_colecao_e_excluida_com_sucesso(String string) {
		throw new PendingException();
	}

	@Entao("a coleção {string} deixa de existir no catálogo")
	public void a_colecao_deixa_de_existir_no_catalogo(String string) {
		throw new PendingException();
	}

	@Quando("eu tento excluir a coleção {string}")
	public void eu_tento_excluir_a_colecao(String string) {
		throw new PendingException();
	}

	@Entao("a exclusão da coleção é rejeitada porque a coleção {string} possui cartas ou produtos vinculados")
	public void a_exclusao_da_colecao_e_rejeitada_porque_a_colecao_possui_cartas_ou_produtos_vinculados(String string) {
		throw new PendingException();
	}

	@Entao("a coleção {string} continua a existir no catálogo")
	public void a_colecao_continua_a_existir_no_catalogo(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar a coleção {string} com nome {string}")
	public void eu_tento_cadastrar_a_colecao_com_nome(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a operação no catálogo é rejeitada por falta de autorização")
	public void a_operacao_no_catalogo_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Quando("eu cadastro a carta {string} com nome {string} do tipo {string}, cor {string} e raridade {string} na coleção {string}")
	public void eu_cadastro_a_carta_com_nome_do_tipo_cor_e_raridade_na_colecao(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("a carta {string} é cadastrada com sucesso")
	public void a_carta_e_cadastrada_com_sucesso(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar a carta {string} com nome {string} do tipo {string}, cor {string} e raridade {string} na coleção {string}")
	public void eu_tento_cadastrar_a_carta_com_nome_do_tipo_cor_e_raridade_na_colecao(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("o cadastro da carta é rejeitado porque já existe uma carta com o código {string}")
	public void o_cadastro_da_carta_e_rejeitado_porque_ja_existe_uma_carta_com_o_codigo(String string) {
		throw new PendingException();
	}

	@Quando("eu busco cartas pelo código {string}")
	public void eu_busco_cartas_pelo_codigo(String string) {
		throw new PendingException();
	}

	@Entao("a busca de cartas retorna apenas a carta {string}")
	public void a_busca_de_cartas_retorna_apenas_a_carta(String string) {
		throw new PendingException();
	}

	@Quando("eu busco cartas pelo nome {string}")
	public void eu_busco_cartas_pelo_nome(String string) {
		throw new PendingException();
	}

	@Quando("eu cadastro o deck {string} com o líder {string}, {string} cartas no deck principal com no máximo {string} cópias por carta e {string} cartas DON!!")
	public void eu_cadastro_o_deck_com_o_lider_cartas_no_deck_principal_com_no_maximo_copias_por_carta_e_cartas_don(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("o deck {string} é cadastrado com sucesso")
	public void o_deck_e_cadastrado_com_sucesso(String string) {
		throw new PendingException();
	}

	@Entao("a composição do deck {string} soma {string} cartas, {string} líder e {string} cartas DON!!")
	public void a_composicao_do_deck_soma_cartas_lider_e_cartas_don(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar o deck {string} sem líder, com {string} cartas no deck principal e {string} cartas DON!!")
	public void eu_tento_cadastrar_o_deck_sem_lider_com_cartas_no_deck_principal_e_cartas_don(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o cadastro do deck é rejeitado porque o deck precisa de exatamente {string} líder")
	public void o_cadastro_do_deck_e_rejeitado_porque_o_deck_precisa_de_exatamente_lider(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar o deck {string} com o líder {string}, {string} cartas no deck principal e {string} cartas DON!!")
	public void eu_tento_cadastrar_o_deck_com_o_lider_cartas_no_deck_principal_e_cartas_don(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o cadastro do deck é rejeitado porque o deck principal deve ter exatamente {string} cartas")
	public void o_cadastro_do_deck_e_rejeitado_porque_o_deck_principal_deve_ter_exatamente_cartas(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar o deck {string} com o líder {string}, {string} cartas no deck principal incluindo {string} cópias da carta {string} e {string} cartas DON!!")
	public void eu_tento_cadastrar_o_deck_com_o_lider_cartas_no_deck_principal_incluindo_copias_da_carta_e_cartas_don(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("o cadastro do deck é rejeitado porque a carta {string} excede o limite de {string} cópias")
	public void o_cadastro_do_deck_e_rejeitado_porque_a_carta_excede_o_limite_de_copias(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o cadastro do deck é rejeitado porque o deck deve ter exatamente {string} cartas DON!!")
	public void o_cadastro_do_deck_e_rejeitado_porque_o_deck_deve_ter_exatamente_cartas_don(String string) {
		throw new PendingException();
	}

	@Quando("eu cadastro o produto {string} da categoria {string} vinculado à carta {string}, preço {string}, preço mínimo de segurança {string} e estoque {string}")
	public void eu_cadastro_o_produto_da_categoria_vinculado_a_carta_preco_preco_minimo_de_seguranca_e_estoque(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("eu cadastro o produto {string} da categoria {string} vinculado à coleção {string}, preço {string}, preço mínimo de segurança {string} e estoque {string}")
	public void eu_cadastro_o_produto_da_categoria_vinculado_a_colecao_preco_preco_minimo_de_seguranca_e_estoque(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("eu cadastro o produto {string} da categoria {string} vinculado ao deck {string}, preço {string}, preço mínimo de segurança {string} e estoque {string}")
	public void eu_cadastro_o_produto_da_categoria_vinculado_ao_deck_preco_preco_minimo_de_seguranca_e_estoque(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("os produtos {string}, {string} e {string} são cadastrados com visibilidade {string}")
	public void os_produtos_e_sao_cadastrados_com_visibilidade(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("cada cadastro gera um ajuste de estoque do tipo {string} com a quantidade inicial")
	public void cada_cadastro_gera_um_ajuste_de_estoque_do_tipo_com_a_quantidade_inicial(String string) {
		throw new PendingException();
	}

	@Dado("que existe o produto {string} com preço {string}, preço mínimo de segurança {string}, estoque {string} e visibilidade {string}")
	public void que_existe_o_produto_com_preco_preco_minimo_de_seguranca_estoque_e_visibilidade(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("eu edito a descrição do produto {string} para {string}")
	public void eu_edito_a_descricao_do_produto_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter a descrição {string}")
	public void o_produto_passa_a_ter_a_descricao(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que existem produtos das categorias {string}, {string} e {string} com visibilidades {string}, {string} e {string}")
	public void que_existem_produtos_das_categorias_e_com_visibilidades_e(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("eu listo os produtos filtrando pela categoria {string}, coleção {string} e visibilidade {string}")
	public void eu_listo_os_produtos_filtrando_pela_categoria_colecao_e_visibilidade(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a listagem de produtos retorna apenas os produtos compatíveis com todos os filtros")
	public void a_listagem_de_produtos_retorna_apenas_os_produtos_compativeis_com_todos_os_filtros() {
		throw new PendingException();
	}

	@Quando("eu altero o preço do produto {string} para {string}")
	public void eu_altero_o_preco_do_produto_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter o preço {string}")
	public void o_produto_passa_a_ter_o_preco(String string, String string2) {
		throw new PendingException();
	}

	@Entao("é gravado um registro de histórico de preço do produto {string} de {string} para {string} com o administrador responsável e a data da alteração")
	public void e_gravado_um_registro_de_historico_de_preco_do_produto_de_para_com_o_administrador_responsavel_e_a_data_da_alteracao(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("é disparado o evento {string} para o produto {string}")
	public void e_disparado_o_evento_para_o_produto(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o produto {string} teve o preço alterado de {string} para {string} e depois de {string} para {string}")
	public void que_o_produto_teve_o_preco_alterado_de_para_e_depois_de_para(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Quando("eu consulto o histórico de preços do produto {string}")
	public void eu_consulto_o_historico_de_precos_do_produto(String string) {
		throw new PendingException();
	}

	@Entao("o histórico de preços exibe {string} registros ordenados da alteração mais recente para a mais antiga")
	public void o_historico_de_precos_exibe_registros_ordenados_da_alteracao_mais_recente_para_a_mais_antiga(String string) {
		throw new PendingException();
	}

	@Quando("eu tento cadastrar o produto {string} da categoria {string} com preço {string}, preço mínimo de segurança {string} e estoque {string}")
	public void eu_tento_cadastrar_o_produto_da_categoria_com_preco_preco_minimo_de_seguranca_e_estoque(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Entao("a gravação do produto é rejeitada porque o preço deve ser maior que zero")
	public void a_gravacao_do_produto_e_rejeitada_porque_o_preco_deve_ser_maior_que_zero() {
		throw new PendingException();
	}

	@Quando("eu tento alterar o preço do produto {string} para {string}")
	public void eu_tento_alterar_o_preco_do_produto_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a gravação do produto é rejeitada porque o preço não pode ser menor que o preço mínimo de segurança {string}")
	public void a_gravacao_do_produto_e_rejeitada_porque_o_preco_nao_pode_ser_menor_que_o_preco_minimo_de_seguranca(String string) {
		throw new PendingException();
	}

	@Entao("nenhum registro de histórico de preço é gravado para o produto {string}")
	public void nenhum_registro_de_historico_de_preco_e_gravado_para_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("a gravação do produto é rejeitada porque o estoque não pode ser negativo")
	public void a_gravacao_do_produto_e_rejeitada_porque_o_estoque_nao_pode_ser_negativo() {
		throw new PendingException();
	}

	@Quando("eu cadastro o produto {string} da categoria {string} com preço {string}, preço mínimo de segurança {string}, estoque {string} e visibilidade {string}")
	public void eu_cadastro_o_produto_da_categoria_com_preco_preco_minimo_de_seguranca_estoque_e_visibilidade(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Entao("o produto {string} é cadastrado com visibilidade {string}")
	public void o_produto_e_cadastrado_com_visibilidade(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o produto {string} nunca foi vendido")
	public void que_o_produto_nunca_foi_vendido(String string) {
		throw new PendingException();
	}

	@Quando("eu excluo o produto {string}")
	public void eu_excluo_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("o produto {string} é excluído do catálogo")
	public void o_produto_e_excluido_do_catalogo(String string) {
		throw new PendingException();
	}

	@Dado("que o produto {string} já consta em itens de pedido")
	public void que_o_produto_ja_consta_em_itens_de_pedido(String string) {
		throw new PendingException();
	}

	@Quando("eu tento excluir o produto {string}")
	public void eu_tento_excluir_o_produto(String string) {
		throw new PendingException();
	}

	@Entao("a exclusão física do produto é bloqueada porque o produto {string} já foi vendido")
	public void a_exclusao_fisica_do_produto_e_bloqueada_porque_o_produto_ja_foi_vendido(String string) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter visibilidade {string}")
	public void o_produto_passa_a_ter_visibilidade(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o produto {string} continua registrado no banco, mas indisponível para compra")
	public void o_produto_continua_registrado_no_banco_mas_indisponivel_para_compra(String string) {
		throw new PendingException();
	}

	@Quando("eu oculto o produto {string}")
	public void eu_oculto_o_produto(String string) {
		throw new PendingException();
	}

	@Quando("eu reexibo o produto {string}")
	public void eu_reexibo_o_produto(String string) {
		throw new PendingException();
	}

	@Quando("eu esgoto o produto {string} com o motivo {string} e Avise-me {string}")
	public void eu_esgoto_o_produto_com_o_motivo_e_avise_me(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter estoque {string} e visibilidade {string}")
	public void o_produto_passa_a_ter_estoque_e_visibilidade(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("é gerado um ajuste de estoque do tipo {string} com o motivo {string}, quantidade anterior {string} e um código de auditoria único")
	public void e_gerado_um_ajuste_de_estoque_do_tipo_com_o_motivo_quantidade_anterior_e_um_codigo_de_auditoria_unico(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o botão Avise-me do produto {string} fica {string}")
	public void o_botao_avise_me_do_produto_fica(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu tento esgotar o produto {string} sem informar o motivo")
	public void eu_tento_esgotar_o_produto_sem_informar_o_motivo(String string) {
		throw new PendingException();
	}

	@Entao("o esgotamento é rejeitado porque o motivo é obrigatório")
	public void o_esgotamento_e_rejeitado_porque_o_motivo_e_obrigatorio() {
		throw new PendingException();
	}

	@Entao("o produto {string} continua com estoque {string} e visibilidade {string}")
	public void o_produto_continua_com_estoque_e_visibilidade(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o pedido {string} com {string} unidades do produto {string} é pago")
	public void o_pedido_com_unidades_do_produto_e_pago(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o produto {string} passa a ter estoque {string}")
	public void o_produto_passa_a_ter_estoque(String string, String string2) {
		throw new PendingException();
	}

	@Entao("é gerado um ajuste de estoque do tipo {string} de {string} unidades referente ao pedido {string}")
	public void e_gerado_um_ajuste_de_estoque_do_tipo_de_unidades_referente_ao_pedido(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o pedido {string} com {string} unidades do produto {string} foi pago")
	public void que_o_pedido_com_unidades_do_produto_foi_pago(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("o pedido {string} sofre {string}")
	public void o_pedido_sofre(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu reponho {string} unidades do produto {string}")
	public void eu_reponho_unidades_do_produto(String string, String string2) {
		throw new PendingException();
	}

	@Entao("é gerado um ajuste de estoque do tipo {string} de {string} unidades")
	public void e_gerado_um_ajuste_de_estoque_do_tipo_de_unidades(String string, String string2) {
		throw new PendingException();
	}
}
