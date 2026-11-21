package com.cesar.tgchub.tcghub.dominio.comunidade.deckfavorito;

public final class CatalogoIndisponivelExcecao extends RuntimeException {
	public static final String MENSAGEM_PADRAO = "O serviço de Catálogo está temporariamente indisponível";

	public CatalogoIndisponivelExcecao() {
		super(MENSAGEM_PADRAO);
	}

	public CatalogoIndisponivelExcecao(String mensagem) {
		super(mensagem);
	}

	public CatalogoIndisponivelExcecao(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}
