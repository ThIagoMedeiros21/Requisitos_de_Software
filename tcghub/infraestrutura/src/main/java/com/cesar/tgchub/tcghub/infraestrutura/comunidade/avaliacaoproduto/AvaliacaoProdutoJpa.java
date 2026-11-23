package com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.avaliacaoproduto.AvaliacaoProduto;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.ProdutoIdentificador;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "AVA_AVALIACAO_PRODUTO", uniqueConstraints = @UniqueConstraint(columnNames = { "AVA_USR_ID",
		"AVA_PRODUTO_ID" }))
public class AvaliacaoProdutoJpa implements IAgregadoJpa<AvaliacaoProduto> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AVA_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "AVA_USR_ID", nullable = false)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@Column(name = "AVA_PRODUTO_ID", nullable = false)
	@NotNull
	@Size(max = ProdutoIdentificador.TAMANHO)
	private String produto;

	@Column(name = "AVA_NOTA", nullable = false)
	private int nota;

	@Column(name = "AVA_COMENTARIO")
	@Size(max = AvaliacaoProduto.TAMANHO_COMENTARIO)
	private @Nullable String comentario;

	@Column(name = "AVA_COMPRA_VERIFICADA", nullable = false)
	private boolean compraVerificada;

	@Column(name = "AVA_VALIDA", nullable = false)
	private boolean valida;

	@Column(name = "AVA_AVALIADO_EM", nullable = false)
	@NotNull
	private Instant avaliadoEm;

	protected AvaliacaoProdutoJpa() {
	}

	public AvaliacaoProdutoJpa(@Nullable Long identificador, String usuario, String produto, int nota,
			@Nullable String comentario, boolean compraVerificada, boolean valida, Instant avaliadoEm) {
		this.identificador = identificador;
		this.usuario = usuario;
		this.produto = produto;
		this.nota = nota;
		this.comentario = comentario;
		this.compraVerificada = compraVerificada;
		this.valida = valida;
		this.avaliadoEm = avaliadoEm;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getProduto() {
		return produto;
	}

	public int getNota() {
		return nota;
	}

	public @Nullable String getComentario() {
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

	public void atualizar(int nota, @Nullable String comentario, boolean compraVerificada, boolean valida,
			Instant avaliadoEm) {
		this.nota = nota;
		this.comentario = comentario;
		this.compraVerificada = compraVerificada;
		this.valida = valida;
		this.avaliadoEm = avaliadoEm;
	}
}
