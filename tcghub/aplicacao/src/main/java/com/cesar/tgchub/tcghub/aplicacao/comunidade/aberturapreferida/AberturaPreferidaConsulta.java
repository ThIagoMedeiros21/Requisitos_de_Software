package com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida;

import java.util.Objects;
import java.util.Optional;

import dev.sauloaraujo.d5.framework.aplicacao.impl.Consulta;

public final class AberturaPreferidaConsulta extends Consulta<AberturaPreferidaOrdenacao> {
	private final Optional<String> usuario;
	private final Optional<Boolean> visivelNoPerfil;

	public AberturaPreferidaConsulta(Optional<AberturaPreferidaOrdenacao> ordenacao, Optional<Boolean> descendente,
			Optional<Integer> indicePagina, Optional<Integer> tamanhoPagina, Optional<String> usuario,
			Optional<Boolean> visivelNoPerfil) {
		super(ordenacao, descendente, indicePagina, tamanhoPagina);
		this.usuario = Objects.requireNonNull(usuario);
		this.visivelNoPerfil = Objects.requireNonNull(visivelNoPerfil);
	}

	public Optional<String> getUsuario() {
		return usuario;
	}

	public Optional<Boolean> getVisivelNoPerfil() {
		return visivelNoPerfil;
	}
}
