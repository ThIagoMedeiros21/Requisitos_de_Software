package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi.IAmizadeRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class AmizadeRepositorio
		extends AgregadoRepositorioAbstrato<AmizadeIdentificador, Amizade, Long, AmizadeJpa>
		implements IAmizadeRepositorio {
	private final IAmizadeRepositorioJpa repositorioJpa;

	public AmizadeRepositorio(IAmizadeRepositorioJpa repositorioJpa) {
		super(repositorioJpa, AmizadeServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected AmizadeJpa paraJpa(Amizade agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		return new AmizadeJpa(identificador, agregado.getSolicitante().toString(),
				agregado.getDestinatario().toString(), agregado.getStatus(), agregado.getCriadoEm(),
				agregado.getAtualizadoEm().orElse(null));
	}

	@Override
	protected Amizade paraAgregado(AmizadeJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var identificador = new AmizadeIdentificador(Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		return new Amizade(Optional.of(identificador), new UsuarioIdentificador(agregadoJpa.getSolicitante()),
				new UsuarioIdentificador(agregadoJpa.getDestinatario()), agregadoJpa.getStatus(),
				agregadoJpa.getCriadoEm(), Optional.ofNullable(agregadoJpa.getAtualizadoEm()));
	}

	@Override
	protected Long paraIdentificadorJpa(AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);

		return converter(identificador).orElseThrow(() -> {
			var mensagem = AmizadeServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador);
			return new AgregadoInexistenteExcecao(mensagem);
		});
	}

	private static Optional<Long> converter(AmizadeIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(AmizadeIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existeRelacaoEntre(UsuarioIdentificador usuarioA, UsuarioIdentificador usuarioB) {
		Objects.requireNonNull(usuarioA);
		Objects.requireNonNull(usuarioB);
		return repositorioJpa.existeRelacaoEntre(usuarioA.toString(), usuarioB.toString());
	}

	@Override
	public Amizade buscarRelacaoEntre(UsuarioIdentificador usuarioA, UsuarioIdentificador usuarioB) {
		Objects.requireNonNull(usuarioA);
		Objects.requireNonNull(usuarioB);
		return repositorioJpa.buscarRelacaoEntre(usuarioA.toString(), usuarioB.toString()).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						"Não existe relação entre %s e %s".formatted(usuarioA, usuarioB)));
	}

	@Override
	public List<Amizade> listarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.listarPorUsuario(usuario.toString()).stream().map(this::paraAgregado).toList();
	}

	@Override
	public List<Amizade> listarAmizadesAtivas(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.listarAmizadesAtivas(usuario.toString()).stream().map(this::paraAgregado).toList();
	}
}
