package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "AMI_AMIZADE", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "AMI_SOLICITANTE_ID", "AMI_DESTINATARIO_ID" }) })
public class AmizadeJpa implements IAgregadoJpa<Amizade> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AMI_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "AMI_SOLICITANTE_ID")
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String solicitante;

	@Column(name = "AMI_DESTINATARIO_ID")
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String destinatario;

	@Enumerated(EnumType.STRING)
	@Column(name = "AMI_STATUS", length = 20)
	@NotNull
	private StatusAmizade status;

	@Column(name = "AMI_CRIADO_EM")
	@NotNull
	private Instant criadoEm;

	@Column(name = "AMI_ATUALIZADO_EM")
	private @Nullable Instant atualizadoEm;

	protected AmizadeJpa() {
	}

	public AmizadeJpa(@Nullable Long identificador, String solicitante, String destinatario, StatusAmizade status,
			Instant criadoEm, @Nullable Instant atualizadoEm) {
		this.identificador = identificador;
		this.solicitante = solicitante;
		this.destinatario = destinatario;
		this.status = status;
		this.criadoEm = criadoEm;
		this.atualizadoEm = atualizadoEm;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public StatusAmizade getStatus() {
		return status;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}

	public @Nullable Instant getAtualizadoEm() {
		return atualizadoEm;
	}
}
