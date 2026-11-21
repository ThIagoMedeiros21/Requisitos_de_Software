package com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade;

import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class AmizadeConsulta extends Consulta<AmizadeOrdenacao> {
	private final Optional<String> usuario;
	private final Optional<StatusAmizade> status;

	public AmizadeConsulta(Optional<AmizadeOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> usuario,
			Optional<StatusAmizade> status) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.usuario = Objects.requireNonNull(usuario);
		this.status = Objects.requireNonNull(status);
	}

	public Optional<String> getUsuario() {
		return usuario;
	}

	public Optional<StatusAmizade> getStatus() {
		return status;
	}
}
