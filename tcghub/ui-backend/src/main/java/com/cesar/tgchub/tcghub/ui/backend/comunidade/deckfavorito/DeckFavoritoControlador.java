package com.cesar.tgchub.tcghub.ui.backend.comunidade.deckfavorito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.CatalogoIndisponivelExcecao;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ComposicaoDeck;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ICatalogoDeckProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.ItemComposicaoDeck;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "comunidade/deck-favorito")
public class DeckFavoritoControlador {
	private final ISegurancaServico segurancaServico;
	private final DeckFavoritoServico deckFavoritoServico;
	private final ICatalogoDeckProxy catalogoDeckProxy;
	private final IPerfilComunitarioRepositorio perfilRepositorio;
	private final PerfilComunitarioServico perfilServico;

	public DeckFavoritoControlador(ISegurancaServico segurancaServico, DeckFavoritoServico deckFavoritoServico,
			ICatalogoDeckProxy catalogoDeckProxy, IPerfilComunitarioRepositorio perfilRepositorio,
			PerfilComunitarioServico perfilServico) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
		this.deckFavoritoServico = Objects.requireNonNull(deckFavoritoServico);
		this.catalogoDeckProxy = Objects.requireNonNull(catalogoDeckProxy);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
		this.perfilServico = Objects.requireNonNull(perfilServico);
	}

	@GetMapping("pesquisa")
	public DecksFavoritosListaRecurso pesquisarProprios() {
		var solicitante = usuarioAutenticado();
		var perfil = perfilRepositorio.buscarPorUsuario(solicitante);
		return montarLista(solicitante, perfil);
	}

	@GetMapping("perfil/{perfilId}")
	public DecksFavoritosListaRecurso pesquisarPorPerfil(@PathVariable PerfilComunitarioIdentificador perfilId) {
		Objects.requireNonNull(perfilId);
		var solicitante = usuarioAutenticado();
		var perfil = perfilServico.buscar(perfilId);
		return montarLista(solicitante, perfil);
	}

	@GetMapping("catalogo")
	public List<DeckCatalogoResumoRecurso> pesquisarCatalogo(@RequestParam(required = false) @Nullable String termo,
			@RequestParam(required = false) @Nullable String cor) {
		var solicitante = usuarioAutenticado();
		var filtroTermo = Objects.requireNonNullElse(termo, "").trim();
		var filtroCor = Objects.requireNonNullElse(cor, "").trim().toLowerCase(Locale.ROOT);

		var favoritosUsuario = deckFavoritoServico.listarVisiveisNoPerfil(solicitante, solicitante).stream()
				.map(fav -> fav.getDeck().toString()).collect(Collectors.toSet());

		return deckFavoritoServico.pesquisarDecksDisponiveis(filtroTermo).stream()
				.filter(deck -> filtroCor.isEmpty() || deck.getCor().toLowerCase(Locale.ROOT).contains(filtroCor))
				.map(deck -> new DeckCatalogoResumoRecurso(deck.getDeck().toString(), deck.getNomeDeck(),
						deck.getLider(), deck.getLiderCodigo(), deck.getLiderImagem(), deck.getCor(), deck.isAtivo(),
						deck.contarCartasMainDeck(), deck.calcularCustoEstimadoMercado(),
						favoritosUsuario.contains(deck.getDeck().toString())))
				.toList();
	}

	@GetMapping("{deckId}/composicao")
	public ComposicaoDeckRecurso obterComposicao(@PathVariable String deckId,
			@RequestParam(required = false) @Nullable String perfilId) {
		Objects.requireNonNull(deckId);
		var solicitante = usuarioAutenticado();
		var alvo = (perfilId != null && !perfilId.isBlank())
				? perfilServico.buscar(new PerfilComunitarioIdentificador(perfilId.trim())).getUsuario()
				: solicitante;
		var composicao = deckFavoritoServico.obterComposicaoExpandida(solicitante, alvo,
				new DeckIdentificador(deckId.trim()));
		return paraComposicaoRecurso(composicao);
	}

	@PostMapping("incluir")
	public DeckFavoritoItemRecurso incluir(@RequestBody IncluirDeckFavoritoRequisicao requisicao) {
		Objects.requireNonNull(requisicao);
		var solicitante = usuarioAutenticado();
		var deckId = new DeckIdentificador(Objects.requireNonNullElse(requisicao.deckId(), "").trim());
		var salvo = deckFavoritoServico.adicionar(solicitante, solicitante, deckId);
		return paraItemRecurso(salvo);
	}

	@PostMapping("{deckId}/excluir")
	public void excluir(@PathVariable String deckId) {
		Objects.requireNonNull(deckId);
		var solicitante = usuarioAutenticado();
		deckFavoritoServico.remover(solicitante, solicitante, new DeckIdentificador(deckId.trim()));
	}

	@PostMapping("ocultar")
	public void ocultar() {
		var solicitante = usuarioAutenticado();
		deckFavoritoServico.ocultarNoPerfilPublico(solicitante, solicitante);
	}

	@PostMapping("reexibir")
	public void reexibir() {
		var solicitante = usuarioAutenticado();
		deckFavoritoServico.reexibirNoPerfilPublico(solicitante, solicitante);
	}

	@ExceptionHandler(CatalogoIndisponivelExcecao.class)
	public ResponseEntity<Map<String, String>> tratarCatalogoIndisponivel(CatalogoIndisponivelExcecao excecao) {
		var mensagem = Objects.requireNonNullElse(excecao.getMessage(),
				"Catálogo de decks temporariamente indisponível");
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of("mensagem", mensagem));
	}

	private DecksFavoritosListaRecurso montarLista(UsuarioIdentificador solicitante, PerfilComunitario perfil) {
		var ehProprietario = solicitante.equals(perfil.getUsuario());
		var favoritos = deckFavoritoServico.listarVisiveisNoPerfil(solicitante, perfil.getUsuario());
		var itens = favoritos.stream().map(this::paraItemRecurso).toList();
		return new DecksFavoritosListaRecurso(perfil.getIdentificador().orElseThrow().toString(), itens.size(),
				DeckFavorito.MAX_DECKS_FAVORITOS, perfil.isDecksFavoritosVisiveis(), ehProprietario, itens);
	}

	private DeckFavoritoItemRecurso paraItemRecurso(DeckFavorito favorito) {
		var composicao = catalogoDeckProxy.obterComposicaoDeck(favorito.getDeck());
		return new DeckFavoritoItemRecurso(favorito.getIdentificador().orElseThrow().toString(),
				favorito.getDeck().toString(), favorito.getOrdem(), favorito.getAdicionadoEm(),
				composicao.getNomeDeck(), composicao.getLider(), composicao.getLiderCodigo(),
				composicao.getLiderImagem(), composicao.getCor(), composicao.isAtivo(),
				composicao.contarCartasMainDeck(), composicao.calcularCustoEstimadoMercado());
	}

	private static ComposicaoDeckRecurso paraComposicaoRecurso(ComposicaoDeck composicao) {
		var totaisPorTipo = new LinkedHashMap<String, Integer>();
		totaisPorTipo.put("Leader", composicao.contarCartasPorTipo("Leader"));
		totaisPorTipo.put("Character", composicao.contarCartasPorTipo("Character"));
		totaisPorTipo.put("Event", composicao.contarCartasPorTipo("Event"));
		totaisPorTipo.put("Stage", composicao.contarCartasPorTipo("Stage"));

		var itens = composicao.getItens().stream().map(DeckFavoritoControlador::paraItemComposicaoRecurso).toList();
		return new ComposicaoDeckRecurso(composicao.getDeck().toString(), composicao.getNomeDeck(),
				composicao.getLider(), composicao.getLiderCodigo(), composicao.getLiderImagem(), composicao.getCor(),
				composicao.isAtivo(), composicao.contarCartasMainDeck(), composicao.contarTotalComLider(),
				composicao.isConformidadeBandai(), composicao.calcularCustoEstimadoMercado(), totaisPorTipo,
				composicao.formatarDecklistBandai(), itens);
	}

	private static ItemComposicaoDeckRecurso paraItemComposicaoRecurso(ItemComposicaoDeck item) {
		var codigo = item.getCarta().toString();
		return new ItemComposicaoDeckRecurso(codigo, codigo, item.getNomeCarta(), item.getSetId(), item.getSetName(),
				item.getRarity(), item.getCardColor(), item.getCardType(), item.getCardCost().orElse(null),
				item.getCardPower().orElse(null), item.getSubTypes().orElse(null), item.getCounterAmount().orElse(null),
				item.getAttribute().orElse(null), item.getPrecoUnitario(), item.getInventoryPrice(),
				item.getCardImageId(), item.getCardImage(), item.getQuantidade(), item.calcularSubtotal());
	}

	private UsuarioIdentificador usuarioAutenticado() {
		return new UsuarioIdentificador(segurancaServico.getUsuario());
	}

	public record IncluirDeckFavoritoRequisicao(@Nullable String deckId) {
	}

	public record DeckFavoritoItemRecurso(String id, String deckId, int ordem, Instant adicionadoEm, String nomeDeck,
			String lider, String liderCodigo, String liderImagem, String cor, boolean ativo, int totalCartasMainDeck,
			BigDecimal custoEstimadoMercado) {
	}

	public record DecksFavoritosListaRecurso(String perfilId, int total, int limite, boolean decksFavoritosVisiveis,
			boolean ehProprietario, List<DeckFavoritoItemRecurso> decks) {
	}

	public record DeckCatalogoResumoRecurso(String deckId, String nomeDeck, String lider, String liderCodigo,
			String liderImagem, String cor, boolean ativo, int totalCartasMainDeck, BigDecimal custoEstimadoMercado,
			boolean jaFavoritado) {
	}

	public record ItemComposicaoDeckRecurso(String cartaId, String card_set_id, String card_name, String set_id,
			String set_name, String rarity, String card_color, String card_type, @Nullable String card_cost,
			@Nullable String card_power, @Nullable String sub_types, @Nullable String counter_amount,
			@Nullable String attribute, BigDecimal market_price, BigDecimal inventory_price, String card_image_id,
			String card_image, int quantidade, BigDecimal subtotal) {
	}

	public record ComposicaoDeckRecurso(String deckId, String nomeDeck, String lider, String liderCodigo,
			String liderImagem, String cor, boolean ativo, int totalCartasMainDeck, int totalCartasComLider,
			boolean conformidadeBandai, BigDecimal custoEstimadoMercado, Map<String, Integer> totaisPorTipo,
			String decklistBandai, List<ItemComposicaoDeckRecurso> itens) {
	}
}
