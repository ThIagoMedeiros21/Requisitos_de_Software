package com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class DeckFavoritoConsulta extends Consulta<DeckFavoritoOrdenacao> {
	private final Optional<String> usuario;

	public DeckFavoritoConsulta(Optional<DeckFavoritoOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> usuario) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.usuario = Objects.requireNonNull(usuario);
	}

	public Optional<String> getUsuario() {
		return usuario;
	}
}
