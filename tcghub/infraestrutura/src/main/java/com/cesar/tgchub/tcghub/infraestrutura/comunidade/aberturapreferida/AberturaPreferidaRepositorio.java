package com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi.IAberturaPreferidaRepositorio;
import com.cesar.tgchub.tcghub.infraestrutura.adm.abertura.IAberturaRepositorioJpa;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class AberturaPreferidaRepositorio extends
		AgregadoRepositorioAbstrato<AberturaPreferidaIdentificador, AberturaPreferida, Long, AberturaPreferidaJpa>
		implements IAberturaPreferidaRepositorio {
	private final IAberturaPreferidaRepositorioJpa repositorioJpa;
	private final IAberturaRepositorioJpa aberturaRepositorioJpa;

	public AberturaPreferidaRepositorio(IAberturaPreferidaRepositorioJpa repositorioJpa,
			IAberturaRepositorioJpa aberturaRepositorioJpa) {
		super(repositorioJpa, AberturaPreferidaServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
		this.aberturaRepositorioJpa = Objects.requireNonNull(aberturaRepositorioJpa);
	}

	@Override
	protected AberturaPreferidaJpa paraJpa(AberturaPreferida agregado) {
		Objects.requireNonNull(agregado);

		var aberturaId = converterAberturaId(agregado.getAbertura()).orElseThrow(() -> new AgregadoInexistenteExcecao(
				AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(agregado.getAbertura())));
		var aberturaJpa = aberturaRepositorioJpa.findById(aberturaId).orElseThrow(() -> new AgregadoInexistenteExcecao(
				AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(agregado.getAbertura())));

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(AberturaPreferidaServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.atualizar(aberturaJpa, agregado.isVisivelNoPerfil(), agregado.getEscolhidaEm());
			return existente;
		}
		return new AberturaPreferidaJpa(null, agregado.getUsuario().toString(), aberturaJpa,
				agregado.isVisivelNoPerfil(), agregado.getEscolhidaEm());
	}

	@Override
	protected AberturaPreferida paraAgregado(AberturaPreferidaJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var id = new AberturaPreferidaIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		var aberturaId = new AberturaIdentificador(
				Objects.requireNonNull(agregadoJpa.getAbertura().getIdentificador()).toString());
		return new AberturaPreferida(Optional.of(id), new UsuarioIdentificador(agregadoJpa.getUsuario()), aberturaId,
				agregadoJpa.isVisivelNoPerfil(), agregadoJpa.getEscolhidaEm());
	}

	@Override
	protected Long paraIdentificadorJpa(AberturaPreferidaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).orElseThrow(() -> new AgregadoInexistenteExcecao(
				AberturaPreferidaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador)));
	}

	private static Optional<Long> converter(AberturaPreferidaIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	private static Optional<Long> converterAberturaId(AberturaIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(AberturaPreferidaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existePorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.existsByUsuario(usuario.toString());
	}

	@Override
	public AberturaPreferida buscarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.findByUsuario(usuario.toString()).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						AberturaPreferidaServico.USUARIO_SEM_PREFERIDA_MODELO.formatted(usuario)));
	}

	@Override
	public List<AberturaPreferida> listarTodas() {
		return repositorioJpa.findAll().stream().map(this::paraAgregado).toList();
	}

	@Override
	public int contarPorAbertura(AberturaIdentificador abertura) {
		Objects.requireNonNull(abertura);
		return converterAberturaId(abertura).map(repositorioJpa::countByAberturaIdentificador).orElse(0);
	}

	@Override
	public List<AberturaPreferida> listarPorAbertura(AberturaIdentificador abertura) {
		Objects.requireNonNull(abertura);
		return converterAberturaId(abertura).map(repositorioJpa::findByAberturaIdentificador).orElse(List.of())
				.stream().map(this::paraAgregado).toList();
	}
}
