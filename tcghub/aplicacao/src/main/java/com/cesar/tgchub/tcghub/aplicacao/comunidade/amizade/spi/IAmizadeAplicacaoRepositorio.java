package com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.spi;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeProjecao;

import dev.sauloaraujo.d5.framework.aplicacao.spi.IAgregadoAplicacaoRepositorio;

public interface IAmizadeAplicacaoRepositorio
		extends IAgregadoAplicacaoRepositorio<AmizadeProjecao, AmizadeConsulta> {
}
