package com.cesar.tgchub.tcghub.infraestrutura.adm.abertura;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaProjecao;

public interface IAberturaRepositorioJpa extends JpaRepository<AberturaJpa, Long> {
	boolean existsByNumero(int numero);

	Optional<AberturaJpa> findByNumero(int numero);

	List<AberturaJpa> findAllByOrderByNumeroAsc();

	List<AberturaJpa> findByAtivaOrderByNumeroAsc(boolean ativa);

	@Query("""
			select count(a) > 0
			  from AberturaJpa a
			 where a.numero = :numero
			   and (:identificadorIgnorado is null or a.identificador <> :identificadorIgnorado)
			""")
	boolean existePorNumero(int numero, @Nullable Long identificadorIgnorado);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.adm.abertura.AberturaProjecao(
			       a.identificador, a.numero, a.nome, a.artista, a.episodios, a.imagem, a.video, a.ativa,
			       (select count(ap) from AberturaPreferidaJpa ap where ap.abertura.identificador = a.identificador))
			  from AberturaJpa a
			 where (:ativa is null or a.ativa = :ativa)
			   and (:termo is null
			        or lower(a.nome) like :termo escape '\\'
			        or lower(a.artista) like :termo escape '\\'
			        or cast(a.numero as string) like :termo escape '\\')
			""")
	Page<AberturaProjecao> pesquisar(@Nullable String termo, @Nullable Boolean ativa, Pageable pageable);
}
