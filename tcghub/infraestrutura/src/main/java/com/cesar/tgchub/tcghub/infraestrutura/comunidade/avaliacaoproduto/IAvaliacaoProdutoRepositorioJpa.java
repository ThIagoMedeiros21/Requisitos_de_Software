package com.cesar.tgchub.tcghub.infraestrutura.comunidade.avaliacaoproduto;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoProjecao;

public interface IAvaliacaoProdutoRepositorioJpa extends JpaRepository<AvaliacaoProdutoJpa, Long> {
	boolean existsByUsuarioAndProduto(String usuario, String produto);

	Optional<AvaliacaoProdutoJpa> findByUsuarioAndProduto(String usuario, String produto);

	List<AvaliacaoProdutoJpa> findByProdutoAndValidaTrueOrderByAvaliadoEmDesc(String produto);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.avaliacaoproduto.AvaliacaoProdutoProjecao(
			       a.identificador, a.usuario, a.produto, a.nota, a.comentario, a.compraVerificada, a.valida,
			       a.avaliadoEm)
			  from AvaliacaoProdutoJpa a
			 where (:produto is null or lower(a.produto) = lower(:produto))
			   and (:usuario is null or lower(a.usuario) = lower(:usuario))
			   and (:apenasValidas is null or :apenasValidas = false or a.valida = true)
			""")
	Page<AvaliacaoProdutoProjecao> pesquisar(@Nullable String produto, @Nullable String usuario,
			@Nullable Boolean apenasValidas, Pageable pageable);
}
