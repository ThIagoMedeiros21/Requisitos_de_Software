package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.ICatalogoDeckConsulta;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

public final class CatalogoDeckProxy implements ICatalogoDeckProxy {
	private final ICatalogoDeckConsulta consulta;

	public CatalogoDeckProxy(ICatalogoDeckConsulta consulta) {
		this.consulta = Objects.requireNonNull(consulta);
	}

	@Override
	public List<ComposicaoDeck> pesquisarDecksAtivos(String termo) {
		Objects.requireNonNull(termo);
		var filtro = termo.trim().toLowerCase();
		try {
			return consulta.pesquisarAtivos(filtro).stream()
					.filter(ComposicaoDeck::isAtivo)
					.filter(deck -> filtro.isEmpty()
							|| deck.getNomeDeck().toLowerCase().contains(filtro)
							|| deck.getLider().toLowerCase().contains(filtro)
							|| deck.getCor().toLowerCase().contains(filtro)
							|| deck.getDeck().toString().toLowerCase().contains(filtro))
					.toList();
		} catch (CatalogoIndisponivelExcecao excecao) {
			throw excecao;
		} catch (RuntimeException excecao) {
			throw new CatalogoIndisponivelExcecao(CatalogoIndisponivelExcecao.MENSAGEM_PADRAO, excecao);
		}
	}

	@Override
	public boolean verificarDeckAtivo(DeckIdentificador deck) {
		Objects.requireNonNull(deck);
		var encontrado = buscarSeguro(deck)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(DeckFavoritoServico.DECK_INEXISTENTE_MODELO.formatted(deck)));
		return encontrado.isAtivo();
	}

	@Override
	public ComposicaoDeck obterComposicaoDeck(DeckIdentificador deck) {
		Objects.requireNonNull(deck);
		return buscarSeguro(deck)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(DeckFavoritoServico.DECK_INEXISTENTE_MODELO.formatted(deck)));
	}

	@Override
	public Optional<ComposicaoDeck> obterResumoDeck(DeckIdentificador deck) {
		Objects.requireNonNull(deck);
		return buscarSeguro(deck);
	}

	private Optional<ComposicaoDeck> buscarSeguro(DeckIdentificador deck) {
		try {
			return consulta.buscarPorIdentificador(deck);
		} catch (CatalogoIndisponivelExcecao excecao) {
			throw excecao;
		} catch (RuntimeException excecao) {
			throw new CatalogoIndisponivelExcecao(CatalogoIndisponivelExcecao.MENSAGEM_PADRAO, excecao);
		}
	}
}
