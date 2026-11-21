package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class AberturaParaEscolhaVisao implements IObjetoValor {
	private final Abertura abertura;
	private final int quantidadeAmigos;
	private final boolean ehMinhaPreferida;
	private final Optional<Integer> posicaoRanking;

	public AberturaParaEscolhaVisao(Abertura abertura, int quantidadeAmigos, boolean ehMinhaPreferida,
			Optional<Integer> posicaoRanking) {
		this.abertura = Objects.requireNonNull(abertura);
		Validate.isTrue(quantidadeAmigos >= 0, "A quantidade de amigos não pode ser negativa");
		this.quantidadeAmigos = quantidadeAmigos;
		this.ehMinhaPreferida = ehMinhaPreferida;
		this.posicaoRanking = Objects.requireNonNull(posicaoRanking);
	}

	public Abertura getAbertura() {
		return abertura;
	}

	public int getQuantidadeAmigos() {
		return quantidadeAmigos;
	}

	public boolean isEhMinhaPreferida() {
		return ehMinhaPreferida;
	}

	public Optional<Integer> getPosicaoRanking() {
		return posicaoRanking;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (AberturaParaEscolhaVisao) objeto;
		return quantidadeAmigos == outra.quantidadeAmigos && ehMinhaPreferida == outra.ehMinhaPreferida
				&& Objects.equals(abertura, outra.abertura) && Objects.equals(posicaoRanking, outra.posicaoRanking);
	}

	@Override
	public int hashCode() {
		return Objects.hash(abertura, quantidadeAmigos, ehMinhaPreferida, posicaoRanking);
	}
}
