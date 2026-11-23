package com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.spi.ICompraVerificadaAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class CompraVerificadaAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<CompraVerificadaIdentificador, CompraVerificada, CompraVerificadaAutorizacoes, CompraVerificadaProjecao, CompraVerificadaConsulta> {
	public CompraVerificadaAplicacaoServico(ICompraVerificadaAplicacaoRepositorio repositorio,
			CompraVerificadaServico servico) {
		super(repositorio, servico);
	}
}
