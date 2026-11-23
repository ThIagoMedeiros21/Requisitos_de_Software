package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class ResumoAvaliacoesProduto implements IObjetoValor {
	private final ProdutoIdentificador produto;
	private final BigDecimal media;
	private final int quantidadeValida;
	private final Map<Integer, Integer> distribuicaoNotas;

	public ResumoAvaliacoesProduto(ProdutoIdentificador produto, BigDecimal media, int quantidadeValida,
			Map<Integer, Integer> distribuicaoNotas) {
		this.produto = Objects.requireNonNull(produto);
		this.media = Objects.requireNonNull(media);
		Validate.isTrue(quantidadeValida >= 0, "A quantidade de avaliações válidas não pode ser negativa");
		this.quantidadeValida = quantidadeValida;
		var copia = new LinkedHashMap<Integer, Integer>();
		for (int nota = NotaAvaliacao.MAXIMO; nota >= NotaAvaliacao.MINIMO; nota--) {
			copia.put(nota, Objects.requireNonNull(distribuicaoNotas).getOrDefault(nota, 0));
		}
		this.distribuicaoNotas = Collections.unmodifiableMap(copia);
	}

	public ProdutoIdentificador getProduto() {
		return produto;
	}

	public BigDecimal getMedia() {
		return media;
	}

	public int getQuantidadeValida() {
		return quantidadeValida;
	}

	public Map<Integer, Integer> getDistribuicaoNotas() {
		return distribuicaoNotas;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outro = (ResumoAvaliacoesProduto) objeto;
		return quantidadeValida == outro.quantidadeValida && Objects.equals(produto, outro.produto)
				&& media.compareTo(outro.media) == 0 && Objects.equals(distribuicaoNotas, outro.distribuicaoNotas);
	}

	@Override
	public int hashCode() {
		return Objects.hash(produto, media.stripTrailingZeros(), quantidadeValida, distribuicaoNotas);
	}
}
