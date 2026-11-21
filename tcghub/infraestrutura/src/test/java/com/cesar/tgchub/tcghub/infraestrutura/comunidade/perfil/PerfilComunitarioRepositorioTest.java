package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;

@DataJpaTest
class PerfilComunitarioRepositorioTest {
	@Autowired
	private IPerfilComunitarioRepositorioJpa repositorioJpa;

	private PerfilComunitarioRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new PerfilComunitarioRepositorio(repositorioJpa);
	}

	private static PerfilComunitario criar(String usuario, String apelido, List<String> cartas) {
		return new PerfilComunitario(Optional.empty(), new UsuarioIdentificador(usuario), new Apelido(apelido),
				Optional.of("Recife"), Optional.of("Colecionador"), Optional.of("avatar.png"), Optional.empty(), true,
				cartas.stream().map(CartaIdentificador::new).toList());
	}

	@Test
	void incluiEBuscaPorIdentificadorEUsuario() {
		var salvo = repositorio.incluir(criar("lucas", "LuffyBR", List.of("OP01-001", "OP01-016")));
		var id = salvo.getIdentificador().orElseThrow();

		var buscadoPorId = repositorio.buscarPorIdentificador(id);
		assertEquals("LuffyBR", buscadoPorId.getApelido().toString());
		assertEquals(List.of(new CartaIdentificador("OP01-001"), new CartaIdentificador("OP01-016")),
				buscadoPorId.getCartasFavoritas());

		var buscadoPorUsuario = repositorio.buscarPorUsuario(new UsuarioIdentificador("lucas"));
		assertEquals(id, buscadoPorUsuario.getIdentificador().orElseThrow());
	}

	@Test
	void trataIdentificadorNaoNumericoEInexistente() {
		assertFalse(repositorio.existePorIdentificador(new PerfilComunitarioIdentificador("abc")));
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new PerfilComunitarioIdentificador("abc")));
		assertThrows(AgregadoInexistenteExcecao.class,
				() -> repositorio.buscarPorIdentificador(new PerfilComunitarioIdentificador("999")));
	}

	@Test
	void validaUnicidadeCaseInsensitiveDeApelidoIgnorandoOProprio() {
		var lucas = repositorio.incluir(criar("lucas", "LuffyBR", List.of()));
		repositorio.incluir(criar("ricardo", "ZoroTC", List.of()));

		assertTrue(repositorio.existePorApelido(new Apelido("zorotc"), lucas.getIdentificador()));
		assertFalse(repositorio.existePorApelido(new Apelido("luffybr"), lucas.getIdentificador()));
		assertTrue(repositorio.existePorApelido(new Apelido("luffybr"), Optional.empty()));
	}

	@Test
	void alteraSubstituindoDestaquesSemViolacaoDeIndiceOuDuplicidade() {
		var salvo = repositorio.incluir(criar("lucas", "LuffyBR", List.of("OP01-001", "OP01-002", "OP01-003", "OP01-004")));
		var atualizado = salvo.removerCartaFavorita(new CartaIdentificador("OP01-004"))
				.fixarCartaFavorita(new CartaIdentificador("OP01-005"));

		repositorio.alterar(atualizado);

		var recarregado = repositorio.buscarPorIdentificador(salvo.getIdentificador().orElseThrow());
		assertEquals(List.of(new CartaIdentificador("OP01-001"), new CartaIdentificador("OP01-002"),
				new CartaIdentificador("OP01-003"), new CartaIdentificador("OP01-005")),
				recarregado.getCartasFavoritas());
	}
}
