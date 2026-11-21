package com.cesar.tgchub.tcghub.dominio.comunidade.amizade.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.Amizade;
import com.cesar.tgchub.tcghub.dominio.comunidade.amizade.AmizadeIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IAmizadeRepositorio extends IAgregadoRepositorio<AmizadeIdentificador, Amizade> {
	boolean existeRelacaoEntre(UsuarioIdentificador usuarioA, UsuarioIdentificador usuarioB);

	Amizade buscarRelacaoEntre(UsuarioIdentificador usuarioA, UsuarioIdentificador usuarioB);

	List<Amizade> listarPorUsuario(UsuarioIdentificador usuario);

	List<Amizade> listarAmizadesAtivas(UsuarioIdentificador usuario);
}
