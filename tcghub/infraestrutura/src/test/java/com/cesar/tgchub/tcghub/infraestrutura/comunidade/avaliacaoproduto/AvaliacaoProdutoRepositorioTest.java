package com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.NotaAvaliacao;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

@DataJpaTest
class AvaliacaoProdutoRepositorioTest {
	@Autowired
	private IAvaliacaoProdutoRepositorioJpa repositorioJpa;

	private AvaliacaoProdutoRepositorio repositorio;
	private AvaliacaoProdutoAplicacaoRepositorio aplicacaoRepositorio;

	@BeforeEach
	void configurar() {
		repositorio = new AvaliacaoProdutoRepositorio(repositorioJpa);
		aplicacaoRepositorio = new AvaliacaoProdutoAplicacaoRepositorio(repositorioJpa);
	}

	@Test
	void incluiAtualizaInvalidaEListaSomenteValidas() {
		var lucas = new UsuarioIdentificador("lucas");
		var carta = new ProdutoIdentificador("OP05-119");

		var salva = repositorio.incluir(new AvaliacaoProduto(Optional.empty(), lucas, carta, new NotaAvaliacao(4),
				Optional.of("Ótima carta"), true, true, Instant.parse("2026-11-23T10:00:00Z")));

		assertTrue(repositorio.existePorUsuarioEProduto(lucas, carta));
		assertEquals(1, repositorio.listarValidasPorProduto(carta).size());

		repositorio.alterar(salva.atualizar(new NotaAvaliacao(5), Optional.of("Perfeita!"),
				Instant.parse("2026-11-23T11:00:00Z")));
		var recarregada = repositorio.buscarPorUsuarioEProduto(lucas, carta);
		assertEquals(5, recarregada.getNotaValor());
		assertEquals(Optional.of("Perfeita!"), recarregada.getComentario());

		var pesquisaValidas = aplicacaoRepositorio
				.pesquisar(new AvaliacaoProdutoConsulta(Optional.of(AvaliacaoProdutoOrdenacao.NOTA), Optional.of(true),
						Optional.of(0), Optional.of(10), Optional.of("OP05-119"), Optional.empty(), Optional.of(true)));
		assertEquals(1, pesquisaValidas.getTotal());

		repositorio.alterar(recarregada.invalidar());
		assertFalse(repositorio.buscarPorUsuarioEProduto(lucas, carta).isValida());
		assertEquals(0, repositorio.listarValidasPorProduto(carta).size());
	}
}
