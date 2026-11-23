package com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.spi;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaProjecao;

import dev.sauloaraujo.d5.framework.aplicacao.spi.IAgregadoAplicacaoRepositorio;

public interface ICompraVerificadaAplicacaoRepositorio
		extends IAgregadoAplicacaoRepositorio<CompraVerificadaProjecao, CompraVerificadaConsulta> {
}
