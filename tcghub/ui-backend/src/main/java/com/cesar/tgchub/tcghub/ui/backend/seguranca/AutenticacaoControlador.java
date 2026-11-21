package com.cesar.tgchub.tcghub.ui.backend.seguranca;

import java.util.Objects;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeServico;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioServico;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;
import dev.sauloaraujo.d5.framework.ui.seguranca.AutenticacaoControladorAbstrato;

@RestController
@RequestMapping(Backend.PREFIXO + "autenticacao")
public class AutenticacaoControlador extends AutenticacaoControladorAbstrato {
	private final PerfilComunitarioServico perfilComunitarioServico;
	private final AmizadeServico amizadeServico;

	public AutenticacaoControlador(ISegurancaServico segurancaServico,
			PerfilComunitarioServico perfilComunitarioServico, AmizadeServico amizadeServico) {
		super(segurancaServico);
		this.perfilComunitarioServico = Objects.requireNonNull(perfilComunitarioServico);
		this.amizadeServico = Objects.requireNonNull(amizadeServico);
	}

	@Override
	protected AutenticacaoRecurso recurso() {
		var perfilComunitario = perfilComunitarioServico.criarAutorizacoes(Optional.empty());
		var amizade = amizadeServico.criarAutorizacoes(Optional.empty());
		return new AutenticacaoRecurso(segurancaServico.getUsuario(), perfilComunitario, amizade);
	}
}
