package com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.infraestrutura.adm.abertura.AberturaJpa;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "APR_ABERTURA_PREFERIDA")
public class AberturaPreferidaJpa implements IAgregadoJpa<AberturaPreferida> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APR_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "APR_USR_ID", nullable = false, unique = true)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ABR_ID", nullable = false)
	@NotNull
	private AberturaJpa abertura;

	@Column(name = "APR_VISIVEL_NO_PERFIL", nullable = false)
	private boolean visivelNoPerfil;

	@Column(name = "APR_ESCOLHIDA_EM", nullable = false)
	@NotNull
	private Instant escolhidaEm;

	protected AberturaPreferidaJpa() {
	}

	public AberturaPreferidaJpa(@Nullable Long identificador, String usuario, AberturaJpa abertura,
			boolean visivelNoPerfil, Instant escolhidaEm) {
		this.identificador = identificador;
		this.usuario = usuario;
		this.abertura = abertura;
		this.visivelNoPerfil = visivelNoPerfil;
		this.escolhidaEm = escolhidaEm;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getUsuario() {
		return usuario;
	}

	public AberturaJpa getAbertura() {
		return abertura;
	}

	public boolean isVisivelNoPerfil() {
		return visivelNoPerfil;
	}

	public Instant getEscolhidaEm() {
		return escolhidaEm;
	}

	public void atualizar(AberturaJpa novaAbertura, boolean visivelNoPerfil, Instant escolhidaEm) {
		this.abertura = novaAbertura;
		this.visivelNoPerfil = visivelNoPerfil;
		this.escolhidaEm = escolhidaEm;
	}
}
