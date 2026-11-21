package com.cesar.tgchub.tcghub.aplicacao.adm.abertura;

import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.spi.IAberturaAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class AberturaAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<AberturaIdentificador, Abertura, AberturaAutorizacoes, AberturaProjecao, AberturaConsulta> {
	public AberturaAplicacaoServico(IAberturaAplicacaoRepositorio repositorio, AberturaServico servico) {
		super(repositorio, servico);
	}
}
