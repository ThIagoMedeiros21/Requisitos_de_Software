package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

public final class AvaliacaoElegibilidadeProxy implements IAvaliacaoElegibilidadeProxy {
	private final AvaliacaoProdutoServico avaliacaoProdutoServico;
	private final ICompraVerificadaRepositorio compraVerificadaRepositorio;

	public AvaliacaoElegibilidadeProxy(AvaliacaoProdutoServico avaliacaoProdutoServico,
			ICompraVerificadaRepositorio compraVerificadaRepositorio) {
		this.avaliacaoProdutoServico = Objects.requireNonNull(avaliacaoProdutoServico);
		this.compraVerificadaRepositorio = Objects.requireNonNull(compraVerificadaRepositorio);
	}

	@Override
	public AvaliacaoProduto registrarOuAtualizarAvaliacao(UsuarioIdentificador solicitante, UsuarioIdentificador autor,
			ProdutoIdentificador produto, NotaAvaliacao nota, Optional<String> comentario) {
		Objects.requireNonNull(solicitante);
		Objects.requireNonNull(autor);
		Objects.requireNonNull(produto);
		Objects.requireNonNull(nota);
		Objects.requireNonNull(comentario);

		if (!solicitante.equals(autor)) {
			throw new NaoAutorizadoExcecao(AvaliacaoProdutoServico.AUTORIZACAO_PROPRIA_OBRIGATORIA);
		}

		var elegivel = compraVerificadaRepositorio.existeCompraElegivel(autor, produto);
		Validate.validState(elegivel, AvaliacaoProdutoServico.COMPRA_NAO_ELEGIVEL_MODELO, autor, produto);

		return avaliacaoProdutoServico.avaliar(autor, produto, nota, comentario);
	}

	@Override
	public ElegibilidadeAvaliacaoVisao verificarElegibilidade(UsuarioIdentificador usuario,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(produto);

		if (compraVerificadaRepositorio.existeCompraElegivel(usuario, produto)) {
			return new ElegibilidadeAvaliacaoVisao(true, Optional.empty());
		}
		return new ElegibilidadeAvaliacaoVisao(false,
				Optional.of(AvaliacaoProdutoServico.COMPRA_NAO_ELEGIVEL_MODELO.formatted(usuario, produto)));
	}
}
