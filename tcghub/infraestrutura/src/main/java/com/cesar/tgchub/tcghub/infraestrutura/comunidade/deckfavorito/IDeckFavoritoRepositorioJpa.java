package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoProjecao;

public interface IDeckFavoritoRepositorioJpa extends JpaRepository<DeckFavoritoJpa, Long> {
	int countByUsuario(String usuario);

	boolean existsByUsuarioAndDeck(String usuario, String deck);

	Optional<DeckFavoritoJpa> findByUsuarioAndDeck(String usuario, String deck);

	List<DeckFavoritoJpa> findByUsuarioOrderByOrdemAsc(String usuario);

	@Query("""
			select new com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoProjecao(
			       d.identificador, d.usuario, d.deck, d.ordem, d.adicionadoEm)
			  from DeckFavoritoJpa d
			 where (:usuario is null or lower(d.usuario) = lower(:usuario))
			""")
	Page<DeckFavoritoProjecao> pesquisar(@Nullable String usuario, Pageable pageable);
}
