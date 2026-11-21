package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IPerfilComunitarioRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class PerfilComunitarioRepositorio
		extends AgregadoRepositorioAbstrato<PerfilComunitarioIdentificador, PerfilComunitario, Long, PerfilComunitarioJpa>
		implements IPerfilComunitarioRepositorio {
	private final IPerfilComunitarioRepositorioJpa repositorioJpa;

	public PerfilComunitarioRepositorio(IPerfilComunitarioRepositorioJpa repositorioJpa) {
		super(repositorioJpa, PerfilComunitarioServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected PerfilComunitarioJpa paraJpa(PerfilComunitario agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		var cartas = agregado.getCartasFavoritas().stream().map(CartaIdentificador::toString).toList();
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(PerfilComunitarioServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.getCartasFavoritas().clear();
			repositorioJpa.flush();
			existente.atualizarDados(agregado.getApelido().toString(), agregado.getCidade().orElse(null),
					agregado.getDescricao().orElse(null), agregado.getFoto().orElse(null),
					agregado.getPreferenciasPublicas().orElse(null), agregado.isDecksFavoritosVisiveis(), cartas);
			return existente;
		}

		var entidade = new PerfilComunitarioJpa(null, agregado.getUsuario().toString(),
				agregado.getApelido().toString(), agregado.getCidade().orElse(null),
				agregado.getDescricao().orElse(null), agregado.getFoto().orElse(null),
				agregado.getPreferenciasPublicas().orElse(null), agregado.isDecksFavoritosVisiveis());
		entidade.substituirCartasFavoritas(cartas);
		return entidade;
	}

	@Override
	protected PerfilComunitario paraAgregado(PerfilComunitarioJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var identificador = new PerfilComunitarioIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		var cartas = agregadoJpa.getCartasFavoritas().stream()
				.map(item -> new CartaIdentificador(item.getCarta())).toList();
		return new PerfilComunitario(Optional.of(identificador), new UsuarioIdentificador(agregadoJpa.getUsuario()),
				new Apelido(agregadoJpa.getApelido()), Optional.ofNullable(agregadoJpa.getCidade()),
				Optional.ofNullable(agregadoJpa.getDescricao()), Optional.ofNullable(agregadoJpa.getFoto()),
				Optional.ofNullable(agregadoJpa.getPreferenciasPublicas()), agregadoJpa.isDecksFavoritosVisiveis(),
				cartas);
	}

	@Override
	protected Long paraIdentificadorJpa(PerfilComunitarioIdentificador identificador) {
		Objects.requireNonNull(identificador);

		return converter(identificador).orElseThrow(() -> {
			var mensagem = PerfilComunitarioServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador);
			return new AgregadoInexistenteExcecao(mensagem);
		});
	}

	private static Optional<Long> converter(PerfilComunitarioIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(PerfilComunitarioIdentificador identificador) {
		Objects.requireNonNull(identificador);

		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existePorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.existsByUsuario(usuario.toString());
	}

	@Override
	public PerfilComunitario buscarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.findByUsuario(usuario.toString()).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						"Não existe perfil comunitário para o usuário %s".formatted(usuario)));
	}

	@Override
	public boolean existePorApelido(Apelido apelido, Optional<PerfilComunitarioIdentificador> identificadorIgnorado) {
		Objects.requireNonNull(apelido);
		Objects.requireNonNull(identificadorIgnorado);

		var ignoradoJpa = identificadorIgnorado.map(this::paraIdentificadorJpa).orElse(null);
		return repositorioJpa.existePorApelido(apelido.toString(), ignoradoJpa);
	}
}
