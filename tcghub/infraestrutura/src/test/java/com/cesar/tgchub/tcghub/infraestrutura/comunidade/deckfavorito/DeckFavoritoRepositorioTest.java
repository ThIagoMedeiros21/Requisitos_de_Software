package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

@DataJpaTest
class DeckFavoritoRepositorioTest {
	@Autowired
	private IDeckFavoritoRepositorioJpa repositorioJpa;

	private DeckFavoritoRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new DeckFavoritoRepositorio(repositorioJpa);
	}

	@Test
	void incluiListaOrdenadoPorOrdemEReordenaNaAlteracao() {
		var usuario = new UsuarioIdentificador("lucas");
		var primeiro = repositorio.incluir(new DeckFavorito(Optional.empty(), usuario, new DeckIdentificador("ST01"), 1,
				Instant.parse("2026-11-21T10:00:00Z")));
		var segundo = repositorio.incluir(new DeckFavorito(Optional.empty(), usuario,
				new DeckIdentificador("OP01-ZORO"), 2, Instant.parse("2026-11-21T10:05:00Z")));

		assertEquals(2, repositorio.contarPorUsuario(usuario));
		assertTrue(repositorio.existePorUsuarioEDeck(usuario, new DeckIdentificador("ST01")));
		assertEquals(segundo.getIdentificador().orElseThrow(),
				repositorio.buscarPorUsuarioEDeck(usuario, new DeckIdentificador("OP01-ZORO")).getIdentificador()
						.orElseThrow());

		repositorio.excluir(primeiro.getIdentificador().orElseThrow());
		repositorio.alterar(segundo.comOrdem(1));

		var restantes = repositorio.listarPorUsuario(usuario);
		assertEquals(1, restantes.size());
		assertEquals("OP01-ZORO", restantes.get(0).getDeck().toString());
		assertEquals(1, restantes.get(0).getOrdem());
	}

	@Test
	void trataIdentificadorNaoNumericoEInexistente() {
		assertFalse(repositorio.existePorIdentificador(new DeckFavoritoIdentificador("abc")));
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new DeckFavoritoIdentificador("abc")));
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new DeckFavoritoIdentificador("999")));
	}

	@Test
	void catalogoCuradoPossuiDecksAtivosEmConformidadeBandai() {
		var catalogo = new CatalogoDeckConsultaInfra();
		var ativos = catalogo.pesquisarAtivos("");
		assertEquals(5, ativos.size());
		for (var deck : ativos) {
			assertTrue(deck.isAtivo(), "Deck deveria estar ativo: " + deck.getDeck());
			assertTrue(deck.isConformidadeBandai(), "Deck deveria estar em conformidade Bandai: " + deck.getDeck());
			assertEquals(50, deck.contarCartasMainDeck(), "Main deck deve ter 50 cartas: " + deck.getDeck());
			assertEquals(51, deck.contarTotalComLider(), "Total com líder deve ser 51: " + deck.getDeck());
		}

		var inativo = catalogo.buscarPorIdentificador(new DeckIdentificador("ST09-INATIVO")).orElseThrow();
		assertFalse(inativo.isAtivo());
	}
}
