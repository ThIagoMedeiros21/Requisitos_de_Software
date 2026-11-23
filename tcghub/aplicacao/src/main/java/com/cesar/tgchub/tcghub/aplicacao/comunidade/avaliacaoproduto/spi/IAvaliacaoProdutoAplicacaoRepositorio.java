package com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.spi;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoProjecao;

import dev.sauloaraujo.d5.framework.aplicacao.spi.IAgregadoAplicacaoRepositorio;

public interface IAvaliacaoProdutoAplicacaoRepositorio
		extends IAgregadoAplicacaoRepositorio<AvaliacaoProdutoProjecao, AvaliacaoProdutoConsulta> {
}
