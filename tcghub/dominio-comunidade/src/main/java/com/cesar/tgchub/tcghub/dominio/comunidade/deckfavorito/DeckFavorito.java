package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class DeckFavorito implements IAgregado<DeckFavoritoIdentificador> {
	public static final int MAX_DECKS_FAVORITOS = 4;

	private final Optional<DeckFavoritoIdentificador> identificador;
	private final UsuarioIdentificador usuario;
	private final DeckIdentificador deck;
	private final int ordem;
	private final Instant adicionadoEm;

	public DeckFavorito(Optional<DeckFavoritoIdentificador> identificador, UsuarioIdentificador usuario,
			DeckIdentificador deck, int ordem, Instant adicionadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.deck = Objects.requireNonNull(deck);
		Validate.isTrue(ordem >= 1 && ordem <= MAX_DECKS_FAVORITOS,
				"A ordem do deck favorito deve estar entre 1 e %d", MAX_DECKS_FAVORITOS);
		this.ordem = ordem;
		this.adicionadoEm = Objects.requireNonNull(adicionadoEm);
	}

	@Override
	public Optional<DeckFavoritoIdentificador> getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public DeckIdentificador getDeck() {
		return deck;
	}

	public int getOrdem() {
		return ordem;
	}

	public Instant getAdicionadoEm() {
		return adicionadoEm;
	}

	public DeckFavorito comOrdem(int novaOrdem) {
		return new DeckFavorito(identificador, usuario, deck, novaOrdem, adicionadoEm);
	}
}
