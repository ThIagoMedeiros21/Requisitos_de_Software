package com.cesar.tgchub.tcghub.dominio.adm.abertura;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoExistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;

public final class AberturaServico
		extends AgregadoServicoAbstrato<AberturaIdentificador, Abertura, AberturaAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe abertura com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe uma abertura com o identificador %s";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador da abertura é gerado na inclusão e não deve ser informado";
	public static final String NUMERO_EXISTENTE_MODELO = "Já existe uma abertura cadastrada com o número %d";
	public static final String NUMERO_INEXISTENTE_MODELO = "Não existe abertura com o número %d";
	public static final String ABERTURA_JA_ESCOLHIDA_EXCLUSAO_MODELO = "A abertura número %d já foi escolhida por usuários e não pode ser excluída; utilize a opção de inativar";

	private final IAberturaRepositorio repositorio;

	public AberturaServico(ISegurancaServico segurancaServico, IAberturaRepositorio repositorio) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
	}

	@Override
	public AberturaAutorizacoes criarAutorizacoes(Optional<AberturaIdentificador> identificador) {
		Objects.requireNonNull(identificador);
		var ehAdmin = getSegurancaServico().possuiAutorizacao(Autorizacao.ADMINISTRADOR.name());
		return new AberturaAutorizacoes(ehAdmin, true, ehAdmin, ehAdmin, ehAdmin, ehAdmin);
	}

	@Override
	protected void validarInclusao(Abertura agregado) {
		Objects.requireNonNull(agregado);
		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
	}

	@Override
	protected void validarInclusaoAlteracao(Abertura agregado) {
		Objects.requireNonNull(agregado);
		if (repositorio.existePorNumero(agregado.getNumeroValor(), agregado.getIdentificador())) {
			throw new AgregadoExistenteExcecao(NUMERO_EXISTENTE_MODELO.formatted(agregado.getNumeroValor()));
		}
	}

	@Override
	protected void validarExclusao(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		if (repositorio.possuiEscolhaPorUsuario(identificador)) {
			var abertura = repositorio.buscarPorIdentificador(identificador);
			throw new IllegalStateException(
					ABERTURA_JA_ESCOLHIDA_EXCLUSAO_MODELO.formatted(abertura.getNumeroValor()));
		}
	}

	public Abertura buscarPorNumero(int numero) {
		validarAutorizacao(Optional.empty(), AberturaAutorizacoes::getBuscar);
		if (!repositorio.existePorNumero(numero)) {
			throw new AgregadoInexistenteExcecao(NUMERO_INEXISTENTE_MODELO.formatted(numero));
		}
		return repositorio.buscarPorNumero(numero);
	}

	public Abertura inativar(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		validarAutorizacao(Optional.of(identificador), AberturaAutorizacoes::getInativar);
		var existente = repositorio.buscarPorIdentificador(identificador);
		var inativada = existente.inativar();
		repositorio.alterar(inativada);
		return inativada;
	}

	public Abertura reativar(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		validarAutorizacao(Optional.of(identificador), AberturaAutorizacoes::getReativar);
		var existente = repositorio.buscarPorIdentificador(identificador);
		var reativada = existente.reativar();
		repositorio.alterar(reativada);
		return reativada;
	}

	public Abertura inativarPorNumero(int numero) {
		validarAutorizacao(Optional.empty(), AberturaAutorizacoes::getInativar);
		if (!repositorio.existePorNumero(numero)) {
			throw new AgregadoInexistenteExcecao(NUMERO_INEXISTENTE_MODELO.formatted(numero));
		}
		var existente = repositorio.buscarPorNumero(numero);
		return inativar(existente.getIdentificador().orElseThrow());
	}

	public Abertura reativarPorNumero(int numero) {
		validarAutorizacao(Optional.empty(), AberturaAutorizacoes::getReativar);
		if (!repositorio.existePorNumero(numero)) {
			throw new AgregadoInexistenteExcecao(NUMERO_INEXISTENTE_MODELO.formatted(numero));
		}
		var existente = repositorio.buscarPorNumero(numero);
		return reativar(existente.getIdentificador().orElseThrow());
	}

	public void excluirPorNumero(int numero) {
		validarAutorizacao(Optional.empty(), AberturaAutorizacoes::getExcluir);
		if (!repositorio.existePorNumero(numero)) {
			throw new AgregadoInexistenteExcecao(NUMERO_INEXISTENTE_MODELO.formatted(numero));
		}
		var existente = repositorio.buscarPorNumero(numero);
		excluir(existente.getIdentificador().orElseThrow());
	}

	public List<Abertura> listarPorStatus(Optional<Boolean> ativa) {
		Objects.requireNonNull(ativa);
		validarAutorizacao(Optional.empty(), AberturaAutorizacoes::getBuscar);
		return ativa.map(repositorio::listarPorStatus).orElseGet(repositorio::listarTodas);
	}

	public List<Abertura> listarAtivas() {
		return listarPorStatus(Optional.of(true));
	}
}
