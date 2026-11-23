package com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada;

import java.time.Instant;
import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class CompraVerificadaProjecao implements IProjecao<CompraVerificada> {
	private final CompraVerificadaIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final ProdutoIdentificador produto;
	private final PedidoIdentificador pedido;
	private final boolean paga;
	private final boolean reembolsada;
	private final Instant atualizadoEm;

	public CompraVerificadaProjecao(CompraVerificadaIdentificador identificador, UsuarioIdentificador usuario,
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

	public CompraVerificadaProjecao(Long identificador, String usuario, String produto, String pedido, boolean paga,
			boolean reembolsada, Instant atualizadoEm) {
		this(new CompraVerificadaIdentificador(identificador.toString()), new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto), new PedidoIdentificador(pedido), paga, reembolsada, atualizadoEm);
	}

	public CompraVerificadaIdentificador getIdentificador() {
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
}
