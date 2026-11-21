package com.cesar.tgchub.tcghub.dominio.comunidade.amizade;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.IIdentificador;
import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class AmizadeIdentificador extends ObjetoValorStringAbstrato implements IIdentificador {
	public AmizadeIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador da amizade é obrigatório");
	}
}
