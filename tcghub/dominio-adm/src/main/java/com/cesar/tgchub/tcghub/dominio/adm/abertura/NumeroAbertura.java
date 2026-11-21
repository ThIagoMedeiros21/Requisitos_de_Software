package com.cesar.tgchub.tcghub.dominio.adm.abertura;

import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class NumeroAbertura implements IObjetoValor {
	private final int valor;

	public NumeroAbertura(int valor) {
		Validate.isTrue(valor > 0, "O número da abertura deve ser maior que zero");
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
		var outro = (NumeroAbertura) objeto;
		return valor == outro.valor;
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
