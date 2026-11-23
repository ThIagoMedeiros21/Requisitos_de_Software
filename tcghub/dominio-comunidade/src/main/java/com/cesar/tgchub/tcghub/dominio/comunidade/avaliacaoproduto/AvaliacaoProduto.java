package com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class AvaliacaoProduto implements IAgregado<AvaliacaoProdutoIdentificador> {
	public static final int TAMANHO_COMENTARIO = 1000;
	public static final String SELO_COMPRA_VERIFICADA = "compra verificada";

	private final Optional<AvaliacaoProdutoIdentificador> identificador;
	private final UsuarioIdentificador usuario;
	private final ProdutoIdentificador produto;
	private final NotaAvaliacao nota;
	private final Optional<String> comentario;
	private final boolean compraVerificada;
	private final boolean valida;
	private final Instant avaliadoEm;

	public AvaliacaoProduto(Optional<AvaliacaoProdutoIdentificador> identificador, UsuarioIdentificador usuario,
			ProdutoIdentificador produto, NotaAvaliacao nota, Optional<String> comentario, boolean compraVerificada,
			boolean valida, Instant avaliadoEm) {
		this.identificador = Objects.requireNonNull(identificador);
		this.usuario = Objects.requireNonNull(usuario);
		this.produto = Objects.requireNonNull(produto);
		this.nota = Objects.requireNonNull(nota);
		this.comentario = normalizarComentario(Objects.requireNonNull(comentario));
		this.compraVerificada = compraVerificada;
		this.valida = valida;
		this.avaliadoEm = Objects.requireNonNull(avaliadoEm);
	}

	private static Optional<String> normalizarComentario(Optional<String> comentario) {
		return comentario.map(String::trim).filter(v -> !v.isEmpty()).map(v -> {
			Validate.isTrue(v.length() <= TAMANHO_COMENTARIO, "O comentário não pode ter mais de %d caracteres",
					TAMANHO_COMENTARIO);
			return v;
		});
	}

	@Override
	public Optional<AvaliacaoProdutoIdentificador> getIdentificador() {
		return identificador;
	}

	public UsuarioIdentificador getUsuario() {
		return usuario;
	}

	public ProdutoIdentificador getProduto() {
		return produto;
	}

	public NotaAvaliacao getNota() {
		return nota;
	}

	public int getNotaValor() {
		return nota.getValor();
	}

	public Optional<String> getComentario() {
		return comentario;
	}

	public boolean isCompraVerificada() {
		return compraVerificada;
	}

	public String getSelo() {
		return compraVerificada ? SELO_COMPRA_VERIFICADA : "";
	}

	public boolean isValida() {
		return valida;
	}

	public Instant getAvaliadoEm() {
		return avaliadoEm;
	}

	public AvaliacaoProduto atualizar(NotaAvaliacao novaNota, Optional<String> novoComentario, Instant momento) {
		return new AvaliacaoProduto(identificador, usuario, produto, Objects.requireNonNull(novaNota),
				Objects.requireNonNull(novoComentario), true, true, Objects.requireNonNull(momento));
	}

	public AvaliacaoProduto invalidar() {
		return new AvaliacaoProduto(identificador, usuario, produto, nota, comentario, compraVerificada, false,
				avaliadoEm);
	}

	@Override
	public boolean equals(@Nullable Object objeto) {
		if (objeto == null || getClass() != objeto.getClass()) {
			return false;
		}
		var outra = (AvaliacaoProduto) objeto;
		return Objects.equals(identificador, outra.identificador) && Objects.equals(usuario, outra.usuario)
				&& Objects.equals(produto, outra.produto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identificador, usuario, produto);
	}
}
