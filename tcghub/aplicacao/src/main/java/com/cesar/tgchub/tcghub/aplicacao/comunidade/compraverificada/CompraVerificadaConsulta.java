package com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class CompraVerificadaConsulta extends Consulta<CompraVerificadaOrdenacao> {
	private final Optional<String> usuario;
	private final Optional<String> produto;

	public CompraVerificadaConsulta(Optional<CompraVerificadaOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> usuario,
			Optional<String> produto) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.usuario = Objects.requireNonNull(usuario);
		this.produto = Objects.requireNonNull(produto);
	}

	public Optional<String> getUsuario() {
		return usuario;
	}

	public Optional<String> getProduto() {
		return produto;
	}
}
