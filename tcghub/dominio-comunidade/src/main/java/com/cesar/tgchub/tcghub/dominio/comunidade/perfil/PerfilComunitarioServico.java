package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class PerfilComunitarioServico
		extends AgregadoServicoAbstrato<PerfilComunitarioIdentificador, PerfilComunitario, PerfilComunitarioAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe perfil comunitário com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe um perfil comunitário com o identificador %s";
	public static final String USUARIO_EXISTENTE_MODELO = "Já existe um perfil comunitário para o usuário %s";
	public static final String APELIDO_EXISTENTE_MODELO = "O apelido %s já está em uso";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador do perfil comunitário é gerado na inclusão e não deve ser informado";
	public static final String CARTA_NAO_POSSUIDA_MODELO = "A carta %s não pertence à coleção do usuário";
	public static final String USUARIO_PROPRIO_OBRIGATORIO = "Apenas o proprietário pode gerenciar este perfil comunitário";

	private final IPerfilComunitarioRepositorio repositorio;
	private final IColecaoCartaConsulta colecaoCartaConsulta;

	public PerfilComunitarioServico(ISegurancaServico segurancaServico, IPerfilComunitarioRepositorio repositorio,
			IColecaoCartaConsulta colecaoCartaConsulta) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
		this.colecaoCartaConsulta = Objects.requireNonNull(colecaoCartaConsulta);
	}

	@Override
	public PerfilComunitarioAutorizacoes criarAutorizacoes(Optional<PerfilComunitarioIdentificador> identificador) {
		Objects.requireNonNull(identificador);

		var possuiPapelUsuario = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name());
		if (identificador.isEmpty() || !repositorio.existePorIdentificador(identificador.get())) {
			return new PerfilComunitarioAutorizacoes(possuiPapelUsuario, true, possuiPapelUsuario, false,
					possuiPapelUsuario, possuiPapelUsuario);
		}

		var perfil = repositorio.buscarPorIdentificador(identificador.get());
		var ehProprietario = possuiPapelUsuario
				&& getSegurancaServico().getUsuario().equals(perfil.getUsuario().toString());
		return new PerfilComunitarioAutorizacoes(possuiPapelUsuario, true, ehProprietario, false, ehProprietario,
				ehProprietario);
	}

	@Override
	protected void validarInclusao(PerfilComunitario agregado) {
		Objects.requireNonNull(agregado);

		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
		validarInexistencia(repositorio::existePorUsuario, agregado.getUsuario(), USUARIO_EXISTENTE_MODELO);
	}

	@Override
	protected void validarInclusaoAlteracao(PerfilComunitario agregado) {
		Objects.requireNonNull(agregado);

		validarInexistencia(apelido -> repositorio.existePorApelido(apelido, agregado.getIdentificador()),
				agregado.getApelido(), APELIDO_EXISTENTE_MODELO);
	}

	@Override
	protected void validarAlteracao(PerfilComunitario agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().orElseThrow();
		var existente = repositorio.buscarPorIdentificador(identificador);
		if (!getSegurancaServico().getUsuario().equals(existente.getUsuario().toString())) {
			throw new NaoAutorizadoExcecao(USUARIO_PROPRIO_OBRIGATORIO);
		}
		Validate.isTrue(existente.getUsuario().equals(agregado.getUsuario()),
				"O proprietário do perfil comunitário não pode ser alterado");
	}

	public PerfilComunitario buscarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorio.buscarPorUsuario(usuario);
	}

	public PerfilComunitario fixarCartaFavorita(UsuarioIdentificador usuario, CartaIdentificador carta) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(carta);

		var perfil = repositorio.buscarPorUsuario(usuario);
		validarAutorizacao(perfil.getIdentificador(), PerfilComunitarioAutorizacoes::getFixarDestaque);

		var atualizado = perfil.fixarCartaFavorita(carta);
		Validate.validState(colecaoCartaConsulta.usuarioPossuiCarta(usuario, carta), CARTA_NAO_POSSUIDA_MODELO, carta);

		repositorio.alterar(atualizado);
		return atualizado;
	}

	public PerfilComunitario removerCartaFavorita(UsuarioIdentificador usuario, CartaIdentificador carta) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(carta);

		var perfil = repositorio.buscarPorUsuario(usuario);
		validarAutorizacao(perfil.getIdentificador(), PerfilComunitarioAutorizacoes::getRemoverDestaque);

		var atualizado = perfil.removerCartaFavorita(carta);
		repositorio.alterar(atualizado);
		return atualizado;
	}

	public PerfilComunitario alterarVisibilidadeDecksFavoritos(UsuarioIdentificador usuario, boolean visivel) {
		Objects.requireNonNull(usuario);

		var perfil = repositorio.buscarPorUsuario(usuario);
		validarAutorizacao(perfil.getIdentificador(), PerfilComunitarioAutorizacoes::getAlterar);

		var atualizado = perfil.alterarVisibilidadeDecksFavoritos(visivel);
		repositorio.alterar(atualizado);
		return atualizado;
	}
}
