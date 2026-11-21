package com.cesar.tgchub.tcghub.dominio.comunidade.amizade;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class AmizadeServico
		extends AgregadoServicoAbstrato<AmizadeIdentificador, Amizade, AmizadeAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe amizade com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe uma amizade com o identificador %s";
	public static final String DESTINATARIO_INEXISTENTE_MODELO = "O usuário %s não existe";
	public static final String SOLICITACAO_PENDENTE_MODELO = "Já existe uma solicitação pendente entre %s e %s";
	public static final String AMIZADE_JA_ATIVA_MODELO = "Os usuários %s e %s já são amigos";
	public static final String NAO_SAO_AMIGOS_MODELO = "Os usuários %s e %s não são amigos";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador da amizade é gerado na inclusão e não deve ser informado";

	private final IAmizadeRepositorio repositorio;
	private final IPerfilComunitarioRepositorio perfilRepositorio;

	public AmizadeServico(ISegurancaServico segurancaServico, IAmizadeRepositorio repositorio,
			IPerfilComunitarioRepositorio perfilRepositorio) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
		this.perfilRepositorio = Objects.requireNonNull(perfilRepositorio);
	}

	@Override
	public AmizadeAutorizacoes criarAutorizacoes(Optional<AmizadeIdentificador> identificador) {
		Objects.requireNonNull(identificador);

		var possuiPapelUsuario = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name());
		if (identificador.isEmpty() || !repositorio.existePorIdentificador(identificador.get())) {
			return new AmizadeAutorizacoes(possuiPapelUsuario, true, false, possuiPapelUsuario, possuiPapelUsuario,
					possuiPapelUsuario, possuiPapelUsuario, possuiPapelUsuario);
		}

		var amizade = repositorio.buscarPorIdentificador(identificador.get());
		var usuarioAtual = new UsuarioIdentificador(getSegurancaServico().getUsuario());
		var ehSolicitante = amizade.getSolicitante().equals(usuarioAtual);
		var ehDestinatario = amizade.getDestinatario().equals(usuarioAtual);
		var ehParticipante = ehSolicitante || ehDestinatario;

		return new AmizadeAutorizacoes(possuiPapelUsuario, ehParticipante, false, ehParticipante,
				possuiPapelUsuario && ehDestinatario && amizade.estaPendente(),
				possuiPapelUsuario && ehDestinatario && amizade.estaPendente(),
				possuiPapelUsuario && ehSolicitante && amizade.estaPendente(),
				possuiPapelUsuario && ehParticipante && amizade.estaAceita());
	}

	@Override
	protected void validarInclusao(Amizade agregado) {
		Objects.requireNonNull(agregado);

		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
		exigirUsuarioAutenticado(agregado.getSolicitante());
		Validate.isTrue(agregado.estaPendente(), "Toda nova solicitação de amizade deve iniciar pendente");
	}

	@Override
	protected void validarInclusaoAlteracao(Amizade agregado) {
		Objects.requireNonNull(agregado);

		if (agregado.getIdentificador().isEmpty()) {
			validarDestinoERelacao(agregado.getSolicitante(), agregado.getDestinatario());
		}
	}

	public Amizade enviarSolicitacao(UsuarioIdentificador solicitante, UsuarioIdentificador destinatario) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(destinatario);

		validarAutorizacao(Optional.empty(), AmizadeAutorizacoes::getIncluir);
		exigirUsuarioAutenticado(solicitante);
		Validate.isTrue(!solicitante.equals(destinatario), Amizade.AUTO_SOLICITACAO);
		validarDestinoERelacao(solicitante, destinatario);

		var nova = new Amizade(Optional.empty(), solicitante, destinatario, StatusAmizade.PENDENTE, Instant.now(),
				Optional.empty());
		return repositorio.incluir(nova);
	}

	public Amizade aceitarSolicitacao(UsuarioIdentificador destinatario, AmizadeIdentificador identificador) {
		Objects.requireNonNull(destinatario);
		Objects.requireNonNull(identificador);

		exigirUsuarioAutenticado(destinatario);
		validarExistencia(identificador);

		var amizade = repositorio.buscarPorIdentificador(identificador);
		if (!amizade.getDestinatario().equals(destinatario)) {
			throw new NaoAutorizadoExcecao("Apenas o destinatário pode aceitar a solicitação de amizade");
		}

		var aceita = amizade.aceitar(Instant.now());
		repositorio.alterar(aceita);
		return aceita;
	}

	public void recusarSolicitacao(UsuarioIdentificador destinatario, AmizadeIdentificador identificador) {
		Objects.requireNonNull(destinatario);
		Objects.requireNonNull(identificador);

		exigirUsuarioAutenticado(destinatario);
		validarExistencia(identificador);

		var amizade = repositorio.buscarPorIdentificador(identificador);
		if (!amizade.getDestinatario().equals(destinatario)) {
			throw new NaoAutorizadoExcecao("Apenas o destinatário pode recusar a solicitação de amizade");
		}
		Validate.validState(amizade.estaPendente(), "A solicitação de amizade já foi respondida");

		repositorio.excluir(identificador);
	}

	public void cancelarSolicitacao(UsuarioIdentificador solicitante, AmizadeIdentificador identificador) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(identificador);

		exigirUsuarioAutenticado(solicitante);
		validarExistencia(identificador);

		var amizade = repositorio.buscarPorIdentificador(identificador);
		if (!amizade.getSolicitante().equals(solicitante)) {
			throw new NaoAutorizadoExcecao("Apenas o solicitante pode cancelar a solicitação de amizade");
		}
		Validate.validState(amizade.estaPendente(), "A solicitação de amizade não está mais pendente");

		repositorio.excluir(identificador);
	}

	public void removerAmizadeBilateral(UsuarioIdentificador solicitante, UsuarioIdentificador amigo) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(amigo);

		exigirUsuarioAutenticado(solicitante);
		Validate.isTrue(!solicitante.equals(amigo), Amizade.AUTO_SOLICITACAO);

		if (!repositorio.existeRelacaoEntre(solicitante, amigo)) {
			throw new IllegalStateException(NAO_SAO_AMIGOS_MODELO.formatted(solicitante, amigo));
		}

		var amizade = repositorio.buscarRelacaoEntre(solicitante, amigo);
		Validate.validState(amizade.estaAceita(), NAO_SAO_AMIGOS_MODELO, solicitante, amigo);

		repositorio.excluir(amizade.getIdentificador().orElseThrow());
	}

	public List<Amizade> listarAmizadesAtivas(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorio.listarAmizadesAtivas(usuario);
	}

	public List<Amizade> listarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorio.listarPorUsuario(usuario);
	}

	private void validarDestinoERelacao(UsuarioIdentificador solicitante, UsuarioIdentificador destinatario) {
		if (!perfilRepositorio.existePorUsuario(destinatario)) {
			throw new AgregadoInexistenteExcecao(DESTINATARIO_INEXISTENTE_MODELO.formatted(destinatario));
		}
		if (repositorio.existeRelacaoEntre(solicitante, destinatario)) {
			var existente = repositorio.buscarRelacaoEntre(solicitante, destinatario);
			if (existente.estaAceita()) {
				throw new IllegalStateException(AMIZADE_JA_ATIVA_MODELO.formatted(solicitante, destinatario));
			}
			throw new IllegalStateException(SOLICITACAO_PENDENTE_MODELO.formatted(solicitante, destinatario));
		}
	}

	private void exigirUsuarioAutenticado(UsuarioIdentificador usuario) {
		if (!getSegurancaServico().getUsuario().equals(usuario.toString())) {
			throw new NaoAutorizadoExcecao("Ação não autorizada para outro usuário");
		}
	}
}
