package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;

@DataJpaTest
class PerfilComunitarioAplicacaoRepositorioTest {
	@Autowired
	private IPerfilComunitarioRepositorioJpa repositorioJpa;

	private PerfilComunitarioRepositorio repositorio;
	private PerfilComunitarioAplicacaoRepositorio aplicacaoRepositorio;

	@BeforeEach
	void configurar() {
		repositorio = new PerfilComunitarioRepositorio(repositorioJpa);
		aplicacaoRepositorio = new PerfilComunitarioAplicacaoRepositorio(repositorioJpa);
	}

	private void incluir(String usuario, String apelido, String cidade) {
		repositorio.incluir(new PerfilComunitario(Optional.empty(), new UsuarioIdentificador(usuario),
				new Apelido(apelido), Optional.of(cidade), Optional.empty(), Optional.empty(), Optional.empty(), true,
				List.of()));
	}

	@Test
	void pesquisaFiltrandoPorTermoEOrdenandoPorApelido() {
		incluir("ricardo", "ZoroTC", "Olinda");
		incluir("lucas", "LuffyBR", "Recife");
		incluir("thiago", "SanjiOP", "Recife");

		var consulta = new PerfilComunitarioConsulta(Optional.of(PerfilComunitarioOrdenacao.APELIDO),
				Optional.of(false), Optional.empty(), Optional.empty(), Optional.of("rec"));
		var resultado = aplicacaoRepositorio.pesquisar(consulta);

		assertEquals(2, resultado.getTotal());
		assertEquals("LuffyBR", resultado.getProjecoes().get(0).getApelido().toString());
		assertEquals("SanjiOP", resultado.getProjecoes().get(1).getApelido().toString());
	}

	@Test
	void escapaCaracteresEspeciaisNoFiltroLike() {
		incluir("u1", "100%OP", "Recife");
		incluir("u2", "1000OP", "Recife");

		var consulta = new PerfilComunitarioConsulta(Optional.empty(), Optional.empty(), Optional.empty(),
				Optional.empty(), Optional.of("100%"));
		var resultado = aplicacaoRepositorio.pesquisar(consulta);

		assertEquals(1, resultado.getTotal());
		assertEquals("100%OP", resultado.getProjecoes().get(0).getApelido().toString());
	}
}
