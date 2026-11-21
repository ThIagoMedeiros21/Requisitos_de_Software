package com.cesar.tgchub.tcghub.dominio.loja.wishlist;

import io.cucumber.java.PendingException;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public final class WishlistStepDefinitions {

	@Dado("que existem cartas catalogadas com suas variantes comerciais de produto e estoque")
	public void que_existem_cartas_catalogadas_com_suas_variantes_comerciais_de_produto_e_estoque() {
		throw new PendingException();
	}

	@Dado("que sou o usuário {string} autenticado")
	public void que_sou_o_usuario_autenticado(String string) {
		throw new PendingException();
	}

	@Dado("que existe a carta {string} no catálogo")
	public void que_existe_a_carta_no_catalogo(String string) {
		throw new PendingException();
	}

	@Quando("eu adiciono {string} à minha wishlist com condição {string}, quantidade desejada {string}, preço-alvo {string}, alerta de preço ativo e alerta de reposição ativo")
	public void eu_adiciono_a_minha_wishlist_com_condicao_quantidade_desejada_preco_alvo_alerta_de_preco_ativo_e_alerta_de_reposicao_ativo(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o item {string} na condição {string} é cadastrado na wishlist com quantidade desejada {string}")
	public void o_item_na_condicao_e_cadastrado_na_wishlist_com_quantidade_desejada(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu consulto minha wishlist")
	public void eu_consulto_minha_wishlist() {
		throw new PendingException();
	}

	@Entao("o item {string} é exibido com condição {string}, quantidade desejada {string} e quantidade restante {string}")
	public void o_item_e_exibido_com_condicao_quantidade_desejada_e_quantidade_restante(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu edito o item {string} na condição {string} para quantidade desejada {string} e preço-alvo {string}")
	public void eu_edito_o_item_na_condicao_para_quantidade_desejada_e_preco_alvo(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Entao("o item passa a ter quantidade desejada {string} e preço-alvo {string}")
	public void o_item_passa_a_ter_quantidade_desejada_e_preco_alvo(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu removo o item {string} na condição {string} da minha wishlist")
	public void eu_removo_o_item_na_condicao_da_minha_wishlist(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o item deixa de constar na minha wishlist ativa")
	public void o_item_deixa_de_constar_na_minha_wishlist_ativa() {
		throw new PendingException();
	}

	@Dado("que minha wishlist já contém a carta {string} na condição {string}")
	public void que_minha_wishlist_ja_contem_a_carta_na_condicao(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu tento adicionar novamente {string} na condição {string}")
	public void eu_tento_adicionar_novamente_na_condicao(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a inclusão é rejeitada por duplicidade de carta e condição na wishlist")
	public void a_inclusao_e_rejeitada_por_duplicidade_de_carta_e_condicao_na_wishlist() {
		throw new PendingException();
	}

	@Quando("eu adiciono {string} na condição {string} com quantidade desejada {string}")
	public void eu_adiciono_na_condicao_com_quantidade_desejada(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a wishlist passa a conter {string} em {string} e {string} em {string}")
	public void a_wishlist_passa_a_conter_em_e_em(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("eu tento adicionar {string} na condição {string} com quantidade desejada {string}")
	public void eu_tento_adicionar_na_condicao_com_quantidade_desejada(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a inclusão é rejeitada porque a quantidade desejada deve ser positiva")
	public void a_inclusao_e_rejeitada_porque_a_quantidade_desejada_deve_ser_positiva() {
		throw new PendingException();
	}

	@Dado("que sou o usuário {string} autenticado com a wishlist contendo {string}")
	public void que_sou_o_usuario_autenticado_com_a_wishlist_contendo(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o usuário {string} tenta acessar a wishlist de {string} sem token")
	public void que_o_usuario_tenta_acessar_a_wishlist_de_sem_token(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o acesso de {string} é rejeitado porque a wishlist é privada por padrão")
	public void o_acesso_de_e_rejeitado_porque_a_wishlist_e_privada_por_padrao(String string) {
		throw new PendingException();
	}

	@Quando("eu habilito o compartilhamento da minha wishlist")
	public void eu_habilito_o_compartilhamento_da_minha_wishlist() {
		throw new PendingException();
	}

	@Entao("é gerado um token público único {string} associado à wishlist")
	public void e_gerado_um_token_publico_unico_associado_a_wishlist(String string) {
		throw new PendingException();
	}

	@Quando("{string} acessa a wishlist usando o token {string}")
	public void acessa_a_wishlist_usando_o_token(String string, String string2) {
		throw new PendingException();
	}

	@Entao("{string} visualiza os itens desejados da wishlist")
	public void visualiza_os_itens_desejados_da_wishlist(String string) {
		throw new PendingException();
	}

	@Quando("{string} tenta adicionar ou remover um item na wishlist de {string}")
	public void tenta_adicionar_ou_remover_um_item_na_wishlist_de(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a alteração é rejeitada por falta de autorização")
	public void a_alteracao_e_rejeitada_por_falta_de_autorizacao() {
		throw new PendingException();
	}

	@Quando("eu desabilito o compartilhamento da minha wishlist")
	public void eu_desabilito_o_compartilhamento_da_minha_wishlist() {
		throw new PendingException();
	}

	@Entao("o token {string} é revogado")
	public void o_token_e_revogado(String string) {
		throw new PendingException();
	}

	@Quando("{string} tenta acessar novamente a wishlist com o token {string}")
	public void tenta_acessar_novamente_a_wishlist_com_o_token(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o acesso é rejeitado porque o compartilhamento foi revogado")
	public void o_acesso_e_rejeitado_porque_o_compartilhamento_foi_revogado() {
		throw new PendingException();
	}

	@Dado("que a wishlist compartilhada de {string} possui a carta {string} na condição desejada {string} e quantidade {string}")
	public void que_a_wishlist_compartilhada_de_possui_a_carta_na_condicao_desejada_e_quantidade(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que existe apenas uma variante ativa de {string} na condição {string} com estoque {string} e nenhuma na condição {string} com estoque disponível")
	public void que_existe_apenas_uma_variante_ativa_de_na_condicao_com_estoque_e_nenhuma_na_condicao_com_estoque_disponivel(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o visitante {string} visualiza a wishlist compartilhada pelo token válido")
	public void o_visitante_visualiza_a_wishlist_compartilhada_pelo_token_valido(String string) {
		throw new PendingException();
	}

	@Entao("o item {string} em condição {string} é exibido como indisponível")
	public void o_item_em_condicao_e_exibido_como_indisponivel(String string, String string2) {
		throw new PendingException();
	}

	@Quando("{string} tenta iniciar a compra de presente para o item {string}")
	public void tenta_iniciar_a_compra_de_presente_para_o_item(String string, String string2) {
		throw new PendingException();
	}

	@Entao("a compra é rejeitada porque não há variante ativa compatível com a condição desejada e com estoque disponível")
	public void a_compra_e_rejeitada_porque_nao_ha_variante_ativa_compativel_com_a_condicao_desejada_e_com_estoque_disponivel() {
		throw new PendingException();
	}

	@Dado("que minha wishlist possui {string} na condição {string}, preço-alvo {string} e alerta de preço ativado")
	public void que_minha_wishlist_possui_na_condicao_preco_alvo_e_alerta_de_preco_ativado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que as variantes compatíveis ativas e com estoque de {string} em {string} custam {string} e {string}")
	public void que_as_variantes_compativeis_ativas_e_com_estoque_de_em_custam_e(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Quando("o monitoramento verifica os preços disponíveis")
	public void o_monitoramento_verifica_os_precos_disponiveis() {
		throw new PendingException();
	}

	@Entao("nenhuma notificação de preço é gerada porque o menor preço compatível é {string}")
	public void nenhuma_notificacao_de_preco_e_gerada_porque_o_menor_preco_compativel_e(String string) {
		throw new PendingException();
	}

	@Quando("uma das variantes compatíveis tem seu preço reduzido para {string}")
	public void uma_das_variantes_compativeis_tem_seu_preco_reduzido_para(String string) {
		throw new PendingException();
	}

	@Entao("é gerada uma única notificação de alerta de preço para {string} informando o menor preço {string}")
	public void e_gerada_uma_unica_notificacao_de_alerta_de_preco_para_informando_o_menor_preco(String string, String string2) {
		throw new PendingException();
	}

	@Quando("outra variante compatível também reduz o preço para {string} enquanto a condição já estava atendida")
	public void outra_variante_compativel_tambem_reduz_o_preco_para_enquanto_a_condicao_ja_estava_atendida(String string) {
		throw new PendingException();
	}

	@Entao("nenhuma notificação repetida de preço é gerada")
	public void nenhuma_notificacao_repetida_de_preco_e_gerada() {
		throw new PendingException();
	}

	@Quando("os preços voltam a subir para {string} e depois caem novamente para {string}")
	public void os_precos_voltam_a_subir_para_e_depois_caem_novamente_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("uma nova notificação de preço é gerada na nova transição para condição atendida")
	public void uma_nova_notificacao_de_preco_e_gerada_na_nova_transicao_para_condicao_atendida() {
		throw new PendingException();
	}

	@Dado("que minha wishlist possui {string} na condição {string} com alerta de reposição ativado e atualmente sem estoque compatível")
	public void que_minha_wishlist_possui_na_condicao_com_alerta_de_reposicao_ativado_e_atualmente_sem_estoque_compativel(String string, String string2) {
		throw new PendingException();
	}

	@Quando("uma variante ativa de {string} na condição {string} recebe estoque {string}")
	public void uma_variante_ativa_de_na_condicao_recebe_estoque(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("é gerada uma única notificação de reposição para {string}")
	public void e_gerada_uma_unica_notificacao_de_reposicao_para(String string) {
		throw new PendingException();
	}

	@Quando("outra variante compatível também recebe estoque {string} enquanto o item já estava disponível")
	public void outra_variante_compativel_tambem_recebe_estoque_enquanto_o_item_ja_estava_disponivel(String string) {
		throw new PendingException();
	}

	@Entao("nenhuma notificação repetida de reposição é gerada")
	public void nenhuma_notificacao_repetida_de_reposicao_e_gerada() {
		throw new PendingException();
	}

	@Dado("que a wishlist compartilhada de {string} com token {string} possui {string} em {string} disponível")
	public void que_a_wishlist_compartilhada_de_com_token_possui_em_disponivel(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que {string} não possui nenhum endereço de entrega válido cadastrado")
	public void que_nao_possui_nenhum_endereco_de_entrega_valido_cadastrado(String string) {
		throw new PendingException();
	}

	@Dado("que sou o visitante {string} autenticado")
	public void que_sou_o_visitante_autenticado(String string) {
		throw new PendingException();
	}

	@Quando("eu tento iniciar a compra de presente de {string} unidade de {string} da wishlist de {string}")
	public void eu_tento_iniciar_a_compra_de_presente_de_unidade_de_da_wishlist_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a compra de presente é rejeitada porque o destinatário não possui endereço de entrega válido")
	public void a_compra_de_presente_e_rejeitada_porque_o_destinatario_nao_possui_endereco_de_entrega_valido() {
		throw new PendingException();
	}

	@Dado("que {string} possui endereço válido cadastrado {string}")
	public void que_possui_endereco_valido_cadastrado(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que a wishlist compartilhada de {string} com token {string} possui {string} em {string} com quantidade desejada {string}")
	public void que_a_wishlist_compartilhada_de_com_token_possui_em_com_quantidade_desejada(String string, String string2, String string3, String string4, String string5) {
		throw new PendingException();
	}

	@Dado("que existe variante compatível ativa com estoque {string} e preço {string}")
	public void que_existe_variante_compativel_ativa_com_estoque_e_preco(String string, String string2) {
		throw new PendingException();
	}

	@Quando("eu crio um pedido de presente para {string} unidade de {string} usando o token {string}")
	public void eu_crio_um_pedido_de_presente_para_unidade_de_usando_o_token(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("o pedido de presente é criado em nome do comprador {string} com destinatário {string}")
	public void o_pedido_de_presente_e_criado_em_nome_do_comprador_com_destinatario(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o endereço completo de {string} permanece oculto para {string} no checkout, nos detalhes do pedido e no rastreamento")
	public void o_endereco_completo_de_permanece_oculto_para_no_checkout_nos_detalhes_do_pedido_e_no_rastreamento(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que {string} possui endereço válido cadastrado e wishlist compartilhada com {string} em {string} com quantidade desejada {string}")
	public void que_possui_endereco_valido_cadastrado_e_wishlist_compartilhada_com_em_com_quantidade_desejada(String string, String string2, String string3, String string4) {
		throw new PendingException();
	}

	@Dado("que {string} comprou {string} unidades de {string} em um pedido normal próprio fora da wishlist")
	public void que_comprou_unidades_de_em_um_pedido_normal_proprio_fora_da_wishlist(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Dado("que o visitante {string} criou um pedido de presente pendente reservando {string} unidade de {string}")
	public void que_o_visitante_criou_um_pedido_de_presente_pendente_reservando_unidade_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Quando("eu tento comprar {string} unidades de {string} como presente na wishlist de {string}")
	public void eu_tento_comprar_unidades_de_como_presente_na_wishlist_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a compra é rejeitada porque resta apenas {string} unidade disponível para presentear")
	public void a_compra_e_rejeitada_porque_resta_apenas_unidade_disponivel_para_presentear(String string) {
		throw new PendingException();
	}

	@Quando("eu compro {string} unidade de {string} como presente na wishlist de {string}")
	public void eu_compro_unidade_de_como_presente_na_wishlist_de(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a compra de {string} unidade é aceita e a quantidade restante da wishlist passa a ser {string}")
	public void a_compra_de_unidade_e_aceita_e_a_quantidade_restante_da_wishlist_passa_a_ser(String string, String string2) {
		throw new PendingException();
	}

	@Dado("que o visitante {string} criou o pedido de presente pendente {string} para {string} unidade de {string} da wishlist de {string} com quantidade desejada {string}")
	public void que_o_visitante_criou_o_pedido_de_presente_pendente_para_unidade_de_da_wishlist_de_com_quantidade_desejada(String string, String string2, String string3, String string4, String string5, String string6) {
		throw new PendingException();
	}

	@Quando("uma tentativa de pagamento de {string} falha dentro do prazo de {int} minutos")
	public void uma_tentativa_de_pagamento_de_falha_dentro_do_prazo_de_minutos(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a reserva de presente de {string} permanece {string} e o estoque do produto permanece reservado")
	public void a_reserva_de_presente_de_permanece_e_o_estoque_do_produto_permanece_reservado(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o pedido {string} expira após {int} minutos ou é cancelado antes do pagamento")
	public void o_pedido_expira_apos_minutos_ou_e_cancelado_antes_do_pagamento(String string, Integer int1) {
		throw new PendingException();
	}

	@Entao("a reserva de presente passa para {string}")
	public void a_reserva_de_presente_passa_para(String string) {
		throw new PendingException();
	}

	@Entao("o item {string} volta a ter {string} unidade restante disponível para ser presenteada por outro visitante")
	public void o_item_volta_a_ter_unidade_restante_disponivel_para_ser_presenteada_por_outro_visitante(String string, String string2) {
		throw new PendingException();
	}

	@Quando("o visitante {string} cria o pedido de presente {string} para {string} unidade e o pagamento é aprovado")
	public void o_visitante_cria_o_pedido_de_presente_para_unidade_e_o_pagamento_e_aprovado(String string, String string2, String string3) {
		throw new PendingException();
	}

	@Entao("a reserva de presente de {string} passa para {string}")
	public void a_reserva_de_presente_de_passa_para(String string, String string2) {
		throw new PendingException();
	}

	@Entao("o item {string} passa a registrar {string} unidade adquirida e {string} restante")
	public void o_item_passa_a_registrar_unidade_adquirida_e_restante(String string, String string2, String string3) {
		throw new PendingException();
	}

}
