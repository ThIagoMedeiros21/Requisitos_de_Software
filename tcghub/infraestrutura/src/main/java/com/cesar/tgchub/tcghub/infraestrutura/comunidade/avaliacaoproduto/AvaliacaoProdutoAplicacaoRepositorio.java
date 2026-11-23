package com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class AvaliacaoProdutoAplicacaoRepositorio extends
		AgregadoAplicacaoRepositorioAbstrato<AvaliacaoProdutoProjecao, AvaliacaoProdutoOrdenacao, AvaliacaoProdutoConsulta>
		implements IAvaliacaoProdutoAplicacaoRepositorio {
	private final IAvaliacaoProdutoRepositorioJpa repositorioJpa;

	public AvaliacaoProdutoAplicacaoRepositorio(IAvaliacaoProdutoRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<AvaliacaoProdutoProjecao> pesquisar(AvaliacaoProdutoConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var produto = consulta.getProduto().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		var usuario = consulta.getUsuario().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		var apenasValidas = consulta.getApenasValidas().orElse(null);
		return repositorioJpa.pesquisar(produto, usuario, apenasValidas, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(AvaliacaoProdutoOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);
		return switch (ordenacao) {
		case AVALIADO_EM -> "avaliadoEm";
		case NOTA -> "nota";
		};
	}
}
