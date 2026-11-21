package com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.infraestrutura.adm.abertura.AberturaRepositorio;
import com.cesar.tgchub.tcghub.infraestrutura.adm.abertura.IAberturaRepositorioJpa;

@DataJpaTest
class AberturaPreferidaRepositorioTest {
	@Autowired
	private IAberturaRepositorioJpa aberturaJpa;

	@Autowired
	private IAberturaPreferidaRepositorioJpa preferidaJpa;

	private AberturaRepositorio aberturaRepositorio;
	private AberturaPreferidaRepositorio repositorio;
	private AberturaPreferidaAplicacaoRepositorio aplicacaoRepositorio;

	@BeforeEach
	void configurar() {
		aberturaRepositorio = new AberturaRepositorio(aberturaJpa, preferidaJpa);
		repositorio = new AberturaPreferidaRepositorio(preferidaJpa, aberturaJpa);
		aplicacaoRepositorio = new AberturaPreferidaAplicacaoRepositorio(preferidaJpa);
	}

	@Test
	void incluiSubstituiAberturaAlteraVisibilidadeEPesquisa() {
		var op1 = aberturaRepositorio.incluir(new Abertura(Optional.empty(), 1, "We Are!", "Hiroshi Kitadani",
				"1 a 47", "img1.jpg", "vid1.webm", true));
		var op2 = aberturaRepositorio.incluir(new Abertura(Optional.empty(), 2, "Believe", "Folder5", "48 a 115",
				"img2.jpg", "vid2.webm", true));

		var lucas = new UsuarioIdentificador("lucas");
		var salva = repositorio.incluir(new AberturaPreferida(Optional.empty(), lucas,
				op1.getIdentificador().orElseThrow(), true, Instant.parse("2026-11-21T10:00:00Z")));

		assertTrue(repositorio.existePorUsuario(lucas));
		assertEquals(1, repositorio.contarPorAbertura(op1.getIdentificador().orElseThrow()));

		var substituida = salva.substituirAbertura(op2.getIdentificador().orElseThrow(),
				Instant.parse("2026-11-21T11:00:00Z")).alterarVisibilidade(false);
		repositorio.alterar(substituida);

		var recarregada = repositorio.buscarPorUsuario(lucas);
		assertEquals(op2.getIdentificador().orElseThrow(), recarregada.getAbertura());
		assertFalse(recarregada.isVisivelNoPerfil());

		var pesquisa = aplicacaoRepositorio
				.pesquisar(new AberturaPreferidaConsulta(Optional.of(AberturaPreferidaOrdenacao.USUARIO),
						Optional.of(false), Optional.of(0), Optional.of(10), Optional.of("lucas"),
						Optional.of(false)));
		assertEquals(1, pesquisa.getTotal());
		assertEquals("lucas", pesquisa.getProjecoes().get(0).getUsuario().toString());
	}
}
