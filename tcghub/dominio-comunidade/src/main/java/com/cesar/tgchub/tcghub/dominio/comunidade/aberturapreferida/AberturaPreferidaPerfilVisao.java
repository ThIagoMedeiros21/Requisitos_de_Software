package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class AberturaPreferidaPerfilVisao implements IObjetoValor {
	private final boolean ehProprietario;
	private final boolean visivelNoPerfil;
	private final boolean mesmaPreferenciaDoSolicitante;
	private final Optional<AberturaPreferida> preferencia;
	private final Optional<Abertura> abertura;

	public AberturaPreferidaPerfilVisao(boolean ehProprietario, boolean visivelNoPerfil,
			boolean mesmaPreferenciaDoSolicitante, Optional<AberturaPreferida> preferencia,
			Optional<Abertura> abertura) {
		this.ehProprietario = ehProprietario;
		this.visivelNoPerfil = visivelNoPerfil;
		this.mesmaPreferenciaDoSolicitante = mesmaPreferenciaDoSolicitante;
		this.preferencia = Objects.requireNonNull(preferencia);
		this.abertura = Objects.requireNonNull(abertura);
	}

	public boolean isEhProprietario() {
		return ehProprietario;
	}

	public boolean isVisivelNoPerfil() {
		return visivelNoPerfil;
	}

	public boolean isMesmaPreferenciaDoSolicitante() {
		return mesmaPreferenciaDoSolicitante;
	}

	public Optional<AberturaPreferida> getPreferencia() {
		return preferencia;
	}

	public Optional<Abertura> getAbertura() {
		return abertura;
	}

	public boolean possuiPreferidaVisivel() {
		return abertura.isPresent();
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (AberturaPreferidaPerfilVisao) objeto;
		return ehProprietario == outra.ehProprietario && visivelNoPerfil == outra.visivelNoPerfil
				&& mesmaPreferenciaDoSolicitante == outra.mesmaPreferenciaDoSolicitante
				&& Objects.equals(preferencia, outra.preferencia) && Objects.equals(abertura, outra.abertura);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ehProprietario, visivelNoPerfil, mesmaPreferenciaDoSolicitante, preferencia, abertura);
	}
}
