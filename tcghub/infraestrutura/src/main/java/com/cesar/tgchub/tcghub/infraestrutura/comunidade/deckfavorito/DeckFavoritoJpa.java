package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavorito;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckIdentificador;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "DFA_DECK_FAVORITO", uniqueConstraints = @UniqueConstraint(columnNames = { "DFA_USR_ID", "DFA_DECK_ID" }))
public class DeckFavoritoJpa implements IAgregadoJpa<DeckFavorito> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DFA_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "DFA_USR_ID", nullable = false)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@Column(name = "DFA_DECK_ID", nullable = false)
	@NotNull
	@Size(max = DeckIdentificador.TAMANHO)
	private String deck;

	@Column(name = "DFA_ORDEM", nullable = false)
	private int ordem;

	@Column(name = "DFA_ADICIONADO_EM", nullable = false)
	@NotNull
	private Instant adicionadoEm;

	protected DeckFavoritoJpa() {
	}

	public DeckFavoritoJpa(@Nullable Long identificador, String usuario, String deck, int ordem, Instant adicionadoEm) {
		this.identificador = identificador;
		this.usuario = usuario;
		this.deck = deck;
		this.ordem = ordem;
		this.adicionadoEm = adicionadoEm;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getDeck() {
		return deck;
	}

	public int getOrdem() {
		return ordem;
	}

	public Instant getAdicionadoEm() {
		return adicionadoEm;
	}

	public void atualizarOrdem(int novaOrdem) {
		this.ordem = novaOrdem;
	}
}
