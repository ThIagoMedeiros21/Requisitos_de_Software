package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IAvaliacaoProdutoRepositorio
		extends IAgregadoRepositorio<AvaliacaoProdutoIdentificador, AvaliacaoProduto> {
	boolean existePorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto);

	AvaliacaoProduto buscarPorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto);

	List<AvaliacaoProduto> listarValidasPorProduto(ProdutoIdentificador produto);
}
