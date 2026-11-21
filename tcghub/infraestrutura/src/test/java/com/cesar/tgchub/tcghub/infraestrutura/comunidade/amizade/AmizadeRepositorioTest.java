package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

@DataJpaTest
class AmizadeRepositorioTest {
	private static final UsuarioIdentificador LUCAS = new UsuarioIdentificador("lucas");
	private static final UsuarioIdentificador RICARDO = new UsuarioIdentificador("ricardo");
	private static final UsuarioIdentificador THIAGO = new UsuarioIdentificador("thiago");

	@Autowired
	private IAmizadeRepositorioJpa repositorioJpa;

	private AmizadeRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new AmizadeRepositorio(repositorioJpa);
	}

	@Test
	void incluiAceitaListaERemoveBilateralmente() {
		var pendente = repositorio
				.incluir(new Amizade(Optional.empty(), LUCAS, RICARDO, StatusAmizade.PENDENTE, Instant.now(),
						Optional.empty()));
		repositorio.incluir(
				new Amizade(Optional.empty(), THIAGO, LUCAS, StatusAmizade.PENDENTE, Instant.now(), Optional.empty()));

		assertTrue(repositorio.existeRelacaoEntre(RICARDO, LUCAS));
		assertEquals(0, repositorio.listarAmizadesAtivas(LUCAS).size());

		var aceita = pendente.aceitar(Instant.now());
		repositorio.alterar(aceita);

		assertEquals(1, repositorio.listarAmizadesAtivas(LUCAS).size());
		assertEquals(1, repositorio.listarAmizadesAtivas(RICARDO).size());
		assertEquals(2, repositorio.listarPorUsuario(LUCAS).size());

		repositorio.excluir(pendente.getIdentificador().orElseThrow());

		assertFalse(repositorio.existeRelacaoEntre(LUCAS, RICARDO));
		assertEquals(0, repositorio.listarAmizadesAtivas(LUCAS).size());
		assertEquals(0, repositorio.listarAmizadesAtivas(RICARDO).size());
	}
}
