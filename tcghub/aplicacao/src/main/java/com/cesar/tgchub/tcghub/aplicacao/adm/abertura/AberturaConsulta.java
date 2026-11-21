package com.cesar.tgchub.tcghub.aplicacao.adm.abertura;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class AberturaConsulta extends Consulta<AberturaOrdenacao> {
	private final Optional<String> termo;
	private final Optional<Boolean> ativa;

	public AberturaConsulta(Optional<AberturaOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> termo,
			Optional<Boolean> ativa) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.termo = Objects.requireNonNull(termo);
		this.ativa = Objects.requireNonNull(ativa);
	}

	public Optional<String> getTermo() {
		return termo;
	}

	public Optional<Boolean> getAtiva() {
		return ativa;
	}
}
