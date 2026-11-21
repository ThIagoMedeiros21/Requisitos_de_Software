package com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.spi;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoProjecao;

import dev.sauloaraujo.d5.framework.aplicacao.spi.IAgregadoAplicacaoRepositorio;

public interface IDeckFavoritoAplicacaoRepositorio
		extends IAgregadoAplicacaoRepositorio<DeckFavoritoProjecao, DeckFavoritoConsulta> {
}
