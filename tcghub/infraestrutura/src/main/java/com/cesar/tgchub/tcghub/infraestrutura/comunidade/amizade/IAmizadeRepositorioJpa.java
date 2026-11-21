package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeProjecao;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade;

public interface IAmizadeRepositorioJpa extends JpaRepository<AmizadeJpa, Long> {
	@Query("""
			select count(a) > 0
			  from AmizadeJpa a
			 where (a.solicitante = :usuarioA and a.destinatario = :usuarioB)
			    or (a.solicitante = :usuarioB and a.destinatario = :usuarioA)
			""")
	boolean existeRelacaoEntre(String usuarioA, String usuarioB);

	@Query("""
			select a
			  from AmizadeJpa a
			 where (a.solicitante = :usuarioA and a.destinatario = :usuarioB)
			    or (a.solicitante = :usuarioB and a.destinatario = :usuarioA)
			""")
	Optional<AmizadeJpa> buscarRelacaoEntre(String usuarioA, String usuarioB);

	@Query("""
			select a
			  from AmizadeJpa a
			 where a.solicitante = :usuario
			    or a.destinatario = :usuario
			 order by a.criadoEm desc
			""")
	List<AmizadeJpa> listarPorUsuario(String usuario);

	@Query("""
			select a
			  from AmizadeJpa a
			 where a.status = com.cesar.tgchub.tcghub.dominio.comunidade.amizade.StatusAmizade.ACEITA
			   and (a.solicitante = :usuario or a.destinatario = :usuario)
			 order by a.criadoEm desc
			""")
	List<AmizadeJpa> listarAmizadesAtivas(String usuario);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeProjecao(
			       a.identificador, a.solicitante, a.destinatario, a.status, a.criadoEm, a.atualizadoEm)
			  from AmizadeJpa a
			 where (:usuario is null or a.solicitante = :usuario or a.destinatario = :usuario)
			   and (:status is null or a.status = :status)
			""")
	Page<AmizadeProjecao> pesquisar(@Nullable String usuario, @Nullable StatusAmizade status, Pageable pageable);
}
