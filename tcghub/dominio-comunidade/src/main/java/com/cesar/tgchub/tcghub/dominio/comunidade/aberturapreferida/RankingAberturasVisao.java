package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class RankingAberturasVisao implements IObjetoValor {
	private final int totalEscolhas;
	private final List<ItemRankingAbertura> itens;

	public RankingAberturasVisao(int totalEscolhas, List<ItemRankingAbertura> itens) {
		Validate.isTrue(totalEscolhas >= 0, "O total de escolhas não pode ser negativo");
		this.totalEscolhas = totalEscolhas;
		this.itens = List.copyOf(Objects.requireNonNull(itens));
	}

	public int getTotalEscolhas() {
		return totalEscolhas;
	}

	public List<ItemRankingAbertura> getItens() {
		return itens;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (RankingAberturasVisao) objeto;
		return totalEscolhas == outro.totalEscolhas && Objects.equals(itens, outro.itens);
	}

	@Override
	public int hashCode() {
		return Objects.hash(totalEscolhas, itens);
	}
}
