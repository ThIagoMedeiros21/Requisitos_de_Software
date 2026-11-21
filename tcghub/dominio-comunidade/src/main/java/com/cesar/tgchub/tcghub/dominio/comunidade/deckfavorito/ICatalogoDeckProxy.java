package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.util.List;
import java.util.Optional;

public interface ICatalogoDeckProxy {
	List<ComposicaoDeck> pesquisarDecksAtivos(String termo);

	boolean verificarDeckAtivo(DeckIdentificador deck);

	ComposicaoDeck obterComposicaoDeck(DeckIdentificador deck);

	Optional<ComposicaoDeck> obterResumoDeck(DeckIdentificador deck);
}
