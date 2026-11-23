package com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.cesar.tgchub.tcghub.dominio.Autorizacao;
import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.spi.ICompraVerificadaRepositorio;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.dominio.impl.AgregadoServicoAbstrato;
import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;

public final class CompraVerificadaServico extends
		AgregadoServicoAbstrato<CompraVerificadaIdentificador, CompraVerificada, CompraVerificadaAutorizacoes> {
	public static final String IDENTIFICADOR_INEXISTENTE_MODELO = "Não existe compra verificada com o identificador %s";
	public static final String IDENTIFICADOR_EXISTENTE_MODELO = "Já existe uma compra verificada com o identificador %s";
	public static final String IDENTIFICADOR_INFORMADO = "O identificador da compra verificada é gerado na inclusão e não deve ser informado";
	public static final String COMPRA_INEXISTENTE_MODELO = "Não existe compra registrada para o usuário %s, pedido %s e produto %s";

	private final ICompraVerificadaRepositorio repositorio;

	public CompraVerificadaServico(ISegurancaServico segurancaServico, ICompraVerificadaRepositorio repositorio) {
		super(segurancaServico, repositorio, IDENTIFICADOR_EXISTENTE_MODELO, IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorio = Objects.requireNonNull(repositorio);
	}

	@Override
	public CompraVerificadaAutorizacoes criarAutorizacoes(Optional<CompraVerificadaIdentificador> identificador) {
		Objects.requireNonNull(identificador);
		var autorizado = getSegurancaServico().possuiAutorizacao(Autorizacao.USUARIO.name())
				|| getSegurancaServico().possuiAutorizacao(Autorizacao.ADMINISTRADOR.name());
		return new CompraVerificadaAutorizacoes(autorizado, true, autorizado, false);
	}

	@Override
	protected void validarInclusao(CompraVerificada agregado) {
		Objects.requireNonNull(agregado);
		Validate.isTrue(agregado.getIdentificador().isEmpty(), IDENTIFICADOR_INFORMADO);
	}

	public CompraVerificada registrarCompraPaga(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);

		var agora = Instant.now();
		if (repositorio.existePorUsuarioEPedidoEProduto(usuario, pedido, produto)) {
			var existente = repositorio.buscarPorUsuarioEPedidoEProduto(usuario, pedido, produto);
			var atualizada = existente.marcarComoPaga(agora);
			repositorio.alterar(atualizada);
			return atualizada;
		}
		return repositorio
				.incluir(new CompraVerificada(Optional.empty(), usuario, produto, pedido, true, false, agora));
	}

	public CompraVerificada registrarCompraPendente(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);

		var agora = Instant.now();
		if (repositorio.existePorUsuarioEPedidoEProduto(usuario, pedido, produto)) {
			return repositorio.buscarPorUsuarioEPedidoEProduto(usuario, pedido, produto);
		}
		return repositorio
				.incluir(new CompraVerificada(Optional.empty(), usuario, produto, pedido, false, false, agora));
	}

	public CompraVerificada registrarReembolso(UsuarioIdentificador usuario, PedidoIdentificador pedido,
			ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(pedido);
		Objects.requireNonNull(produto);

		if (!repositorio.existePorUsuarioEPedidoEProduto(usuario, pedido, produto)) {
			throw new AgregadoInexistenteExcecao(COMPRA_INEXISTENTE_MODELO.formatted(usuario, pedido, produto));
		}
		var existente = repositorio.buscarPorUsuarioEPedidoEProduto(usuario, pedido, produto);
		if (existente.isReembolsada()) {
			return existente;
		}
		var reembolsada = existente.marcarComoReembolsada(Instant.now());
		repositorio.alterar(reembolsada);
		return reembolsada;
	}
}
