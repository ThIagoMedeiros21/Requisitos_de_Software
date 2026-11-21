package com.cesar.tgchub.tcghub.dominio.comunidade.amizade;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class Amizade implements IAgregado<AmizadeIdentificador> {
	public static final String AUTO_SOLICITACAO = "Não é possível adicionar a si mesmo como amigo";

	private final Optional<AmizadeIdentificador> identificador;
	private final UsuarioIdentificador solicitante;
	private final UsuarioIdentificador destinatario;
	private final StatusAmizade status;
	private final Instant criadoEm;
	private final Optional<Instant> atualizadoEm;

	public Amizade(Optional<AmizadeIdentificador> identificador, UsuarioIdentificador solicitante,
			UsuarioIdentificador destinatario, StatusAmizade status, Instant criadoEm,
			Optional<Instant> atualizadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.solicitante = Objects.requireNonNull(solicitante);
		this.destinatario = Objects.requireNonNull(destinatario);
		Validate.isTrue(!this.solicitante.equals(this.destinatario), AUTO_SOLICITACAO);
		this.status = Objects.requireNonNull(status);
		this.criadoEm = Objects.requireNonNull(criadoEm);
		this.atualizadoEm = Objects.requireNonNull(atualizadoEm);
	}

	@Override
	public Optional<AmizadeIdentificador> getIdentificador() {
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

	public boolean estaAceita() {
		return status == StatusAmizade.ACEITA;
	}

	public boolean estaPendente() {
		return status == StatusAmizade.PENDENTE;
	}

	public boolean envolve(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return solicitante.equals(usuario) || destinatario.equals(usuario);
	}

	public UsuarioIdentificador outroUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		if (solicitante.equals(usuario)) {
			return destinatario;
		}
		if (destinatario.equals(usuario)) {
			return solicitante;
		}
		throw new IllegalArgumentException("O usuário %s não participa desta amizade".formatted(usuario));
	}

	public Amizade aceitar(Instant agora) {
		Objects.requireNonNull(agora);
		Validate.validState(status == StatusAmizade.PENDENTE, "A solicitação de amizade já foi respondida");
		return new Amizade(identificador, solicitante, destinatario, StatusAmizade.ACEITA, criadoEm, Optional.of(agora));
	}
}
