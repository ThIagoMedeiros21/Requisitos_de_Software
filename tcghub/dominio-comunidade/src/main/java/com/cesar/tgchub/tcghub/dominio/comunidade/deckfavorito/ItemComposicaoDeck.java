package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class ItemComposicaoDeck implements IObjetoValor {
	public static final int MAX_COPIAS_BANDAI = 4;

	private final CartaIdentificador carta;
	private final String nomeCarta;
	private final String setId;
	private final String setName;
	private final String rarity;
	private final String cardColor;
	private final String cardType;
	private final Optional<String> cardCost;
	private final Optional<String> cardPower;
	private final Optional<String> subTypes;
	private final Optional<String> counterAmount;
	private final Optional<String> attribute;
	private final int quantidade;
	private final BigDecimal precoUnitario;
	private final BigDecimal inventoryPrice;
	private final String cardImageId;
	private final String cardImage;

	public ItemComposicaoDeck(CartaIdentificador carta, String nomeCarta, int quantidade, BigDecimal precoUnitario) {
		this(carta, nomeCarta, "OP-01", "Romance Dawn", "R", "Red", "Character", Optional.of("1"), Optional.of("2000"),
				Optional.of("Straw Hat Crew"), Optional.of("1000"), Optional.of("Special"), quantidade, precoUnitario,
				precoUnitario, carta.toString(),
				"https://optcgapi.com/media/static/Card_Images/" + carta + ".jpg");
	}

	public ItemComposicaoDeck(CartaIdentificador carta, String nomeCarta, String setId, String setName, String rarity,
			String cardColor, String cardType, Optional<String> cardCost, Optional<String> cardPower,
			Optional<String> subTypes, Optional<String> counterAmount, Optional<String> attribute, int quantidade,
			BigDecimal precoUnitario, BigDecimal inventoryPrice, String cardImageId, String cardImage) {
		this.carta = Objects.requireNonNull(carta);
		this.nomeCarta = Validate.notBlank(nomeCarta, "O nome da carta é obrigatório");
		this.setId = Validate.notBlank(setId, "O código da coleção é obrigatório");
		this.setName = Validate.notBlank(setName, "O nome da coleção é obrigatório");
		this.rarity = Validate.notBlank(rarity, "A raridade é obrigatória");
		this.cardColor = Validate.notBlank(cardColor, "A cor da carta é obrigatória");
		this.cardType = Validate.notBlank(cardType, "O tipo da carta é obrigatório");
		this.cardCost = Objects.requireNonNull(cardCost);
		this.cardPower = Objects.requireNonNull(cardPower);
		this.subTypes = Objects.requireNonNull(subTypes);
		this.counterAmount = Objects.requireNonNull(counterAmount);
		this.attribute = Objects.requireNonNull(attribute);
		Validate.isTrue(quantidade >= 1 && quantidade <= MAX_COPIAS_BANDAI,
				"A quantidade da carta no deck deve estar entre 1 e %d", MAX_COPIAS_BANDAI);
		this.quantidade = quantidade;
		this.precoUnitario = Objects.requireNonNull(precoUnitario);
		Validate.isTrue(precoUnitario.compareTo(BigDecimal.ZERO) >= 0, "O preço unitário não pode ser negativo");
		this.inventoryPrice = Objects.requireNonNull(inventoryPrice);
		Validate.isTrue(inventoryPrice.compareTo(BigDecimal.ZERO) >= 0, "O preço de inventário não pode ser negativo");
		this.cardImageId = Validate.notBlank(cardImageId, "O identificador da imagem é obrigatório");
		this.cardImage = Validate.notBlank(cardImage, "A URL da imagem é obrigatória");
	}

	public CartaIdentificador getCarta() {
		return carta;
	}

	public String getNomeCarta() {
		return nomeCarta;
	}

	public String getSetId() {
		return setId;
	}

	public String getSetName() {
		return setName;
	}

	public String getRarity() {
		return rarity;
	}

	public String getCardColor() {
		return cardColor;
	}

	public String getCardType() {
		return cardType;
	}

	public Optional<String> getCardCost() {
		return cardCost;
	}

	public Optional<String> getCardPower() {
		return cardPower;
	}

	public Optional<String> getSubTypes() {
		return subTypes;
	}

	public Optional<String> getCounterAmount() {
		return counterAmount;
	}

	public Optional<String> getAttribute() {
		return attribute;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public BigDecimal getInventoryPrice() {
		return inventoryPrice;
	}

	public String getCardImageId() {
		return cardImageId;
	}

	public String getCardImage() {
		return cardImage;
	}

	public BigDecimal calcularSubtotal() {
		return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (ItemComposicaoDeck) objeto;
		return quantidade == outro.quantidade && Objects.equals(carta, outro.carta)
				&& Objects.equals(nomeCarta, outro.nomeCarta)
				&& precoUnitario.compareTo(outro.precoUnitario) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carta, nomeCarta, quantidade, precoUnitario.stripTrailingZeros());
	}
}
