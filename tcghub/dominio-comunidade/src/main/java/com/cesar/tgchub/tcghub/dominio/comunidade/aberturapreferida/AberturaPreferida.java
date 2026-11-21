package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class AberturaPreferida implements IAgregado<AberturaPreferidaIdentificador> {
	private final Optional<AberturaPreferidaIdentificador> identificador;
	private final UsuarioIdentificador usuario;
	private final AberturaIdentificador abertura;
	private final boolean visivelNoPerfil;
	private final Instant escolhidaEm;

	public AberturaPreferida(Optional<AberturaPreferidaIdentificador> identificador, UsuarioIdentificador usuario,
			AberturaIdentificador abertura, boolean visivelNoPerfil, Instant escolhidaEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.abertura = Objects.requireNonNull(abertura);
		this.visivelNoPerfil = visivelNoPerfil;
		this.escolhidaEm = Objects.requireNonNull(escolhidaEm);
	}

	@Override
	public Optional<AberturaPreferidaIdentificador> getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public AberturaIdentificador getAbertura() {
		return abertura;
	}

	public boolean isVisivelNoPerfil() {
		return visivelNoPerfil;
	}

	public Instant getEscolhidaEm() {
		return escolhidaEm;
	}

	public AberturaPreferida substituirAbertura(AberturaIdentificador novaAbertura, Instant momento) {
		return new AberturaPreferida(identificador, usuario, Objects.requireNonNull(novaAbertura), visivelNoPerfil,
				Objects.requireNonNull(momento));
	}

	public AberturaPreferida alterarVisibilidade(boolean visivel) {
		return new AberturaPreferida(identificador, usuario, abertura, visivel, escolhidaEm);
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (AberturaPreferida) objeto;
		return Objects.equals(identificador, outra.identificador) && Objects.equals(usuario, outra.usuario);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identificador, usuario);
	}
}
