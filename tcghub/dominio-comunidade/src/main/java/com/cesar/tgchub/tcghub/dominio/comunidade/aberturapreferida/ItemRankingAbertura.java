package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class ItemRankingAbertura implements IObjetoValor {
	private final int posicao;
	private final AberturaIdentificador abertura;
	private final int numero;
	private final String nome;
	private final String artista;
	private final String imagem;
	private final int quantidadeEscolhas;
	private final BigDecimal percentual;

	public ItemRankingAbertura(int posicao, AberturaIdentificador abertura, int numero, String nome, String artista,
			String imagem, int quantidadeEscolhas, BigDecimal percentual) {
		Validate.isTrue(posicao >= 1, "A posição no ranking deve ser maior ou igual a 1");
		Validate.isTrue(numero > 0, "O número da abertura deve ser maior que zero");
		Validate.isTrue(quantidadeEscolhas >= 0, "A quantidade de escolhas não pode ser negativa");
		this.posicao = posicao;
		this.abertura = Objects.requireNonNull(abertura);
		this.numero = numero;
		this.nome = Validate.notBlank(nome, "O nome da abertura é obrigatório");
		this.artista = Validate.notBlank(artista, "O artista da abertura é obrigatório");
		this.imagem = Validate.notBlank(imagem, "A imagem da abertura é obrigatória");
		this.quantidadeEscolhas = quantidadeEscolhas;
		this.percentual = Objects.requireNonNull(percentual);
	}

	public int getPosicao() {
		return posicao;
	}

	public AberturaIdentificador getAbertura() {
		return abertura;
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

	public String getImagem() {
		return imagem;
	}

	public int getQuantidadeEscolhas() {
		return quantidadeEscolhas;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (ItemRankingAbertura) objeto;
		return posicao == outro.posicao && numero == outro.numero && quantidadeEscolhas == outro.quantidadeEscolhas
				&& Objects.equals(abertura, outro.abertura) && Objects.equals(nome, outro.nome)
				&& Objects.equals(artista, outro.artista) && percentual.compareTo(outro.percentual) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(posicao, abertura, numero, nome, artista, quantidadeEscolhas,
				percentual.stripTrailingZeros());
	}
}
