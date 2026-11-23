package com.cesar.tgchub.tcghub.ui.backend.comunidade.avaliacaoproduto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.IAvaliacaoElegibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.NotaAvaliacao;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.ResumoAvaliacoesProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaColecaoResumo;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "comunidade/avaliacao-produto")
public class AvaliacaoProdutoControlador {
	private final ISegurancaServico segurancaServico;
	private final AvaliacaoProdutoServico avaliacaoServico;
	private final IAvaliacaoElegibilidadeProxy elegibilidadeProxy;
	private final IAvaliacaoProdutoRepositorio avaliacaoRepositorio;
	private final CompraVerificadaServico compraServico;
	private final ICompraVerificadaRepositorio compraRepositorio;
	private final IColecaoCartaConsulta colecaoCartaConsulta;
	private final IPerfilComunitarioRepositorio perfilRepositorio;

	public AvaliacaoProdutoControlador(ISegurancaServico segurancaServico, AvaliacaoProdutoServico avaliacaoServico,
			IAvaliacaoElegibilidadeProxy elegibilidadeProxy, IAvaliacaoProdutoRepositorio avaliacaoRepositorio,
			CompraVerificadaServico compraServico, ICompraVerificadaRepositorio compraRepositorio,
			IColecaoCartaConsulta colecaoCartaConsulta, IPerfilComunitarioRepositorio perfilRepositorio) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
		this.avaliacaoServico = Objects.requireNonNull(avaliacaoServico);
		this.elegibilidadeProxy = Objects.requireNonNull(elegibilidadeProxy);
		this.avaliacaoRepositorio = Objects.requireNonNull(avaliacaoRepositorio);
		this.compraServico = Objects.requireNonNull(compraServico);
		this.compraRepositorio = Objects.requireNonNull(compraRepositorio);
		this.colecaoCartaConsulta = Objects.requireNonNull(colecaoCartaConsulta);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
	}

	@GetMapping("catalogo")
	public List<CartaCatalogoAvaliacaoRecurso> listarCatalogo(@RequestParam(required = false) @Nullable String termo) {
		var solicitante = usuarioAutenticado();
		var filtro = Objects.requireNonNullElse(termo, "").trim().toLowerCase(Locale.ROOT);

		return colecaoCartaConsulta.listarCartasDisponiveis(solicitante).stream()
				.filter(carta -> filtro.isEmpty()
						|| carta.getCodigo().toLowerCase(Locale.ROOT).contains(filtro)
						|| carta.getNomeCarta().toLowerCase(Locale.ROOT).contains(filtro)
						|| carta.getNomeColecao().toLowerCase(Locale.ROOT).contains(filtro)
						|| carta.getCor().toLowerCase(Locale.ROOT).contains(filtro))
				.map(carta -> montarItemCatalogo(solicitante, carta)).toList();
	}

	@GetMapping("{produtoId}/pesquisa")
	public DetalhesAvaliacoesCartaRecurso consultarDetalhes(@PathVariable String produtoId) {
		Objects.requireNonNull(produtoId);
		var solicitante = usuarioAutenticado();
		var produto = new ProdutoIdentificador(produtoId.trim());

		var cartaRecurso = resolverCartaRecurso(solicitante, produto);
		var resumo = avaliacaoServico.consultarResumo(produto);
		var elegibilidade = elegibilidadeProxy.verificarElegibilidade(solicitante, produto);
		var minhaAvaliacao = buscarMinhaAvaliacaoValida(solicitante, produto);
		var compras = compraRepositorio.listarPorUsuarioEProduto(solicitante, produto).stream()
				.map(AvaliacaoProdutoControlador::paraCompraRecurso).toList();
		var avaliacoes = avaliacaoServico.listarAvaliacoesValidas(produto).stream()
				.map(this::paraAvaliacaoRecurso).toList();

		return new DetalhesAvaliacoesCartaRecurso(cartaRecurso, paraResumoRecurso(resumo),
				elegibilidade.isPodeAvaliar(), elegibilidade.getMotivoInelegibilidade().orElse(null), minhaAvaliacao,
				compras, avaliacoes);
	}

	@PostMapping("{produtoId}/avaliar")
	public AvaliacaoItemRecurso avaliar(@PathVariable String produtoId,
			@RequestBody AvaliarCartaRequisicao requisicao) {
		Objects.requireNonNull(produtoId);
		Objects.requireNonNull(requisicao);

		var solicitante = usuarioAutenticado();
		var autor = (requisicao.autorUsuario() != null && !requisicao.autorUsuario().isBlank())
				? new UsuarioIdentificador(requisicao.autorUsuario().trim())
				: solicitante;
		var produto = new ProdutoIdentificador(produtoId.trim());
		var nota = new NotaAvaliacao(requisicao.nota());
		var comentario = Optional.ofNullable(requisicao.comentario());

		var salva = elegibilidadeProxy.registrarOuAtualizarAvaliacao(solicitante, autor, produto, nota, comentario);
		return paraAvaliacaoRecurso(salva);
	}

	@PostMapping("compras/registrar")
	public CompraVerificadaItemRecurso registrarCompra(@RequestBody RegistrarCompraRequisicao requisicao) {
		Objects.requireNonNull(requisicao);
		var solicitante = usuarioAutenticado();
		var pedido = new PedidoIdentificador(Objects.requireNonNullElse(requisicao.pedidoId(), "").trim());
		var produto = new ProdutoIdentificador(Objects.requireNonNullElse(requisicao.produtoId(), "").trim());
		var compra = requisicao.paga()
				? compraServico.registrarCompraPaga(solicitante, pedido, produto)
				: compraServico.registrarCompraPendente(solicitante, pedido, produto);
		return paraCompraRecurso(compra);
	}

	@PostMapping("compras/{pedidoId}/reembolsar")
	public ResumoAvaliacaoRecurso reembolsarCompra(@PathVariable String pedidoId,
			@RequestBody ReembolsarCompraRequisicao requisicao) {
		Objects.requireNonNull(pedidoId);
		Objects.requireNonNull(requisicao);
		var solicitante = usuarioAutenticado();
		var pedido = new PedidoIdentificador(pedidoId.trim());
		var produto = new ProdutoIdentificador(Objects.requireNonNullElse(requisicao.produtoId(), "").trim());
		var resumoAtualizado = avaliacaoServico.invalidarPorReembolso(solicitante, pedido, produto);
		return paraResumoRecurso(resumoAtualizado);
	}

	private CartaCatalogoAvaliacaoRecurso montarItemCatalogo(UsuarioIdentificador solicitante,
			CartaColecaoResumo carta) {
		var produto = new ProdutoIdentificador(carta.getCarta().toString());
		var resumo = avaliacaoServico.consultarResumo(produto);
		var elegibilidade = elegibilidadeProxy.verificarElegibilidade(solicitante, produto);
		var minhaAvaliacao = buscarMinhaAvaliacaoValida(solicitante, produto);
		var img = carta.getImagem().orElse(null);
		return new CartaCatalogoAvaliacaoRecurso(produto.toString(), carta.getCodigo(), carta.getNomeCarta(),
				carta.getColecao(), carta.getNomeColecao(), carta.getRaridade(), carta.getCor(), carta.getTipoCarta(),
				carta.getCustoCarta(), carta.getPoderCarta(), carta.getPrecoMercado(), carta.getPrecoInventario(),
				carta.getImagemId(), img, resumo.getMedia(), resumo.getQuantidadeValida(),
				converterDistribuicao(resumo.getDistribuicaoNotas()), elegibilidade.isPodeAvaliar(),
				elegibilidade.getMotivoInelegibilidade().orElse(null), minhaAvaliacao);
	}

	private CartaMetadadosRecurso resolverCartaRecurso(UsuarioIdentificador solicitante, ProdutoIdentificador produto) {
		var encontrada = colecaoCartaConsulta.listarCartasDisponiveis(solicitante).stream()
				.filter(c -> c.getCarta().toString().equalsIgnoreCase(produto.toString())).findFirst();
		if (encontrada.isPresent()) {
			var c = encontrada.get();
			return new CartaMetadadosRecurso(produto.toString(), c.getCodigo(), c.getNomeCarta(), c.getColecao(),
					c.getNomeColecao(), c.getRaridade(), c.getCor(), c.getTipoCarta(), c.getCustoCarta(),
					c.getPoderCarta(), c.getPrecoMercado(), c.getPrecoInventario(), c.getImagemId(),
					c.getImagem().orElse(null));
		}
		var codigo = produto.toString();
		var imgPadrao = "https://optcgapi.com/media/static/Card_Images/" + codigo + ".jpg";
		return new CartaMetadadosRecurso(codigo, codigo, codigo, "OP-01", "One Piece TCG", "SR", "Red", "Character",
				"3", "5000", BigDecimal.ZERO, BigDecimal.ZERO, codigo, imgPadrao);
	}

	private @Nullable AvaliacaoItemRecurso buscarMinhaAvaliacaoValida(UsuarioIdentificador usuario,
			ProdutoIdentificador produto) {
		if (!avaliacaoRepositorio.existePorUsuarioEProduto(usuario, produto)) {
			return null;
		}
		var av = avaliacaoRepositorio.buscarPorUsuarioEProduto(usuario, produto);
		return av.isValida() ? paraAvaliacaoRecurso(av) : null;
	}

	private AvaliacaoItemRecurso paraAvaliacaoRecurso(AvaliacaoProduto av) {
		var apelido = perfilRepositorio.existePorUsuario(av.getUsuario())
				? perfilRepositorio.buscarPorUsuario(av.getUsuario()).getApelido().toString()
				: av.getUsuario().toString();
		return new AvaliacaoItemRecurso(av.getIdentificador().orElseThrow().toString(), av.getUsuario().toString(),
				apelido, av.getProduto().toString(), av.getNotaValor(), av.getComentario().orElse(null),
				av.isCompraVerificada(), av.getSelo(), av.isValida(), av.getAvaliadoEm());
	}

	private static CompraVerificadaItemRecurso paraCompraRecurso(CompraVerificada compra) {
		return new CompraVerificadaItemRecurso(compra.getIdentificador().orElseThrow().toString(),
				compra.getPedido().toString(), compra.getProduto().toString(), compra.isPaga(), compra.isReembolsada(),
				compra.isElegivel(), compra.getAtualizadoEm());
	}

	private static ResumoAvaliacaoRecurso paraResumoRecurso(ResumoAvaliacoesProduto resumo) {
		return new ResumoAvaliacaoRecurso(resumo.getProduto().toString(), resumo.getMedia(),
				resumo.getQuantidadeValida(), converterDistribuicao(resumo.getDistribuicaoNotas()));
	}

	private static Map<String, Integer> converterDistribuicao(Map<Integer, Integer> distribuicao) {
		Map<String, Integer> mapa = new LinkedHashMap<>();
		for (int n = NotaAvaliacao.MAXIMO; n >= NotaAvaliacao.MINIMO; n--) {
			mapa.put(Integer.toString(n), distribuicao.getOrDefault(n, 0));
		}
		return mapa;
	}

	private UsuarioIdentificador usuarioAutenticado() {
		return new UsuarioIdentificador(segurancaServico.getUsuario());
	}

	public record AvaliarCartaRequisicao(int nota, @Nullable String comentario, @Nullable String autorUsuario) {
	}

	public record RegistrarCompraRequisicao(@Nullable String pedidoId, @Nullable String produtoId, boolean paga) {
	}

	public record ReembolsarCompraRequisicao(@Nullable String produtoId) {
	}

	public record AvaliacaoItemRecurso(String id, String usuario, String apelido, String produtoId, int nota,
			@Nullable String comentario, boolean compraVerificada, String selo, boolean valida, Instant avaliadoEm) {
	}

	public record CompraVerificadaItemRecurso(String id, String pedidoId, String produtoId, boolean paga,
			boolean reembolsada, boolean elegivel, Instant atualizadoEm) {
	}

	public record ResumoAvaliacaoRecurso(String produtoId, BigDecimal media, int quantidadeValida,
			Map<String, Integer> distribuicao) {
	}

	public record CartaMetadadosRecurso(String produtoId, String card_set_id, String card_name, String set_id,
			String set_name, String rarity, String card_color, String card_type, String card_cost, String card_power,
			BigDecimal market_price, BigDecimal inventory_price, String card_image_id, @Nullable String card_image) {
	}

	public record CartaCatalogoAvaliacaoRecurso(String produtoId, String card_set_id, String card_name, String set_id,
			String set_name, String rarity, String card_color, String card_type, String card_cost, String card_power,
			BigDecimal market_price, BigDecimal inventory_price, String card_image_id, @Nullable String card_image,
			BigDecimal media, int quantidadeValida, Map<String, Integer> distribuicao, boolean podeAvaliar,
			@Nullable String motivoInelegibilidade, @Nullable AvaliacaoItemRecurso minhaAvaliacao) {
	}

	public record DetalhesAvaliacoesCartaRecurso(CartaMetadadosRecurso carta, ResumoAvaliacaoRecurso resumo,
			boolean podeAvaliar, @Nullable String motivoInelegibilidade, @Nullable AvaliacaoItemRecurso minhaAvaliacao,
			List<CompraVerificadaItemRecurso> comprasDoUsuario, List<AvaliacaoItemRecurso> avaliacoes) {
	}
}
