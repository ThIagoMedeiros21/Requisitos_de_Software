package com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class CompraVerificada implements IAgregado<CompraVerificadaIdentificador> {
	private final Optional<CompraVerificadaIdentificador> identificador;
	private final UsuarioIdentificador usuario;
	private final ProdutoIdentificador produto;
	private final PedidoIdentificador pedido;
	private final boolean paga;
	private final boolean reembolsada;
	private final Instant atualizadoEm;

	public CompraVerificada(Optional<CompraVerificadaIdentificador> identificador, UsuarioIdentificador usuario,
			ProdutoIdentificador produto, PedidoIdentificador pedido, boolean paga, boolean reembolsada,
			Instant atualizadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.produto = Objects.requireNonNull(produto);
		this.pedido = Objects.requireNonNull(pedido);
		this.paga = paga;
		this.reembolsada = reembolsada;
		this.atualizadoEm = Objects.requireNonNull(atualizadoEm);
	}

	@Override
	public Optional<CompraVerificadaIdentificador> getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public ProdutoIdentificador getProduto() {
		return produto;
	}

	public PedidoIdentificador getPedido() {
		return pedido;
	}

	public boolean isPaga() {
		return paga;
	}

	public boolean isReembolsada() {
		return reembolsada;
	}

	public Instant getAtualizadoEm() {
		return atualizadoEm;
	}

	public boolean isElegivel() {
		return paga && !reembolsada;
	}

	public CompraVerificada marcarComoPaga(Instant momento) {
		return new CompraVerificada(identificador, usuario, produto, pedido, true, reembolsada,
				Objects.requireNonNull(momento));
	}

	public CompraVerificada marcarComoReembolsada(Instant momento) {
		return new CompraVerificada(identificador, usuario, produto, pedido, paga, true,
				Objects.requireNonNull(momento));
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (CompraVerificada) objeto;
		return Objects.equals(identificador, outra.identificador) && Objects.equals(usuario, outra.usuario)
				&& Objects.equals(pedido, outra.pedido) && Objects.equals(produto, outra.produto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identificador, usuario, pedido, produto);
	}
}
