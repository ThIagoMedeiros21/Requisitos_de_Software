package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import java.util.List;
import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;

import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class PerfilComunitarioVisibilidadeProxy implements IPerfilComunitarioVisibilidadeProxy {
	private final PerfilComunitarioServico servico;
	private final IColecaoCartaConsulta colecaoCartaConsulta;

	public PerfilComunitarioVisibilidadeProxy(PerfilComunitarioServico servico,
			IColecaoCartaConsulta colecaoCartaConsulta) {
		this.servico = Objects.requireNonNull(servico);
		this.colecaoCartaConsulta = Objects.requireNonNull(colecaoCartaConsulta);
	}

	@Override
	public PerfilComunitarioVisao consultarPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);

		var perfil = servico.buscarPorUsuario(alvo);
		return paraVisao(solicitante, perfil);
	}

	@Override
	public PerfilComunitarioVisao consultarPerfilPorIdentificador(UsuarioIdentificador solicitante,
			PerfilComunitarioIdentificador identificador) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(identificador);

		var perfil = servico.buscar(identificador);
		return paraVisao(solicitante, perfil);
	}

	@Override
	public void editarPerfil(UsuarioIdentificador solicitante, PerfilComunitario agregado) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(agregado);

		if (!solicitante.equals(agregado.getUsuario())) {
			throw new NaoAutorizadoExcecao(PerfilComunitarioServico.USUARIO_PROPRIO_OBRIGATORIO);
		}
		servico.alterar(agregado);
	}

	@Override
	public PerfilComunitario fixarCartaFavorita(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			CartaIdentificador carta) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(carta);

		exigirProprietario(solicitante, alvo);
		return servico.fixarCartaFavorita(alvo, carta);
	}

	@Override
	public PerfilComunitario removerCartaFavorita(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			CartaIdentificador carta) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(alvo);
		Objects.requireNonNull(carta);

		exigirProprietario(solicitante, alvo);
		return servico.removerCartaFavorita(alvo, carta);
	}

	private PerfilComunitarioVisao paraVisao(UsuarioIdentificador solicitante, PerfilComunitario perfil) {
		var ehProprietario = solicitante.equals(perfil.getUsuario());
		var cartasDisponiveis = ehProprietario ? colecaoCartaConsulta.listarCartasDisponiveis(perfil.getUsuario())
				: List.<CartaColecaoResumo>of();
		return new PerfilComunitarioVisao(perfil.getIdentificador().orElseThrow(), perfil.getUsuario(),
				perfil.getApelido(), perfil.getCidade(), perfil.getDescricao(), perfil.getFoto(),
				perfil.getPreferenciasPublicas(), perfil.isDecksFavoritosVisiveis(), perfil.getCartasFavoritas(),
				ehProprietario, cartasDisponiveis);
	}

	private static void exigirProprietario(UsuarioIdentificador solicitante, UsuarioIdentificador alvo) {
		if (!solicitante.equals(alvo)) {
			throw new NaoAutorizadoExcecao(PerfilComunitarioServico.USUARIO_PROPRIO_OBRIGATORIO);
		}
	}
}
