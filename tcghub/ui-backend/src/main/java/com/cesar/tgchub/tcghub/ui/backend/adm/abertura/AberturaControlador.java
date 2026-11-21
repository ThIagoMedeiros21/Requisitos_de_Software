package com.cesar.tgchub.tcghub.ui.backend.adm.abertura;

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

import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaAplicacaoServico;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaProjecao;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.NumeroAbertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.StatusAbertura;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;

import dev.sauloaraujo.d5.framework.ui.Backend;

@RestController
@RequestMapping(Backend.PREFIXO + "adm/abertura")
public class AberturaControlador {
	private final AberturaServico servico;
	private final AberturaAplicacaoServico aplicacaoServico;
	private final IAberturaPreferidaRepositorio preferidaRepositorio;

	public AberturaControlador(AberturaServico servico, AberturaAplicacaoServico aplicacaoServico,
			IAberturaPreferidaRepositorio preferidaRepositorio) {
		this.servico = Objects.requireNonNull(servico);
		this.aplicacaoServico = Objects.requireNonNull(aplicacaoServico);
		this.preferidaRepositorio = Objects.requireNonNull(preferidaRepositorio);
	}

	@GetMapping("pesquisa")
	public AberturaPesquisaRecurso pesquisar(@RequestParam(required = false) @Nullable String termo,
			@RequestParam(required = false) @Nullable String status) {
		var filtroAtiva = interpretarStatus(status);
		var filtroTermo = Optional.ofNullable(termo).map(String::trim).filter(v -> !v.isEmpty());
		var resultado = aplicacaoServico.pesquisar(new AberturaConsulta(Optional.of(AberturaOrdenacao.NUMERO),
				Optional.of(false), Optional.of(0), Optional.of(200), filtroTermo, filtroAtiva));

		var todas = servico.listarPorStatus(Optional.empty());
		int ativas = (int) todas.stream().filter(Abertura::isAtiva).count();
		int inativas = todas.size() - ativas;

		var itens = resultado.getProjecoes().stream().map(AberturaControlador::paraRecurso).toList();
		return new AberturaPesquisaRecurso(todas.size(), ativas, inativas, itens);
	}

	@GetMapping("{identificador}/visualizacao")
	public AberturaAdminRecurso visualizar(@PathVariable AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		var abertura = servico.buscar(identificador);
		return paraRecurso(abertura);
	}

	@PostMapping("incluir")
	public AberturaAdminRecurso incluir(@RequestBody SalvarAberturaRequisicao requisicao) {
		Objects.requireNonNull(requisicao);
		var nova = new Abertura(Optional.empty(), requisicao.numero(),
				Objects.requireNonNullElse(requisicao.nome(), ""), Objects.requireNonNullElse(requisicao.artista(), ""),
				Objects.requireNonNullElse(requisicao.episodios(), ""),
				Objects.requireNonNullElse(requisicao.imagem(), ""), Objects.requireNonNullElse(requisicao.video(), ""),
				requisicao.ativa());
		var salva = servico.incluir(nova);
		return paraRecurso(salva);
	}

	@PostMapping("{identificador}/alterar")
	public AberturaAdminRecurso alterar(@PathVariable AberturaIdentificador identificador,
			@RequestBody SalvarAberturaRequisicao requisicao) {
		Objects.requireNonNull(identificador);
		Objects.requireNonNull(requisicao);
		var existente = servico.buscar(identificador);
		var editada = existente.editar(new NumeroAbertura(requisicao.numero()),
				Objects.requireNonNullElse(requisicao.nome(), ""), Objects.requireNonNullElse(requisicao.artista(), ""),
				Objects.requireNonNullElse(requisicao.episodios(), ""),
				Objects.requireNonNullElse(requisicao.imagem(), ""), Objects.requireNonNullElse(requisicao.video(), ""),
				StatusAbertura.deBooleano(requisicao.ativa()));
		servico.alterar(editada);
		return paraRecurso(editada);
	}

	@PostMapping("{identificador}/inativar")
	public void inativar(@PathVariable AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.inativar(identificador);
	}

	@PostMapping("{identificador}/reativar")
	public void reativar(@PathVariable AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.reativar(identificador);
	}

	@PostMapping("{identificador}/excluir")
	public void excluir(@PathVariable AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		servico.excluir(identificador);
	}

	private static Optional<Boolean> interpretarStatus(@Nullable String status) {
		if (status == null || status.isBlank()) {
			return Optional.empty();
		}
		return switch (status.trim().toUpperCase(Locale.ROOT)) {
		case "ATIVAS", "ATIVA", "TRUE" -> Optional.of(true);
		case "INATIVAS", "INATIVA", "FALSE" -> Optional.of(false);
		default -> Optional.empty();
		};
	}

	private AberturaAdminRecurso paraRecurso(Abertura abertura) {
		var id = abertura.getIdentificador().orElseThrow();
		long preferencias = preferidaRepositorio.contarPorAbertura(id);
		return new AberturaAdminRecurso(id.toString(), abertura.getNumeroValor(), abertura.getNome(),
				abertura.getArtista(), abertura.getEpisodios(), abertura.getImagem(), abertura.getVideo(),
				abertura.isAtiva(), preferencias);
	}

	private static AberturaAdminRecurso paraRecurso(AberturaProjecao projecao) {
		return new AberturaAdminRecurso(projecao.getIdentificador().toString(), projecao.getNumero(),
				projecao.getNome(), projecao.getArtista(), projecao.getEpisodios(), projecao.getImagem(),
				projecao.getVideo(), projecao.isAtiva(), projecao.getQuantidadePreferencias());
	}

	public record SalvarAberturaRequisicao(int numero, @Nullable String nome, @Nullable String artista,
			@Nullable String episodios, @Nullable String imagem, @Nullable String video, boolean ativa) {
	}

	public record AberturaAdminRecurso(String id, int numero, String nome, String artista, String episodios,
			String imagem, String video, boolean ativa, long quantidadePreferencias) {
	}

	public record AberturaPesquisaRecurso(int total, int ativas, int inativas, List<AberturaAdminRecurso> itens) {
	}
}
