package com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProdutoIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class AvaliacaoProdutoProjecao implements IProjecao<AvaliacaoProduto> {
	private final AvaliacaoProdutoIdentificador identificador;
	private final UsuarioIdentificador usuario;
	private final ProdutoIdentificador produto;
	private final int nota;
	private final Optional<String> comentario;
	private final boolean compraVerificada;
	private final boolean valida;
	private final Instant avaliadoEm;

	public AvaliacaoProdutoProjecao(AvaliacaoProdutoIdentificador identificador, UsuarioIdentificador usuario,
			ProdutoIdentificador produto, int nota, Optional<String> comentario, boolean compraVerificada,
			boolean valida, Instant avaliadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.produto = Objects.requireNonNull(produto);
		this.nota = nota;
		this.comentario = Objects.requireNonNull(comentario);
		this.compraVerificada = compraVerificada;
		this.valida = valida;
		this.avaliadoEm = Objects.requireNonNull(avaliadoEm);
	}

	public AvaliacaoProdutoProjecao(Long identificador, String usuario, String produto, int nota,
			@Nullable String comentario, boolean compraVerificada, boolean valida, Instant avaliadoEm) {
		this(new AvaliacaoProdutoIdentificador(identificador.toString()), new UsuarioIdentificador(usuario),
				new ProdutoIdentificador(produto), nota, Optional.ofNullable(comentario), compraVerificada, valida,
				avaliadoEm);
	}

	public AvaliacaoProdutoIdentificador getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public ProdutoIdentificador getProduto() {
		return produto;
	}

	public int getNota() {
		return nota;
	}

	public Optional<String> getComentario() {
		return comentario;
	}

	public boolean isCompraVerificada() {
		return compraVerificada;
	}

	public boolean isValida() {
		return valida;
	}

	public Instant getAvaliadoEm() {
		return avaliadoEm;
	}
}
