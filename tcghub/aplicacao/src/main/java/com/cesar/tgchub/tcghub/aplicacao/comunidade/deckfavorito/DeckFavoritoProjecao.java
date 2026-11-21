package com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito;

import java.time.Instant;
import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class DeckFavoritoProjecao implements IProjecao<DeckFavorito> {
	private final DeckFavoritoIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final DeckIdentificador deck;
	private final int ordem;
	private final Instant adicionadoEm;

	public DeckFavoritoProjecao(DeckFavoritoIdentificador identificador, UsuarioIdentificador usuario,
			DeckIdentificador deck, int ordem, Instant adicionadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.deck = Objects.requireNonNull(deck);
		this.ordem = ordem;
		this.adicionadoEm = Objects.requireNonNull(adicionadoEm);
	}

	public DeckFavoritoProjecao(Long identificador, String usuario, String deck, int ordem, Instant adicionadoEm) {
		this(new DeckFavoritoIdentificador(identificador.toString()), new UsuarioIdentificador(usuario),
				new DeckIdentificador(deck), ordem, adicionadoEm);
	}

	public DeckFavoritoIdentificador getIdentificador() {
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
}
