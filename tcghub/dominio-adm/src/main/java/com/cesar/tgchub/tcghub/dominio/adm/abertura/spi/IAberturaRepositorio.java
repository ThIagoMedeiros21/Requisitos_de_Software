package com.cesar.tgchub.tcghub.dominio.adm.abertura.spi;

import java.util.List;
import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.adm.abertura.Abertura;
import com.cesar.tgchub.tcghub.dominio.adm.abertura.AberturaIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IAberturaRepositorio extends IAgregadoRepositorio<AberturaIdentificador, Abertura> {
	boolean existePorNumero(int numero, Optional<AberturaIdentificador> identificadorIgnorado);

	boolean existePorNumero(int numero);

	Abertura buscarPorNumero(int numero);

	boolean possuiEscolhaPorUsuario(AberturaIdentificador identificador);

	List<Abertura> listarTodas();

	List<Abertura> listarPorStatus(boolean ativa);
}
