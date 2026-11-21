package com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferida;
import com.cesar.tgchub.tcghub.dominio.comunidade.aberturapreferida.AberturaPreferidaIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IAberturaPreferidaRepositorio
		extends IAgregadoRepositorio<AberturaPreferidaIdentificador, AberturaPreferida> {
	boolean existePorUsuario(UsuarioIdentificador usuario);

	AberturaPreferida buscarPorUsuario(UsuarioIdentificador usuario);

	List<AberturaPreferida> listarTodas();

	int contarPorAbertura(AberturaIdentificador abertura);

	List<AberturaPreferida> listarPorAbertura(AberturaIdentificador abertura);
}
