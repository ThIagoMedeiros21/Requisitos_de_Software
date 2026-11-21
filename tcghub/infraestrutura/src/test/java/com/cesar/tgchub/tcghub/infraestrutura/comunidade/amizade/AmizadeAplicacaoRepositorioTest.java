package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

@DataJpaTest
class AmizadeAplicacaoRepositorioTest {
	@Autowired
	private IAmizadeRepositorioJpa repositorioJpa;

	private AmizadeRepositorio repositorio;
	private AmizadeAplicacaoRepositorio aplicacaoRepositorio;

	@BeforeEach
	void configurar() {
		repositorio = new AmizadeRepositorio(repositorioJpa);
		aplicacaoRepositorio = new AmizadeAplicacaoRepositorio(repositorioJpa);
	}

	@Test
	void pesquisaPorUsuarioEStatus() {
		var pendente = repositorio.incluir(new Amizade(Optional.empty(), new UsuarioIdentificador("lucas"),
				new UsuarioIdentificador("ricardo"), StatusAmizade.PENDENTE, Instant.now(), Optional.empty()));
		repositorio.alterar(pendente.aceitar(Instant.now()));
		repositorio.incluir(new Amizade(Optional.empty(), new UsuarioIdentificador("thiago"),
				new UsuarioIdentificador("lucas"), StatusAmizade.PENDENTE, Instant.now(), Optional.empty()));

		var ativasLucas = aplicacaoRepositorio
				.pesquisar(new AmizadeConsulta(Optional.of(AmizadeOrdenacao.CRIADO_EM), Optional.of(true),
						Optional.empty(), Optional.empty(), Optional.of("lucas"), Optional.of(StatusAmizade.ACEITA)));
		assertEquals(1, ativasLucas.getTotal());
		assertEquals("ricardo", ativasLucas.getProjecoes().get(0).getDestinatario().toString());
	}
}
