package com.cesar.tgchub.tcghub.dominio.comunidade.amizade;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class AmizadeAutorizacoes extends AgregadoAutorizacoes<Amizade> {
	private final boolean aceitar;
	private final boolean recusar;
	private final boolean cancelar;
	private final boolean remover;

	public AmizadeAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir, boolean aceitar,
			boolean recusar, boolean cancelar, boolean remover) {
		super(incluir, buscar, alterar, excluir);
		this.aceitar = aceitar;
		this.recusar = recusar;
		this.cancelar = cancelar;
		this.remover = remover;
	}

	public boolean getAceitar() {
		return aceitar;
	}

	public boolean getRecusar() {
		return recusar;
	}

	public boolean getCancelar() {
		return cancelar;
	}

	public boolean getRemover() {
		return remover;
	}
}
