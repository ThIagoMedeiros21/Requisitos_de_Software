package com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida;

import java.time.Instant;
import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class AberturaPreferidaProjecao implements IProjecao<AberturaPreferida> {
	private final AberturaPreferidaIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final AberturaIdentificador abertura;
	private final boolean visivelNoPerfil;
	private final Instant escolhidaEm;

	public AberturaPreferidaProjecao(AberturaPreferidaIdentificador identificador, UsuarioIdentificador usuario,
			AberturaIdentificador abertura, boolean visivelNoPerfil, Instant escolhidaEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.abertura = Objects.requireNonNull(abertura);
		this.visivelNoPerfil = visivelNoPerfil;
		this.escolhidaEm = Objects.requireNonNull(escolhidaEm);
	}

	public AberturaPreferidaProjecao(Long identificador, String usuario, Long abertura, boolean visivelNoPerfil,
			Instant escolhidaEm) {
		this(new AberturaPreferidaIdentificador(identificador.toString()), new UsuarioIdentificador(usuario),
				new AberturaIdentificador(abertura.toString()), visivelNoPerfil, escolhidaEm);
	}

	public AberturaPreferidaIdentificador getIdentificador() {
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
}
