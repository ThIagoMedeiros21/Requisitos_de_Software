package com.cesar.tgchub.tcghub.ui.backend.comunidade.perfil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaColecaoResumo;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.IPerfilComunitarioVisibilidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioVisao;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "comunidade/perfil-comunitario")
public class PerfilComunitarioControlador {
	private final ISegurancaServico segurancaServico;
	private final PerfilComunitarioServico servico;
	private final IPerfilComunitarioVisibilidadeProxy proxy;
	private final IColecaoCartaConsulta colecaoCartaConsulta;
	private final IAmizadeRepositorio amizadeRepositorio;

	public PerfilComunitarioControlador(ISegurancaServico segurancaServico, PerfilComunitarioServico servico,
			IPerfilComunitarioVisibilidadeProxy proxy, IColecaoCartaConsulta colecaoCartaConsulta,
			IAmizadeRepositorio amizadeRepositorio) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
		this.servico = Objects.requireNonNull(servico);
		this.proxy = Objects.requireNonNull(proxy);
		this.colecaoCartaConsulta = Objects.requireNonNull(colecaoCartaConsulta);
		this.amizadeRepositorio = Objects.requireNonNull(amizadeRepositorio);
	}

	@GetMapping("visualizacao")
	public PerfilVisualizacaoRecurso visualizarProprio() {
		var solicitante = usuarioAutenticado();
		var visao = proxy.consultarPerfil(solicitante, solicitante);
		return paraVisualizacaoRecurso(solicitante, visao);
	}

	@GetMapping("{identificador}/visualizacao")
	public PerfilVisualizacaoRecurso visualizarPorIdentificador(
			@PathVariable PerfilComunitarioIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var solicitante = usuarioAutenticado();
		var visao = proxy.consultarPerfilPorIdentificador(solicitante, identificador);
		return paraVisualizacaoRecurso(solicitante, visao);
	}

	@GetMapping("{identificador}/alteracao")
	public PerfilEdicaoRecurso obterParaAlteracao(@PathVariable PerfilComunitarioIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var solicitante = usuarioAutenticado();
		var perfil = servico.buscar(identificador);
		if (!solicitante.equals(perfil.getUsuario())) {
			throw new NaoAutorizadoExcecao(PerfilComunitarioServico.USUARIO_PROPRIO_OBRIGATORIO);
		}
		return new PerfilEdicaoRecurso(perfil.getIdentificador().orElseThrow().toString(),
				perfil.getUsuario().toString(), perfil.getApelido().toString(), perfil.getCidade().orElse(null),
				perfil.getDescricao().orElse(null), perfil.getFoto().orElse(null),
				perfil.getPreferenciasPublicas().orElse(null), perfil.isDecksFavoritosVisiveis());
	}

	@PostMapping("{identificador}/alterar")
	public void alterar(@PathVariable PerfilComunitarioIdentificador identificador,
			@RequestBody AlterarPerfilRequisicao requisicao) {
		Objects.requireNonNull(identificador);
		Objects.requireNonNull(requisicao);

		var solicitante = usuarioAutenticado();
		var existente = servico.buscar(identificador);
		var editado = existente.editarDados(new Apelido(Objects.requireNonNullElse(requisicao.apelido(), "")),
				Optional.ofNullable(requisicao.cidade()), Optional.ofNullable(requisicao.descricao()),
				Optional.ofNullable(requisicao.foto()), Optional.ofNullable(requisicao.preferenciasPublicas()),
				requisicao.decksFavoritosVisiveis());
		proxy.editarPerfil(solicitante, editado);
	}

	@PostMapping("{identificador}/fixar-destaque")
	public void fixarDestaque(@PathVariable PerfilComunitarioIdentificador identificador,
			@RequestBody CartaDestaqueRequisicao requisicao) {
		Objects.requireNonNull(identificador);
		Objects.requireNonNull(requisicao);

		var solicitante = usuarioAutenticado();
		var perfil = servico.buscar(identificador);
		proxy.fixarCartaFavorita(solicitante, perfil.getUsuario(),
				new CartaIdentificador(Objects.requireNonNullElse(requisicao.cartaId(), "")));
	}

	@PostMapping("{identificador}/remover-destaque")
	public void removerDestaque(@PathVariable PerfilComunitarioIdentificador identificador,
			@RequestBody CartaDestaqueRequisicao requisicao) {
		Objects.requireNonNull(identificador);
		Objects.requireNonNull(requisicao);

		var solicitante = usuarioAutenticado();
		var perfil = servico.buscar(identificador);
		proxy.removerCartaFavorita(solicitante, perfil.getUsuario(),
				new CartaIdentificador(Objects.requireNonNullElse(requisicao.cartaId(), "")));
	}

	private UsuarioIdentificador usuarioAutenticado() {
		return new UsuarioIdentificador(segurancaServico.getUsuario());
	}

	private PerfilVisualizacaoRecurso paraVisualizacaoRecurso(UsuarioIdentificador solicitante,
			PerfilComunitarioVisao visao) {
		var catalogo = colecaoCartaConsulta.listarCartasDisponiveis(visao.getUsuario()).stream()
				.collect(Collectors.toMap(item -> item.getCarta().toString(), Function.identity(), (a, b) -> a));
		var idsFavoritas = visao.getCartasFavoritas().stream().map(CartaIdentificador::toString)
				.collect(Collectors.toSet());

		var cartasFavoritas = visao.getCartasFavoritas().stream()
				.map(carta -> paraCartaDestaqueRecurso(carta.toString(), catalogo)).toList();
		var cartasDisponiveis = visao.isEhProprietario()
				? visao.getCartasColecaoDisponiveis().stream()
						.map(item -> paraCartaColecaoRecurso(item, idsFavoritas)).toList()
				: List.<CartaColecaoRecurso>of();

		var relacao = resolverRelacao(solicitante, visao.getUsuario());
		return new PerfilVisualizacaoRecurso(visao.getIdentificador().toString(), visao.getUsuario().toString(),
				visao.getApelido().toString(), visao.getCidade().orElse(null), visao.getDescricao().orElse(null),
				visao.getFoto().orElse(null), visao.getPreferenciasPublicas().orElse(null),
				visao.isDecksFavoritosVisiveis(), visao.isEhProprietario(), relacao.status(), relacao.amizadeId(),
				cartasFavoritas, cartasDisponiveis);
	}

	private RelacaoResumo resolverRelacao(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		if (solicitante.equals(alvo)) {
			return new RelacaoResumo("PROPRIO", null);
		}
		if (!amizadeRepositorio.existeRelacaoEntre(solicitante, alvo)) {
			return new RelacaoResumo("NENHUMA", null);
		}
		var amizade = amizadeRepositorio.buscarRelacaoEntre(solicitante, alvo);
		var id = amizade.getIdentificador().orElseThrow().toString();
		if (amizade.estaAceita()) {
			return new RelacaoResumo("AMIGO", id);
		}
		var status = amizade.getSolicitante().equals(solicitante) ? "PENDENTE_ENVIADA" : "PENDENTE_RECEBIDA";
		return new RelacaoResumo(status, id);
	}

	private static CartaDestaqueRecurso paraCartaDestaqueRecurso(String cartaId,
			Map<String, CartaColecaoResumo> catalogo) {
		var resumo = catalogo.get(cartaId);
		if (resumo == null) {
			var imgPadrao = "https://optcgapi.com/media/static/Card_Images/" + cartaId + ".jpg";
			return new CartaDestaqueRecurso(cartaId, cartaId, cartaId, "SR", "Near Mint", "Red", BigDecimal.ZERO,
					imgPadrao, cartaId, cartaId, "OP-01", "Romance Dawn", "SR", "Red", "Character", "3", "5000",
					"Straw Hat Crew", 1000, "Slash", BigDecimal.ZERO, BigDecimal.ZERO, cartaId, imgPadrao);
		}
		var img = resumo.getImagem().orElse(null);
		return new CartaDestaqueRecurso(resumo.getCarta().toString(), resumo.getCodigo(), resumo.getNomeCarta(),
				resumo.getRaridade(), resumo.getEdicaoCondicao(), resumo.getCor(), resumo.getPrecoMercado(), img,
				resumo.getCodigo(), resumo.getNomeCarta(), resumo.getColecao(), resumo.getNomeColecao(),
				resumo.getRaridade(), resumo.getCor(), resumo.getTipoCarta(), resumo.getCustoCarta(),
				resumo.getPoderCarta(), resumo.getSubTipos(), resumo.getContraAtaque(), resumo.getAtributo(),
				resumo.getPrecoMercado(), resumo.getPrecoInventario(), resumo.getImagemId(), img);
	}

	private static CartaColecaoRecurso paraCartaColecaoRecurso(CartaColecaoResumo resumo, Set<String> idsFavoritas) {
		var cartaId = resumo.getCarta().toString();
		var img = resumo.getImagem().orElse(null);
		return new CartaColecaoRecurso(cartaId, resumo.getCodigo(), resumo.getNomeCarta(), resumo.getRaridade(),
				resumo.getEdicaoCondicao(), resumo.getCor(), resumo.getPrecoMercado(), img,
				idsFavoritas.contains(cartaId), resumo.getCodigo(), resumo.getNomeCarta(), resumo.getColecao(),
				resumo.getNomeColecao(), resumo.getRaridade(), resumo.getCor(), resumo.getTipoCarta(),
				resumo.getCustoCarta(), resumo.getPoderCarta(), resumo.getSubTipos(), resumo.getContraAtaque(),
				resumo.getAtributo(), resumo.getPrecoMercado(), resumo.getPrecoInventario(), resumo.getImagemId(), img);
	}

	private record RelacaoResumo(String status, @Nullable String amizadeId) {
	}

	public record AlterarPerfilRequisicao(@Nullable String apelido, @Nullable String cidade,
			@Nullable String descricao, @Nullable String foto, @Nullable String preferenciasPublicas,
			boolean decksFavoritosVisiveis) {
	}

	public record CartaDestaqueRequisicao(@Nullable String cartaId) {
	}

	public record PerfilEdicaoRecurso(String id, String usuario, String apelido, @Nullable String cidade,
			@Nullable String descricao, @Nullable String foto, @Nullable String preferenciasPublicas,
			boolean decksFavoritosVisiveis) {
	}

	public record CartaDestaqueRecurso(String cartaId, String codigo, String nome, String raridade,
			String edicaoCondicao, String cor, BigDecimal precoMercado, @Nullable String imagem, String card_set_id,
			String card_name, String set_id, String set_name, String rarity, String card_color, String card_type,
			String card_cost, String card_power, String sub_types, int counter_amount, String attribute,
			BigDecimal market_price, BigDecimal inventory_price, String card_image_id, @Nullable String card_image) {
	}

	public record CartaColecaoRecurso(String cartaId, String codigo, String nome, String raridade,
			String edicaoCondicao, String cor, BigDecimal precoMercado, @Nullable String imagem, boolean jaDestacada,
			String card_set_id, String card_name, String set_id, String set_name, String rarity, String card_color,
			String card_type, String card_cost, String card_power, String sub_types, int counter_amount,
			String attribute, BigDecimal market_price, BigDecimal inventory_price, String card_image_id,
			@Nullable String card_image) {
	}

	public record PerfilVisualizacaoRecurso(String id, String usuario, String apelido, @Nullable String cidade,
			@Nullable String descricao, @Nullable String foto, @Nullable String preferenciasPublicas,
			boolean decksFavoritosVisiveis, boolean ehProprietario, String statusRelacao, @Nullable String amizadeId,
			List<CartaDestaqueRecurso> cartasFavoritas, List<CartaColecaoRecurso> cartasColecaoDisponiveis) {
	}
}
