package com.cesar.tgchub.tcghub.infraestrutura.comunidade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi.IColecaoCartaConsulta;
import com.cesar.tgchub.tcghub.infraestrutura.comunidade.perfil.ColecaoCartaConsultaInfra;

@Configuration
public class ComunidadeInfraConfiguracao {
	@Bean
	IColecaoCartaConsulta colecaoCartaConsulta() {
		return new ColecaoCartaConsultaInfra();
	}
}
