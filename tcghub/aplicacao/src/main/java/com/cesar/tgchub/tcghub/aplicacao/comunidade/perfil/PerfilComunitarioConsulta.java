package com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class PerfilComunitarioConsulta extends Consulta<PerfilComunitarioOrdenacao> {
	private final Optional<String> termo;

	public PerfilComunitarioConsulta(Optional<PerfilComunitarioOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> termo) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.termo = Objects.requireNonNull(termo);
	}

	public Optional<String> getTermo() {
		return termo;
	}
}
