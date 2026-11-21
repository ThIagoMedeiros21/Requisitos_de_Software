package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi;

import java.util.List;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ComposicaoDeck;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

public interface ICatalogoDeckConsulta {
	List<ComposicaoDeck> pesquisarAtivos(String termo);

	Optional<ComposicaoDeck> buscarPorIdentificador(DeckIdentificador deck);
}
