package com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaProjecao;

public interface IAberturaPreferidaRepositorioJpa extends JpaRepository<AberturaPreferidaJpa, Long> {
	boolean existsByUsuario(String usuario);

	Optional<AberturaPreferidaJpa> findByUsuario(String usuario);

	boolean existsByAberturaIdentificador(Long aberturaId);

	int countByAberturaIdentificador(Long aberturaId);

	List<AberturaPreferidaJpa> findByAberturaIdentificador(Long aberturaId);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaProjecao(
			       ap.identificador, ap.usuario, ap.abertura.identificador, ap.visivelNoPerfil, ap.escolhidaEm)
			  from AberturaPreferidaJpa ap
			 where (:usuario is null or lower(ap.usuario) = lower(:usuario))
			   and (:visivel is null or ap.visivelNoPerfil = :visivel)
			""")
	Page<AberturaPreferidaProjecao> pesquisar(@Nullable String usuario, @Nullable Boolean visivel, Pageable pageable);
}
