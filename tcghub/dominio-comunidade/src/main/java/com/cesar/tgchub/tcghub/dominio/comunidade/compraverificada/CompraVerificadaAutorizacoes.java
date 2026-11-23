package com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class CompraVerificadaAutorizacoes extends AgregadoAutorizacoes<CompraVerificada> {
	public CompraVerificadaAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir) {
		super(incluir, buscar, alterar, excluir);
	}
}
