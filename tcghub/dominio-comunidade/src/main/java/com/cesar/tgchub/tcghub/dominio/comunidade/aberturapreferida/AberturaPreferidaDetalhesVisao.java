package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;

import dev.sauloaraujo.d5.framework.dominio.IObjetoValor;

public final class AberturaPreferidaDetalhesVisao implements IObjetoValor {
	private final Abertura abertura;
	private final boolean ehMinhaPreferida;
	private final boolean visivelNoPerfil;
	private final Optional<Integer> posicaoRanking;
	private final int totalEscolhasComunidade;
	private final List<UsuarioIdentificador> amigosComMesmaPreferencia;

	public AberturaPreferidaDetalhesVisao(Abertura abertura, boolean ehMinhaPreferida, boolean visivelNoPerfil,
			Optional<Integer> posicaoRanking, int totalEscolhasComunidade,
			List<UsuarioIdentificador> amigosComMesmaPreferencia) {
		this.abertura = Objects.requireNonNull(abertura);
		this.ehMinhaPreferida = ehMinhaPreferida;
		this.visivelNoPerfil = visivelNoPerfil;
		this.posicaoRanking = Objects.requireNonNull(posicaoRanking);
		Validate.isTrue(totalEscolhasComunidade >= 0, "O total de escolhas não pode ser negativo");
		this.totalEscolhasComunidade = totalEscolhasComunidade;
		this.amigosComMesmaPreferencia = List.copyOf(Objects.requireNonNull(amigosComMesmaPreferencia));
	}

	public Abertura getAbertura() {
		return abertura;
	}

	public boolean isEhMinhaPreferida() {
		return ehMinhaPreferida;
	}

	public boolean isVisivelNoPerfil() {
		return visivelNoPerfil;
	}

	public Optional<Integer> getPosicaoRanking() {
		return posicaoRanking;
	}

	public int getTotalEscolhasComunidade() {
		return totalEscolhasComunidade;
	}

	public List<UsuarioIdentificador> getAmigosComMesmaPreferencia() {
		return amigosComMesmaPreferencia;
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (AberturaPreferidaDetalhesVisao) objeto;
		return ehMinhaPreferida == outra.ehMinhaPreferida && visivelNoPerfil == outra.visivelNoPerfil
				&& totalEscolhasComunidade == outra.totalEscolhasComunidade && Objects.equals(abertura, outra.abertura)
				&& Objects.equals(posicaoRanking, outra.posicaoRanking)
				&& Objects.equals(amigosComMesmaPreferencia, outra.amigosComMesmaPreferencia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(abertura, ehMinhaPreferida, visivelNoPerfil, posicaoRanking, totalEscolhasComunidade,
				amigosComMesmaPreferencia);
	}
}
