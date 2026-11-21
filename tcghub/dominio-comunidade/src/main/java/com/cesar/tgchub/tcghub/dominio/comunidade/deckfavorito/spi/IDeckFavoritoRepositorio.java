package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IDeckFavoritoRepositorio extends IAgregadoRepositorio<DeckFavoritoIdentificador, DeckFavorito> {
	int contarPorUsuario(UsuarioIdentificador usuario);

	boolean existePorUsuarioEDeck(UsuarioIdentificador usuario, DeckIdentificador deck);

	DeckFavorito buscarPorUsuarioEDeck(UsuarioIdentificador usuario, DeckIdentificador deck);

	List<DeckFavorito> listarPorUsuario(UsuarioIdentificador usuario);
}
