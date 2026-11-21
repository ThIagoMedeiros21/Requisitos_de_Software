package com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta;

import java.time.Instant;

import com.cesar.tgchub.tcghub.dominio.UsuarioConta;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "USR_USUARIO_CONTA")
public class UsuarioContaJpa {
	@Id
	@Column(name = "USR_ID", updatable = false)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@Column(name = "USR_EMAIL", unique = true)
	@NotNull
	@Size(max = UsuarioConta.TAMANHO_EMAIL)
	private String email;

	@Column(name = "USR_SENHA_HASH")
	@NotNull
	@Size(max = UsuarioConta.TAMANHO_SENHA_HASH)
	private String senhaHash;

	@Column(name = "USR_AUTORIZACAO")
	@NotNull
	@Size(max = 30)
	private String autorizacao;

	@Column(name = "USR_CRIADO_EM")
	@NotNull
	private Instant criadoEm;

	protected UsuarioContaJpa() {
	}

	public UsuarioContaJpa(String usuario, String email, String senhaHash, String autorizacao, Instant criadoEm) {
		this.usuario = usuario;
		this.email = email;
		this.senhaHash = senhaHash;
		this.autorizacao = autorizacao;
		this.criadoEm = criadoEm;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getEmail() {
		return email;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public String getAutorizacao() {
		return autorizacao;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}
}
