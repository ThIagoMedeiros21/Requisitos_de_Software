package com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.spi.IAmizadeAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class AmizadeAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<AmizadeIdentificador, Amizade, AmizadeAutorizacoes, AmizadeProjecao, AmizadeConsulta> {
	public AmizadeAplicacaoServico(IAmizadeAplicacaoRepositorio repositorio, AmizadeServico servico) {
		super(repositorio, servico);
	}
}
