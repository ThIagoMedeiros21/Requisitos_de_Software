package com.cesar.tgchub.tcghub.infraestrutura.comunidade.deckfavorito;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.DeckFavoritoProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.deckfavorito.spi.IDeckFavoritoAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class DeckFavoritoAplicacaoRepositorio
		extends AgregadoAplicacaoRepositorioAbstrato<DeckFavoritoProjecao, DeckFavoritoOrdenacao, DeckFavoritoConsulta>
		implements IDeckFavoritoAplicacaoRepositorio {
	private final IDeckFavoritoRepositorioJpa repositorioJpa;

	public DeckFavoritoAplicacaoRepositorio(IDeckFavoritoRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<DeckFavoritoProjecao> pesquisar(DeckFavoritoConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var usuario = consulta.getUsuario().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		return repositorioJpa.pesquisar(usuario, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(DeckFavoritoOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);

		return switch (ordenacao) {
		case ORDEM -> "ordem";
		case ADICIONADO_EM -> "adicionadoEm";
		};
	}
}
