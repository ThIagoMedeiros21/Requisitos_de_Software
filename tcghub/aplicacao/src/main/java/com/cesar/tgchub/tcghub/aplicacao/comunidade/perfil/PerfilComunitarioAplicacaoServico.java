package com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.spi.IPerfilComunitarioAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class PerfilComunitarioAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<PerfilComunitarioIdentificador, PerfilComunitario, PerfilComunitarioAutorizacoes, PerfilComunitarioProjecao, PerfilComunitarioConsulta> {
	public PerfilComunitarioAplicacaoServico(IPerfilComunitarioAplicacaoRepositorio repositorio,
			PerfilComunitarioServico servico) {
		super(repositorio, servico);
	}
}
