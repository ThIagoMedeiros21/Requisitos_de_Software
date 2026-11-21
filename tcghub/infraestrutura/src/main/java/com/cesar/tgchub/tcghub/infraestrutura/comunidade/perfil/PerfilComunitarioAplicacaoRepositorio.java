package com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioConsulta;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioOrdenacao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.PerfilComunitarioProjecao;
import com.cesar.tgchub.tcghub.aplicacao.comunidade.perfil.spi.IPerfilComunitarioAplicacaoRepositorio;

import dev.sauloaraujo.d5.framework.infraestrutura.AgregadoAplicacaoRepositorioAbstrato;

public class PerfilComunitarioAplicacaoRepositorio
		extends AgregadoAplicacaoRepositorioAbstrato<PerfilComunitarioProjecao, PerfilComunitarioOrdenacao, PerfilComunitarioConsulta>
		implements IPerfilComunitarioAplicacaoRepositorio {
	private final IPerfilComunitarioRepositorioJpa repositorioJpa;

	public PerfilComunitarioAplicacaoRepositorio(IPerfilComunitarioRepositorioJpa repositorioJpa) {
		this.repositorioJpa = Objects.requireNonNull(repositorioJpa);
	}

	@Override
	protected Page<PerfilComunitarioProjecao> pesquisar(PerfilComunitarioConsulta consulta, Pageable pageable) {
		Objects.requireNonNull(consulta);
		Objects.requireNonNull(pageable);

		var termo = paraLike(consulta.getTermo());
		return repositorioJpa.pesquisar(termo, pageable);
	}

	@Override
	protected String paraPropriedadeOrdenacao(PerfilComunitarioOrdenacao ordenacao) {
		Objects.requireNonNull(ordenacao);

		return switch (ordenacao) {
		case IDENTIFICADOR -> "identificador";
		case APELIDO -> "apelido";
		case CIDADE -> "cidade";
		};
	}
}
