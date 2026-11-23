package com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoAplicacaoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoAutorizacoes;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoServico;

import dev.sauloaraujo.d5.framework.aplicacao.impl.AgregadoAplicacaoServicoAbstrato;

public final class AvaliacaoProdutoAplicacaoServico extends
		AgregadoAplicacaoServicoAbstrato<AvaliacaoProdutoIdentificador, AvaliacaoProduto, AvaliacaoProdutoAutorizacoes, AvaliacaoProdutoProjecao, AvaliacaoProdutoConsulta> {
	public AvaliacaoProdutoAplicacaoServico(IAvaliacaoProdutoAplicacaoRepositorio repositorio,
			AvaliacaoProdutoServico servico) {
		super(repositorio, servico);
	}
}
