package com.cesar.tgchub.tcghub.dominio.adm.abertura;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class Abertura implements IAgregado<AberturaIdentificador> {
	public static final int TAMANHO_NOME = 150;
	public static final int TAMANHO_ARTISTA = 150;
	public static final int TAMANHO_EPISODIOS = 100;
	public static final int TAMANHO_URL = 500;

	private final Optional<AberturaIdentificador> identificador;
	private final NumeroAbertura numero;
	private final String nome;
	private final String artista;
	private final String episodios;
	private final String imagem;
	private final String video;
	private final StatusAbertura status;

	public Abertura(Optional<AberturaIdentificador> identificador, NumeroAbertura numero, String nome, String artista,
			String episodios, String imagem, String video, StatusAbertura status) {
		this.identificador = Objects.requireNonNull(identificador);
		this.numero = Objects.requireNonNull(numero);
		this.nome = validarTexto(nome, TAMANHO_NOME, "O nome da abertura é obrigatório",
				"O nome da abertura não pode ter mais de %d caracteres".formatted(TAMANHO_NOME));
		this.artista = validarTexto(artista, TAMANHO_ARTISTA, "O artista da abertura é obrigatório",
				"O artista da abertura não pode ter mais de %d caracteres".formatted(TAMANHO_ARTISTA));
		this.episodios = validarTexto(episodios, TAMANHO_EPISODIOS, "Os episódios da abertura são obrigatórios",
				"Os episódios da abertura não podem ter mais de %d caracteres".formatted(TAMANHO_EPISODIOS));
		this.imagem = validarTexto(imagem, TAMANHO_URL, "A imagem da abertura é obrigatória",
				"A URL da imagem não pode ter mais de %d caracteres".formatted(TAMANHO_URL));
		this.video = validarTexto(video, TAMANHO_URL, "O vídeo da abertura é obrigatório",
				"A URL do vídeo não pode ter mais de %d caracteres".formatted(TAMANHO_URL));
		this.status = Objects.requireNonNull(status);
	}

	public Abertura(Optional<AberturaIdentificador> identificador, int numero, String nome, String artista,
			String episodios, String imagem, String video, boolean ativa) {
		this(identificador, new NumeroAbertura(numero), nome, artista, episodios, imagem, video,
				StatusAbertura.deBooleano(ativa));
	}

	private static String validarTexto(String valor, int maximo, String mensagemVazio, String mensagemTamanho) {
		var limpo = Objects.requireNonNull(valor, mensagemVazio).trim();
		Validate.notBlank(limpo, mensagemVazio);
		Validate.isTrue(limpo.length() <= maximo, mensagemTamanho);
		return limpo;
	}

	@Override
	public Optional<AberturaIdentificador> getIdentificador() {
		return identificador;
	}

	public NumeroAbertura getNumero() {
		return numero;
	}

	public int getNumeroValor() {
		return numero.getValor();
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

	public StatusAbertura getStatus() {
		return status;
	}

	public boolean isAtiva() {
		return status.isAtiva();
	}

	public Abertura editar(NumeroAbertura novoNumero, String novoNome, String novoArtista, String novosEpisodios,
			String novaImagem, String novoVideo, StatusAbertura novoStatus) {
		return new Abertura(identificador, novoNumero, novoNome, novoArtista, novosEpisodios, novaImagem, novoVideo,
				novoStatus);
	}

	public Abertura inativar() {
		return new Abertura(identificador, numero, nome, artista, episodios, imagem, video, StatusAbertura.INATIVA);
	}

	public Abertura reativar() {
		return new Abertura(identificador, numero, nome, artista, episodios, imagem, video, StatusAbertura.ATIVA);
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (Abertura) objeto;
		return Objects.equals(identificador, outra.identificador) && Objects.equals(numero, outra.numero);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identificador, numero);
	}
}
