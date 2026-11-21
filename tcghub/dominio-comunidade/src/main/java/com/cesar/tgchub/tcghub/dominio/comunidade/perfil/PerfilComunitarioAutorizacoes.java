package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class PerfilComunitarioAutorizacoes extends AgregadoAutorizacoes<PerfilComunitario> {
	private final boolean fixarDestaque;
	private final boolean removerDestaque;

	public PerfilComunitarioAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir,
			boolean fixarDestaque, boolean removerDestaque) {
		super(incluir, buscar, alterar, excluir);
		this.fixarDestaque = fixarDestaque;
		this.removerDestaque = removerDestaque;
	}

	public boolean getFixarDestaque() {
		return fixarDestaque;
	}

	public boolean getRemoverDestaque() {
		return removerDestaque;
	}
}
