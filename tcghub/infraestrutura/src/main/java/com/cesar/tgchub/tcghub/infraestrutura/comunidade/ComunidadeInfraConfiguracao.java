package com.cesar.tgchub.tcghub.infraestrutura.comunidade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesar.tgchub.tcghub.dominio.IUsuarioContaRepositorio;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta.IUsuarioContaRepositorioJpa;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.conta.UsuarioContaRepositorio;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil.ColecaoCartaConsultaInfra;

@Configuration
public class ComunidadeInfraConfiguracao {
	@Bean
	IColecaoCartaConsulta colecaoCartaConsulta() {
		return new ColecaoCartaConsultaInfra();
	}

	@Bean
	IUsuarioContaRepositorio usuarioContaRepositorio(IUsuarioContaRepositorioJpa repositorioJpa) {
		return new UsuarioContaRepositorio(repositorioJpa);
	}
}
