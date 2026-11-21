package com.cesar.tgchub.tcghub.ui.backend.seguranca;

import java.util.Objects;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito.DeckFavoritoAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioAutorizacoes;

import dev.sauloaraujo.d5.framework.ui.seguranca.AutenticacaoRecursoAbstrato;

public class AutenticacaoRecurso extends AutenticacaoRecursoAbstrato {
	private final PerfilComunitarioAutorizacoes perfilComunitario;
	private final AmizadeAutorizacoes amizade;
	private final DeckFavoritoAutorizacoes deckFavorito;
	private final AberturaAutorizacoes abertura;
	private final AberturaPreferidaAutorizacoes aberturaPreferida;

	public AutenticacaoRecurso(String usuario, PerfilComunitarioAutorizacoes perfilComunitario,
			AmizadeAutorizacoes amizade, DeckFavoritoAutorizacoes deckFavorito, AberturaAutorizacoes abertura,
			AberturaPreferidaAutorizacoes aberturaPreferida) {
		super(usuario);
		this.perfilComunitario = Objects.requireNonNull(perfilComunitario);
		this.amizade = Objects.requireNonNull(amizade);
		this.deckFavorito = Objects.requireNonNull(deckFavorito);
		this.abertura = Objects.requireNonNull(abertura);
		this.aberturaPreferida = Objects.requireNonNull(aberturaPreferida);
	}

	public PerfilComunitarioAutorizacoes getPerfilComunitario() {
		return perfilComunitario;
	}

	public AmizadeAutorizacoes getAmizade() {
		return amizade;
	}

	public DeckFavoritoAutorizacoes getDeckFavorito() {
		return deckFavorito;
	}

	public AberturaAutorizacoes getAbertura() {
		return abertura;
	}

	public AberturaPreferidaAutorizacoes getAberturaPreferida() {
		return aberturaPreferida;
	}
}
