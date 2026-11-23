package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class AvaliacaoProdutoAutorizacoes extends AgregadoAutorizacoes<AvaliacaoProduto> {
	private final boolean avaliar;

	public AvaliacaoProdutoAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir,
			boolean avaliar) {
		super(incluir, buscar, alterar, excluir);
		this.avaliar = avaliar;
	}

	public boolean getAvaliar() {
		return avaliar;
	}
}
