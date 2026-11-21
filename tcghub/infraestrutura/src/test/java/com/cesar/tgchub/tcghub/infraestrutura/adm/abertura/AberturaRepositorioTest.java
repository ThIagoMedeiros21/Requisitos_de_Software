package com.cesar.tgchub.tcghub.infraestrutura.adm.abertura;

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

import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida.AberturaPreferidaRepositorio;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida.IAberturaPreferidaRepositorioJpa;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

@DataJpaTest
class AberturaRepositorioTest {
	@Autowired
	private IAberturaRepositorioJpa aberturaJpa;

	@Autowired
	private IAberturaPreferidaRepositorioJpa preferidaJpa;

	private AberturaRepositorio repositorio;
	private AberturaAplicacaoRepositorio aplicacaoRepositorio;
	private AberturaPreferidaRepositorio preferidaRepositorio;

	@BeforeEach
	void configurar() {
		repositorio = new AberturaRepositorio(aberturaJpa, preferidaJpa);
		aplicacaoRepositorio = new AberturaAplicacaoRepositorio(aberturaJpa);
		preferidaRepositorio = new AberturaPreferidaRepositorio(preferidaJpa, aberturaJpa);
	}

	@Test
	void incluiBuscaPorNumeroVerificaUnicidadeEVinculoComPreferida() {
		var op1 = repositorio.incluir(new Abertura(Optional.empty(), 1, "We Are!", "Hiroshi Kitadani", "1 a 47",
				"https://api.animethemes.moe/img/op1.jpg", "https://v.animethemes.moe/OnePiece-OP1.webm", true));
		var op2 = repositorio.incluir(new Abertura(Optional.empty(), 2, "Believe", "Folder5", "48 a 115",
				"https://api.animethemes.moe/img/op2.jpg", "https://v.animethemes.moe/OnePiece-OP2.webm", false));

		assertTrue(repositorio.existePorNumero(1));
		assertFalse(repositorio.existePorNumero(1, op1.getIdentificador()));
		assertTrue(repositorio.existePorNumero(1, op2.getIdentificador()));
		assertEquals("We Are!", repositorio.buscarPorNumero(1).getNome());
		assertFalse(repositorio.possuiEscolhaPorUsuario(op1.getIdentificador().orElseThrow()));

		preferidaRepositorio.incluir(new AberturaPreferida(Optional.empty(), new UsuarioIdentificador("lucas"),
				op1.getIdentificador().orElseThrow(), true, Instant.now()));

		assertTrue(repositorio.possuiEscolhaPorUsuario(op1.getIdentificador().orElseThrow()));
		assertFalse(repositorio.possuiEscolhaPorUsuario(op2.getIdentificador().orElseThrow()));
	}

	@Test
	void pesquisaFiltrandoPorStatusETermoComContagemDePreferencias() {
		var op1 = repositorio.incluir(new Abertura(Optional.empty(), 1, "We Are!", "Hiroshi Kitadani", "1 a 47",
				"img1.jpg", "vid1.webm", true));
		repositorio.incluir(new Abertura(Optional.empty(), 2, "Believe", "Folder5", "48 a 115", "img2.jpg",
				"vid2.webm", false));
		preferidaRepositorio.incluir(new AberturaPreferida(Optional.empty(), new UsuarioIdentificador("lucas"),
				op1.getIdentificador().orElseThrow(), true, Instant.now()));

		var ativas = aplicacaoRepositorio.pesquisar(new AberturaConsulta(Optional.of(AberturaOrdenacao.NUMERO),
				Optional.of(false), Optional.of(0), Optional.of(10), Optional.of("Kitadani"), Optional.of(true)));
		assertEquals(1, ativas.getTotal());
		assertEquals(1L, ativas.getProjecoes().get(0).getQuantidadePreferencias());
	}

	@Test
	void trataIdentificadorNaoNumericoEInexistente() {
		assertFalse(repositorio.existePorIdentificador(new AberturaIdentificador("abc")));
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new AberturaIdentificador("abc")));
	}
}
