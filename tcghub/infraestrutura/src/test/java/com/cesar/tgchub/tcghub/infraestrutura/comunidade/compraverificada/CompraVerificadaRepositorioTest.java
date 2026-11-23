package com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada;

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
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

@DataJpaTest
class CompraVerificadaRepositorioTest {
	@Autowired
	private ICompraVerificadaRepositorioJpa repositorioJpa;

	private CompraVerificadaRepositorio repositorio;

	@BeforeEach
	void configurar() {
		repositorio = new CompraVerificadaRepositorio(repositorioJpa);
	}

	@Test
	void validaElegibilidadePorCompraPagaENaoReembolsada() {
		var lucas = new UsuarioIdentificador("lucas");
		var carta = new ProdutoIdentificador("OP05-119");
		var ped1 = new PedidoIdentificador("PED-100");
		var ped2 = new PedidoIdentificador("PED-101");

		var salva1 = repositorio.incluir(
				new CompraVerificada(Optional.empty(), lucas, carta, ped1, false, false, Instant.now()));
		assertFalse(repositorio.existeCompraElegivel(lucas, carta));

		repositorio.alterar(salva1.marcarComoPaga(Instant.now()));
		assertTrue(repositorio.existeCompraElegivel(lucas, carta));

		repositorio.incluir(new CompraVerificada(Optional.empty(), lucas, carta, ped2, true, false, Instant.now()));
		repositorio.alterar(repositorio.buscarPorUsuarioEPedidoEProduto(lucas, ped1, carta)
				.marcarComoReembolsada(Instant.now()));
		assertTrue(repositorio.existeCompraElegivel(lucas, carta));

		repositorio.alterar(repositorio.buscarPorUsuarioEPedidoEProduto(lucas, ped2, carta)
				.marcarComoReembolsada(Instant.now()));
		assertFalse(repositorio.existeCompraElegivel(lucas, carta));
		assertEquals(2, repositorio.listarPorUsuarioEProduto(lucas, carta).size());
	}
}
