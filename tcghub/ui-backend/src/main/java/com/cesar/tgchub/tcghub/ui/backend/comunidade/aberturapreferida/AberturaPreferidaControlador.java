package com.cesar.tgchub.tcghub.ui.backend.comunidade.aberturapreferida;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
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
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaParaEscolhaVisao;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.IAberturaPreferidaPrivacidadeProxy;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.ItemRankingAbertura;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.RankingAberturasVisao;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "comunidade/abertura-preferida")
public class AberturaPreferidaControlador {
	private final ISegurancaServico segurancaServico;
	private final AberturaPreferidaServico servico;
	private final IAberturaPreferidaPrivacidadeProxy proxy;
	private final IAberturaRepositorio aberturaRepositorio;
	private final PerfilComunitarioServico perfilServico;
	private final IPerfilComunitarioRepositorio perfilRepositorio;

	public AberturaPreferidaControlador(ISegurancaServico segurancaServico, AberturaPreferidaServico servico,
			IAberturaPreferidaPrivacidadeProxy proxy, IAberturaRepositorio aberturaRepositorio,
			PerfilComunitarioServico perfilServico, IPerfilComunitarioRepositorio perfilRepositorio) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
		this.servico = Objects.requireNonNull(servico);
		this.proxy = Objects.requireNonNull(proxy);
		this.aberturaRepositorio = Objects.requireNonNull(aberturaRepositorio);
		this.perfilServico = Objects.requireNonNull(perfilServico);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
	}

	@GetMapping("pesquisa")
	public AberturaPreferidaPesquisaRecurso pesquisar(@RequestParam(required = false) @Nullable String termo,
			@RequestParam(required = false) @Nullable String filtro) {
		var solicitante = usuarioAutenticado();
		var minhaVisao = proxy.consultarAberturaNoPerfil(solicitante, solicitante);
		var minhaPreferida = minhaVisao.getPreferencia().map(this::paraMinhaPreferidaRecurso).orElse(null);

		var ativas = proxy.listarAberturasAtivasParaEscolha(solicitante, Optional.ofNullable(termo));
		var filtroNormalizado = Objects.requireNonNullElse(filtro, "").trim().toLowerCase(Locale.ROOT);
		var ativasFiltradas = ativas.stream().filter(item -> switch (filtroNormalizado) {
		case "amigos" -> item.getQuantidadeAmigos() > 0;
		case "ranking" -> item.getPosicaoRanking().map(p -> p <= AberturaPreferidaServico.LIMITE_RANKING).orElse(false);
		default -> true;
		}).map(AberturaPreferidaControlador::paraAberturaEscolhaRecurso).toList();

		var ranking = paraRankingRecurso(servico.consultarRankingTop5());
		return new AberturaPreferidaPesquisaRecurso(minhaPreferida, ativasFiltradas, ranking);
	}

	@GetMapping("ranking")
	public RankingAberturasRecurso consultarRanking() {
		return paraRankingRecurso(servico.consultarRankingTop5());
	}

	@GetMapping("perfil/{perfilId}")
	public AberturaPreferidaPerfilRecurso consultarNoPerfil(@PathVariable PerfilComunitarioIdentificador perfilId) {
		Objects.requireNonNull(perfilId);
		var solicitante = usuarioAutenticado();
		var perfil = perfilServico.buscar(perfilId);
		var visao = proxy.consultarAberturaNoPerfil(solicitante, perfil.getUsuario());

		AberturaPerfilResumoRecurso resumo = null;
		if (visao.getAbertura().isPresent() && visao.getPreferencia().isPresent()) {
			var ab = visao.getAbertura().get();
			var pref = visao.getPreferencia().get();
			resumo = new AberturaPerfilResumoRecurso(ab.getIdentificador().orElseThrow().toString(),
					ab.getNumeroValor(), ab.getNome(), ab.getArtista(), ab.getEpisodios(), ab.getImagem(),
					ab.getVideo(), ab.isAtiva(), pref.getEscolhidaEm());
		}

		return new AberturaPreferidaPerfilRecurso(perfilId.toString(), visao.isEhProprietario(),
				visao.possuiPreferidaVisivel(), visao.isVisivelNoPerfil(), visao.isMesmaPreferenciaDoSolicitante(),
				resumo);
	}

	@GetMapping("{identificador}/visualizacao")
	public AberturaDetalhesRecurso visualizarDetalhes(@PathVariable AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var solicitante = usuarioAutenticado();
		var detalhes = proxy.consultarDetalhes(solicitante, identificador);
		var ab = detalhes.getAbertura();
		var amigos = detalhes.getAmigosComMesmaPreferencia().stream().map(this::paraAmigoResumo).toList();
		return new AberturaDetalhesRecurso(ab.getIdentificador().orElseThrow().toString(), ab.getNumeroValor(),
				ab.getNome(), ab.getArtista(), ab.getEpisodios(), ab.getImagem(), ab.getVideo(), ab.isAtiva(),
				detalhes.isEhMinhaPreferida(), detalhes.isVisivelNoPerfil(), detalhes.getPosicaoRanking().orElse(null),
				detalhes.getTotalEscolhasComunidade(), amigos.size(), amigos);
	}

	@PostMapping("escolher")
	public MinhaAberturaPreferidaRecurso escolher(@RequestBody EscolherAberturaRequisicao requisicao) {
		Objects.requireNonNull(requisicao);
		var solicitante = usuarioAutenticado();
		AberturaPreferida salva;
		if (requisicao.aberturaId() != null && !requisicao.aberturaId().isBlank()) {
			salva = proxy.escolher(solicitante, solicitante, new AberturaIdentificador(requisicao.aberturaId().trim()));
		} else if (requisicao.numero() != null) {
			salva = proxy.escolherPorNumero(solicitante, solicitante, requisicao.numero());
		} else {
			throw new IllegalArgumentException("Informe o identificador ou número da abertura");
		}
		return paraMinhaPreferidaRecurso(salva);
	}

	@PostMapping("ocultar")
	public void ocultar() {
		var solicitante = usuarioAutenticado();
		proxy.alterarVisibilidade(solicitante, solicitante, false);
	}

	@PostMapping("reexibir")
	public void reexibir() {
		var solicitante = usuarioAutenticado();
		proxy.alterarVisibilidade(solicitante, solicitante, true);
	}

	private MinhaAberturaPreferidaRecurso paraMinhaPreferidaRecurso(AberturaPreferida preferida) {
		var ab = aberturaRepositorio.buscarPorIdentificador(preferida.getAbertura());
		return new MinhaAberturaPreferidaRecurso(preferida.getIdentificador().orElseThrow().toString(),
				ab.getIdentificador().orElseThrow().toString(), ab.getNumeroValor(), ab.getNome(), ab.getArtista(),
				ab.getEpisodios(), ab.getImagem(), ab.getVideo(), ab.isAtiva(), preferida.isVisivelNoPerfil(),
				preferida.getEscolhidaEm());
	}

	private static AberturaEscolhaRecurso paraAberturaEscolhaRecurso(AberturaParaEscolhaVisao visao) {
		var ab = visao.getAbertura();
		return new AberturaEscolhaRecurso(ab.getIdentificador().orElseThrow().toString(), ab.getNumeroValor(),
				ab.getNome(), ab.getArtista(), ab.getEpisodios(), ab.getImagem(), ab.getVideo(), ab.isAtiva(),
				visao.getQuantidadeAmigos(), visao.isEhMinhaPreferida(), visao.getPosicaoRanking().orElse(null));
	}

	private static RankingAberturasRecurso paraRankingRecurso(RankingAberturasVisao ranking) {
		var itens = ranking.getItens().stream().map(AberturaPreferidaControlador::paraItemRankingRecurso).toList();
		return new RankingAberturasRecurso(ranking.getTotalEscolhas(), itens);
	}

	private static ItemRankingRecurso paraItemRankingRecurso(ItemRankingAbertura item) {
		return new ItemRankingRecurso(item.getPosicao(), item.getAbertura().toString(), item.getNumero(),
				item.getNome(), item.getArtista(), item.getImagem(), item.getQuantidadeEscolhas(),
				item.getPercentual());
	}

	private AmigoResumoRecurso paraAmigoResumo(UsuarioIdentificador usuario) {
		if (perfilRepositorio.existePorUsuario(usuario)) {
			var perfil = perfilRepositorio.buscarPorUsuario(usuario);
			return new AmigoResumoRecurso(usuario.toString(), perfil.getApelido().toString(),
					perfil.getIdentificador().orElseThrow().toString());
		}
		return new AmigoResumoRecurso(usuario.toString(), usuario.toString(), null);
	}

	private UsuarioIdentificador usuarioAutenticado() {
		return new UsuarioIdentificador(segurancaServico.getUsuario());
	}

	public record EscolherAberturaRequisicao(@Nullable String aberturaId, @Nullable Integer numero) {
	}

	public record MinhaAberturaPreferidaRecurso(String id, String aberturaId, int numero, String nome, String artista,
			String episodios, String imagem, String video, boolean ativa, boolean visivelNoPerfil,
			Instant escolhidaEm) {
	}

	public record AberturaEscolhaRecurso(String id, int numero, String nome, String artista, String episodios,
			String imagem, String video, boolean ativa, int quantidadeAmigos, boolean ehMinhaPreferida,
			@Nullable Integer posicaoRanking) {
	}

	public record ItemRankingRecurso(int posicao, String aberturaId, int numero, String nome, String artista,
			String imagem, int quantidadeEscolhas, BigDecimal percentual) {
	}

	public record RankingAberturasRecurso(int totalEscolhas, List<ItemRankingRecurso> itens) {
	}

	public record AberturaPreferidaPesquisaRecurso(@Nullable MinhaAberturaPreferidaRecurso minhaPreferida,
			List<AberturaEscolhaRecurso> aberturasAtivas, RankingAberturasRecurso ranking) {
	}

	public record AberturaPerfilResumoRecurso(String aberturaId, int numero, String nome, String artista,
			String episodios, String imagem, String video, boolean ativa, Instant escolhidaEm) {
	}

	public record AberturaPreferidaPerfilRecurso(String perfilId, boolean ehProprietario,
			boolean possuiPreferidaVisivel, boolean visivelNoPerfil, boolean mesmaPreferenciaDoVisitante,
			@Nullable AberturaPerfilResumoRecurso abertura) {
	}

	public record AmigoResumoRecurso(String usuario, String apelido, @Nullable String perfilId) {
	}

	public record AberturaDetalhesRecurso(String id, int numero, String nome, String artista, String episodios,
			String imagem, String video, boolean ativa, boolean ehMinhaPreferida, boolean visivelNoPerfil,
			@Nullable Integer posicaoRanking, int totalEscolhasComunidade, int quantidadeAmigos,
			List<AmigoResumoRecurso> amigosComMesmaPreferencia) {
	}
}
