package com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class AmizadeProjecao implements IProjecao<Amizade> {
	private final AmizadeIdentificador identificador;
	private final UsuarioIdentificador solicitante;
	private final UsuarioIdentificador destinatario;
	private final StatusAmizade status;
	private final Instant criadoEm;
	private final Optional<Instant> atualizadoEm;

	public AmizadeProjecao(AmizadeIdentificador identificador, UsuarioIdentificador solicitante,
			UsuarioIdentificador destinatario, StatusAmizade status, Instant criadoEm,
			Optional<Instant> atualizadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.solicitante = Objects.requireNonNull(solicitante);
		this.destinatario = Objects.requireNonNull(destinatario);
		this.status = Objects.requireNonNull(status);
		this.criadoEm = Objects.requireNonNull(criadoEm);
		this.atualizadoEm = Objects.requireNonNull(atualizadoEm);
	}

	public AmizadeProjecao(Long identificador, String solicitante, String destinatario, StatusAmizade status,
			Instant criadoEm, @Nullable Instant atualizadoEm) {
		this(new AmizadeIdentificador(identificador.toString()), new UsuarioIdentificador(solicitante),
				new UsuarioIdentificador(destinatario), status, criadoEm, Optional.ofNullable(atualizadoEm));
	}

	public AmizadeIdentificador getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getSolicitante() {
		return solicitante;
	}

	public UsuarioIdentificador getDestinatario() {
		return destinatario;
	}

	public StatusAmizade getStatus() {
		return status;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}

	public Optional<Instant> getAtualizadoEm() {
		return atualizadoEm;
	}
}
