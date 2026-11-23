package com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.CompraVerificada;
import com.cesar.tgchub.tcghub.dominio.comunidade.compraverificada.PedidoIdentificador;
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
@Table(name = "CVE_COMPRA_VERIFICADA", uniqueConstraints = @UniqueConstraint(columnNames = { "CVE_USR_ID",
		"CVE_PEDIDO_ID", "CVE_PRODUTO_ID" }))
public class CompraVerificadaJpa implements IAgregadoJpa<CompraVerificada> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CVE_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "CVE_USR_ID", nullable = false)
	@NotNull
	@Size(max = UsuarioIdentificador.TAMANHO)
	private String usuario;

	@Column(name = "CVE_PRODUTO_ID", nullable = false)
	@NotNull
	@Size(max = ProdutoIdentificador.TAMANHO)
	private String produto;

	@Column(name = "CVE_PEDIDO_ID", nullable = false)
	@NotNull
	@Size(max = PedidoIdentificador.TAMANHO)
	private String pedido;

	@Column(name = "CVE_PAGA", nullable = false)
	private boolean paga;

	@Column(name = "CVE_REEMBOLSADA", nullable = false)
	private boolean reembolsada;

	@Column(name = "CVE_ATUALIZADO_EM", nullable = false)
	@NotNull
	private Instant atualizadoEm;

	protected CompraVerificadaJpa() {
	}

	public CompraVerificadaJpa(@Nullable Long identificador, String usuario, String produto, String pedido,
			boolean paga, boolean reembolsada, Instant atualizadoEm) {
		this.identificador = identificador;
		this.usuario = usuario;
		this.produto = produto;
		this.pedido = pedido;
		this.paga = paga;
		this.reembolsada = reembolsada;
		this.atualizadoEm = atualizadoEm;
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

	public String getPedido() {
		return pedido;
	}

	public boolean isPaga() {
		return paga;
	}

	public boolean isReembolsada() {
		return reembolsada;
	}

	public Instant getAtualizadoEm() {
		return atualizadoEm;
	}

	public void atualizar(boolean paga, boolean reembolsada, Instant atualizadoEm) {
		this.paga = paga;
		this.reembolsada = reembolsada;
		this.atualizadoEm = atualizadoEm;
	}
}
