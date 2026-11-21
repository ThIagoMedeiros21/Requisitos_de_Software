package com.cesar.tgchub.tcghub.infraestrutura.comunidade.amizade;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.AmizadeProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.amizade.spi.IAmizadeAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class AmizadeAplicacaoRepositorio
		extends AgregadoAplicacaoRepositorioAbstrato<AmizadeProjecao, AmizadeOrdenacao, AmizadeConsulta>
		implements IAmizadeAplicacaoRepositorio {
	private final IAmizadeRepositorioJpa repositorioJpa;

	public AmizadeAplicacaoRepositorio(IAmizadeRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<AmizadeProjecao> pesquisar(AmizadeConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var usuario = consulta.getUsuario().orElse(null);
		var status = consulta.getStatus().orElse(null);
		return repositorioJpa.pesquisar(usuario, status, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(AmizadeOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);

		return switch (ordenacao) {
		case IDENTIFICADOR -> "identificador";
		case CRIADO_EM -> "criadoEm";
		case STATUS -> "status";
		};
	}
}
