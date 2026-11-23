package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class NotaAvaliacao implements IObjetoValor {
	public static final int MINIMO = 1;
	public static final int MAXIMO = 5;
	public static final String MENSAGEM_FAIXA = "A nota deve estar entre 1 e 5";

	private final int valor;

	public NotaAvaliacao(int valor) {
		Validate.isTrue(valor >= MINIMO && valor <= MAXIMO, MENSAGEM_FAIXA);
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (NotaAvaliacao) objeto;
		return valor == outra.valor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(valor);
	}

	@Override
	public String toString() {
		return Integer.toString(valor);
	}
}
