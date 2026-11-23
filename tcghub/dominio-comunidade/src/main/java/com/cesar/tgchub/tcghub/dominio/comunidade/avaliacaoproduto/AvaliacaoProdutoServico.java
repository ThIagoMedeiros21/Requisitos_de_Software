package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificadaServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;

public final class AvaliacaoProdutoServico extends
		AgregadoServicoAbstrato<AvaliacaoProdutoIdentificador, AvaliacaoProduto, AvaliacaoProdutoAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe avaliação com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe uma avaliação com o identificador %s";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador da avaliação é gerado na inclusão e não deve ser informado";
	public static final String COMPRA_NAO_ELEGIVEL_MODELO = "O usuário %s não possui compra paga e não reembolsada da carta %s";
	public static final String AUTORIZACAO_PROPRIA_OBRIGATORIA = "Apenas o autor pode registrar ou alterar sua própria avaliação";

	private final IAvaliacaoProdutoRepositorio repositorio;
	private final ICompraVerificadaRepositorio compraVerificadaRepositorio;
	private final CompraVerificadaServico compraVerificadaServico;

	public AvaliacaoProdutoServico(ISegurancaServico segurancaServico, IAvaliacaoProdutoRepositorio repositorio,
			ICompraVerificadaRepositorio compraVerificadaRepositorio, CompraVerificadaServico compraVerificadaServico) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
		this.compraVerificadaRepositorio = Objects.requireNonNull(compraVerificadaRepositorio);
		this.compraVerificadaServico = Objects.requireNonNull(compraVerificadaServico);
	}

	@Override
	public AvaliacaoProdutoAutorizacoes criarAutorizacoes(Optional<AvaliacaoProdutoIdentificador> identificador) {
		Objects.requireNonNull(identificador);
		var possuiPapelUsuario = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name());
		if (identificador.isEmpty() || !repositorio.existePorIdentificador(identificador.get())) {
			return new AvaliacaoProdutoAutorizacoes(possuiPapelUsuario, true, possuiPapelUsuario, false,
					possuiPapelUsuario);
		}
		var avaliacao = repositorio.buscarPorIdentificador(identificador.get());
		var ehAutor = possuiPapelUsuario
				&& getSegurancaServico().getUsuario().equals(avaliacao.getUsuario().toString());
		return new AvaliacaoProdutoAutorizacoes(possuiPapelUsuario, true, ehAutor, false, ehAutor);
	}

	@Override
	protected void validarInclusao(AvaliacaoProduto agregado) {
		Objects.requireNonNull(agregado);
		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
	}

	public AvaliacaoProduto avaliar(UsuarioIdentificador autor, ProdutoIdentificador produto, NotaAvaliacao nota,
			Optional<String> comentario) {
		Objects.requireNonNull(autor);
		Objects.requireNonNull(produto);
		Objects.requireNonNull(nota);
		Objects.requireNonNull(comentario);

		validarAutorizacao(Optional.empty(), AvaliacaoProdutoAutorizacoes::getAvaliar);

		var agora = Instant.now();
		if (repositorio.existePorUsuarioEProduto(autor, produto)) {
			var existente = repositorio.buscarPorUsuarioEProduto(autor, produto);
			var atualizada = existente.atualizar(nota, comentario, agora);
			repositorio.alterar(atualizada);
			return atualizada;
		}
		var nova = new AvaliacaoProduto(Optional.empty(), autor, produto, nota, comentario, true, true, agora);
		return repositorio.incluir(nova);
	}

	public ResumoAvaliacoesProduto invalidarPorReembolso(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);

		compraVerificadaServico.registrarReembolso(usuario, pedido, produto);

		if (!compraVerificadaRepositorio.existeCompraElegivel(usuario, produto)
				&& repositorio.existePorUsuarioEProduto(usuario, produto)) {
			var existente = repositorio.buscarPorUsuarioEProduto(usuario, produto);
			if (existente.isValida()) {
				repositorio.alterar(existente.invalidar());
			}
		}
		return consultarResumo(produto);
	}

	public ResumoAvaliacoesProduto consultarResumo(ProdutoIdentificador produto) {
		Objects.requireNonNull(produto);
		var validas = repositorio.listarValidasPorProduto(produto);
		Map<Integer, Integer> distribuicao = new LinkedHashMap<>();
		for (int n = NotaAvaliacao.MAXIMO; n >= NotaAvaliacao.MINIMO; n--) {
			distribuicao.put(n, 0);
		}
		if (validas.isEmpty()) {
			return new ResumoAvaliacoesProduto(produto, BigDecimal.ZERO.setScale(1, RoundingMode.HALF_UP), 0,
					distribuicao);
		}

		int soma = 0;
		for (var av : validas) {
			soma += av.getNotaValor();
			distribuicao.merge(av.getNotaValor(), 1, Integer::sum);
		}
		var media = BigDecimal.valueOf(soma).divide(BigDecimal.valueOf(validas.size()), 1, RoundingMode.HALF_UP);
		return new ResumoAvaliacoesProduto(produto, media, validas.size(), distribuicao);
	}

	public List<AvaliacaoProduto> listarAvaliacoesValidas(ProdutoIdentificador produto) {
		Objects.requireNonNull(produto);
		return repositorio.listarValidasPorProduto(produto);
	}
}
