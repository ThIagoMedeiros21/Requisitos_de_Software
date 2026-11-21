package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class ComposicaoDeck implements IObjetoValor {
	public static final int TAMANHO_MAIN_DECK_BANDAI = 50;

	private final DeckIdentificador deck;
	private final String nomeDeck;
	private final String lider;
	private final String liderCodigo;
	private final String liderImagem;
	private final String cor;
	private final boolean ativo;
	private final List<ItemComposicaoDeck> itens;

	public ComposicaoDeck(DeckIdentificador deck, String nomeDeck, String lider, String cor, boolean ativo,
			List<ItemComposicaoDeck> itens) {
		this(deck, nomeDeck, lider, extrairCodigoLider(lider, itens), extrairImagemLider(lider, itens), cor, ativo,
				itens);
	}

	public ComposicaoDeck(DeckIdentificador deck, String nomeDeck, String lider, String liderCodigo, String liderImagem,
			String cor, boolean ativo, List<ItemComposicaoDeck> itens) {
		this.deck = Objects.requireNonNull(deck);
		this.nomeDeck = Validate.notBlank(nomeDeck, "O nome do deck é obrigatório");
		this.lider = Validate.notBlank(lider, "O líder do deck é obrigatório");
		this.liderCodigo = Validate.notBlank(liderCodigo, "O código do líder é obrigatório");
		this.liderImagem = Validate.notBlank(liderImagem, "A imagem do líder é obrigatória");
		this.cor = Validate.notBlank(cor, "A cor do deck é obrigatória");
		this.ativo = ativo;
		this.itens = List.copyOf(Objects.requireNonNull(itens));
	}

	private static String extrairCodigoLider(String lider, List<ItemComposicaoDeck> itens) {
		return itens.stream().filter(i -> "Leader".equalsIgnoreCase(i.getCardType())).findFirst()
				.map(i -> i.getCarta().toString()).orElseGet(() -> {
					int abre = lider.lastIndexOf('(');
					int fecha = lider.lastIndexOf(')');
					if (abre >= 0 && fecha > abre + 1) {
						return lider.substring(abre + 1, fecha).trim();
					}
					return lider.trim();
				});
	}

	private static String extrairImagemLider(String lider, List<ItemComposicaoDeck> itens) {
		return itens.stream().filter(i -> "Leader".equalsIgnoreCase(i.getCardType())).findFirst()
				.map(ItemComposicaoDeck::getCardImage)
				.orElse("https://optcgapi.com/media/static/Card_Images/" + extrairCodigoLider(lider, itens) + ".jpg");
	}

	public DeckIdentificador getDeck() {
		return deck;
	}

	public String getNomeDeck() {
		return nomeDeck;
	}

	public String getLider() {
		return lider;
	}

	public String getLiderCodigo() {
		return liderCodigo;
	}

	public String getLiderImagem() {
		return liderImagem;
	}

	public String getCor() {
		return cor;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public List<ItemComposicaoDeck> getItens() {
		return itens;
	}

	public BigDecimal calcularCustoEstimadoMercado() {
		return itens.stream().map(ItemComposicaoDeck::calcularSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int contarCartasPorTipo(String tipo) {
		Objects.requireNonNull(tipo);
		return itens.stream().filter(i -> tipo.equalsIgnoreCase(i.getCardType()))
				.mapToInt(ItemComposicaoDeck::getQuantidade).sum();
	}

	public int contarCartasMainDeck() {
		return itens.stream().filter(i -> !"Leader".equalsIgnoreCase(i.getCardType()))
				.mapToInt(ItemComposicaoDeck::getQuantidade).sum();
	}

	public int contarTotalComLider() {
		return itens.stream().mapToInt(ItemComposicaoDeck::getQuantidade).sum();
	}

	public boolean isConformidadeBandai() {
		int lideres = contarCartasPorTipo("Leader");
		int mainDeck = contarCartasMainDeck();
		boolean copiasValidas = itens.stream()
				.allMatch(i -> i.getQuantidade() >= 1 && i.getQuantidade() <= ItemComposicaoDeck.MAX_COPIAS_BANDAI);
		return lideres == 1 && mainDeck == TAMANHO_MAIN_DECK_BANDAI && copiasValidas;
	}

	public String formatarDecklistBandai() {
		var linhas = itens.stream()
				.sorted((a, b) -> {
					boolean aLider = "Leader".equalsIgnoreCase(a.getCardType());
					boolean bLider = "Leader".equalsIgnoreCase(b.getCardType());
					if (aLider != bLider) {
						return aLider ? -1 : 1;
					}
					return a.getCarta().toString().compareTo(b.getCarta().toString());
				})
				.map(item -> item.getQuantidade() + "x" + item.getCarta().toString())
				.collect(Collectors.toList());
		if (linhas.isEmpty()) {
			return "1x" + liderCodigo;
		}
		return String.join("\n", linhas);
	}

	public ComposicaoDeck comStatusAtivo(boolean novoStatus) {
		return new ComposicaoDeck(deck, nomeDeck, lider, liderCodigo, liderImagem, cor, novoStatus, itens);
	}

	public Optional<ItemComposicaoDeck> buscarItem(String codigoCarta) {
		Objects.requireNonNull(codigoCarta);
		return itens.stream().filter(i -> i.getCarta().toString().equalsIgnoreCase(codigoCarta)).findFirst();
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (ComposicaoDeck) objeto;
		return ativo == outro.ativo && Objects.equals(deck, outro.deck) && Objects.equals(nomeDeck, outro.nomeDeck)
				&& Objects.equals(lider, outro.lider) && Objects.equals(cor, outro.cor)
				&& Objects.equals(itens, outro.itens);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deck, nomeDeck, lider, cor, ativo, itens);
	}
}
