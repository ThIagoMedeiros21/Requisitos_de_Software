package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class ElegibilidadeAvaliacaoVisao implements IObjetoValor {
	private final boolean podeAvaliar;
	private final Optional<String> motivoInelegibilidade;

	public ElegibilidadeAvaliacaoVisao(boolean podeAvaliar, Optional<String> motivoInelegibilidade) {
		this.podeAvaliar = podeAvaliar;
		this.motivoInelegibilidade = Objects.requireNonNull(motivoInelegibilidade);
	}

	public boolean isPodeAvaliar() {
		return podeAvaliar;
	}

	public Optional<String> getMotivoInelegibilidade() {
		return motivoInelegibilidade;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (ElegibilidadeAvaliacaoVisao) objeto;
		return podeAvaliar == outra.podeAvaliar && Objects.equals(motivoInelegibilidade, outra.motivoInelegibilidade);
	}

	@Override
	public int hashCode() {
		return Objects.hash(podeAvaliar, motivoInelegibilidade);
	}
}
