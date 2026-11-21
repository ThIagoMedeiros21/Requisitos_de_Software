package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PCF_PERFIL_CARTA_FAVORITA", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PCM_ID", "PCF_INDICE" }),
		@UniqueConstraint(columnNames = { "PCM_ID", "PCF_CARTA_ID" }) })
public class PerfilCartaFavoritaJpa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PCF_ID", updatable = false)
	private @Nullable Long identificador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PCM_ID")
	@NotNull
	private PerfilComunitarioJpa perfil;

	@Column(name = "PCF_INDICE")
	private int indice;

	@Column(name = "PCF_CARTA_ID")
	@NotNull
	@Size(max = CartaIdentificador.TAMANHO)
	private String carta;

	protected PerfilCartaFavoritaJpa() {
	}

	public PerfilCartaFavoritaJpa(PerfilComunitarioJpa perfil, int indice, String carta) {
		this.perfil = perfil;
		this.indice = indice;
		this.carta = carta;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public PerfilComunitarioJpa getPerfil() {
		return perfil;
	}

	public int getIndice() {
		return indice;
	}

	public String getCarta() {
		return carta;
	}
}
