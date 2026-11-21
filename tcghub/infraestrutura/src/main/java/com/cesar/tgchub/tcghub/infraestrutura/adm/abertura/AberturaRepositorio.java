package com.cesar.tgchub.tcghub.infraestrutura.adm.abertura;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.spi.IAberturaRepositorio;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida.IAberturaPreferidaRepositorioJpa;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class AberturaRepositorio
		extends AgregadoRepositorioAbstrato<AberturaIdentificador, Abertura, Long, AberturaJpa>
		implements IAberturaRepositorio {
	private final IAberturaRepositorioJpa repositorioJpa;
	private final IAberturaPreferidaRepositorioJpa aberturaPreferidaRepositorioJpa;

	public AberturaRepositorio(IAberturaRepositorioJpa repositorioJpa,
			IAberturaPreferidaRepositorioJpa aberturaPreferidaRepositorioJpa) {
		super(repositorioJpa, AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
		this.aberturaPreferidaRepositorioJpa = Objects.requireNonNull(aberturaPreferidaRepositorioJpa);
	}

	@Override
	protected AberturaJpa paraJpa(Abertura agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.atualizar(agregado.getNumeroValor(), agregado.getNome(), agregado.getArtista(),
					agregado.getEpisodios(), agregado.getImagem(), agregado.getVideo(), agregado.isAtiva());
			return existente;
		}
		return new AberturaJpa(null, agregado.getNumeroValor(), agregado.getNome(), agregado.getArtista(),
				agregado.getEpisodios(), agregado.getImagem(), agregado.getVideo(), agregado.isAtiva());
	}

	@Override
	protected Abertura paraAgregado(AberturaJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var identificador = new AberturaIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		return new Abertura(Optional.of(identificador), agregadoJpa.getNumero(), agregadoJpa.getNome(),
				agregadoJpa.getArtista(), agregadoJpa.getEpisodios(), agregadoJpa.getImagem(), agregadoJpa.getVideo(),
				agregadoJpa.isAtiva());
	}

	@Override
	protected Long paraIdentificadorJpa(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);

		return converter(identificador).orElseThrow(() -> {
			var mensagem = AberturaServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador);
			return new AgregadoInexistenteExcecao(mensagem);
		});
	}

	private static Optional<Long> converter(AberturaIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existePorNumero(int numero, Optional<AberturaIdentificador> identificadorIgnorado) {
		Objects.requireNonNull(identificadorIgnorado);
		var ignoradoJpa = identificadorIgnorado.flatMap(AberturaRepositorio::converter).orElse(null);
		return repositorioJpa.existePorNumero(numero, ignoradoJpa);
	}

	@Override
	public boolean existePorNumero(int numero) {
		return repositorioJpa.existsByNumero(numero);
	}

	@Override
	public Abertura buscarPorNumero(int numero) {
		return repositorioJpa.findByNumero(numero).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						AberturaServico.NUMERO_INEXISTENTE_MODELO.formatted(numero)));
	}

	@Override
	public boolean possuiEscolhaPorUsuario(AberturaIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).map(aberturaPreferidaRepositorioJpa::existsByAberturaIdentificador)
				.orElse(false);
	}

	@Override
	public List<Abertura> listarTodas() {
		return repositorioJpa.findAllByOrderByNumeroAsc().stream().map(this::paraAgregado).toList();
	}

	@Override
	public List<Abertura> listarPorStatus(boolean ativa) {
		return repositorioJpa.findByAtivaOrderByNumeroAsc(ativa).stream().map(this::paraAgregado).toList();
	}
}
