package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class Apelido extends ObjetoValorStringAbstrato {
	public static final int TAMANHO = 50;

	public Apelido(String valor) {
		super(valor);
		Validate.notBlank(valor, "O apelido é obrigatório");
		Validate.isTrue(valor.length() <= TAMANHO, "O apelido deve ter no máximo %d caracteres", TAMANHO);
	}
}
