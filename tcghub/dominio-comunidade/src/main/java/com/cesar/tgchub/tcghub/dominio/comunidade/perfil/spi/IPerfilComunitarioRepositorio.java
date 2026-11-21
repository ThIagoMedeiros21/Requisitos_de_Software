package com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi;

import java.util.Optional;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.Apelido;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitario;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.PerfilComunitarioIdentificador;

import dev.sauloaraujo.d5.framework.dominio.spi.IAgregadoRepositorio;

public interface IPerfilComunitarioRepositorio
		extends IAgregadoRepositorio<PerfilComunitarioIdentificador, PerfilComunitario> {
	boolean existePorUsuario(UsuarioIdentificador usuario);

	PerfilComunitario buscarPorUsuario(UsuarioIdentificador usuario);

	boolean existePorApelido(Apelido apelido, Optional<PerfilComunitarioIdentificador> identificadorIgnorado);
}
