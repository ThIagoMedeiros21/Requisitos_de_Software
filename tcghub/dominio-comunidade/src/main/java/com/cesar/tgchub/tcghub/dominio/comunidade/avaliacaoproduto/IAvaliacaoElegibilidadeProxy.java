package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

public interface IAvaliacaoElegibilidadeProxy {
	AvaliacaoProduto registrarOuAtualizarAvaliacao(UsuarioIdentificador solicitante, UsuarioIdentificador autor,
			ProdutoIdentificador produto, NotaAvaliacao nota, Optional<String> comentario);

	ElegibilidadeAvaliacaoVisao verificarElegibilidade(UsuarioIdentificador usuario, ProdutoIdentificador produto);
}
