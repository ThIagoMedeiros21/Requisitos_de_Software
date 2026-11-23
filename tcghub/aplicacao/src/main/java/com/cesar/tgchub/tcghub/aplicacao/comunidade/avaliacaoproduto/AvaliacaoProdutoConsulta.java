package com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class AvaliacaoProdutoConsulta extends Consulta<AvaliacaoProdutoOrdenacao> {
	private final Optional<String> produto;
	private final Optional<String> usuario;
	private final Optional<Boolean> apenasValidas;

	public AvaliacaoProdutoConsulta(Optional<AvaliacaoProdutoOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> produto,
			Optional<String> usuario, Optional<Boolean> apenasValidas) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.produto = Objects.requireNonNull(produto);
		this.usuario = Objects.requireNonNull(usuario);
		this.apenasValidas = Objects.requireNonNull(apenasValidas);
	}

	public Optional<String> getProduto() {
		return produto;
	}

	public Optional<String> getUsuario() {
		return usuario;
	}

	public Optional<Boolean> getApenasValidas() {
		return apenasValidas;
	}
}
