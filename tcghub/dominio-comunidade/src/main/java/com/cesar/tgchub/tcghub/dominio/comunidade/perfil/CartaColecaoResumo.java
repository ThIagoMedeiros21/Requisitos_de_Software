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
	private final String nomeColecao;
	private final String raridade;
	private final String edicaoCondicao;
	private final String cor;
	private final String tipoCarta;
	private final String custoCarta;
	private final String poderCarta;
	private final String subTipos;
	private final int contraAtaque;
	private final String atributo;
	private final BigDecimal precoMercado;
	private final BigDecimal precoInventario;
	private final String imagemId;
	private final Optional<String> imagem;

	public CartaColecaoResumo(CartaIdentificador carta, String nomeCarta, String colecao) {
		this(carta, carta.toString(), nomeCarta, colecao, "Romance Dawn", "SR", colecao + " · Near Mint", "Red",
				"Character", "3", "5000", "Straw Hat Crew", 1000, "Slash", new BigDecimal("45.00"),
				new BigDecimal("40.00"), carta.toString(),
				Optional.of("https://optcgapi.com/media/static/Card_Images/" + carta + ".jpg"));
	}

	public CartaColecaoResumo(CartaIdentificador carta, String codigo, String nomeCarta, String colecao,
			String raridade, String edicaoCondicao, String cor, BigDecimal precoMercado, Optional<String> imagem) {
		this(carta, codigo, nomeCarta, colecao, "Romance Dawn", raridade, edicaoCondicao, cor, "Character", "3", "5000",
				"Straw Hat Crew", 1000, "Slash", precoMercado, precoMercado, codigo, imagem);
	}

	public CartaColecaoResumo(CartaIdentificador carta, String codigo, String nomeCarta, String colecao,
			String nomeColecao, String raridade, String edicaoCondicao, String cor, String tipoCarta,
			String custoCarta, String poderCarta, String subTipos, int contraAtaque, String atributo,
			BigDecimal precoMercado, BigDecimal precoInventario, String imagemId, Optional<String> imagem) {
		this.carta = Objects.requireNonNull(carta);
		this.codigo = Validate.notBlank(codigo, "O código da carta é obrigatório");
		this.nomeCarta = Validate.notBlank(nomeCarta, "O nome da carta é obrigatório");
		this.colecao = Validate.notBlank(colecao, "A coleção da carta é obrigatória");
		this.nomeColecao = Validate.notBlank(nomeColecao, "O nome da coleção é obrigatório");
		this.raridade = Validate.notBlank(raridade, "A raridade da carta é obrigatória");
		this.edicaoCondicao = Validate.notBlank(edicaoCondicao, "A edição e condição da carta são obrigatórias");
		this.cor = Validate.notBlank(cor, "A cor da carta é obrigatória");
		this.tipoCarta = Validate.notBlank(tipoCarta, "O tipo da carta é obrigatório");
		this.custoCarta = Validate.notBlank(custoCarta, "O custo da carta é obrigatório");
		this.poderCarta = Validate.notBlank(poderCarta, "O poder da carta é obrigatório");
		this.subTipos = Validate.notBlank(subTipos, "Os subtipos da carta são obrigatórios");
		Validate.isTrue(contraAtaque >= 0, "O valor de contra-ataque não pode ser negativo");
		this.contraAtaque = contraAtaque;
		this.atributo = Validate.notBlank(atributo, "O atributo da carta é obrigatório");
		this.precoMercado = Objects.requireNonNull(precoMercado);
		Validate.isTrue(precoMercado.compareTo(BigDecimal.ZERO) >= 0, "O preço de mercado não pode ser negativo");
		this.precoInventario = Objects.requireNonNull(precoInventario);
		Validate.isTrue(precoInventario.compareTo(BigDecimal.ZERO) >= 0,
				"O preço de inventário não pode ser negativo");
		this.imagemId = Validate.notBlank(imagemId, "O identificador de imagem da carta é obrigatório");
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

	public String getNomeColecao() {
		return nomeColecao;
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

	public String getTipoCarta() {
		return tipoCarta;
	}

	public String getCustoCarta() {
		return custoCarta;
	}

	public String getPoderCarta() {
		return poderCarta;
	}

	public String getSubTipos() {
		return subTipos;
	}

	public int getContraAtaque() {
		return contraAtaque;
	}

	public String getAtributo() {
		return atributo;
	}

	public BigDecimal getPrecoMercado() {
		return precoMercado;
	}

	public BigDecimal getPrecoInventario() {
		return precoInventario;
	}

	public String getImagemId() {
		return imagemId;
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
