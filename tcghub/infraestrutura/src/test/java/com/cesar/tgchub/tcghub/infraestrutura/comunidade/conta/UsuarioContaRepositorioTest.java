package com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioConta;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

@DataJpaTest
class UsuarioContaRepositorioTest {
	@Autowired
	private IUsuarioContaRepositorioJpa repositorioJpa;

	private UsuarioContaRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new UsuarioContaRepositorio(repositorioJpa);
	}

	@Test
	void salvaEBuscaPorUsuarioOuEmailIgnoreCase() {
		repositorio.salvar(new UsuarioConta(new UsuarioIdentificador("usopp"), "Usopp@TCGHub.com", "{noop}sniper",
				Autorizacao.USUARIO, Instant.now()));

		assertTrue(repositorio.existePorUsuarioIgnoreCase("USOPP"));
		assertTrue(repositorio.existePorEmailIgnoreCase("usopp@tcghub.com"));
		assertFalse(repositorio.existePorUsuarioIgnoreCase("franky"));

		var porUsuario = repositorio.buscarPorUsuarioOuEmailIgnoreCase("USOPP").orElseThrow();
		assertEquals("usopp", porUsuario.getUsuario().toString());

		var porEmail = repositorio.buscarPorUsuarioOuEmailIgnoreCase("USOPP@TCGHUB.COM").orElseThrow();
		assertEquals("usopp", porEmail.getUsuario().toString());
	}
}
