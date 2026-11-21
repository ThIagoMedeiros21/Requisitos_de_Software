package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class AberturaPreferidaPrivacidadeProxy implements IAberturaPreferidaPrivacidadeProxy {
	private final AberturaPreferidaServico servico;
	private final IAberturaPreferidaRepositorio preferidaRepositorio;
	private final IAberturaRepositorio aberturaRepositorio;
	private final IAmizadeRepositorio amizadeRepositorio;

	public AberturaPreferidaPrivacidadeProxy(AberturaPreferidaServico servico,
			IAberturaPreferidaRepositorio preferidaRepositorio, IAberturaRepositorio aberturaRepositorio,
			IAmizadeRepositorio amizadeRepositorio) {
		this.servico = Objects.requireNonNull(servico);
		this.preferidaRepositorio = Objects.requireNonNull(preferidaRepositorio);
		this.aberturaRepositorio = Objects.requireNonNull(aberturaRepositorio);
		this.amizadeRepositorio = Objects.requireNonNull(amizadeRepositorio);
	}

	@Override
	public AberturaPreferidaPerfilVisao consultarAberturaNoPerfil(UsuarioIdentificador solicitante,
			UsuarioIdentificador alvo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);

		var ehProprietario = solicitante.equals(alvo);
		if (!preferidaRepositorio.existePorUsuario(alvo)) {
			return new AberturaPreferidaPerfilVisao(ehProprietario, true, false, Optional.empty(), Optional.empty());
		}

		var preferida = preferidaRepositorio.buscarPorUsuario(alvo);
		if (!ehProprietario && !preferida.isVisivelNoPerfil()) {
			return new AberturaPreferidaPerfilVisao(false, false, false, Optional.empty(), Optional.empty());
		}

		var abertura = aberturaRepositorio.buscarPorIdentificador(preferida.getAbertura());
		var mesmaPreferencia = !ehProprietario && amigosPossuemMesmaPreferencia(solicitante, alvo);
		return new AberturaPreferidaPerfilVisao(ehProprietario, preferida.isVisivelNoPerfil(), mesmaPreferencia,
				Optional.of(preferida), Optional.of(abertura));
	}

	@Override
	public boolean amigosPossuemMesmaPreferencia(UsuarioIdentificador solicitante, UsuarioIdentificador amigo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(amigo);
		if (solicitante.equals(amigo)) {
			return false;
		}
		if (!saoAmigosAceitos(solicitante, amigo)) {
			return false;
		}
		if (!preferidaRepositorio.existePorUsuario(solicitante) || !preferidaRepositorio.existePorUsuario(amigo)) {
			return false;
		}
		var prefSolicitante = preferidaRepositorio.buscarPorUsuario(solicitante);
		var prefAmigo = preferidaRepositorio.buscarPorUsuario(amigo);
		return prefAmigo.isVisivelNoPerfil() && prefSolicitante.getAbertura().equals(prefAmigo.getAbertura());
	}

	@Override
	public List<UsuarioIdentificador> listarAmigosComAbertura(UsuarioIdentificador usuario,
			AberturaIdentificador abertura) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(abertura);

		return preferidaRepositorio.listarPorAbertura(abertura).stream()
				.filter(AberturaPreferida::isVisivelNoPerfil)
				.map(AberturaPreferida::getUsuario)
				.filter(outro -> !outro.equals(usuario) && saoAmigosAceitos(usuario, outro))
				.toList();
	}

	@Override
	public int contarAmigosComAbertura(UsuarioIdentificador usuario, AberturaIdentificador abertura) {
		return listarAmigosComAbertura(usuario, abertura).size();
	}

	@Override
	public List<AberturaParaEscolhaVisao> listarAberturasAtivasParaEscolha(UsuarioIdentificador usuario,
			Optional<String> termo) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(termo);

		var filtro = termo.map(String::trim).map(s -> s.toLowerCase(Locale.ROOT)).orElse("");
		Optional<AberturaIdentificador> minhaAberturaId = preferidaRepositorio.existePorUsuario(usuario)
				? Optional.of(preferidaRepositorio.buscarPorUsuario(usuario).getAbertura())
				: Optional.empty();

		return aberturaRepositorio.listarPorStatus(true).stream()
				.filter(ab -> filtro.isEmpty()
						|| ab.getNome().toLowerCase(Locale.ROOT).contains(filtro)
						|| ab.getArtista().toLowerCase(Locale.ROOT).contains(filtro)
						|| Integer.toString(ab.getNumeroValor()).equals(filtro))
				.map(ab -> {
					var abId = ab.getIdentificador().orElseThrow();
					int amigos = contarAmigosComAbertura(usuario, abId);
					boolean ehMinha = minhaAberturaId.map(abId::equals).orElse(false);
					var posicao = servico.obterPosicaoNoRanking(abId);
					return new AberturaParaEscolhaVisao(ab, amigos, ehMinha, posicao);
				}).toList();
	}

	@Override
	public AberturaPreferidaDetalhesVisao consultarDetalhes(UsuarioIdentificador solicitante,
			AberturaIdentificador aberturaId) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(aberturaId);

		if (!aberturaRepositorio.existePorIdentificador(aberturaId)) {
			throw new AgregadoInexistenteExcecao(AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(aberturaId));
		}
		var abertura = aberturaRepositorio.buscarPorIdentificador(aberturaId);
		boolean ehMinha = false;
		boolean visivel = true;
		if (preferidaRepositorio.existePorUsuario(solicitante)) {
			var pref = preferidaRepositorio.buscarPorUsuario(solicitante);
			if (pref.getAbertura().equals(aberturaId)) {
				ehMinha = true;
				visivel = pref.isVisivelNoPerfil();
			}
		}
		var posicao = servico.obterPosicaoNoRanking(aberturaId);
		var totalComunidade = preferidaRepositorio.listarTodas().size();
		var amigos = listarAmigosComAbertura(solicitante, aberturaId);
		return new AberturaPreferidaDetalhesVisao(abertura, ehMinha, visivel, posicao, totalComunidade, amigos);
	}

	@Override
	public AberturaPreferida escolher(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			AberturaIdentificador aberturaId) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(aberturaId);
		exigirProprietario(solicitante, alvo);
		return servico.escolher(alvo, aberturaId);
	}

	@Override
	public AberturaPreferida escolherPorNumero(UsuarioIdentificador solicitante, UsuarioIdentificador alvo, int numero) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		exigirProprietario(solicitante, alvo);
		return servico.escolherPorNumero(alvo, numero);
	}

	@Override
	public AberturaPreferida alterarVisibilidade(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			boolean visivel) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		exigirProprietario(solicitante, alvo);
		return servico.alterarVisibilidade(alvo, visivel);
	}

	private boolean saoAmigosAceitos(UsuarioIdentificador u1, UsuarioIdentificador u2) {
		return amizadeRepositorio.existeRelacaoEntre(u1, u2)
				&& amizadeRepositorio.buscarRelacaoEntre(u1, u2).estaAceita();
	}

	private static void exigirProprietario(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		if (!solicitante.equals(alvo)) {
			throw new NaoAutorizadoExcecao(AberturaPreferidaServico.OPERACAO_NAO_AUTORIZADA);
		}
	}
}
