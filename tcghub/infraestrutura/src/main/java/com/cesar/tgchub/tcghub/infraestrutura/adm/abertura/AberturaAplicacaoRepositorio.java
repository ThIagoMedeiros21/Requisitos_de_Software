package com.cesar.tgchub.tcghub.infraestrutura.adm.abertura;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaProjecao;
import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.spi.IAberturaAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class AberturaAplicacaoRepositorio
		extends AgregadoAplicacaoRepositorioAbstrato<AberturaProjecao, AberturaOrdenacao, AberturaConsulta>
		implements IAberturaAplicacaoRepositorio {
	private final IAberturaRepositorioJpa repositorioJpa;

	public AberturaAplicacaoRepositorio(IAberturaRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<AberturaProjecao> pesquisar(AberturaConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var termo = paraLike(consulta.getTermo());
		var ativa = consulta.getAtiva().orElse(null);
		return repositorioJpa.pesquisar(termo, ativa, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(AberturaOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);
		return switch (ordenacao) {
		case NUMERO -> "numero";
		case NOME -> "nome";
		};
	}
}
