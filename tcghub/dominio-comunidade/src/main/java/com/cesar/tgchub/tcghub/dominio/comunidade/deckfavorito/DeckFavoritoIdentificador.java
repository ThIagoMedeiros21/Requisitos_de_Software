package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.IIdentificador;
import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class DeckFavoritoIdentificador extends ObjetoValorStringAbstrato implements IIdentificador {
	public DeckFavoritoIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador do deck favorito é obrigatório");
	}
}
