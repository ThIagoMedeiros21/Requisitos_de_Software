package com.cesar.tgchub.tcghub.ui.backend.comunidade.amizade;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioAplicacaoServico;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioOrdenacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;
import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "comunidade/amizade")
public class AmizadeControlador {
	private final ISegurancaServico segurancaServico;
	private final AmizadeServico servico;
	private final IAmizadeRepositorio amizadeRepositorio;
	private final IPerfilComunitarioRepositorio perfilRepositorio;
	private final PerfilComunitarioAplicacaoServico perfilAplicacaoServico;

	public AmizadeControlador(ISegurancaServico segurancaServico, AmizadeServico servico,
			IAmizadeRepositorio amizadeRepositorio, IPerfilComunitarioRepositorio perfilRepositorio,
			PerfilComunitarioAplicacaoServico perfilAplicacaoServico) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
		this.servico = Objects.requireNonNull(servico);
		this.amizadeRepositorio = Objects.requireNonNull(amizadeRepositorio);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
		this.perfilAplicacaoServico = Objects.requireNonNull(perfilAplicacaoServico);
	}

	@GetMapping("pesquisa")
	public RedeAmizadesRecurso pesquisar() {
		var usuario = usuarioAutenticado();
		var relacoes = servico.listarPorUsuario(usuario);

		var amigos = new ArrayList<ItemRedeRecurso>();
		var recebidas = new ArrayList<ItemRedeRecurso>();
		var enviadas = new ArrayList<ItemRedeRecurso>();

		for (var relacao : relacoes) {
			var outro = relacao.outroUsuario(usuario);
			var perfilOutro = perfilRepositorio.buscarPorUsuario(outro);
			var item = new ItemRedeRecurso(relacao.getIdentificador().orElseThrow().toString(),
					perfilOutro.getIdentificador().orElseThrow().toString(), outro.toString(),
					perfilOutro.getApelido().toString(), perfilOutro.getCidade().orElse(null),
					perfilOutro.getFoto().orElse(null), relacao.getStatus().name(), relacao.getCriadoEm(),
					relacao.getAtualizadoEm().orElse(null));

			if (relacao.estaAceita()) {
				amigos.add(item);
			} else if (relacao.getDestinatario().equals(usuario)) {
				recebidas.add(item);
			} else {
				enviadas.add(item);
			}
		}

		return new RedeAmizadesRecurso(List.copyOf(amigos), List.copyOf(recebidas), List.copyOf(enviadas));
	}

	@GetMapping("inclusao")
	public List<ColecionadorInclusaoRecurso> listarColecionadores(
			@RequestParam(name = "termo", required = false) @Nullable String termo) {
		var solicitante = usuarioAutenticado();
		var filtro = Optional.ofNullable(termo).map(String::trim).filter(valor -> !valor.isEmpty());
		var resultado = perfilAplicacaoServico
				.pesquisar(new PerfilComunitarioConsulta(Optional.of(PerfilComunitarioOrdenacao.APELIDO),
						Optional.of(false), Optional.of(0), Optional.of(100), filtro));

		return resultado.getProjecoes().stream().filter(projecao -> !projecao.getUsuario().equals(solicitante))
				.map(projecao -> {
					var relacao = resolverRelacao(solicitante, projecao.getUsuario());
					return new ColecionadorInclusaoRecurso(projecao.getIdentificador().toString(),
							projecao.getUsuario().toString(), projecao.getApelido().toString(),
							projecao.getCidade().orElse(null), projecao.getFoto().orElse(null), relacao.status(),
							relacao.amizadeId());
				}).toList();
	}

	@GetMapping("{identificador}/visualizacao")
	public AmizadeDetalheRecurso visualizar(@PathVariable AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var solicitanteAtual = usuarioAutenticado();
		var amizade = servico.buscar(identificador);
		var perfilSolicitante = perfilRepositorio.buscarPorUsuario(amizade.getSolicitante());
		var perfilDestinatario = perfilRepositorio.buscarPorUsuario(amizade.getDestinatario());

		return new AmizadeDetalheRecurso(amizade.getIdentificador().orElseThrow().toString(),
				amizade.getSolicitante().toString(), perfilSolicitante.getApelido().toString(),
				amizade.getDestinatario().toString(), perfilDestinatario.getApelido().toString(),
				amizade.getStatus().name(), amizade.getSolicitante().equals(solicitanteAtual),
				amizade.getDestinatario().equals(solicitanteAtual), amizade.getCriadoEm(),
				amizade.getAtualizadoEm().orElse(null));
	}

	@PostMapping("incluir")
	public void incluir(@RequestBody SolicitarAmizadeRequisicao requisicao) {
		Objects.requireNonNull(requisicao);
		var solicitante = usuarioAutenticado();
		var destinatario = new UsuarioIdentificador(Objects.requireNonNullElse(requisicao.destinatario(), ""));
		servico.enviarSolicitacao(solicitante, destinatario);
	}

	@PostMapping("{identificador}/aceitar")
	public void aceitar(@PathVariable AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.aceitarSolicitacao(usuarioAutenticado(), identificador);
	}

	@PostMapping("{identificador}/recusar")
	public void recusar(@PathVariable AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.recusarSolicitacao(usuarioAutenticado(), identificador);
	}

	@PostMapping("{identificador}/cancelar")
	public void cancelar(@PathVariable AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.cancelarSolicitacao(usuarioAutenticado(), identificador);
	}

	@PostMapping("{identificador}/remover")
	public void remover(@PathVariable AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var usuario = usuarioAutenticado();
		var amizade = servico.buscar(identificador);
		if (!amizade.envolve(usuario)) {
			throw new NaoAutorizadoExcecao("Apenas participantes podem remover a amizade");
		}
		servico.removerAmizadeBilateral(usuario, amizade.outroUsuario(usuario));
	}

	private UsuarioIdentificador usuarioAutenticado() {
		return new UsuarioIdentificador(segurancaServico.getUsuario());
	}

	private RelacaoResumo resolverRelacao(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		if (!amizadeRepositorio.existeRelacaoEntre(solicitante, alvo)) {
			return new RelacaoResumo("NENHUMA", null);
		}
		Amizade amizade = amizadeRepositorio.buscarRelacaoEntre(solicitante, alvo);
		var id = amizade.getIdentificador().orElseThrow().toString();
		if (amizade.estaAceita()) {
			return new RelacaoResumo("AMIGO", id);
		}
		var status = amizade.getSolicitante().equals(solicitante) ? "PENDENTE_ENVIADA" : "PENDENTE_RECEBIDA";
		return new RelacaoResumo(status, id);
	}

	private record RelacaoResumo(String status, @Nullable String amizadeId) {
	}

	public record SolicitarAmizadeRequisicao(@Nullable String destinatario) {
	}

	public record ItemRedeRecurso(String amizadeId, String perfilId, String usuario, String apelido,
			@Nullable String cidade, @Nullable String foto, String status, Instant criadoEm,
			@Nullable Instant atualizadoEm) {
	}

	public record RedeAmizadesRecurso(List<ItemRedeRecurso> amigos, List<ItemRedeRecurso> recebidas,
			List<ItemRedeRecurso> enviadas) {
	}

	public record ColecionadorInclusaoRecurso(String perfilId, String usuario, String apelido, @Nullable String cidade,
			@Nullable String foto, String statusRelacao, @Nullable String amizadeId) {
	}

	public record AmizadeDetalheRecurso(String amizadeId, String solicitante, String solicitanteApelido,
			String destinatario, String destinatarioApelido, String status, boolean ehSolicitante,
			boolean ehDestinatario, Instant criadoEm, @Nullable Instant atualizadoEm) {
	}
}
