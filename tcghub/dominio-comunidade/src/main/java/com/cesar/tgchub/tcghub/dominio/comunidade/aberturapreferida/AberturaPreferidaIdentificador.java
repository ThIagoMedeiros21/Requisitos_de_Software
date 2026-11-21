package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.IIdentificador;
import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class AberturaPreferidaIdentificador extends ObjetoValorStringAbstrato implements IIdentificador {
	public static final int TAMANHO = 36;

	public AberturaPreferidaIdentificador(String valor) {
		super(validar(valor));
	}

	private static String validar(String valor) {
		Validate.notBlank(valor, "O identificador da abertura preferida não pode ser vazio");
		var limpo = valor.trim();
		Validate.isTrue(limpo.length() <= TAMANHO,
				"O identificador da abertura preferida não pode ter mais de %d caracteres", TAMANHO);
		return limpo;
	}
}
