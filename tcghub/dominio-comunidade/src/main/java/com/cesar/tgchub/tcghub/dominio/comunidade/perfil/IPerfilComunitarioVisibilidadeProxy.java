package com.cesar.tgchub.tcghub.dominio.comunidade.perfil;

import com.cesar.tgchub.tcghub.dominio.UsuarioIdentificador;

public interface IPerfilComunitarioVisibilidadeProxy {
	PerfilComunitarioVisao consultarPerfil(UsuarioIdentificador solicitante, UsuarioIdentificador alvo);

	PerfilComunitarioVisao consultarPerfilPorIdentificador(UsuarioIdentificador solicitante,
			PerfilComunitarioIdentificador identificador);

	void editarPerfil(UsuarioIdentificador solicitante, PerfilComunitario agregado);

	PerfilComunitario fixarCartaFavorita(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			CartaIdentificador carta);

	PerfilComunitario removerCartaFavorita(UsuarioIdentificador solicitante, UsuarioIdentificador alvo,
			CartaIdentificador carta);
}
