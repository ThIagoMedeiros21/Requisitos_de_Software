package com.cesar.tgchub.tcghub.infraestrutura.comunidade.aberturapreferida;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.AberturaPreferidaProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.aberturapreferida.spi.IAberturaPreferidaAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class AberturaPreferidaAplicacaoRepositorio extends
		AgregadoAplicacaoRepositorioAbstrato<AberturaPreferidaProjecao, AberturaPreferidaOrdenacao, AberturaPreferidaConsulta>
		implements IAberturaPreferidaAplicacaoRepositorio {
	private final IAberturaPreferidaRepositorioJpa repositorioJpa;

	public AberturaPreferidaAplicacaoRepositorio(IAberturaPreferidaRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<AberturaPreferidaProjecao> pesquisar(AberturaPreferidaConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var usuario = consulta.getUsuario().map(String::trim).filter(v -> !v.isEmpty()).orElse(null);
		var visivel = consulta.getVisivelNoPerfil().orElse(null);
		return repositorioJpa.pesquisar(usuario, visivel, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(AberturaPreferidaOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);
		return switch (ordenacao) {
		case ESCOLHIDA_EM -> "escolhidaEm";
		case USUARIO -> "usuario";
		};
	}
}
