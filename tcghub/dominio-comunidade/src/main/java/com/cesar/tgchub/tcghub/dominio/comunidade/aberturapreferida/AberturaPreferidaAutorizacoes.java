package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class AberturaPreferidaAutorizacoes extends AgregadoAutorizacoes<AberturaPreferida> {
	private final boolean escolher;
	private final boolean ocultar;
	private final boolean reexibir;

	public AberturaPreferidaAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir,
			boolean escolher, boolean ocultar, boolean reexibir) {
		super(incluir, buscar, alterar, excluir);
		this.escolher = escolher;
		this.ocultar = ocultar;
		this.reexibir = reexibir;
	}

	public boolean getEscolher() {
		return escolher;
	}

	public boolean getOcultar() {
		return ocultar;
	}

	public boolean getReexibir() {
		return reexibir;
	}
}
