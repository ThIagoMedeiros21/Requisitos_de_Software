package com.cesar.tgchub.tcghub.infraestrutura.adm.abertura;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ABR_ABERTURA")
public class AberturaJpa implements IAgregadoJpa<Abertura> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ABR_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "ABR_NR", nullable = false, unique = true)
	private int numero;

	@Column(name = "ABR_NM", nullable = false)
	@NotNull
	@Size(max = Abertura.TAMANHO_NOME)
	private String nome;

	@Column(name = "ABR_ARTISTA", nullable = false)
	@NotNull
	@Size(max = Abertura.TAMANHO_ARTISTA)
	private String artista;

	@Column(name = "ABR_EPISODIOS", nullable = false)
	@NotNull
	@Size(max = Abertura.TAMANHO_EPISODIOS)
	private String episodios;

	@Column(name = "ABR_IMAGEM", nullable = false)
	@NotNull
	@Size(max = Abertura.TAMANHO_URL)
	private String imagem;

	@Column(name = "ABR_VIDEO", nullable = false)
	@NotNull
	@Size(max = Abertura.TAMANHO_URL)
	private String video;

	@Column(name = "ABR_ATIVA", nullable = false)
	private boolean ativa;

	protected AberturaJpa() {
	}

	public AberturaJpa(@Nullable Long identificador, int numero, String nome, String artista, String episodios,
			String imagem, String video, boolean ativa) {
		this.identificador = identificador;
		this.numero = numero;
		this.nome = nome;
		this.artista = artista;
		this.episodios = episodios;
		this.imagem = imagem;
		this.video = video;
		this.ativa = ativa;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public int getNumero() {
		return numero;
	}

	public String getNome() {
		return nome;
	}

	public String getArtista() {
		return artista;
	}

	public String getEpisodios() {
		return episodios;
	}

	public String getImagem() {
		return imagem;
	}

	public String getVideo() {
		return video;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void atualizar(int numero, String nome, String artista, String episodios, String imagem, String video,
			boolean ativa) {
		this.numero = numero;
		this.nome = nome;
		this.artista = artista;
		this.episodios = episodios;
		this.imagem = imagem;
		this.video = video;
		this.ativa = ativa;
	}
}
