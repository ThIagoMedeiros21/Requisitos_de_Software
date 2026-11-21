package com.cesar.tgchub.tcghub.aplicacao.adm.abertura;

import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class AberturaProjecao implements IProjecao<Abertura> {
	private final AberturaIdentificador identificador;
	private final int numero;
	private final String nome;
	private final String artista;
	private final String episodios;
	private final String imagem;
	private final String video;
	private final boolean ativa;
	private final long quantidadePreferencias;

	public AberturaProjecao(AberturaIdentificador identificador, int numero, String nome, String artista,
			String episodios, String imagem, String video, boolean ativa, long quantidadePreferencias) {
		this.identificador = Objects.requireNonNull(identificador);
		this.numero = numero;
		this.nome = Objects.requireNonNull(nome);
		this.artista = Objects.requireNonNull(artista);
		this.episodios = Objects.requireNonNull(episodios);
		this.imagem = Objects.requireNonNull(imagem);
		this.video = Objects.requireNonNull(video);
		this.ativa = ativa;
		this.quantidadePreferencias = quantidadePreferencias;
	}

	public AberturaProjecao(Long identificador, int numero, String nome, String artista, String episodios,
			String imagem, String video, boolean ativa, long quantidadePreferencias) {
		this(new AberturaIdentificador(identificador.toString()), numero, nome, artista, episodios, imagem, video,
				ativa, quantidadePreferencias);
	}

	public AberturaIdentificador getIdentificador() {
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

	public long getQuantidadePreferencias() {
		return quantidadePreferencias;
	}
}
