package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class DeckIdentificador extends ObjetoValorStringAbstrato {
	public static final int TAMANHO = 100;

	public DeckIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador do deck é obrigatório");
		Validate.isTrue(valor.length() <= TAMANHO, "O identificador do deck deve ter no máximo %d caracteres", TAMANHO);
	}
}
