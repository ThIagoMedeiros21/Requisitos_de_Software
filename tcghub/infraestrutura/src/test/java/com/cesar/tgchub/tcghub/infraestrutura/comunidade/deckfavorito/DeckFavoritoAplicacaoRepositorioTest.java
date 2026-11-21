package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

@DataJpaTest
class DeckFavoritoAplicacaoRepositorioTest {
	@Autowired
	private IDeckFavoritoRepositorioJpa repositorioJpa;

	private DeckFavoritoRepositorio repositorio;
	private DeckFavoritoAplicacaoRepositorio aplicacaoRepositorio;

	@BeforeEach
	void configurar() {
		repositorio = new DeckFavoritoRepositorio(repositorioJpa);
		aplicacaoRepositorio = new DeckFavoritoAplicacaoRepositorio(repositorioJpa);
	}

	@Test
	void pesquisaFiltrandoPorUsuarioEOrdenandoPorOrdem() {
		repositorio.incluir(new DeckFavorito(Optional.empty(), new UsuarioIdentificador("lucas"),
				new DeckIdentificador("OP01-ZORO"), 2, Instant.parse("2026-11-21T10:05:00Z")));
		repositorio.incluir(new DeckFavorito(Optional.empty(), new UsuarioIdentificador("lucas"),
				new DeckIdentificador("ST01"), 1, Instant.parse("2026-11-21T10:00:00Z")));
		repositorio.incluir(new DeckFavorito(Optional.empty(), new UsuarioIdentificador("ricardo"),
				new DeckIdentificador("ST10"), 1, Instant.parse("2026-11-21T10:10:00Z")));

		var consulta = new DeckFavoritoConsulta(Optional.of(DeckFavoritoOrdenacao.ORDEM), Optional.of(false),
				Optional.of(0), Optional.of(10), Optional.of("lucas"));
		var pagina = aplicacaoRepositorio.pesquisar(consulta);

		assertEquals(2, pagina.getTotal());
		assertEquals("ST01", pagina.getProjecoes().get(0).getDeck().toString());
		assertEquals("OP01-ZORO", pagina.getProjecoes().get(1).getDeck().toString());
	}
}
