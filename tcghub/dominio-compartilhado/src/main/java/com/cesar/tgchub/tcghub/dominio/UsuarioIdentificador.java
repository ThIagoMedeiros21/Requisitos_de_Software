package com.cesar.tgchub.tcghub.dominio;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class UsuarioIdentificador extends ObjetoValorStringAbstrato {
	public static final int TAMANHO = 100;

	public UsuarioIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador do usuário é obrigatório");
		Validate.isTrue(valor.length() <= TAMANHO,
				"O identificador do usuário deve ter no máximo %d caracteres", TAMANHO);
	}
}
