package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class AberturaPreferidaServico extends
		AgregadoServicoAbstrato<AberturaPreferidaIdentificador, AberturaPreferida, AberturaPreferidaAutorizacoes> {
	public static final int LIMITE_RANKING = 5;
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe abertura preferida com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe uma abertura preferida com o identificador %s";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador da abertura preferida é gerado na inclusão e não deve ser informado";
	public static final String ABERTURA_INATIVA_MODELO = "A abertura número %d está inativa e não pode ser escolhida";
	public static final String USUARIO_SEM_PREFERIDA_MODELO = "O usuário %s ainda não escolheu uma abertura preferida";
	public static final String OPERACAO_NAO_AUTORIZADA = "Apenas o proprietário pode gerenciar sua abertura preferida";

	private final IAberturaPreferidaRepositorio repositorio;
	private final IAberturaRepositorio aberturaRepositorio;

	public AberturaPreferidaServico(ISegurancaServico segurancaServico, IAberturaPreferidaRepositorio repositorio,
			IAberturaRepositorio aberturaRepositorio) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
		this.aberturaRepositorio = Objects.requireNonNull(aberturaRepositorio);
	}

	@Override
	public AberturaPreferidaAutorizacoes criarAutorizacoes(Optional<AberturaPreferidaIdentificador> identificador) {
		Objects.requireNonNull(identificador);
		var possuiPapelUsuario = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name());
		if (identificador.isEmpty() || !repositorio.existePorIdentificador(identificador.get())) {
			return new AberturaPreferidaAutorizacoes(possuiPapelUsuario, true, possuiPapelUsuario, false,
					possuiPapelUsuario, possuiPapelUsuario, possuiPapelUsuario);
		}
		var preferida = repositorio.buscarPorIdentificador(identificador.get());
		var ehProprietario = possuiPapelUsuario
				&& getSegurancaServico().getUsuario().equals(preferida.getUsuario().toString());
		return new AberturaPreferidaAutorizacoes(possuiPapelUsuario, true, ehProprietario, false, ehProprietario,
				ehProprietario, ehProprietario);
	}

	@Override
	protected void validarInclusao(AberturaPreferida agregado) {
		Objects.requireNonNull(agregado);
		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
	}

	public AberturaPreferida escolher(UsuarioIdentificador usuario, AberturaIdentificador aberturaId) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(aberturaId);
		exigirProprietario(usuario);
		validarAutorizacao(Optional.empty(), AberturaPreferidaAutorizacoes::getEscolher);

		if (!aberturaRepositorio.existePorIdentificador(aberturaId)) {
			throw new AgregadoInexistenteExcecao(AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(aberturaId));
		}
		var abertura = aberturaRepositorio.buscarPorIdentificador(aberturaId);
		Validate.validState(abertura.isAtiva(), ABERTURA_INATIVA_MODELO, abertura.getNumeroValor());

		var agora = Instant.now();
		if (repositorio.existePorUsuario(usuario)) {
			var existente = repositorio.buscarPorUsuario(usuario);
			var atualizada = existente.substituirAbertura(aberturaId, agora);
			repositorio.alterar(atualizada);
			return atualizada;
		}
		return repositorio.incluir(new AberturaPreferida(Optional.empty(), usuario, aberturaId, true, agora));
	}

	public AberturaPreferida escolherPorNumero(UsuarioIdentificador usuario, int numero) {
		Objects.requireNonNull(usuario);
		exigirProprietario(usuario);
		if (!aberturaRepositorio.existePorNumero(numero)) {
			throw new AgregadoInexistenteExcecao(AberturaServico.NUMERO_INEXISTENTE_MODELO.formatted(numero));
		}
		var abertura = aberturaRepositorio.buscarPorNumero(numero);
		return escolher(usuario, abertura.getIdentificador().orElseThrow());
	}

	public AberturaPreferida alterarVisibilidade(UsuarioIdentificador usuario, boolean visivelNoPerfil) {
		Objects.requireNonNull(usuario);
		exigirProprietario(usuario);
		validarAutorizacao(Optional.empty(),
				visivelNoPerfil ? AberturaPreferidaAutorizacoes::getReexibir : AberturaPreferidaAutorizacoes::getOcultar);

		if (!repositorio.existePorUsuario(usuario)) {
			throw new AgregadoInexistenteExcecao(USUARIO_SEM_PREFERIDA_MODELO.formatted(usuario));
		}
		var existente = repositorio.buscarPorUsuario(usuario);
		var atualizada = existente.alterarVisibilidade(visivelNoPerfil);
		repositorio.alterar(atualizada);
		return atualizada;
	}

	public RankingAberturasVisao consultarRankingTop5() {
		var todasEscolhas = repositorio.listarTodas();
		int total = todasEscolhas.size();
		if (total == 0) {
			return new RankingAberturasVisao(0, List.of());
		}

		var entradasOrdenadas = calcularRankingCompletoOrdenado(todasEscolhas);
		var itens = new ArrayList<ItemRankingAbertura>();
		int limite = Math.min(LIMITE_RANKING, entradasOrdenadas.size());
		for (int i = 0; i < limite; i++) {
			var entrada = entradasOrdenadas.get(i);
			var percentual = BigDecimal.valueOf(entrada.quantidade() * 100.0)
					.divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);
			var ab = entrada.abertura();
			itens.add(new ItemRankingAbertura(i + 1, ab.getIdentificador().orElseThrow(), ab.getNumeroValor(),
					ab.getNome(), ab.getArtista(), ab.getImagem(), entrada.quantidade(), percentual));
		}
		return new RankingAberturasVisao(total, itens);
	}

	public Optional<Integer> obterPosicaoNoRanking(AberturaIdentificador aberturaId) {
		Objects.requireNonNull(aberturaId);
		var todasEscolhas = repositorio.listarTodas();
		if (todasEscolhas.isEmpty()) {
			return Optional.empty();
		}
		var entradas = calcularRankingCompletoOrdenado(todasEscolhas);
		for (int i = 0; i < entradas.size(); i++) {
			if (entradas.get(i).abertura().getIdentificador().orElseThrow().equals(aberturaId)) {
				return Optional.of(i + 1);
			}
		}
		return Optional.empty();
	}

	private List<ContagemAbertura> calcularRankingCompletoOrdenado(List<AberturaPreferida> todasEscolhas) {
		Map<AberturaIdentificador, Integer> contagens = new LinkedHashMap<>();
		for (var escolha : todasEscolhas) {
			contagens.merge(escolha.getAbertura(), 1, Integer::sum);
		}
		List<ContagemAbertura> resultado = new ArrayList<>();
		for (var entry : contagens.entrySet()) {
			if (aberturaRepositorio.existePorIdentificador(entry.getKey())) {
				var abertura = aberturaRepositorio.buscarPorIdentificador(entry.getKey());
				resultado.add(new ContagemAbertura(abertura, entry.getValue()));
			}
		}
		resultado.sort(Comparator.comparingInt(ContagemAbertura::quantidade).reversed()
				.thenComparingInt(c -> c.abertura().getNumeroValor()));
		return resultado;
	}

	private void exigirProprietario(UsuarioIdentificador usuario) {
		if (!getSegurancaServico().getUsuario().equals(usuario.toString())) {
			throw new NaoAutorizadoExcecao(OPERACAO_NAO_AUTORIZADA);
		}
	}

	private record ContagemAbertura(Abertura abertura, int quantidade) {
	}
}
