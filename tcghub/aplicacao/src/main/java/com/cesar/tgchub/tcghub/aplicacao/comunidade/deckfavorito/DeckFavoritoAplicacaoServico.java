package com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.spi.IDeckFavoritoAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class DeckFavoritoAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<DeckFavoritoIdentificador, DeckFavorito, DeckFavoritoAutorizacoes, DeckFavoritoProjecao, DeckFavoritoConsulta> {
	public DeckFavoritoAplicacaoServico(IDeckFavoritoAplicacaoRepositorio repositorio, DeckFavoritoServico servico) {
		super(repositorio, servico);
	}
}
