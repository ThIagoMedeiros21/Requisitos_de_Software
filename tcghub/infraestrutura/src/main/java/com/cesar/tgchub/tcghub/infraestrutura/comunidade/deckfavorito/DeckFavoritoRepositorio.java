package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.spi.IDeckFavoritoRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class DeckFavoritoRepositorio
		extends AgregadoRepositorioAbstrato<DeckFavoritoIdentificador, DeckFavorito, Long, DeckFavoritoJpa>
		implements IDeckFavoritoRepositorio {
	private final IDeckFavoritoRepositorioJpa repositorioJpa;

	public DeckFavoritoRepositorio(IDeckFavoritoRepositorioJpa repositorioJpa) {
		super(repositorioJpa, DeckFavoritoServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected DeckFavoritoJpa paraJpa(DeckFavorito agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(DeckFavoritoServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.atualizarOrdem(agregado.getOrdem());
			return existente;
		}

		return new DeckFavoritoJpa(null, agregado.getUsuario().toString(), agregado.getDeck().toString(),
				agregado.getOrdem(), agregado.getAdicionadoEm());
	}

	@Override
	protected DeckFavorito paraAgregado(DeckFavoritoJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var identificador = new DeckFavoritoIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		return new DeckFavorito(Optional.of(identificador), new UsuarioIdentificador(agregadoJpa.getUsuario()),
				new DeckIdentificador(agregadoJpa.getDeck()), agregadoJpa.getOrdem(), agregadoJpa.getAdicionadoEm());
	}

	@Override
	protected Long paraIdentificadorJpa(DeckFavoritoIdentificador identificador) {
		Objects.requireNonNull(identificador);

		return converter(identificador).orElseThrow(() -> {
			var mensagem = DeckFavoritoServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador);
			return new AgregadoInexistenteExcecao(mensagem);
		});
	}

	private static Optional<Long> converter(DeckFavoritoIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(DeckFavoritoIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public int contarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.countByUsuario(usuario.toString());
	}

	@Override
	public boolean existePorUsuarioEDeck(UsuarioIdentificador usuario, DeckIdentificador deck) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(deck);
		return repositorioJpa.existsByUsuarioAndDeck(usuario.toString(), deck.toString());
	}

	@Override
	public DeckFavorito buscarPorUsuarioEDeck(UsuarioIdentificador usuario, DeckIdentificador deck) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(deck);
		return repositorioJpa.findByUsuarioAndDeck(usuario.toString(), deck.toString()).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						DeckFavoritoServico.DECK_NAO_FAVORITADO_MODELO.formatted(deck, usuario)));
	}

	@Override
	public List<DeckFavorito> listarPorUsuario(UsuarioIdentificador usuario) {
		Objects.requireNonNull(usuario);
		return repositorioJpa.findByUsuarioOrderByOrdemAsc(usuario.toString()).stream().map(this::paraAgregado)
				.toList();
	}
}
