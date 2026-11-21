package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioProjecao;

public interface IPerfilComunitarioRepositorioJpa extends JpaRepository<PerfilComunitarioJpa, Long> {
	boolean existsByUsuario(String usuario);

	Optional<PerfilComunitarioJpa> findByUsuario(String usuario);

	@Query("""
			select count(p) > 0
			  from PerfilComunitarioJpa p
			 where lower(p.apelido) = lower(:apelido)
			   and (:identificadorIgnorado is null or p.identificador <> :identificadorIgnorado)
			""")
	boolean existePorApelido(String apelido, @Nullable Long identificadorIgnorado);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioProjecao(
			       p.identificador, p.usuario, p.apelido, p.cidade, p.foto, p.decksFavoritosVisiveis)
			  from PerfilComunitarioJpa p
			 where (:termo is null
			        or lower(p.apelido) like :termo escape '\\'
			        or lower(p.usuario) like :termo escape '\\'
			        or lower(coalesce(p.cidade, '')) like :termo escape '\\')
			""")
	Page<PerfilComunitarioProjecao> pesquisar(@Nullable String termo, Pageable pageable);
}
