package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoAutorizacoes;

public final class DeckFavoritoAutorizacoes extends AgregadoAutorizacoes<DeckFavorito> {
	private final boolean ocultar;
	private final boolean reexibir;
	private final boolean visualizarComposicao;

	public DeckFavoritoAutorizacoes(boolean incluir, boolean buscar, boolean alterar, boolean excluir, boolean ocultar,
			boolean reexibir, boolean visualizarComposicao) {
		super(incluir, buscar, alterar, excluir);
		this.ocultar = ocultar;
		this.reexibir = reexibir;
		this.visualizarComposicao = visualizarComposicao;
	}

	public boolean getOcultar() {
		return ocultar;
	}

	public boolean getReexibir() {
		return reexibir;
	}

	public boolean getVisualizarComposicao() {
		return visualizarComposicao;
	}
}
