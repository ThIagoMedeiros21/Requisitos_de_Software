package com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class CompraVerificadaRepositorio extends
		AgregadoRepositorioAbstrato<CompraVerificadaIdentificador, CompraVerificada, Long, CompraVerificadaJpa>
		implements ICompraVerificadaRepositorio {
	private final ICompraVerificadaRepositorioJpa repositorioJpa;

	public CompraVerificadaRepositorio(ICompraVerificadaRepositorioJpa repositorioJpa) {
		super(repositorioJpa, CompraVerificadaServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected CompraVerificadaJpa paraJpa(CompraVerificada agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(CompraVerificadaServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.atualizar(agregado.isPaga(), agregado.isReembolsada(), agregado.getAtualizadoEm());
			return existente;
		}
		return new CompraVerificadaJpa(null, agregado.getUsuario().toString(), agregado.getProduto().toString(),
				agregado.getPedido().toString(), agregado.isPaga(), agregado.isReembolsada(),
				agregado.getAtualizadoEm());
	}

	@Override
	protected CompraVerificada paraAgregado(CompraVerificadaJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var id = new CompraVerificadaIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		return new CompraVerificada(Optional.of(id), new UsuarioIdentificador(agregadoJpa.getUsuario()),
				new ProdutoIdentificador(agregadoJpa.getProduto()), new PedidoIdentificador(agregadoJpa.getPedido()),
				agregadoJpa.isPaga(), agregadoJpa.isReembolsada(), agregadoJpa.getAtualizadoEm());
	}

	@Override
	protected Long paraIdentificadorJpa(CompraVerificadaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).orElseThrow(() -> new AgregadoInexistenteExcecao(
				CompraVerificadaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador)));
	}

	private static Optional<Long> converter(CompraVerificadaIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(CompraVerificadaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existeCompraElegivel(UsuarioIdentificador usuario, ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(produto);
		return repositorioJpa.existsByUsuarioAndProdutoAndPagaTrueAndReembolsadaFalse(usuario.toString(),
				produto.toString());
	}

	@Override
	public boolean existePorUsuarioEPedidoEProduto(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);
		return repositorioJpa.existsByUsuarioAndPedidoAndProduto(usuario.toString(), pedido.toString(),
				produto.toString());
	}

	@Override
	public CompraVerificada buscarPorUsuarioEPedidoEProduto(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);
		return repositorioJpa
				.findByUsuarioAndPedidoAndProduto(usuario.toString(), pedido.toString(), produto.toString())
				.map(this::paraAgregado).orElseThrow(() -> new AgregadoInexistenteExcecao(
						CompraVerificadaServico.COMPRA_INEXISTENTE_MODELO.formatted(usuario, pedido, produto)));
	}

	@Override
	public List<CompraVerificada> listarPorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(produto);
		return repositorioJpa.findByUsuarioAndProdutoOrderByAtualizadoEmDesc(usuario.toString(), produto.toString())
				.stream().map(this::paraAgregado).toList();
	}

	@Override
	public List<CompraVerificada> listarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.findByUsuarioOrderByAtualizadoEmDesc(usuario.toString()).stream().map(this::paraAgregado)
				.toList();
	}
}
