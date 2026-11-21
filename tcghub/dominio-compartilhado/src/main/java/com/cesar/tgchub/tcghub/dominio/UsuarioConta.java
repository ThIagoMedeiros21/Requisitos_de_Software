package com.cesar.tgchub.tcghub.dominio;

import java.time.Instant;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class UsuarioConta implements IObjetoValor {
	public static final int TAMANHO_EMAIL = 150;
	public static final int TAMANHO_SENHA_HASH = 255;

	private final UsuarioIdentificador usuario;
	private final String email;
	private final String senhaHash;
	private final Autorizacao autorizacao;
	private final Instant criadoEm;

	public UsuarioConta(UsuarioIdentificador usuario, String email, String senhaHash, Autorizacao autorizacao,
			Instant criadoEm) {
		this.usuario = Objects.requireNonNull(usuario);
		this.email = Validate.notBlank(email, "O e-mail é obrigatório").trim();
		Validate.isTrue(this.email.length() <= TAMANHO_EMAIL, "O e-mail deve ter no máximo %d caracteres",
				TAMANHO_EMAIL);
		this.senhaHash = Validate.notBlank(senhaHash, "O hash da senha é obrigatório");
		this.autorizacao = Objects.requireNonNull(autorizacao);
		this.criadoEm = Objects.requireNonNull(criadoEm);
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public String getEmail() {
		return email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public Autorizacao getAutorizacao() {
		return autorizacao;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}
}
