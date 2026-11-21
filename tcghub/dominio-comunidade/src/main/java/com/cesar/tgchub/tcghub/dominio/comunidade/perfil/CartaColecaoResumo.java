package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class CartaColecaoResumo implements IObjetoValor {
	private final CartaIdentificador carta;
	private final String codigo;
	private final String nomeCarta;
	private final String colecao;
	private final String raridade;
	private final String edicaoCondicao;
	private final String cor;
	private final BigDecimal precoMercado;
	private final Optional<String> imagem;

	public CartaColecaoResumo(CartaIdentificador carta, String nomeCarta, String colecao) {
		this(carta, carta.toString(), nomeCarta, colecao, "SR", colecao + " · Near Mint", "Vermelho",
				new BigDecimal("45.00"), Optional.empty());
	}

	public CartaColecaoResumo(CartaIdentificador carta, String codigo, String nomeCarta, String colecao,
			String raridade, String edicaoCondicao, String cor, BigDecimal precoMercado, Optional<String> imagem) {
		this.carta = Objects.requireNonNull(carta);
		this.codigo = Validate.notBlank(codigo, "O código da carta é obrigatório");
		this.nomeCarta = Validate.notBlank(nomeCarta, "O nome da carta é obrigatório");
		this.colecao = Validate.notBlank(colecao, "A coleção da carta é obrigatória");
		this.raridade = Validate.notBlank(raridade, "A raridade da carta é obrigatória");
		this.edicaoCondicao = Validate.notBlank(edicaoCondicao, "A edição e condição da carta são obrigatórias");
		this.cor = Validate.notBlank(cor, "A cor da carta é obrigatória");
		this.precoMercado = Objects.requireNonNull(precoMercado);
		Validate.isTrue(precoMercado.compareTo(BigDecimal.ZERO) >= 0, "O preço de mercado não pode ser negativo");
		this.imagem = Objects.requireNonNull(imagem);
	}

	public CartaIdentificador getCarta() {
		return carta;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNomeCarta() {
		return nomeCarta;
	}

	public String getColecao() {
		return colecao;
	}

	public String getRaridade() {
		return raridade;
	}

	public String getEdicaoCondicao() {
		return edicaoCondicao;
	}

	public String getCor() {
		return cor;
	}

	public BigDecimal getPrecoMercado() {
		return precoMercado;
	}

	public Optional<String> getImagem() {
		return imagem;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (CartaColecaoResumo) objeto;
		return Objects.equals(carta, outro.carta) && Objects.equals(codigo, outro.codigo)
				&& Objects.equals(nomeCarta, outro.nomeCarta) && Objects.equals(colecao, outro.colecao);
	}

	@Override
	public int hashCode() {
		return Objects.hash(carta, codigo, nomeCarta, colecao);
	}
}
