package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class CartaIdentificador extends ObjetoValorStringAbstrato {
	public static final int TAMANHO = 100;

	public CartaIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador da carta é obrigatório");
		Validate.isTrue(valor.length() <= TAMANHO,
				"O identificador da carta deve ter no máximo %d caracteres", TAMANHO);
	}
}
