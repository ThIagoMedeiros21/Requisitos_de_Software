package com.cesar.tgchub.tcghub.infraestrutura.comunidade.compraverificada;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaProjecao;

public interface ICompraVerificadaRepositorioJpa extends JpaRepository<CompraVerificadaJpa, Long> {
	boolean existsByUsuarioAndProdutoAndPagaTrueAndReembolsadaFalse(String usuario, String produto);

	boolean existsByUsuarioAndPedidoAndProduto(String usuario, String pedido, String produto);

	Optional<CompraVerificadaJpa> findByUsuarioAndPedidoAndProduto(String usuario, String pedido, String produto);

	List<CompraVerificadaJpa> findByUsuarioAndProdutoOrderByAtualizadoEmDesc(String usuario, String produto);

	List<CompraVerificadaJpa> findByUsuarioOrderByAtualizadoEmDesc(String usuario);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.compraverificada.CompraVerificadaProjecao(
			       c.identificador, c.usuario, c.produto, c.pedido, c.paga, c.reembolsada, c.atualizadoEm)
			  from CompraVerificadaJpa c
			 where (:usuario is null or lower(c.usuario) = lower(:usuario))
			   and (:produto is null or lower(c.produto) = lower(:produto))
			""")
	Page<CompraVerificadaProjecao> pesquisar(@Nullable String usuario, @Nullable String produto, Pageable pageable);
}
