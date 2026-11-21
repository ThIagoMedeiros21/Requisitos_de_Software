package com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.spi;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioProjecao;

import dev.sauloaraujo.d5.framework.aplicacao.spi.IAgregadoAplicacaoRepositorio;

public interface IPerfilComunitarioAplicacaoRepositorio
		extends IAgregadoAplicacaoRepositorio<PerfilComunitarioProjecao, PerfilComunitarioConsulta> {
}
