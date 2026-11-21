package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import org.apache.commons.lang3.Validate;

import dev.sauloaraujo.d5.framework.dominio.IIdentificador;
import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorStringAbstrato;

public final class PerfilComunitarioIdentificador extends ObjetoValorStringAbstrato implements IIdentificador {
	public PerfilComunitarioIdentificador(String valor) {
		super(valor);
		Validate.notBlank(valor, "O identificador do perfil comunitário é obrigatório");
	}
}
