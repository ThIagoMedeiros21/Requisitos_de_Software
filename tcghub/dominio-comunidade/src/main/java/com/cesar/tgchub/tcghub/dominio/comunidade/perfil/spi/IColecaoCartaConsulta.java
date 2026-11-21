package com.cesar.tgchub.tcghub.dominio.comunidade.perfil.spi;

import java.util.List;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaColecaoResumo;
import com.cesar.tgchub.tcghub.dominio.comunidade.perfil.CartaIdentificador;

public interface IColecaoCartaConsulta {
	boolean usuarioPossuiCarta(UsuarioIdentificador usuario, CartaIdentificador carta);

	List<CartaColecaoResumo> listarCartasDisponiveis(UsuarioIdentificador usuario);
}
