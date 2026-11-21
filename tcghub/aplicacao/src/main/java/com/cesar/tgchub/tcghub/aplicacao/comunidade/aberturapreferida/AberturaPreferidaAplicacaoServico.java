package com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.spi.IAberturaPreferidaAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class AberturaPreferidaAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<AberturaPreferidaIdentificador, AberturaPreferida, AberturaPreferidaAutorizacoes, AberturaPreferidaProjecao, AberturaPreferidaConsulta> {
	public AberturaPreferidaAplicacaoServico(IAberturaPreferidaAplicacaoRepositorio repositorio,
			AberturaPreferidaServico servico) {
		super(repositorio, servico);
	}
}
