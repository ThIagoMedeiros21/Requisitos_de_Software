package com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class ProdutoIdentificador extends ObjetoValorStringAbstrato {
	public static final int TAMANHO = 100;

	public ProdutoIdentificador(String valor) {
		super(validar(valor));
	}

	private static String validar(String valor) {
		Validate.notBlank(valor, "O identificador do produto não pode ser vazio");
		var limpo = valor.trim();
		Validate.isTrue(limpo.length() <= TAMANHO, "O identificador do produto não pode ter mais de %d caracteres",
				TAMANHO);
		return limpo;
	}
}
