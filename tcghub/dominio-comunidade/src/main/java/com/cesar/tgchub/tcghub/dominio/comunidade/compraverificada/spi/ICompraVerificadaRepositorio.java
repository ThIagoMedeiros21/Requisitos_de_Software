package com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface ICompraVerificadaRepositorio
		extends IAgregadoRepositorio<CompraVerificadaIdentificador, CompraVerificada> {
	boolean existeCompraElegivel(UsuarioIdentificador usuario, ProdutoIdentificador produto);

	boolean existePorUsuarioEPedidoEProduto(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto);

	CompraVerificada buscarPorUsuarioEPedidoEProduto(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto);

	List<CompraVerificada> listarPorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto);

	List<CompraVerificada> listarPorUsuario(UsuarioIdentificador usuario);
}
