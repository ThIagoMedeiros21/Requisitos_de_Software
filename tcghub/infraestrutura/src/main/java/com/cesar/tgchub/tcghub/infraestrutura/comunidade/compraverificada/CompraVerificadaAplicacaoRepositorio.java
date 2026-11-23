package com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.spi.ICompraVerificadaAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class CompraVerificadaAplicacaoRepositorio extends
		AgregadoAplicacaoRepositorioAbstrato<CompraVerificadaProjecao, CompraVerificadaOrdenacao, CompraVerificadaConsulta>
		implements ICompraVerificadaAplicacaoRepositorio {
	private final ICompraVerificadaRepositorioJpa repositorioJpa;

	public CompraVerificadaAplicacaoRepositorio(ICompraVerificadaRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<CompraVerificadaProjecao> pesquisar(CompraVerificadaConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var usuario = consulta.getUsuario().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		var produto = consulta.getProduto().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		return repositorioJpa.pesquisar(usuario, produto, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(CompraVerificadaOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);
		return switch (ordenacao) {
		case ATUALIZADO_EM -> "atualizadoEm";
		case PEDIDO -> "pedido";
		};
	}
}
