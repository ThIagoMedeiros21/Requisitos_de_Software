package com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.NotaAvaliacao;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.spi.IAvaliacaoProdutoRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.dominio.AgregadoInexistenteExcecao;
import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoRepositorioAbstrato;

public class AvaliacaoProdutoRepositorio extends
		AgregadoRepositorioAbstrato<AvaliacaoProdutoIdentificador, AvaliacaoProduto, Long, AvaliacaoProdutoJpa>
		implements IAvaliacaoProdutoRepositorio {
	private final IAvaliacaoProdutoRepositorioJpa repositorioJpa;

	public AvaliacaoProdutoRepositorio(IAvaliacaoProdutoRepositorioJpa repositorioJpa) {
		super(repositorioJpa, AvaliacaoProdutoServico.IDENTIFICADOR_INEXISTENTE_MODELO);
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected AvaliacaoProdutoJpa paraJpa(AvaliacaoProduto agregado) {
		Objects.requireNonNull(agregado);

		var identificador = agregado.getIdentificador().map(this::paraIdentificadorJpa).orElse(null);
		if (identificador != null) {
			var existente = repositorioJpa.findById(identificador).orElseThrow(
					() -> new AgregadoInexistenteExcecao(AvaliacaoProdutoServico.IDENTIFICADOR_INEXISTENTE_MODELO
							.formatted(agregado.getIdentificador().orElseThrow())));
			existente.atualizar(agregado.getNotaValor(), agregado.getComentario().orElse(null),
					agregado.isCompraVerificada(), agregado.isValida(), agregado.getAvaliadoEm());
			return existente;
		}
		return new AvaliacaoProdutoJpa(null, agregado.getUsuario().toString(), agregado.getProduto().toString(),
				agregado.getNotaValor(), agregado.getComentario().orElse(null), agregado.isCompraVerificada(),
				agregado.isValida(), agregado.getAvaliadoEm());
	}

	@Override
	protected AvaliacaoProduto paraAgregado(AvaliacaoProdutoJpa agregadoJpa) {
		Objects.requireNonNull(agregadoJpa);

		var id = new AvaliacaoProdutoIdentificador(
				Objects.requireNonNull(agregadoJpa.getIdentificador()).toString());
		return new AvaliacaoProduto(Optional.of(id), new UsuarioIdentificador(agregadoJpa.getUsuario()),
				new ProdutoIdentificador(agregadoJpa.getProduto()), new NotaAvaliacao(agregadoJpa.getNota()),
				Optional.ofNullable(agregadoJpa.getComentario()), agregadoJpa.isCompraVerificada(),
				agregadoJpa.isValida(), agregadoJpa.getAvaliadoEm());
	}

	@Override
	protected Long paraIdentificadorJpa(AvaliacaoProdutoIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).orElseThrow(() -> new AgregadoInexistenteExcecao(
				AvaliacaoProdutoServico.IDENTIFICADOR_INEXISTENTE_MODELO.formatted(identificador)));
	}

	private static Optional<Long> converter(AvaliacaoProdutoIdentificador identificador) {
		try {
			return Optional.of(Long.valueOf(identificador.toString()));
		} catch (NumberFormatException excecao) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existePorIdentificador(AvaliacaoProdutoIdentificador identificador) {
		Objects.requireNonNull(identificador);
		return converter(identificador).isPresent() && super.existePorIdentificador(identificador);
	}

	@Override
	public boolean existePorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(produto);
		return repositorioJpa.existsByUsuarioAndProduto(usuario.toString(), produto.toString());
	}

	@Override
	public AvaliacaoProduto buscarPorUsuarioEProduto(UsuarioIdentificador usuario, ProdutoIdentificador produto) {
		Objects.requireNonNull(usuario);
		Objects.requireNonNull(produto);
		return repositorioJpa.findByUsuarioAndProduto(usuario.toString(), produto.toString()).map(this::paraAgregado)
				.orElseThrow(() -> new AgregadoInexistenteExcecao(
						"Não existe avaliação do usuário %s para o produto %s".formatted(usuario, produto)));
	}

	@Override
	public List<AvaliacaoProduto> listarValidasPorProduto(ProdutoIdentificador produto) {
		Objects.requireNonNull(produto);
		return repositorioJpa.findByProdutoAndValidaTrueOrderByAvaliadoEmDesc(produto.toString()).stream()
				.map(this::paraAgregado).toList();
	}
}
