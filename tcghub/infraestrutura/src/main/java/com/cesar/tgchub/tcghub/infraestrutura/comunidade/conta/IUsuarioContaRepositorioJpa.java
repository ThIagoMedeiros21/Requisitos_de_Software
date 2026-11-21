package com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUsuarioContaRepositorioJpa extends JpaRepository<UsuarioContaJpa, String> {
	@Query("""
			select count(c) > 0
			  from UsuarioContaJpa c
			 where lower(c.usuario) = lower(:usuario)
			""")
	boolean existePorUsuarioIgnoreCase(String usuario);

	@Query("""
			select count(c) > 0
			  from UsuarioContaJpa c
			 where lower(c.email) = lower(:email)
			""")
	boolean existePorEmailIgnoreCase(String email);

	@Query("""
			select c
			  from UsuarioContaJpa c
			 where lower(c.usuario) = lower(:identificador)
			    or lower(c.email) = lower(:identificador)
			""")
	Optional<UsuarioContaJpa> buscarPorUsuarioOuEmailIgnoreCase(String identificador);
}
